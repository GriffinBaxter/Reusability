import VueRouter from 'vue-router'
import Vue from 'vue'
import Cookies from "js-cookie";

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'Login',
        meta: {
            title: 'Login'
        },
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import('../views/Login')
    },
    {
        path: '/registration',
        name: 'Registration',
        meta: {
            title: 'Registration'
        },
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import('../views/Registration.vue')
    },
    {
        path: '/home',
        name: 'Home',
        meta: {
            title: 'Home'
        },
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import('../views/Home.vue')
    },
    {
        path: '/profile/:id?',
        name: 'Profile',
        meta: {
            title: 'Profile'
        },
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import('../views/Profile.vue')
    },
    {
        path: '/profile/:id/edit',
        name: 'EditProfile',
        meta: {
            title: 'EditProfile'
        },
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import('../views/EditProfile.vue')
    },
    {
        path: '/search',
        name: 'Search',
        meta: {
            title: 'Search'
        },
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import('../views/Search.vue')
    },
    {
        path: '/noUser',
        name: 'NoUser',
        meta: {
            title: 'No User'
        },
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import('../views/NoSuchUser.vue')
    },
    {
        path: '/noBusiness',
        name: 'NoBusiness',
        meta: {
            title: 'No Business'
        },
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import('../views/NoSuchBusiness.vue')
    },
    {
        path: '/pageDoesNotExist',
        name: 'NoSuchPage',
        meta: {
            title: 'No Such Page'
        },
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import('../views/NoSuchPage.vue')
    },
    {
        path: '/noCard',
        name: 'NoCard',
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import('../views/NoSuchCard.vue')
    },
    {
        path: '/noListing',
        name: 'NoListing',
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import('../views/NoSuchListing.vue')
    },
    {
        path: '/forbidden',
        name: 'Forbidden',
        meta: {
            title: 'Forbidden'
        },
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import('../views/Forbidden.vue')
    },
    {
        path: '/timeout',
        name: 'ServerTimeout',
        meta: {
            title: 'Server Timeout'
        },
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import('../views/ServerTimeout.vue')
    },
    {
        path: '/invalidtoken',
        name: 'InvalidToken',
        meta: {
            title: 'Invalid Token'
        },
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import('../views/InvalidToken.vue')
    },
    {
        path: '/businessProfile/:id?',
        name: 'BusinessProfile',
        meta: {
            title: 'Business Profile'
        },
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import('../views/BusinessProfile.vue')
    },
    {
        path: '/businessProfile/:id/edit',
        name: 'EditBusinessProfile',
        meta: {
            title: 'Edit Business Profile'
        },
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import('../views/EditBusinessProfile.vue')
    },
    {
        path: '/businessRegistration',
        name: 'BusinessRegistration',
        meta: {
            title: 'Business Registration'
        },
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import('../views/BusinessRegistration.vue')
    },
    {
        path: '/businessProfile/:id/productCatalogue',
        name: 'ProductCatalogue',
        meta: {
            title: 'Product Catalogue'
        },
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import('../views/ProductCatalogue')
    },
    {
        path: '/businessProfile/:id/inventory',
        name: 'Inventory',
        meta: {
            title: 'Inventory'
        },
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import('../views/Inventory')
    },
    {
        path: '/businessProfile/:id/listings',
        name: 'Listings',
        props: true,
        meta: {
            title: 'Listings'
        },
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import('../views/Listings')
    },
    {
        path: '/businessProfile/:businessId/listings/:listingId',
        name: 'SaleListing',
        props: true,
        meta: {
            title: 'SaleListing'
        },
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import('../views/SaleListing')
    },
    {
        path: '/marketplace',
        name: 'Marketplace',
        meta: {
            title: 'Marketplace'
        },
        props: true,
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import('../views/Marketplace')
    },
    {
        path: '/browseListings',
        name: 'BrowseListings',
        meta: {
            title: 'BrowseListings'
        },
        props: true,
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import('../views/BrowseListings')
    },
    {
        path: '/businessProfile/:id/sales',
        name: 'Sales',
        meta: {
            title: 'Sales'
        },
        props: true,
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import('../views/Sales')
    },
    {
        path: '*',
        name: 'catchAll',
        component: () => import('../views/Login')
    }
]

const router = new VueRouter({
    mode: 'history',
    base: process.env.VUE_APP_BASE_URL,
    routes
})
router.beforeEach((to, from, next) => {
    if(to.name !== "Login" && to.name !== "Registration") {
        if (!Cookies.get('userID')) {
            next({ name: "Login"});
        } else {
            next();
        }
    } else {
        next();
    }
});

export default router

