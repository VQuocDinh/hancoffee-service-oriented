import categoryModel from "../models/categoryModel.js";
import fs from "fs";
import { bucket } from "../routers/firebase-helper.js";
import { v4 as uuidv4 } from "uuid";
import productModel from "../models/productModel.js";

const addCategory = async (categoryData, res) => {
  const category = new categoryModel({
    name: categoryData.name,
    id: categoryData.id,
    status: categoryData.status,
    image: categoryData.image,
  });

  try {
    await category.save();
    res.json({ success: true, message: "Category added successfully" });
  } catch (error) {
    console.log(error);
    res.json({ success: false, message: "Error adding category" });
  }
};
//all categories list
const listCategories = async (req, res) => {
  try {
    const categories = await categoryModel.find({});
    res.json({ success: true, data: categories });
  } catch (error) {
    console.log(error);
    res.json({ success: false, message: "Error" });
  }
};

//remove category item
const removeCategory = async (req, res) => {
  try {
    const categoryId = req.params.id;
    const category = await categoryModel.findById(categoryId);

    if (!category) {
      return res
        .status(404)
        .json({ success: false, message: "Category not found" });
    }

    // Thay đổi trạng thái của loại sản phẩm thành 0 để ẩn loại sản phẩm
    category.status = 1; // Hoặc giá trị khác tùy vào logic của bạn

    await category.save();
    // Tìm và cập nhật trạng thái của tất cả sản phẩm thuộc loại này
    await productModel.updateMany({ idCategory: categoryId }, { status: 1 });

    res.json({ success: true, message: "Category removed successfully" });
  } catch (error) {
    console.error("Error removing category:", error);
    res
      .status(500)
      .json({ success: false, message: "Failed to remove category" });
  }
};

//edit category
const editCategory = async (req, res) => {
  const categoryId = req.params.id;

  try {
    const updatedCategoryData = {
      name: req.body.name,
      status: req.body.status,
    };

    const existingCategory = await categoryModel.findById(categoryId);

    if (!existingCategory) {
      return res
        .status(404)
        .json({ success: false, message: "Category not found" });
    }

    // Check if a new image file is uploaded
    if (req.file) {
      // Upload the new image to Firebase Storage
      const metadata = {
        metadata: {
          firebaseStorageDownloadTokens: uuidv4(),
        },
        contentType: req.file.mimetype,
        cacheControl: "public, max-age=31536000",
      };

      const uniqueFilename = `imagesCategory/${uuidv4()}-${
        req.file.originalname
      }`;
      const blob = bucket.file(uniqueFilename);
      const blobStream = blob.createWriteStream({
        metadata: metadata,
        gzip: true,
      });

      blobStream.on("error", (err) => {
        console.error("Stream error:", err);
        return res.status(500).json({ error: "Unable to upload image" });
      });

      blobStream.on("finish", async () => {
        const imageUrl = await blob.getSignedUrl({
          action: "read",
          expires: "03-09-2491",
        });

        updatedCategoryData.image = imageUrl[0];

        // Update the product with new data including the new image URL
        const updatedCategory = await categoryModel.findByIdAndUpdate(
          categoryId,
          updatedCategoryData,
          { new: true }
        );

        res.json({
          success: true,
          message: "Category updated successfully",
          data: updatedCategory,
        });
      });

      blobStream.end(req.file.buffer);
    } else {
      // No new image uploaded, update the product with existing data
      const updatedCategory = await categoryModel.findByIdAndUpdate(
        categoryId,
        updatedCategoryData,
        { new: true }
      );

      res.json({
        success: true,
        message: "Category updated successfully",
        data: updatedCategory,
      });
    }
  } catch (error) {
    console.error("Error updating category:", error);
    res
      .status(500)
      .json({ success: false, message: "Error updating category" });
  }
};

const getCategoryById = async (req, res) => {
  const categoryId = req.params.id;
  try {
    const category = await categoryModel.findById(categoryId);
    if (!category) {
      return res
        .status(404)
        .json({ success: false, message: "Category not found" });
    }
    res.json({ success: true, data: category });
  } catch (error) {
    console.error("Error fetching category:", error);
    res
      .status(500)
      .json({ success: false, message: "Failed to fetch category" });
  }
};

export {
  addCategory,
  listCategories,
  editCategory,
  getCategoryById,
  removeCategory,
};
