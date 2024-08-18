import React from 'react'
import './Header.scss'
const Header = () => {
  return (
    <div className='header'>
        <div className="header__content">
            <h2 className='header__content-title'>enjoy your coffee</h2>
            <p className="header__content-desc">A dark brown powder with a strong flavour and smell that is made by crushing dark beans from a tropical bush and used to make a drink.</p>
            <button className='header__content-btn-buy'>buy now</button>
        </div>
    </div>
  )
}

export default Header
