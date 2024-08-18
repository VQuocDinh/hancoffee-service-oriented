import mongoose from "mongoose";

const productSchema = new mongoose.Schema({
    id:{type:String, require:true},
    name:{type:String, require:true},
    description:{type:String, require:true},
    idCategory:{type:String, require:true},
    price:{type:Number, require:true},
    image:{type:String, require:true},
    status:{type:Number, require:true},
    quantity:{type:Number, require:true},

})

const productModel = mongoose.models.product || mongoose.model("product", productSchema)
export default productModel
