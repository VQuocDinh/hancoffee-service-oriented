import React, { useState, useEffect } from 'react'
import axiosInstance from '../../common/library/query'
import './Staff.css'
import { toast } from 'react-toastify'

const Staff = () => {
    const [staffList, setStaffList] = useState([])

    useEffect(() => {
        fetchStaffList()
    }, [])

    const fetchStaffList = async () => {
        try {
            const response = await axiosInstance.get('/api/staff/')
            if (response.data.success) {
                const sortedStaff = response.data.data.filter(
                    (user) => user.role === 1
                )
                setStaffList(sortedStaff)
            } else {
                toast.error('Error fetching staff list')
            }
        } catch (error) {
            console.error('Error fetching staff list:', error)
            toast.error('Failed to fetch staff list')
        }
    }

    return (
        <div className="staff-container">
            <h1>Staff Management</h1>
            <div className="staff-table">
                <div className="staff-table-header">
                    <div>Username</div>
                    <div>Phone</div>
                    <div>Account Created</div>
                    <div>Image</div>
                    <div>Role</div>
                </div>
                {staffList.map((staff) => (
                    <div key={staff._id} className="staff-table-row">
                        <div>{staff.email}</div>
                        <div>{staff.phone || 'Chưa cập nhật'}</div>
                        <div>
                            {new Date(staff.createdAt).toLocaleDateString()}
                        </div>
                        <div>
                            {staff.image ? (
                                <img
                                    className="staff-img"
                                    src={staff.image}
                                    alt={staff.username}
                                />
                            ) : (
                                'Chưa cập nhật'
                            )}
                        </div>
                        <div>Manager</div>
                    </div>
                ))}
            </div>
        </div>
    )
}

export default Staff
