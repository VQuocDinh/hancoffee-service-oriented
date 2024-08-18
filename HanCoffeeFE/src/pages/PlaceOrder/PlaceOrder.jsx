import React, { useContext, useState } from 'react'
import axios from 'axios';
import { useNavigate } from 'react-router-dom'
import './PlaceOrder.css'
import { StoreContext } from '../../context/StoreContext';
const Order = () => {
  const navigate = useNavigate();
  const { getTotalCartAmout, url, token, product_list, cartItems } = useContext(StoreContext)
  const [data, setData] = useState({
    name: "",
    phone: "",
    address: ""
  })

  const oncChangeHandler = (event) => {
    const name = event.target.name
    const value = event.target.value
    setData(data => ({ ...data, [name]: value }))

  }
  const placeOrder = async (event) => {
    event.preventDefault();
    let orderItem = [];
    product_list.map((item) => {
      if (cartItems[item._id] > 0) {
        let itemInfo = item
        itemInfo["quantity"] = cartItems[item._id]
        orderItem.push(itemInfo)
      }
    })
    console.log(orderItem)
    let orderData = {
      totalPrice: getTotalCartAmout() + 20000,
      items: orderItem
    }

    try {
      let response = await axios.post(`${url}/api/order/place`, orderData, {
        headers: { token }
      });
      if (response.data.success) {
        alert("Order placed successfully");
        navigate('/order');
      } else {
        alert("Error placing order");
      }
    } catch (error) {
      console.error('Error placing order:', error);
      alert("Error placing order");
    }



  }
  return (
    <form onSubmit={placeOrder} className='place-order'>
      <div className="order__left">
        <div className="order__left-delivery">
          <h2 className="order__left-delivery-title">Delivery Information</h2>
          <form className="order__left-delivery-type">
            <input required name='name' onChange={oncChangeHandler} value={data.name} className="delivery__name" placeholder='Name' />
            <input required name='phone' onChange={oncChangeHandler} value={data.phone} className="delivery__phone" placeholder='Phone' />
            <input required name='address' onChange={oncChangeHandler} value={data.address} className="delivery__address" placeholder='Address' />
          </form>
        </div>

        <div className="order__left-pay">
          <h2 className="order__left-pay-title">Payment Methods</h2>
          <div className="order__left-pay-list">
            <div className="order__left-pay-list-item">
              <input type="radio" />
              <label htmlFor="">Tiền mặt</label>
            </div>

            <div className="order__left-pay-list-item">
              <input type="radio" />
              <label htmlFor="">ZaloPay</label>
            </div>

            <div className="order__left-pay-list-item">
              <input type="radio" />
              <label htmlFor="">MoMo</label>
            </div>

            <div className="order__left-pay-list-item">
              <input type="radio" />
              <label htmlFor="">Ngân hàng</label>
            </div>
          </div>
        </div>
      </div>

      <div className="order__right">

        <h2>Total Price</h2>
        <div className="order__right-price">
          <div className="order__right-price-delivery">
            <div className="order__total">
              <span>Subtotal</span><span className='total'>{getTotalCartAmout()} VND</span>
            </div>
            <hr />
            <div className="order__total">
              <span>Delivery charge</span><span className='total'> 20000 VND</span>
            </div>
            <hr />

            <div className="order__total">
              <h4>Total</h4><span className='total'> {getTotalCartAmout()+20000} VND</span>
            </div>
            <hr />

          </div>
        </div>


        <div className="order__right-total">
          <div className="order__right-total-left">
            <span>Total </span><span>{getTotalCartAmout() + 20000}</span>
          </div>

          <button type='submit' className='order__right-total-btn'>Order</button>
        </div>
      </div>
    </form>
  )
}

export default Order
