import React, { useState, useEffect } from 'react'
import axiosInstance from '../../common/library/query'
import './Users.css'
import { toast } from 'react-toastify'

const Users = () => {
    const [users, setUsers] = useState([])
    const [roles, setRoles] = useState([]) // Fetch roles dynamically
    const [selectedRoles, setSelectedRoles] = useState({}) // Track selected roles

    useEffect(() => {
        fetchUsers()
        fetchRoles() // Fetch roles on component mount
    }, [])

    const fetchUsers = async () => {
        try {
            const response = await axiosInstance.get('/api/user/')
            if (response.data.success) {
                // Sort users to put admins (role 0) first
                const sortedUsers = response.data.data.sort(
                    (a, b) => a.role - b.role
                )
                setUsers(sortedUsers)
                // Initialize selectedRoles state
                const initialRoles = {}
                sortedUsers.forEach((user) => {
                    initialRoles[user._id] = user.role
                })
                setSelectedRoles(initialRoles)
            } else {
                toast.error('Error fetching users')
            }
        } catch (error) {
            console.error('Error fetching users:', error)
            toast.error('Failed to fetch users')
        }
    }

    const fetchRoles = async () => {
        // Assuming you have an endpoint to fetch roles
        // const response = await axiosInstance.get('/api/roles/')
        // if (response.data.success) {
        //     setRoles(response.data.data)
        // } else {
        //     toast.error('Error fetching roles')
        // }
        // Mock roles for now
        setRoles([
            { value: 0, label: 'Admin' },
            { value: 1, label: 'Manager' },
            { value: 2, label: 'User' },
        ])
    }

    const handleRoleChange = (userId, newRole) => {
        setSelectedRoles({
            ...selectedRoles,
            [userId]: parseInt(newRole),
        })
    }

    const handleUpdateRole = async (userId) => {
        const newRole = selectedRoles[userId]
        try {
            const response = await axiosInstance.put(`/api/user/${userId}`, {
                role: newRole,
            })
            if (response.data.success) {
                toast.success('Role updated successfully')
                fetchUsers()
            } else {
                toast.error('Error updating role')
            }
        } catch (error) {
            console.error('Error updating role:', error)
            toast.error('Failed to update role')
        }
    }

    return (
        <div className="users-container">
            <h1>Users Management</h1>
            <div className="users-table">
                <div className="users-table-header">
                    <div>Username</div>
                    <div>Phone</div>
                    <div>Account Created</div>
                    <div>Image</div>
                    <div>Role</div>
                    <div>Update</div>
                </div>
                {users.map((user) => (
                    <div key={user._id} className="users-table-row">
                        <div>{user.email}</div>
                        <div>{user.phone || 'Chưa cập nhật'}</div>
                        <div>
                            {new Date(user.createdAt).toLocaleDateString()}
                        </div>
                        <div>
                            {user.image ? (
                                <img
                                    className="user-img"
                                    src={user.image}
                                    alt={user.username}
                                />
                            ) : (
                                'Chưa cập nhật'
                            )}
                        </div>
                        <div>
                            <select
                                value={selectedRoles[user._id]}
                                onChange={(e) =>
                                    handleRoleChange(user._id, e.target.value)
                                }
                            >
                                {roles.map((role) => (
                                    <option key={role.value} value={role.value}>
                                        {role.label}
                                    </option>
                                ))}
                            </select>
                        </div>
                        <div>
                            <button onClick={() => handleUpdateRole(user._id)}>
                                Update
                            </button>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    )
}

export default Users
