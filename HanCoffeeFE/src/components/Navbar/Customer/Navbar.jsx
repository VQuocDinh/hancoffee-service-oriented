import React, { useContext, useRef, useState } from 'react'
import './Navbar.css'
import { assets } from '../../../assets/assets'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faCartShopping, faSearch } from '@fortawesome/free-solid-svg-icons'
import { useNavigate } from 'react-router-dom'
import { StoreContext } from '../../../context/StoreContext'

const Navbar = () => {
    const navigate = useNavigate()
    const { productRef } = useRef(null)
    const [menu, setMenu] = useState('home')
    const { token, setToken, itemTotal, setSearchQuery: setSearchQueryGlobal } = useContext(StoreContext)
    const [searchQuery, setSearchQuery] = useState("")

    const handleSearchChange = (event) => {
        setSearchQuery(event.target.value)
        setSearchQueryGlobal(event.target.value)
    }
    console.log(searchQuery)
    const logout = () => {
        localStorage.removeItem("token")
        setToken("")
        navigate('/')
    }

    const handleSearchSubmit = (event) => {
        event.preventDefault();
        const productElement = document.getElementById('product');
        if (productElement) {
            productElement.scrollIntoView({ behavior: 'smooth' });
        }
    };


    return (
        <div className="navbar__customer">
            <div className="navbar__wrap">
                {/* navbar logo */}
                <div onClick={() => navigate('/')} className="navbar__logo">
                    <img
                        src={assets.logoBranch}
                        alt="logo"
                        className="navbar__logo-branch"
                    />
                </div>

                {/* navbar menu */}
                <ul className="navbar__menu">
                    <li
                        onClick={() => setMenu('home')}
                        className={menu === 'home' ? 'navbar__active' : ''}
                    >
                        <a href="/">home</a>
                    </li>
                    <li
                        onClick={() => setMenu('menu')}
                        className={menu === 'menu' ? 'navbar__active' : ''}
                    >
                        <a href="#menu">menu</a>
                    </li>
                    <li
                        onClick={() => setMenu('product')}
                        className={menu === 'product' ? 'navbar__active' : ''}
                    >
                        <a href="#product">product</a>
                    </li>
                    <li
                        onClick={() => setMenu('download')}
                        className={menu === 'download' ? 'navbar__active' : ''}
                    >
                        <a href="#download">download</a>
                    </li>
                </ul>

                {/* navbar search - cart - login */}
                <div className="navbav__right">

                    {/* navbar search */}
                    <div className="navbav__right-search">
                        <form onSubmit={handleSearchSubmit}>
                            <input
                                type="text"
                                className="search__input"
                                placeholder="Tìm sản phẩm"
                                value={searchQuery}
                                onChange={handleSearchChange}
                            />
                        </form>
                        <div className="search__history">
                            <h3 class="search__history-heading">
                                Lịch sử tìm kiếm
                            </h3>
                            <ul class="search__history-list">
                                <li class="search__history-item">
                                    <a href="">Milk Coffe</a>
                                </li>
                                <li class="search__history-item">
                                    <a href="">Capuchino</a>
                                </li>
                            </ul>
                        </div>
                        <a href="#product" className="search__icon">
                            <FontAwesomeIcon icon={faSearch} />
                        </a>
                    </div>

                    {/* navbar cart */}
                    <a onClick={() => navigate('/cart')} className="navbav__right-cart">
                        <i className="cart__icon">
                            <FontAwesomeIcon icon={faCartShopping} />
                        </i>
                        <span className="cart__number-product">{itemTotal}</span>
                    </a>

                    {/* navbar login */}
                    <div className="navbav__right-user">
                        {!token ? (
                            <>
                                <img src={assets.userImg} alt="" className="user__img" />
                                <span onClick={() => {
                                    navigate('/login')
                                    // setIsAuthenticated(true)
                                }} className="user__name">Đăng nhập</span>



                            </>
                        ) : (
                            <>
                                <img
                                    src={assets.userActive}
                                    alt=""
                                    className="user__img"
                                />
                                {/* <span className="user__name">quốc dinh</span> */}

                                {/* login list drop down */}
                                <div className="user__select">
                                    <ul className="user__select-list">
                                        <li
                                            onClick={() => navigate('/user')}
                                            className="user__select-item"
                                        >
                                            Tài khoản của tôi
                                        </li>
                                        <li onClick={() => navigate('/order')} className="user__select-item">Đơn mua</li>
                                        <li onClick={logout} className="user__select-item">Đăng xuất</li>
                                    </ul>
                                </div>
                            </>
                        )}


                    </div>
                </div>
            </div>
        </div>
    )
}

export default Navbar
