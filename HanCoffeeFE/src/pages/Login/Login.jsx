import React, { useState } from 'react';
import SignUpForm from '../../components/LoginForm/SignUpForm';
import SignInForm from '../../components/LoginForm/SignInForm';
import Toggle from '../../components/LoginForm/Toggle';
import './Login.css';
import StoreContextProvider from '../../context/StoreContext';

const Login = () => {
  const [isActive, setIsActive] = useState(false);

  const handleRegisterClick = () => {
    setIsActive(true);
  };

  const handleLoginClick = () => {
    setIsActive(false);
  };

  return (
    <div className='wrap'>
      <div className={`container ${isActive ? 'active' : ''}`} id="container">
        <StoreContextProvider>
          <SignUpForm />
          <SignInForm />
          <Toggle
            onRegisterClick={handleRegisterClick}
            onLoginClick={handleLoginClick}
          />
        </StoreContextProvider>

      </div>
    </div>
  );
};

export default Login;
