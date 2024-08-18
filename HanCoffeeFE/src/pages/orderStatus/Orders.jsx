import React, { useState, useEffect } from 'react'
import axiosInstance from '../../common/library/query'
import './Orders.css'
import { toast } from 'react-toastify'

const Orders = () => {
    const [orders, setOrders] = useState([])
    const [activeTab, setActiveTab] = useState('Chờ xác nhận')
    const [selectedOrder, setSelectedOrder] = useState(null)
    const statuses = ['Chờ xác nhận', 'Đang giao hàng', 'Đã giao', 'Đã hủy']

    useEffect(() => {
        fetchOrders()
    }, [])

    const fetchOrders = async () => {
        try {
            const response = await axiosInstance.get('/api/order-status')
            if (response.data.success) {
                setOrders(response.data.data)
            } else {
                toast.error('Error fetching orders')
            }
        } catch (error) {
            console.error('Error fetching orders:', error)
        }
    }

    const handleStatusChange = async (orderId, newStatus) => {
        try {
            const response = await axiosInstance.patch(
                `/api/order-status/${orderId}`,
                { status: newStatus }
            )
            if (response.data.success) {
                toast.success('Order status updated successfully')
                fetchOrders()
                setSelectedOrder(null)
            } else {
                toast.error('Error updating order status')
            }
        } catch (error) {
            console.error('Error updating order status:', error)
            toast.error('Failed to update order status')
        }
    }

    return (
        <div className="orders-container">
            <h1>Order Management</h1>
            <div className="tabs">
                {statuses.map((status) => (
                    <div
                        key={status}
                        className={`tab ${
                            activeTab === status ? 'active' : ''
                        }`}
                        onClick={() => setActiveTab(status)}
                    >
                        {status}
                    </div>
                ))}
            </div>
            <div className="orders-columns">
                {statuses.map((status) => (
                    <div
                        key={status}
                        className={`orders-column ${
                            activeTab === status ? 'active' : ''
                        }`}
                    >
                        <h2>{status}</h2>
                        {orders
                            .filter((order) => order.status === status)
                            .map((order) => (
                                <div key={order._id} className="order-item">
                                    <div>{order.id}</div>
                                    <div>{order.idCategory}</div>
                                    <div>{order.idUser}</div>
                                    <div>{order.price}</div>
                                    <button
                                        onClick={() => setSelectedOrder(order)}
                                    >
                                        Chi tiết
                                    </button>
                                </div>
                            ))}
                    </div>
                ))}
            </div>
            {selectedOrder && (
                <div className="modal">
                    <div className="modal-content">
                        <span
                            className="close"
                            onClick={() => setSelectedOrder(null)}
                        >
                            &times;
                        </span>
                        <h2>Order Details</h2>
                        <div>Order ID: {selectedOrder.id}</div>
                        <div>Category ID: {selectedOrder.idCategory}</div>
                        <div>User ID: {selectedOrder.idUser}</div>
                        <div>Price: {selectedOrder.price}</div>
                        <div>Status: {selectedOrder.status}</div>
                        <div>
                            <select
                                value={selectedOrder.status}
                                onChange={(e) =>
                                    handleStatusChange(
                                        selectedOrder._id,
                                        e.target.value
                                    )
                                }
                            >
                                {statuses.map((status) => (
                                    <option key={status} value={status}>
                                        {status}
                                    </option>
                                ))}
                            </select>
                        </div>
                        <button
                            onClick={() =>
                                handleStatusChange(
                                    selectedOrder._id,
                                    selectedOrder.status
                                )
                            }
                        >
                            Update Status
                        </button>
                    </div>
                </div>
            )}
        </div>
    )
}

export default Orders
