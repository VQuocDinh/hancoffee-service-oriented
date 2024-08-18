import express from 'express';
import { addCategory, editCategory, getCategoryById, listCategories, removeCategory } from '../controllers/categoryController.js';
import multer from 'multer';
import { v4 as uuidv4 } from 'uuid';
import { bucket } from './firebase-helper.js';

const categoryRouter = express.Router();

const storage = multer.memoryStorage();
const upload = multer({ storage: storage });

categoryRouter.post("/", upload.single("image"), async (req, res) => {
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

    const uniqueFilename = `imagesCategory/${uuidv4()}-${req.file.originalname}`;
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

      const categoryData = {
        name: req.body.name,
        id: req.body.id,
        status: req.body.status,
        image: imageUrl[0],
      };

      try {
        await addCategory(categoryData, res);
      } catch (err) {
        console.error("Error adding category:", err);
        return res.status(500).json({ error: "Unable to add category" });
      }
    });

    blobStream.end(req.file.buffer);
  } catch (err) {
    console.error("Internal server error:", err);
    return res.status(500).json({ error: "Internal server error" });
  }
});

categoryRouter.get("/", listCategories);
categoryRouter.get("/list", listCategories);
categoryRouter.patch("/:id", removeCategory);
categoryRouter.put("/:id", upload.single("image"), async (req, res) => {
  try {
    await editCategory(req, res); // Gọi hàm editCategory từ controller
  } catch (err) {
    console.error("Error editing category:", err);
    return res.status(500).json({ error: "Unable to edit category" });
  }
});
categoryRouter.get("/:id",getCategoryById)

export default categoryRouter;
