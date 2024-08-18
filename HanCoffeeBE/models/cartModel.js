import mongoose from "mongoose";

const cartSchema = mongoose.Schema({
    id:{type:String, require:true},
    user:{type: mongoose.Schema.Types.ObjectId, ref:"User", require:true}
})

const cartModel = mongoose.models.cart || mongoose.model("cart", cartSchema)
export default cartModel