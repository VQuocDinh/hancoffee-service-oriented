// Order.jsx
import axios from 'axios';
import React, { useContext, useEffect, useState } from 'react';
import { assets } from '../../assets/assets'

import './Order.scss';
import { StoreContext } from '../../context/StoreContext';


const Order = () => {
  const { url, token } = useContext(StoreContext)
  const [data, setData] = useState([])
  const fetchOrders = async () => {
    try {
      const response = await axios.post(
        `${url}/api/order/userOrders`,
        {},
        { headers: { token } }
      );
      if (response.data.success) {
        setData(response.data.data);
        console.log(response.data);
      } else {
        console.log(response.data);
        alert("Error");
      }
    } catch (error) {
      console.error('Error fetching orders: ', error);
    }
  }

  useEffect(() => {
    if (token) {
      fetchOrders()
    }
  }, [token])
  return (
    <div className='order'>
      <div className="order__status">
        <ul className="order__status-list">
          <li className="order__status-item"><h4>Tất cả</h4></li>
          <li className="order__status-item">Chờ xác nhận</li>
          <li className="order__status-item">Đang vận chuyển</li>
          <li className="order__status-item">Đã giao</li>
          <li className="order__status-item">Đã hủy</li>
        </ul>
        <hr />
      </div>

      <div className="order__content">
        {data.map((order, index) => {
          return (
            <div key={index} className="order__item">
              {order.items.map((item, index) => {
                return (
                  <>
                    <div className="order__item-header">
                      <img className="order__item-img" src={item.image} alt="Product" />
                      <div className="order__item-infor">
                        <h4 className="order__item-name">{item.name}</h4>
                        <div className="order__item-size">
                          <span>Size</span>
                          <span>L</span>
                        </div>
                        <div className="order__item-quantity">
                          <span>x</span>
                          <span>{item.quantity}</span>
                        </div>
                      </div>
                      <div className="order__item-price">{item.price} VND</div>
                    </div>
                    <hr />
                  </>
                )

              })}



              <div className="order__item-footer">
                <div className="order__total">
                  <span>Thành tiền</span>
                  <span>{order.totalPrice} VND</span>
                </div>
                <button className="order__cancel-button">Hủy đơn</button>
              </div>
            </div>
          )
        })}

      </div>
    </div>
  );
}

export default Order;
