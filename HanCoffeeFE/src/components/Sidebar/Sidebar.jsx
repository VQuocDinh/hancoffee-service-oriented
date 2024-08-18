import React from 'react'
import './Sidebar.css'
import { assets } from '../../assets/assets'
import { NavLink, useNavigate } from 'react-router-dom'
import { PATH_DASHBOARD } from '../../common/routes/path'

const Sidebar = () => {
    const navigate = useNavigate()
    return (
        <div className="sidebar">
            <div className="sidebar-options">
                <NavLink
                    to={PATH_DASHBOARD.general.product.list}
                    className="sidebar-option"
                >
                    <img src={assets.list_icon} alt="" />
                    <p>Products</p>
                </NavLink>

                <NavLink
                    to={PATH_DASHBOARD.general.category.list}
                    className="sidebar-option"
                >
                    <img src={assets.list_icon} alt="" />
                    <p>Category</p>
                </NavLink>

                <NavLink
                    to={PATH_DASHBOARD.general.order.list}
                    className="sidebar-option"
                >
                    <img src={assets.order_icon} alt="" />
                    <p>Orders</p>
                </NavLink>

                <NavLink
                    to={PATH_DASHBOARD.general.report.list}
                    className="sidebar-option"
                >
                    <img src={assets.report_icon} alt="" />
                    <p>Report</p>
                </NavLink>

                <NavLink
                    to={PATH_DASHBOARD.general.user.list}
                    className="sidebar-option"
                >
                    <img src={assets.user_icon} alt="" />
                    <p>Users</p>
                </NavLink>

                <NavLink
                    to={PATH_DASHBOARD.general.staff.list}
                    className="sidebar-option"
                >
                    <img src={assets.user_icon} alt="" />
                    <p>Staff</p>
                </NavLink>

                <NavLink
                    to={PATH_DASHBOARD.general.customer.list}
                    className="sidebar-option"
                >
                    <img src={assets.user_icon} alt="" />
                    <p>Customer</p>
                </NavLink>
            </div>
        </div>
    )
}

export default Sidebar
