
import React, { useContext, useState } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faFacebook, faGithub, faLinkedinIn } from '@fortawesome/free-brands-svg-icons';
import { useNavigate } from 'react-router-dom';
import axiosInstance from '../../common/library/query';
import { PATH_DASHBOARD, ROOT_CUSTOMER, ROOT_DASHBOARD } from '../../common/routes/path';
import { StoreContext } from '../../context/StoreContext';

const SignInForm = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();
  const [rememberMe, setRememberMe] = useState(false);
  const { token, setToken } = useContext(StoreContext)
  const handleForgotPassword = () => {
    navigate('/forgot-password');
  };
  const submitHandler = async (e) => {
    e.preventDefault();
    try {
      const response = await axiosInstance.post('http://localhost:8888/api/user/login', { email, password });
      if (response) {
        const { token, user } = response.data || {};
        console.log(response.data);
        // Save token to local storage
        setToken(response.data.token)
        localStorage.setItem('token', token);
        // Navigate based on user role
        if (user && user.role === 2) {
          navigate(ROOT_CUSTOMER);
        } else if (user && user.role === 1 || user && user.role === 0) {
          navigate(ROOT_DASHBOARD);
        } else {
          console.error('User role is undefined or invalid');
        }
      } else {
        console.error('Response is undefined');
      }
    } catch (error) {
      console.error(error.response.data.message);
    }
  };

  return (
    <div className="form-container sign-in">
      <form onSubmit={submitHandler}>
        <h1>Đăng nhập</h1>
        <div className="social-icons">
          <a href="#" className="icon"><FontAwesomeIcon icon={faFacebook} /></a>
          <a href="#" className="icon"><FontAwesomeIcon icon={faGithub} /></a>
          <a href="#" className="icon"><FontAwesomeIcon icon={faLinkedinIn} /></a>
        </div>
        <span>Đăng nhập bằng email</span>
        <input type="email" placeholder="Nhập email" value={email} onChange={(e) => setEmail(e.target.value)} required />
        <input type="password" placeholder="Nhập mật khẩu" value={password} onChange={(e) => setPassword(e.target.value)} required />
        <a onClick={handleForgotPassword}>Bạn quên mật khẩu?</a>
        <button type="submit">Đăng nhập</button>
      </form>
    </div>
  );
};

export default SignInForm;


