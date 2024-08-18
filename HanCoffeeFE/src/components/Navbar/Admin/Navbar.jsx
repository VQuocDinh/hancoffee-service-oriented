import React, { useState } from 'react'
import './Navbar.css'
import { assets } from '../../../assets/assets'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faCartShopping, faSearch } from '@fortawesome/free-solid-svg-icons';
const Navbar = () => {
    return (
        <div className="navbar">
            <img src={assets.logoBranch} alt="" className="logo-branch" />
            <img src={assets.userImg} alt="" className="profile" />
            {/* <img src={assets.notification_icon} alt="" className="notification" /> */}
        </div>
    )
}

export default Navbar
