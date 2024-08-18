import React, { useEffect, useState } from 'react'
import axiosInstance from '../../../common/library/query'
import './EditCategory.css'
import { toast } from 'react-toastify'
import { assets } from '../../../assets/assets'
import { useNavigate, useParams } from 'react-router-dom'
import { PATH_DASHBOARD } from '../../../common/routes/path'

const EditCategory = () => {
    const navigate = useNavigate()
    const { categoryId } = useParams()
    const [image, setImage] = useState(null)
    const [data, setData] = useState({
        name: '',
        status: '0',
    })

    useEffect(() => {
        const fetchCategory = async () => {
            try {
                const response = await axiosInstance.get(
                    `/api/category/${categoryId}`
                )
                if (response.data.success) {
                    const category = response.data.data
                    setData({
                        name: category.name,
                        status: Number(category.status), // Ensure status is string
                    })
                    setImage(category.image)
                } else {
                    toast.error(response.data.message)
                }
            } catch (error) {
                console.error('Error fetching category:', error)
                toast.error('Failed to fetch category')
            }
        }

        fetchCategory()
    }, [categoryId])

    const onChangeHandler = (event) => {
        const { name, value } = event.target
        setData((prevData) => ({
            ...prevData,
            [name]: value,
        }))
    }

    const onSubmitHandler = async (e) => {
        e.preventDefault()
        const formData = new FormData()
        formData.append('name', data.name)
        formData.append('status', Number(data.status))
        if (image && typeof image !== 'string') {
            formData.append('image', image)
        }

        try {
            const response = await axiosInstance.put(
                `/api/category/${categoryId}`,
                formData
            )
            if (response.data.success) {
                toast.success(response.data.message)
                navigate(PATH_DASHBOARD.general.category.list)
            } else {
                toast.error(response.data.message)
            }
        } catch (error) {
            console.error('Error editing category:', error)
            toast.error('Failed to edit category')
        }
    }

    return (
        <div className="edit-category">
            <form className="flex-col" onSubmit={onSubmitHandler}>
                <div className="edit-img-upload flex-col">
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
                <div className="edit-category-name flex-col">
                    <p>Category Name</p>
                    <input
                        onChange={onChangeHandler}
                        value={data.name}
                        type="text"
                        name="name"
                        placeholder="Type here"
                        required
                    />
                </div>

                <button type="submit" className="edit-btn">
                    UPDATE
                </button>
            </form>
        </div>
    )
}

export default EditCategory
