import React, { Suspense, lazy } from 'react'
import { createBrowserRouter, useRoutes } from 'react-router-dom'
import { PATH_CUSTOMER, PATH_DASHBOARD, PATH_LOGIN } from './path'
import Report from '../../pages/Report/Report'
import Orders from '../../pages/orderStatus/Orders.jsx'
import Users from '../../pages/Users/Users'
import Cart from '../../pages/Cart/Cart'
import ProductDetail from '../../pages/ProductDetail/ProductDetail.jsx'
import Order from '../../pages/Order/Order.jsx'
import PlacecOrder from '../../pages/PlaceOrder/PlaceOrder.jsx'
import User from '../../pages/User/User.jsx'
import GuessGuard from '../guard/GuessGuard'
import AuthGuard from '../guard/AuthGuard'
import AdminLayout from '../layout/AdminLayout'
import CustomerLayout from '../layout/CustomerLayout'
import StoreContextProvider from '../../context/StoreContext.jsx'
import Staff from '../../pages/staff/Staff.jsx'
import Login from '../../pages/Login/Login.jsx'
import Customer from '../../pages/customer/Customer.jsx'
import ForgotPassword from '../../pages/Login/ForgotPassword.jsx'

const Loadable = (Component) => (props) => {
    // const { pathname } = useLocation();

    // const { isAuthenticated } = useAuth();

    // const isDashboard = pathname.includes('/dashboard') && isAuthenticated;

    return (
        <Suspense fallback={<h1>Loading...</h1>}>
            <Component {...props} />
        </Suspense>
    )
}
const HomeComponent = Loadable(lazy(() => import('../../pages/Home/Home')))

// Dashboard
// Product
const ProductListComponent = Loadable(
    lazy(() => import('../../pages/product/list'))
)
const ProductEditComponent = Loadable(
    lazy(() => import('../../pages/product/edit'))
)
const ProductAddComponent = Loadable(
    lazy(() => import('../../pages/product/add'))
)

// Category
const CategoryListComponent = Loadable(
    lazy(() => import('../../pages/category/list'))
)
const CategoryEditComponent = Loadable(
    lazy(() => import('../../pages/category/edit'))
)
const CategoryAddComponent = Loadable(
    lazy(() => import('../../pages/category/add'))
)

export const router = createBrowserRouter([
    {
        path: '',
        element: (
            <GuessGuard>
                <CustomerLayout />
            </GuessGuard>
        ),
        children: [
            {
                children: [
                    {
                        path: PATH_CUSTOMER.general.home.root,
                        element: <HomeComponent />,
                    },
                ],
            },
            {
                path: PATH_CUSTOMER.general.cart.root,
                children: [
                    {
                        path: PATH_CUSTOMER.general.cart.root,
                        element: <Cart />,
                    },
                ],
            },
            {
                path: PATH_CUSTOMER.general.product.root,
                children: [
                    {
                        path: PATH_CUSTOMER.general.product.root,
                        element: <ProductDetail />,
                    },
                ],
            },
            {
                path: PATH_CUSTOMER.general.order.root,
                children: [
                    {
                        path: PATH_CUSTOMER.general.order.root,
                        element: <Order />,
                    },
                ],
            },
            {
                path: PATH_CUSTOMER.general.user.root,
                children: [
                    {
                        path: PATH_CUSTOMER.general.user.root,
                        element: <User />,
                    },
                ],
            },

            {
                path: PATH_CUSTOMER.general.placeOrder.root,
                children: [
                    {
                        path: PATH_CUSTOMER.general.placeOrder.root,
                        element: <PlacecOrder />,
                    },
                ],
            },
        ],
    },
    {
        path: 'dashboard',
        element: (
            <AuthGuard>
                <AdminLayout />
            </AuthGuard>
        ),
        children: [
            {
                children: [
                    {
                        path: PATH_DASHBOARD.general.product.list,
                        element: <ProductListComponent />,
                    },
                    {
                        path: PATH_DASHBOARD.general.product.add,
                        element: <ProductAddComponent />,
                    },
                    {
                        path: PATH_DASHBOARD.general.product.edit,
                        element: <ProductEditComponent />,
                    },
                ],
            },

            {
                children: [
                    {
                        path: PATH_DASHBOARD.general.category.list,
                        element: <CategoryListComponent />,
                    },
                    {
                        path: PATH_DASHBOARD.general.category.add,
                        element: <CategoryAddComponent />,
                    },
                    {
                        path: PATH_DASHBOARD.general.category.edit,
                        element: <CategoryEditComponent />,
                    },
                ],
            },

            {
                children: [
                    {
                        path: PATH_DASHBOARD.general.report.list,
                        element: <Report />,
                    },
                ],
            },

            {
                children: [
                    {
                        path: PATH_DASHBOARD.general.order.list,
                        element: <Orders />,
                    },
                ],
            },

            {
                children: [
                    {
                        path: PATH_DASHBOARD.general.user.list,
                        element: <Users />,
                    },
                ],
            },

            {
                children: [
                    {
                        path: PATH_DASHBOARD.general.staff.list,
                        element: <Staff />,
                    },
                ],
            },

            {
                children: [
                    {
                        path: PATH_DASHBOARD.general.customer.list,
                        element: <Customer />,
                    },
                ],
            },
        ],
    },
    {
        path: 'login',
        element: (
            <Login />
        ),
    },
    {
        path: 'forgot-password',
        element: <ForgotPassword />,
    }
])

// User

// Customer
