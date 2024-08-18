import React, { useContext } from 'react'
import './Cart.css'
import { assets } from '../../assets/assets'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faTrash } from '@fortawesome/free-solid-svg-icons'
import { useNavigate } from 'react-router-dom'
import { StoreContext } from '../../context/StoreContext'
import { ToastContainer, toast, Bounce } from 'react-toastify'
import 'react-toastify/dist/ReactToastify.css';
const Cart = () => {
  const navigate = useNavigate()
  const { cartItems, removeFromCart, product_list, itemTotal, getTotalCartAmout } = useContext(StoreContext)
  const handleRemoveFromCart = async (itemId) => {
    const result = await removeFromCart(itemId)
    if (result) {
      notifySuccess()
    } else {
      notifyError()
    }
  }

  const notifySuccess = () => {
    toast.success("Remove from cart successfully", {
      position: "top-right",
      autoClose: 3000,
      hideProgressBar: true,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
      progress: undefined,
      theme: "light",
      transition: Bounce,
    })
  }

  const notifyError = () => {
    toast.error("Add to cart error", {
      position: "top-right",
      autoClose: 3000,
      hideProgressBar: true,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
      progress: undefined,
      theme: "light",
      transition: Bounce,
    })
  }
  return (
    <div className='cart'>
      {itemTotal === 0 ? (
        <>
          <div className="cart__empty">
            <img src={assets.cartEmpty} className='cart__empty-img' alt="" />
          </div>
        </>
      ) : (
        <>
          <div className="cart__item">
            <div className="cart__items-title">
              <h4>Item</h4>
              <h4>Name</h4>
              <h4>Price</h4>
              <h4>Quantity</h4>
              <h4>Size</h4>
              <h4>Total</h4>
              <h4>Remove</h4>
            </div>
            <hr />
            {product_list.map((item, index) => {
              if (cartItems[item._id] > 0) {
                return (
                  <>
                    <div className="cart__items-item">
                      <img src={item.image} alt="" className="cart__items-item-img" />
                      <p>{item.name}</p>
                      <p>${item.price}</p>
                      <p>{cartItems[item._id]}</p>
                      <p>M</p>
                      <p>{item.price * cartItems[item._id]}</p>
                      <p onClick={() => handleRemoveFromCart(item._id)}><FontAwesomeIcon icon={faTrash} /></p>
                    </div>
                    <hr />

                  </>

                )
              }
            })}
          </div>

          <div className="cart__bottom">
            <div className="cart__bottom-voucher">
              <span>HanCoffee Voucher</span>
              <a className='cart__bottom-voucher-link' href="#">Type or select code</a>
            </div>

            <hr />

            <div className="cart__bottom-order">
              <div className="cart__bottom-order-total">
                <span>Total Payment</span>
                <span>{getTotalCartAmout()} VND</span>
              </div>

              <button onClick={() => navigate('/placeOrder')} className="cart__bottom-order-btn">Order</button>

            </div>


          </div>
          <ToastContainer />
        </>
      )}
    </div>

  )
}

export default Cart