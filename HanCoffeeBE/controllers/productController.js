import productModel from "../models/productModel.js";
import fs from 'fs';
import { bucket } from '../routers/firebase-helper.js';
import { v4 as uuidv4 } from 'uuid';

// add product item

const addProduct = async (productData,res)=> {

    const product = new productModel({
        name:productData.name,
        price:productData.price,
        idCategory:productData.idCategory,
        description:productData.description,
        status:productData.status,
        id:productData.id,
        image:productData.image,
        quantity:productData.quantity
    })
    try {
        await product.save();
        res.json({success:true, message:"Product Added successfully"})
    } catch (error) {
        console.log(error)
        res.json({success:false, message:"Error"})
    }
}

// all products list
const listProducts = async (req, res) => {
    try {
        const products = await productModel.find({});
        res.json({success:true, data:products})
    } catch (error) {
        console.log(error);
        res.json({success:false, message:"Error"})
    }
}

//remove product item
const removeProduct = async (req, res) => {
    try {
        const productId = req.params.id;
        const product = await productModel.findById(productId);

        if (!product) {
            return res.status(404).json({ success: false, message: 'Product not found' });
        }

        // Thay đổi trạng thái của sản phẩm thành 0 để ẩn sản phẩm
        product.status = 1; // Hoặc giá trị khác tùy vào logic của bạn

        await product.save();
        res.json({ success: true, message: 'Product removed successfully' });
    } catch (error) {
        console.error('Error removing product:', error);
        res.status(500).json({ success: false, message: 'Failed to remove product' });
    }
};


const editProduct = async (req, res) => {
  const productId = req.params.id;

  try {
      const updatedProductData = {
          name: req.body.name,
          price: req.body.price,
          idCategory: req.body.idCategory,
          description: req.body.description,
          status: req.body.status,
          quantity: req.body.quantity
      };

      // Find the existing product by ID
      const existingProduct = await productModel.findById(productId);

      if (!existingProduct) {
          return res.status(404).json({ success: false, message: "Product not found" });
      }

      // Check if a new image file is uploaded
      if (req.file) {
          // Upload the new image to Firebase Storage
          const metadata = {
              metadata: {
                  firebaseStorageDownloadTokens: uuidv4()
              },
              contentType: req.file.mimetype,
              cacheControl: "public, max-age=31536000"
          };

          const uniqueFilename = `imagesProduct/${uuidv4()}-${req.file.originalname}`;
          const blob = bucket.file(uniqueFilename);
          const blobStream = blob.createWriteStream({
              metadata: metadata,
              gzip: true
          });

          blobStream.on("error", err => {
              console.error("Stream error:", err);
              return res.status(500).json({ error: "Unable to upload image" });
          });

          blobStream.on("finish", async () => {
              const imageUrl = await blob.getSignedUrl({
                  action: 'read',
                  expires: '03-09-2491'
              });

              updatedProductData.image = imageUrl[0];

              // Update the product with new data including the new image URL
              const updatedProduct = await productModel.findByIdAndUpdate(productId, updatedProductData, { new: true });

              res.json({ success: true, message: "Product updated successfully", data: updatedProduct });
          });

          blobStream.end(req.file.buffer);
      } else {
          // No new image uploaded, update the product with existing data
          const updatedProduct = await productModel.findByIdAndUpdate(productId, updatedProductData, { new: true });

          res.json({ success: true, message: "Product updated successfully", data: updatedProduct });
      }
  } catch (error) {
      console.error("Error updating product:", error);
      res.status(500).json({ success: false, message: "Error updating product" });
  }
};




  const getProductById = async (req, res) => {
    const productId = req.params.id;
    try {
        const product = await productModel.findById(productId);
        if (!product) {
            return res.status(404).json({ success: false, message: 'Product not found' });
        }
        res.json({ success: true, data: product });
    } catch (error) {
        console.error('Error fetching product:', error);
        res.status(500).json({ success: false, message: 'Failed to fetch product' });
    }
};


export { addProduct,listProducts,editProduct,removeProduct,getProductById}

