
import React, { useState } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faFacebook, faGithub, faLinkedinIn } from '@fortawesome/free-brands-svg-icons';
import axios from 'axios';

const SignUpForm = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [message, setMessage] = useState('');

  const submitHandler = async (e) => {
    e.preventDefault();
    if (password !== confirmPassword) {
      alert('Mật khẩu không khớp');
      return;
    }
    try {
      const { data } = await axios.post('http://localhost:8888/api/user/Register', { email, password });
      console.log(data);
      setMessage(data.message);
      setMessage('Đăng ký thành công');
        setEmail('');
        setPassword('');
        setConfirmPassword('');
        setTimeout(() => {
          setMessage('');
        }, 5000);
      // Lưu token hoặc xử lý đăng ký thành công
    } catch (error) {
      console.error(error.response.data.message);
    }
  };

  return (
    <div className="form-container sign-up">
      <form onSubmit={submitHandler}>
        <h1>Đăng ký tài khoản</h1>
        <div className="social-icons">
          <a href="#" className="icon"><FontAwesomeIcon icon={faFacebook} /></a>
          <a href="#" className="icon"><FontAwesomeIcon icon={faGithub} /></a>
          <a href="#" className="icon"><FontAwesomeIcon icon={faLinkedinIn} /></a>
        </div>
        <span>Đăng ký bằng email</span>
        <input type="email" placeholder="Nhập email" value={email} onChange={(e) => setEmail(e.target.value)} />
        <input type="password" placeholder="Nhập mật khẩu" value={password} onChange={(e) => setPassword(e.target.value)} />
        <input type="password" placeholder="Nhập lại mật khẩu" value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)} />
        {message && <p>{message}</p>} {/* Display success message */}
        <button type="submit">Đăng ký</button>
      </form>
    </div>
  );
};

export default SignUpForm;

