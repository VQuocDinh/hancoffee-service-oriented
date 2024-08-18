import React, { useEffect, useState } from 'react'
import './Edit.css'
import { assets } from '../../../assets/assets'
import { toast } from 'react-toastify'
import { useNavigate, useParams } from 'react-router-dom'
import axiosInstance from '../../../common/library/query'
import { PATH_DASHBOARD } from '../../../common/routes/path'

const Edit = () => {
    const navigate = useNavigate()
    const { productId } = useParams() // Get product ID from URL
    const [image, setImage] = useState(null)
    const [categories, setCategories] = useState([])
    const [data, setData] = useState({
        name: '',
        description: '',
        category: '',
        price: '',
        status: '0',
        quantity: '',
    })

    useEffect(() => {
        const fetchCategories = async (productCategory) => {
            try {
                const response = await axiosInstance.get('/api/category/')
                if (response.data.success) {
                    let fetchedCategories = response.data.data
                    // Đưa danh mục của sản phẩm lên đầu
                    if (productCategory) {
                        fetchedCategories = fetchedCategories.filter(
                            (category) => category._id !== productCategory
                        )
                        const currentCategory = response.data.data.find(
                            (category) => category._id === productCategory
                        )
                        if (currentCategory) {
                            fetchedCategories.unshift(currentCategory)
                        }
                    }
                    setCategories(fetchedCategories)
                } else {
                    toast.error(response.data.message)
                }
            } catch (error) {
                console.error('Error fetching categories:', error)
                toast.error('Failed to fetch categories')
            }
        }

        const fetchProduct = async () => {
            try {
                const response = await axiosInstance.get(
                    `/api/product/${productId}`
                )
                if (response.data.success) {
                    const product = response.data.data
                    setData({
                        name: product.name,
                        description: product.description,
                        category: product.idCategory,
                        price: product.price,
                        status: Number(product.status),
                        quantity: product.quantity,
                    })
                    setImage(product.image)
                    fetchCategories(product.idCategory)
                } else {
                    toast.error(response.data.message)
                }
            } catch (error) {
                console.error('Error fetching product:', error)
                toast.error('Failed to fetch product')
            }
        }

        fetchProduct()
    }, [productId])

    const onChangeHandler = (event) => {
        const name = event.target.name
        let value = event.target.value

        if (name === 'price' && value < 0) {
            toast.error('Price cannot be negative')
            value = ''
        }

        setData((prevData) => ({ ...prevData, [name]: value }))
    }

    const onSubmitHandler = async (event) => {
        event.preventDefault()

        const formData = new FormData()
        formData.append('name', data.name)
        formData.append('description', data.description)
        formData.append('price', Number(data.price))
        formData.append('status', Number(data.status))
        formData.append('idCategory', data.category)
        if (image && typeof image !== 'string') {
            formData.append('image', image)
        }
        formData.append('quantity', Number(data.quantity))

        try {
            const response = await axiosInstance.put(
                `/api/product/${productId}`,
                formData
            )
            if (response.data.success) {
                toast.success(response.data.message)
                navigate(PATH_DASHBOARD.general.product.list)
            } else {
                toast.error(response.data.message)
            }
        } catch (error) {
            console.error('Error editing product:', error)
            toast.error('Failed to edit product')
        }
    }

    return (
        <div className="edit">
            <form className="edit-flex-col" onSubmit={onSubmitHandler}>
                <div className="edit-img-upload edit-flex-col">
                    <p>Upload Image</p>
                    <label htmlFor="image">
                        <img
                            src={
                                image
                                    ? typeof image === 'string'
                                        ? image
                                        : URL.createObjectURL(image)
                                    : assets.upload_area
                            }
                            alt=""
                        />
                    </label>
                    <input
                        onChange={(e) => setImage(e.target.files[0])}
                        type="file"
                        id="image"
                        hidden
                    />
                </div>
                <div className="edit-product-name edit-flex-col">
                    <p>Name</p>
                    <input
                        onChange={onChangeHandler}
                        value={data.name}
                        type="text"
                        name="name"
                        placeholder="Type here"
                        required
                    />
                </div>
                <div className="edit-product-description edit-flex-col">
                    <p>Description</p>
                    <textarea
                        onChange={onChangeHandler}
                        value={data.description}
                        name="description"
                        rows="6"
                        placeholder="Write content here"
                        required
                    ></textarea>
                </div>

                <div className="edit-category-price-quantity edit-flex-row">
                    <div className="edit-categories edit-flex-col">
                        <p>Category</p>
                        <select
                            onChange={onChangeHandler}
                            value={data.category}
                            name="category"
                            required
                        >
                            {categories.map((category) => (
                                <option key={category._id} value={category._id}>
                                    {category.name}
                                </option>
                            ))}
                        </select>
                    </div>
                    <div className="edit-price edit-flex-col">
                        <p>Price</p>
                        <input
                            onChange={onChangeHandler}
                            value={data.price}
                            type="number"
                            name="price"
                            placeholder="Type here"
                            required
                        />
                    </div>
                    <div className="edit-quantity edit-flex-col">
                        <p>Quantity</p>
                        <input
                            onChange={onChangeHandler}
                            value={data.quantity}
                            type="number"
                            name="quantity"
                            placeholder="Type here"
                            required
                        />
                    </div>
                </div>
                <button type="submit" className="edit-btn">
                    UPDATE
                </button>
            </form>
        </div>
    )
}

export default Edit
