import React, { useState, useEffect } from 'react'
import axiosInstance from '../../common/library/query'
import './Customer.css'
import { toast } from 'react-toastify'

const Customer = () => {
    const [customerList, setCustomerList] = useState([])

    useEffect(() => {
        fetchCustomerList()
    }, [])

    const fetchCustomerList = async () => {
        try {
            const response = await axiosInstance.get('/api/customer/')
            if (response.data.success) {
                const sortedCustomers = response.data.data.filter(
                    (user) => user.role === 2
                )
                setCustomerList(sortedCustomers)
            } else {
                toast.error('Error fetching customer list')
            }
        } catch (error) {
            console.error('Error fetching customer list:', error)
            toast.error('Failed to fetch customer list')
        }
    }

    return (
        <div className="customer-container">
            <h1>Customer Management</h1>
            <div className="customer-table">
                <div className="customer-table-header">
                    <div>Username</div>
                    <div>Phone</div>
                    <div>Account Created</div>
                    <div>Image</div>
                    <div>Role</div>
                </div>
                {customerList.map((customer) => (
                    <div key={customer._id} className="customer-table-row">
                        <div>{customer.email}</div>
                        <div>{customer.phone || 'Chưa cập nhật'}</div>
                        <div>
                            {new Date(customer.createdAt).toLocaleDateString()}
                        </div>
                        <div>
                            {customer.image ? (
                                <img
                                    className="customer-img"
                                    src={customer.image}
                                    alt={customer.username}
                                />
                            ) : (
                                'Chưa cập nhật'
                            )}
                        </div>
                        <div>User</div>
                    </div>
                ))}
            </div>
        </div>
    )
}

export default Customer
