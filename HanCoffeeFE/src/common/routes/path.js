function path(root, subLink) {
    return `${root}${subLink}`
}

export const ROOT_DASHBOARD = '/dashboard'
export const ROOT_CUSTOMER = '/'
export const ROOT_LOGIN = '/login'

export const PATH_DASHBOARD = {
    root: ROOT_DASHBOARD,
    general: {
        product: {
            root: path(ROOT_DASHBOARD, '/product'),
            add: path(ROOT_DASHBOARD, '/product/add'),
            list: path(ROOT_DASHBOARD, '/product/list'),
            edit: path(ROOT_DASHBOARD, '/product/:productId'),
        },
        category:{
            root: path(ROOT_DASHBOARD,'/category'),
            add: path(ROOT_DASHBOARD,'/category/add'),
            list: path(ROOT_DASHBOARD,'/category/list'),
            edit: path(ROOT_DASHBOARD,'/category/:categoryId'),
            
        },
        report: {
            root: path(ROOT_DASHBOARD, '/report'),
            list: path(ROOT_DASHBOARD, '/report/list'),
        },
        order: {
            root: path(ROOT_DASHBOARD, '/order'),
            list: path(ROOT_DASHBOARD, '/order/list'),
        },
        user: {
            root: path(ROOT_DASHBOARD, '/user'),
            add: path(ROOT_DASHBOARD, '/user/add'),
            list: path(ROOT_DASHBOARD, '/user/list'),
        },
        staff: {
            root: path(ROOT_DASHBOARD, '/staff'),
            add: path(ROOT_DASHBOARD, '/staff/add'),
            list: path(ROOT_DASHBOARD, '/staff/list'),
        },
        customer: {
            root: path(ROOT_DASHBOARD, '/customer'),
            add: path(ROOT_DASHBOARD, '/customer/add'),
            list: path(ROOT_DASHBOARD, '/customer/list'),
        },
    },
}

export const PATH_CUSTOMER = {
    root: ROOT_CUSTOMER,
    general: {
        home: {
            root: path(ROOT_CUSTOMER, ''),
        },
        cart: {
            root: path(ROOT_CUSTOMER, 'cart'),
        },
        product: {
            root: path(ROOT_CUSTOMER, 'product/:productId'),
        },
        placeOrder: {
            root: path(ROOT_CUSTOMER, 'placeOrder'),
        },
        user: {
            root: path(ROOT_CUSTOMER, 'user'),
        },

        order: {
            root: path(ROOT_CUSTOMER, 'order'),
        },
    },
}

export const PATH_LOGIN ={
    root: ROOT_LOGIN,
    general: {
        login: {
            root: path(ROOT_LOGIN, 'login'),
        },
    }
}