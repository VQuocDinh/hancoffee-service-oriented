import mongoose from "mongoose";


const orderSchema = new mongoose.Schema({
    date:{type:Date,default:Date.now()},
    status:{type:String,default:"confirm"},
    userId:{type:String ,required:true},
    totalPrice:{type:Number ,required:true},
    items:{type:Array,required:true},
    // address:{type:Object, required:true},
})

const orderModel = mongoose.models.order || mongoose.model("order", orderSchema);
export default orderModel;