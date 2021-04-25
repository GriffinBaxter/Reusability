import VueRouter from 'vue-router'
import Vue from 'vue'

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
        path: '/search',
        name: 'Search',
        meta: {
            title: 'Search'
        },
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import('../views/Search.vue')
        // redirect: () => {
        //
        //     return '/search?searchQuery=&orderBy=fullNameASC&page=1';
        // }
    },
    // {
    //     path: '/search?searchQuery=&orderBy=fullNameASC&page=1',
    //     name: 'FullSearchPath',
    //
    // },
    {
        path: '/noUser',
        name: 'NoUser',
        meta: {
            title: 'Error'
        },
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import('../views/NoSuchUser.vue')
    },
    {
        path: '/noBusiness',
        name: 'NoBusiness',
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import('../views/NoSuchBusiness.vue')
    },
    {
        path: '/pageDoesNotExist',
        name: 'NoSuchPage',
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import('../views/NoSuchPage.vue')
    },
    {
        path: '/forbidden',
        name: 'Forbidden',
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import('../views/Forbidden.vue')
    },
    {
        path: '/timeout',
        name: 'ServerTimeout',
        meta: {
            title: 'Error'
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
            title: 'Error'
        },
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import('../views/InvalidToken.vue')
    },
    {
        path: '/businessProfile/:id?',
        name: 'BusinessProfile',
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import('../views/BusinessProfile.vue')
    },
    {
        path: '/businessRegistration',
        name: 'BusinessRegistration',
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import('../views/BusinessRegistration.vue')
    },
    {
        path: '/businessProfile/:id?/productCatalogue',
        name: 'ProductCatalogue',
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import('../views/ProductCatalogue')
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

export default router
