import mongoose from "mongoose";

const categoryOderStatusSchema = new mongoose.Schema({
    id: { type: String, require: true },
    name: { type: String, default: 'Chờ xác nhận'},
    })

const categoryOderStatusModel = mongoose.models.categoryOderStatus || mongoose.model("categoryOderStatus", categoryOderStatusSchema)
export default categoryOderStatusModel