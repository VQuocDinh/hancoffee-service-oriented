import mongoose from "mongoose";

export const connectDB = async ()=>{
    await mongoose.connect('mongodb+srv://vqdinh2202:Dinh2202@hancoffee.ayqyjgy.mongodb.net/HanCoffee').then(()=>{
        console.log('DB Connected')
    })
}
