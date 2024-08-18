import React from 'react'
import { Outlet } from 'react-router-dom'
import NavbarCustomer from '../../components/Navbar/Customer/Navbar'
import Footer from '../../components/Footer/Footer'
import StoreContextProvider from '../../context/StoreContext'

const CustomerLayout = () => {
    return (
        <>
            <div className="app">
                <StoreContextProvider>
                    <NavbarCustomer />
                    <Outlet />
                </StoreContextProvider>
            </div>
                    <Footer />
        </>

    )
}

export default CustomerLayout
