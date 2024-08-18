import React from 'react'
import { ToastContainer } from 'react-toastify'
import Sidebar from '../../components/Sidebar/Sidebar'
import { Outlet } from 'react-router-dom'
import NavbarAdmin from '../../components/Navbar/Admin/Navbar'

const AdminLayout = () => {
    return (
        <div>
            <ToastContainer />
            <NavbarAdmin />
            <hr />
            <div className="app-content">
                <Sidebar />
                <Outlet />
            </div>
        </div>
    )
}

export default AdminLayout
