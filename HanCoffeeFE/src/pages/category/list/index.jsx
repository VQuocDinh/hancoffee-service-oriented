import React, { useEffect, useState } from 'react'
import axiosInstance from '../../../common/library/query'
import { useNavigate } from 'react-router-dom'
import './ListCategory.css'
import { toast } from 'react-toastify'
import { PATH_DASHBOARD } from '../../../common/routes/path'
import { BASE_URL } from '../../../config'
import { assets } from '../../../assets/assets'

const CategoryList = () => {
    const [categories, setCategories] = useState([])
    const navigate = useNavigate()

    useEffect(() => {
        fetchCategories()
    }, [])

    const fetchCategories = async () => {
        const response = await axiosInstance.get('/api/category/')
        if (response.data.success) {
            // Chỉ lấy các loại sản phẩm có status = 0
            const filteredList = response.data.data.filter(
                (item) => item.status === 0
            )
            setCategories(filteredList)
        } else {
            toast.error('Error fetching category list')
        }
    }

    const removeCategory = async (categoryId) => {
        try {
            const response = await axiosInstance.patch(
                `/api/category/${categoryId}`
            )

            if (response.data.success) {
                toast.success(response.data.message)
                // Fetch the updated list after successful removal
                fetchCategories()
            } else {
                toast.error('Error removing category')
            }
        } catch (error) {
            console.error('Error removing category:', error)
            toast.error('Failed to remove category')
        }
    }

    const handleEditCategory = (categoryId) => {
        navigate(`${PATH_DASHBOARD.general.category.root}/${categoryId}`)
    }

    const handleAddCategory = () => {
        navigate(PATH_DASHBOARD.general.category.add)
    }

    return (
        <div className="category-list-container">
            <h1>Category Management</h1>
            <button
                className="category-list-btn-add"
                onClick={handleAddCategory}
            >
                Add Category
            </button>
            <div className="category-list-table">
                <div className="category-list-table-header">
                    <b>Name</b>
                    <b>Image</b>
                    <b>Edit</b>
                    <b>Delete</b>
                </div>
                {categories.map((item, index) => (
                    <div key={index} className="category-list-table-row">
                        <p>{item.name}</p>
                        <img
                            className="category-list-product-img"
                            src={item.image}
                            alt={item.name}
                        />
                        <div
                            className="category-list-action-icon"
                            onClick={() => handleEditCategory(item._id)}
                        >
                            <img src={assets.edit_icon} alt="Edit" />
                        </div>
                        <div
                            className="category-list-action-icon"
                            onClick={() => removeCategory(item._id)}
                        >
                            <img src={assets.delete_icon} alt="Delete" />
                        </div>
                    </div>
                ))}
            </div>
        </div>
    )
}

export default CategoryList
