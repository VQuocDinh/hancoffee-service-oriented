import mongoose from "mongoose";

const cartItemsSchema = mongoose.Schema({
    cart: { type: mongoose.Schema.Types.ObjectId, ref: 'Cart', required: true },
    product: { type: mongoose.Schema.Types.ObjectId, ref: 'Product', required: true },
    quantity: { type: Number, default: 0, required: true },
    size: { type: String, default: 'M', required: true }
})

const cartItemsModel = mongoose.models.cartItem || mongoose.model("cartItem", cartItemsSchema)
export default cartItemsModel