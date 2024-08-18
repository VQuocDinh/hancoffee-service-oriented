import mongoose from 'mongoose';
import bcrypt from 'bcrypt';

const userSchema = new mongoose.Schema({
    email: {
        type: String,
        required: true,
        unique: true,
    },
    password: {
        type: String,
        required: true,
    },
    role: {
        type: Number,
        enum: [0, 1, 2],
        default: 2,
    },
    name: {
        type: String,
        default: '',
    },
    phone: {
        type: String,
        default: '',
    },
    address: {
        type: String,
        default: '',
    },
    avatar: {
        type: String,
        default: '',
    },
    cartData:{type:Object,default:{}}

}, {
    timestamps: true,
});

const User = mongoose.model('User', userSchema);

export default User;