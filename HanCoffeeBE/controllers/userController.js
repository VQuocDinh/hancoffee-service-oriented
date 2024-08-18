
import jwt from 'jsonwebtoken';
import bcrypt from 'bcrypt';
import User from '../models/userModel.js';
import nodemailer from 'nodemailer';
import crypto from 'crypto';
const getUsers = async (req, res) => {
  const users = await User.find({});
  res.json({ success: true, data: users });
};


const updateUserRole = async (req, res) => {
  const { id } = req.params;
  const { role } = req.body;

  const user = await User.findById(id);

  if (user) {
    user.role = role;
    await user.save();
    res.json({ success: true, message: 'Customer role updated successfully' });
  } else {
    res.status(404);
    throw new Error('Customer not found');
  }
};

const generateToken = (id) => {
  return jwt.sign({ id },process.env.JWT_SECRET);
};

const registerUser = async (req, res) => {
  const { email, password } = req.body;

  try {
    // Kiểm tra xem người dùng đã tồn tại trong cơ sở dữ liệu chưa
    const userExists = await User.findOne({ email });
    if (userExists) {
      return res.status(400).json({ message: 'User already exists' });
    }

    // Mã hóa mật khẩu trước khi lưu vào cơ sở dữ liệu
    const hashedPassword = await bcrypt.hash(password, 10);

    // Tạo người dùng mới với mật khẩu đã được mã hóa
    const user = await User.create({ email, password: hashedPassword });

    // Tạo token cho người dùng mới đăng ký
    const token = generateToken(user._id);
    console.log("success: ", token)

    // Trả về thông tin người dùng và token
    res.status(201).json({
      _id: user._id,
      email: user.email,
      token: token,
    });
  } catch (error) {
    console.error(error); // Ghi log lỗi để debug
    res.status(400).json({ message: 'Invalid user data' });
  }
};

const authUser = async (req, res) => {
  const { email, password } = req.body;
  try {
    const user = await User.findOne({ email });
    if (user && bcrypt.compareSync(password, user.password)) {
      // **Check if user object exists and role is defined and valid**
      if (user && user.role !== undefined && (user.role === 0 || user.role === 1 || user.role === 2)) {
        const token = jwt.sign({ id: user._id, role: user.role },process.env.JWT_SECRET);
        res.json({ token, user: { _id: user._id, email: user.email, role: user.role } });
      } else {
        console.error('User role is undefined or invalid');
        res.status(400).json({ message: 'Invalid user role' });
      }
    } else {
      res.status(401).json({ message: 'Invalid email or password' });
    }
  } catch (error) {
    console.error(error);
    res.status(500).json({ message: 'Server error' });
  }
};
const transporter = nodemailer.createTransport({
  service: 'Gmail',
  auth: {
    user: 'maptieuthu@gmail.com',
    pass: 'zpwv dhzj vyiv nnss'
  }

});
export const sendRandomPasswordEmail = async (req, res) => {
  const { email } = req.body;

  try {
    const user = await User.findOne({ email });
    if (!user) {
      return res.status(404).send({ message: 'Email không tồn tại' });
    }

    const randomPassword = crypto.randomBytes(8).toString('hex');

    const hashedPassword = await bcrypt.hash(randomPassword, 10);

    user.password = hashedPassword;
    await user.save();

    const mailOptions = {
      to: user.email,
      from: 'your-email@example.com',
      subject: 'Mật khẩu mới của bạn',
      text: `Mật khẩu mới của bạn là: ${randomPassword}. Vui lòng đăng nhập và thay đổi mật khẩu ngay sau khi đăng nhập.`,
    };
    await transporter.sendMail(mailOptions);

    res.send({ message: 'Mật khẩu mới đã được gửi đến email của bạn.' });
  } catch (error) {
    console.error(error);
    res.status(500).send({ message: 'Có lỗi xảy ra khi gửi email.' });
  }
};
export { getUsers, updateUserRole, registerUser, authUser };