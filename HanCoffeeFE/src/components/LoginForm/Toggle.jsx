import React, { useEffect } from 'react';
import '../../pages/Login/Login.css';

const Toggle = ({ onRegisterClick, onLoginClick }) => {
  
  return (
    <div className="toggle-container">
      <div className="toggle" id="toggleElement">
        <div className="toggle-panel toggle-left">
          <h1>Bạn đã có tài khoản ?</h1>
          <p>Đăng nhập ngay để vào cửa hàng thôi</p>
          <button className="hidden" id="login" onClick={onLoginClick}>Đăng nhập</button>
        </div>
        <div className="toggle-panel toggle-right">
          <h1>HanCafe xin chào!</h1>
          <p>Đăng ký tài khoản ngay để trải nghiệm những sản phẩm của cửa hàng</p>
          <button className="hidden" id="register" onClick={onRegisterClick}>Đăng ký ngay</button>
        </div>
      </div>
    </div>
  );
};

export default Toggle;
