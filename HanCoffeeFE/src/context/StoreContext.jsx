import { createContext, useEffect, useState } from "react";
import axios from 'axios';

export const StoreContext = createContext(null);

const StoreContextProvider = (props) => {
    const url = "http://localhost:8888";
    const [product_list, setProductList] = useState([]);
    const [category_list, setCategoryList] = useState([]);
    const [cartItems, setCartItems] = useState({});
    const [itemTotal, setItemTotal] = useState(0)
    const [token, setToken] = useState("");
    const [searchQuery, setSearchQuery] = useState('')

    const fetchProductList = async () => {
        try {
            const productResponse = await axios.get(`${url}/api/product/list`);
            if (productResponse.data.success) {
                const filteredList = productResponse.data.data.filter(
                    (item) => item.status === 0
                )
                setProductList(filteredList)
            }
        } catch (error) {
            console.error('Error fetching product list: ', error);
        }
    };

    const fetchCategoryList = async () => {
        try {
            const categoryResponse = await axios.get(`${url}/api/category/list`);
            if (categoryResponse.data.success) {
                const filterCategoryList = categoryResponse.data.data.filter(item => item.status === 0)
                setCategoryList(filterCategoryList);

            }
        } catch (error) {
            console.error('Error fetching category list: ', error);
        }
    };

    const loadCartData = async () => {
        try {
            if (token) {
                const cartResponse = await axios.post(`${url}/api/cart/get`, {}, { headers: { token } });
                setCartItems(cartResponse.data.cartData);
                // setItemTotal(Object.keys(cartResponse.data.cartData).length)

            }
        } catch (error) {
            console.error('Error loading cart data: ', error);
        }
    };
    const addToCart = async (itemId) => {
        try {
            if (token) {
                const addResponse = await axios.post(`${url}/api/cart/add`, { itemId }, { headers: { token } });
                if (addResponse.data.success) {
                    setCartItems((prevCartItems) => ({
                        ...prevCartItems,
                        [itemId]: (prevCartItems[itemId] || 0) + 1
                    }));
                    return true;
                }
            } else {
                console.error('No token available');
                return false;
            }
        } catch (error) {
            console.error('Error adding to cart: ', error);
            return false;
        }
    };

    const removeFromCart = async (itemId) => {
        try {
            if (token) {
                const response = await axios.post(`${url}/api/cart/remove`, { itemId }, { headers: { token } });
                if (response.data.success) {
                    setCartItems((prevCartItems) => {
                        const updatedCartItems = { ...prevCartItems };
                        if (updatedCartItems[itemId] > 1) {
                            updatedCartItems[itemId] -= 1;
                        } else {
                            delete updatedCartItems[itemId];
                        }
                        return updatedCartItems;
                    });
                    return true;
                }
            } else {
                console.error('No token available');
                return false;
            }
        } catch (error) {
            console.error('Error removing from cart: ', error);
            return false;
        }
    };

    const getTotalCartAmout = () => {
        let totalAmout = 0
        for (const item in cartItems) {
            if (cartItems[item] > 0) {
                let itemInfor = product_list.find((product) => product._id === item)
                totalAmout += itemInfor.price * cartItems[item]
            }
        }

        return totalAmout
    }
    useEffect(() => {
        const loadData = async () => {
            await fetchProductList();
            await fetchCategoryList();
            if (localStorage.getItem("token")) {
                setToken(localStorage.getItem("token"))
                await loadCartData(localStorage.getItem("token"));
            }

        };
        loadData();
    }, []);

    useEffect(() => {
        setItemTotal(Object.values(cartItems || {}).reduce((acc, count) => acc + count, 0));
    }, [cartItems]);

    const contextValue = {
        product_list,
        category_list,
        addToCart,
        removeFromCart,
        cartItems,
        url,
        token,
        setToken,
        itemTotal,
        getTotalCartAmout,
        searchQuery,
        setSearchQuery
    };

    return (
        <StoreContext.Provider value={contextValue}>
            {props.children}
        </StoreContext.Provider>
    );
};

export default StoreContextProvider;
