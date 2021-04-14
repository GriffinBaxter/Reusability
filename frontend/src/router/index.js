import VueRouter from 'vue-router'
import Vue from 'vue'

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'Login',
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import('../views/Login')
    },
    {
        path: '/registration',
        name: 'Registration',
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import('../views/Registration.vue')
    },
    {
        path: '/profile/:id?',
        name: 'Profile',

        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import('../views/Profile.vue')
    },
    {
        path: '/search',
        name: 'Search',
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import('../views/Search.vue')
    },
    {
        path: '/noUser',
        name: 'NoUser',
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
        path: '/timeout',
        name: 'ServerTimeout',
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import('../views/ServerTimeout.vue')
    },
    {
        path: '/invalidtoken',
        name: 'InvalidToken',
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
        path: '*',
        name: 'catchAll',
        component: () => import('../views/Login')
    }
]

const router = new VueRouter({
    base: process.env.VUE_APP_BASE_URL,
    routes
})

export default router
