import React from 'react'
import './Footer.scss'
import { assets } from '../../assets/assets'
const Footer = () => {
    return (
        <div className='footer' id='footer'>
            <div className="footer__contain">
                <div className="footer__contain-left">
                    <h2>About us</h2>
                    <p>Lorem ipsum dolor sit amet consectetur adipisicing elit. Exercitationem inventore a ducimus ut recusandae, maiores veniam, saepe quidem, consequuntur officia impedit. Minus error laudantium commodi laborum, explicabo hic consequatur deserunt.</p>
                </div>
                <div className="footer__contain-center">
                    <h2>Customer services</h2>
                    <ul className='footer__contain-center-list'>
                        <li><a href="#">Frequently asked questions</a></li>
                        <li><a href="#">Privacy policy</a></li>
                    </ul>
                </div>
                <div className="footer__contain-right">
                <h2>Contact us</h2>
                    <div className="footer__contain-right-social">
                        <img src={assets.instagramLogo} alt="" className="social-logo" />
                        <img src={assets.facebookLogo} alt="" className="social-logo" />
                    </div>
                </div>
            </div>
        </div>
    )
}

export default Footer
