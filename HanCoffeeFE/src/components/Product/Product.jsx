import React, { useContext, useState } from 'react'
import { StoreContext } from '../../context/StoreContext'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faClose, faPlusCircle } from '@fortawesome/free-solid-svg-icons'
import { useNavigate } from 'react-router-dom'
import { ToastContainer, toast, Bounce } from 'react-toastify'
import 'react-toastify/dist/ReactToastify.css';
import './Product.scss'
import Modal from 'react-modal';

const Product = ({ category }) => {
  // declare hook need to be used
  const navigate = useNavigate();
  const { product_list, addToCart, searchQuery } = useContext(StoreContext)
  const [selectedProduct, setSelectedProduct] = useState(null)
  const [showModal, setShowModal] = useState(false)

  // open/close modal product detail
  const handleClose = () => setShowModal(false);
  const handleOpen = (product, event) => {
    event.stopPropagation()
    setSelectedProduct(product)
    setShowModal(true)
  };

  // handle add product
  const handleAddToCart = async (itemId) => {
    const result = await addToCart(itemId);
    if (result) {
      notifySuccess();
      handleClose();
    } else {
      handleClose();
      notifyError();
    }
  };



const normalizeString = (str) => {
  return str
    .normalize('NFD')
    .replace(/[\u0300-\u036f]/g, '') // Remove accents
    .toLowerCase();
};
  // filter product from search
  const filteredProducts = product_list.filter(item =>
    normalizeString(item.name).includes(normalizeString(searchQuery))
  );
  const notifySuccess = () => {
    toast.success("Add to cart successfully", {
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

    <div className='product' id='product'>
      <h1 className='product__heading'>reach for your favorite beverage</h1>

      <div className="product__list">
        {filteredProducts.map((item, index) => {
          if (category === "All" || category === item.idCategory) {
            return (

              <div onClick={() => navigate(`/product/${item._id}`)} className="product__list-item">
                <img src={item.image} alt="" className="item-img" />
                <div className="item__content">
                  <div className="item__content-name">
                    <p>
                      {item.name}
                    </p>
                  </div>
                  <div className="item__content-price">
                    <span>
                      {item.price} VND
                    </span>
                  </div>


                </div>

                <div onClick={(event) => handleOpen(item, event)} className="item__content-add-cart">
                  <FontAwesomeIcon icon={faPlusCircle} />

                </div>
              </div>
            )
          }

        })}
      </div>
      <ToastContainer />

      <Modal
        isOpen={showModal}
        onRequestClose={handleClose}
        contentLabel="Thông báo"
        className="product_modal"
        overlayClassName="product_modal-overlay"
      >
        <div className="product_modal-header">
          <p>Thêm sản phẩm vào giỏ hàng</p>
          <i onClick={handleClose} className='product__modal-close'>
            <FontAwesomeIcon icon={faClose} />
          </i>
        </div>

        {selectedProduct && (
          <div className='product_modal-body'>
            <img src={selectedProduct.image} alt="" className="product_modal-body-img" />
            <h3 className='product_modal-body-name'>{selectedProduct.name}</h3>
            <div className="product__detail-content-desc">
              <p>Lorem ipsum dolor sit amet consectetur adipisicing elit. Pariatur sint repudiandae, asperiores nobis veritatis autem omnis fugiat id quo, eum consectetur ex quis necessitatibus accusantium. Asperiores libero sapiente iste architecto.</p>
            </div>
            <div className="product__detail-content-size">
              <span>Size: </span>
              <ul className="size__list">
                <li className="size__list-item">S</li>
                <li className="size__list-item">M</li>
                <li className="size__list-item">L</li>
              </ul>
            </div>
            <div className="product_modal-body-quantity">
              <span>Số lượng</span>
              <input className='product__modal-input-quantity' type="Number" />

            </div>
            <p className='product_modal-body-price'>${selectedProduct.price}</p>
            <button onClick={() => {
              handleAddToCart(selectedProduct._id)
            }} className='product_modal-body-add btn-main'>Thêm</button>
          </div>
        )}

      </Modal>
    </div>

  )
}

export default Product
