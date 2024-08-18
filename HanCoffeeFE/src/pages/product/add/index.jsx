import React, { useEffect, useState } from 'react'
import './Add.css'
import { assets } from '../../../assets/assets'
import { toast } from 'react-toastify'
import { useNavigate } from 'react-router-dom'
import axiosInstance from '../../../common/library/query'
import { PATH_DASHBOARD } from '../../../common/routes/path'

const Add = () => {
    const navigate = useNavigate()
    const [image, setImage] = useState(null)
    const [categories, setCategories] = useState([])
    const [data, setData] = useState({
        name: '',
        description: '',
        category: '', // Initially empty to ensure it is set correctly
        price: '',
        status: '0',
        quantity: '', // Add quantity to state
    })

    useEffect(() => {
        const fetchCategories = async () => {
            try {
                const response = await axiosInstance.get('/api/category/')
                if (response.data.success) {
                    setCategories(response.data.data)
                    if (response.data.data.length > 0) {
                        setData((prevData) => ({
                            ...prevData,
                            category: response.data.data[0]._id,
                        }))
                    }
                } else {
                    toast.error(response.data.message)
                }
            } catch (error) {
                console.error('Error fetching categories:', error)
                toast.error('Failed to fetch categories')
            }
        }

        fetchCategories()
    }, [])

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

        if (!image) {
            toast.error('Please upload an image')
            return
        }

        const formData = new FormData()
        formData.append('name', data.name)
        formData.append('description', data.description)
        formData.append('price', Number(data.price))
        formData.append('status', Number(data.status))
        formData.append('idCategory', data.category) // Add category name
        formData.append('image', image)
        formData.append('quantity', Number(data.quantity))

        try {
            const response = await axiosInstance.post('/api/product/', formData)
            if (response.data.success) {
                toast.success(response.data.message)
                setData({
                    name: '',
                    description: '',
                    category: categories.length > 0 ? categories[0]._id : '',
                    price: '',
                    status: '0',
                    quantity: '', // Reset quantity
                })
                setImage(null)
                handleNavigate()
            } else {
                toast.error(response.data.message)
            }
        } catch (error) {
            console.error('Error adding product:', error)
            toast.error('Failed to add product')
        }
    }

    const handleNavigate = () => {
        navigate(PATH_DASHBOARD.general.product.list)
    }

    return (
        <div className="add">
            <form className="flex-col" onSubmit={onSubmitHandler}>
                <div className="add-img-upload flex-col">
                    <p>Upload Image</p>
                    <label htmlFor="image">
                        <img
                            src={
                                image
                                    ? URL.createObjectURL(image)
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
                        required
                    />
                </div>
                <div className="add-product-name flex-col">
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
                <div className="add-product-description flex-col">
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

                <div className="add-category-price-quantity flex-row">
                    <div className="add-categories flex-col">
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
                    <div className="add-price flex-col">
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
                    <div className="add-quantity flex-col">
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
                <button type="submit" className="add-btn">
                    ADD
                </button>
            </form>
        </div>
    )
}

export default Add
