import React, { useContext, useEffect, useState } from 'react'
import './ProductDetail.css'
import { assets } from '../../assets/assets'
import { Navigate, useNavigate, useParams } from 'react-router-dom'
import { StoreContext } from '../../context/StoreContext'

const ProductDetail = () => {
  const navigate = useNavigate();
  const {product_list} = useContext(StoreContext)
  const {productId} = useParams()
  const [product,setProduct] = useState(null)
  useEffect(()=>{
    const selectedProduct = product_list.find(item => item._id === productId)
    setProduct(selectedProduct)
  },[productId, product_list])
  
  if(!product){
    return <div>Loading...</div>;
  }
  return (
    <div className='product__detail'>
      <div>
        <h1  className="product__detail-name">{product.name}</h1>
      </div>

      <div className="product__detail-body">
        <div className="product__detail-img">
          <img src={product.image} alt="" />

        </div>
        <div className="product__detail-contain">
          <div className="product__detail-content">



            <div className="product__detail-content-desc">
              <p>{product.description}</p>
            </div>

            <div className="product__detail-content-size">
              <span>Size: </span>
              <ul className="size__list">
                <li className="size__list-item">S</li>
                <li className="size__list-item">M</li>
                <li className="size__list-item">L</li>
              </ul>
            </div>

            <div className="product__detail-content-price">
              <span>Giá: </span>
              <div className="price__product">{product.price} VND</div>
            </div>


          </div>

          <button onClick={()=> navigate('/placeOrder')} className='product__detail-content-buy-btn'>Đặt mua ngay</button>

        </div>


      </div>
    </div>
  )
}

export default ProductDetail
