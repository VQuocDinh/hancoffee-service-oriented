import express from 'express'
import cors from 'cors'
import { connectDB } from './config/db.js'
import productRouter from './routers/productRouter.js'
import userRouter from './routers/userRouter.js'
import 'dotenv/config'
import categoryRouter from './routers/categoryRouter.js'
import cartRouter from './routers/cartRouter.js'
import staffRouter from './routers/staffRouter.js'
import customerRouter from './routers/customerRouter.js'
import orderRouter from './routers/orderRouter.js'

// app connfig
const app = express()
const port = 8888

//middelware
app.use(express.json())
app.use(cors())

//db connect
connectDB();

//api endpoints
app.use("/api/product",productRouter)
app.use("/api/user",userRouter)
app.use("/api/category",categoryRouter)
app.use("/api/cart", cartRouter)
app.use("/api/order", orderRouter)
app.use("/api/staff", staffRouter)
app.use("/api/customer", customerRouter)
app.use("/login",userRouter)
app.use("/images", express.static('uploads'))

// app router
app.get("/", (req,res)=> {
    res.send("Initial Success")
})

app.listen(port,()=>{
    console.log(`Server started on http://localhost:${port}`)
})

