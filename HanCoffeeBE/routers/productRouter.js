import express from 'express';
import { addProduct, editProduct, listProducts, removeProduct, getProductById } from '../controllers/productController.js';
import multer from 'multer';
import { v4 as uuidv4 } from 'uuid';
import { bucket } from './firebase-helper.js';

const productRouter = express.Router();

const storage = multer.memoryStorage();
const upload = multer({ storage: storage });

productRouter.post("/", upload.single("image"), async (req, res) => {
  try {
    if (!req.file) {
      console.error("No file uploaded");
      return res.status(404).send("No file uploaded");
    }

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

      const productData = {
        name: req.body.name,
        price: req.body.price,
        idCategory: req.body.idCategory,
        description: req.body.description,
        status: req.body.status,
        id: req.body.id,
        image: imageUrl[0],
        quantity: req.body.quantity
      };

      try {
        await addProduct(productData, res);
      } catch (err) {
        console.error("Error adding product:", err);
        return res.status(500).json({ error: "Unable to add product" });
      }
    });

    blobStream.end(req.file.buffer);
  } catch (err) {
    console.error("Internal server error:", err);
    return res.status(500).json({ error: "Internal server error" });
  }
});

productRouter.get("/", listProducts);
productRouter.get("/list", listProducts);
productRouter.patch("/:id", removeProduct);
productRouter.put("/:id", upload.single("image"), async (req, res) => {
  try {
    await editProduct(req, res); // Call editProduct function from controller
  } catch (err) {
    console.error("Error editing product:", err);
    return res.status(500).json({ error: "Unable to edit product" });
  }
});

productRouter.get("/:id", getProductById);

export default productRouter;
