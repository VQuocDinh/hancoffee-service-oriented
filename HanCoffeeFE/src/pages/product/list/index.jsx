import React, { useState, useEffect } from 'react'
import './List.css'
import { toast } from 'react-toastify'
import { assets } from '../../../assets/assets'
import axiosInstance from '../../../common/library/query'
import { BASE_URL } from '../../../config'
import { useNavigate, useParams } from 'react-router-dom'
import { PATH_DASHBOARD } from '../../../common/routes/path'

const List = () => {
    const [list, setList] = useState([])
    const navigate = useNavigate()

    const fetchList = async () => {
        try {
            const response = await axiosInstance.get(`/api/product/`)
            if (response.data.success) {
                // Chỉ lấy các sản phẩm có status = 0
                const filteredList = response.data.data.filter(
                    (item) => item.status === 0
                )
                setList(filteredList)
            } else {
                toast.error('Error fetching product list')
            }
        } catch (error) {
            console.error('Error fetching product list:', error)
            toast.error('Failed to fetch product list')
        }
    }

    const removeProduct = async (productId) => {
        try {
            const response = await axiosInstance.patch(
                `/api/product/${productId}`
            )

            if (response.data.success) {
                toast.success(response.data.message)
                // Fetch the updated list after successful removal
                fetchList()
            } else {
                toast.error('Error removing product')
            }
        } catch (error) {
            console.error('Error removing product:', error)
            toast.error('Failed to remove product')
        }
    }

    useEffect(() => {
        fetchList()
    }, [])

    const handleNavigate = () => {
        navigate(PATH_DASHBOARD.general.product.add)
    }

    const handleEditProduct = (productId) => {
        navigate(`${PATH_DASHBOARD.general.product.root}/${productId}`)
    }

    return (
        <div className="list-container">
            <h1>Products Management</h1>
            <div className="handle-button-end">
                <button className="btn-add" onClick={handleNavigate}>
                    Add Product
                </button>
            </div>
            <div className="list-table">
                <div className="list-table-format title">
                    <b>Name</b>
                    <b>Image</b>
                    <b>Price</b>
                    <b>Description</b>
                    <b>Quantity</b>
                    <b>Edit</b>
                    <b>Delete</b>
                </div>
                {list.map((item, index) => (
                    <div key={index} className="list-table-format">
                        <p>{item.name}</p>
                        <img
                            className="product-img"
                            src={item.image}
                            alt={item.name}
                        />
                        <p>{item.price}đ</p>
                        <p>{item.description}</p>
                        <p>{item.quantity}</p>
                        <div
                            className="action-icon"
                            onClick={() => handleEditProduct(item._id)}
                        >
                            <img src={assets.edit_icon} alt="Edit" />
                        </div>
                        <div
                            className="action-icon"
                            onClick={() => removeProduct(item._id)}
                        >
                            <img src={assets.delete_icon} alt="Delete" />
                        </div>
                    </div>
                ))}
            </div>
        </div>
    )
}

export default List
