/*
 * Created on Wed Feb 10 2021
 *
 * The Unlicense
 * This is free and unencumbered software released into the public domain.
 *
 * Anyone is free to copy, modify, publish, use, compile, sell, or distribute
 * this software, either in source code form or as a compiled binary, for any
 * purpose, commercial or non-commercial, and by any means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors of this
 * software dedicate any and all copyright interest in the software to the public
 * domain. We make this dedication for the benefit of the public at large and to
 * the detriment of our heirs and successors. We intend this dedication to be an
 * overt act of relinquishment in perpetuity of all present and future rights to
 * this software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, please refer to <https://unlicense.org>
 */

/**
 * Declare all available services here
 */
import axios from 'axios'

const SERVER_URL = process.env.VUE_APP_SERVER_ADD;

const instance = axios.create({
    baseURL: SERVER_URL,
    timeout: 3000
});


export class InventoryItem {

    // This is a config for the Inventory Item requirement details
    static config = {
        productId: {
            name: "Product ID",
            minLength: 3,
            maxLength: 15,
            regex: /^[A-Z0-9-]+$/,
            regexMessage: "Must only contain uppercase alphanumeric characters, numbers, or -",
        },
        quantity: {
            name: "Quantity",
            minLength: 1,
            maxLength: 12,
            regex: /^[0-9]+$/,
            regexMessage: "Must only contain numbers",
        },
        pricePerItem: {
            name: "Price Per Item",
            minLength: 0,
            maxLength: 16,
            regex: /^(?:[1-9]\d*|0)?(?:\.\d+)?$/,
            regexMessage: "Must be a positive double precision floating point number e.g 1.00"
        },
        totalPrice: {
            name: "Total Price",
            minLength: 0,
            maxLength: 16,
            regex: /^(?:[1-9]\d*|0)?(?:\.\d+)?$/,
            regexMessage: "Must be a positive double precision floating point number e.g 1.00"
        },
        manufactured: {
            name: "manufactured"
        },
        sellBy: {
            name: "Sell By"
        },
        bestBefore: {
            name: "Best Before"
        },
        expires: {
            name: "Expires",
        },
    };

    constructor({productId, quantity, pricePerItem, totalPrice, manufactured, sellBy, bestBefore, expires}) {
        this.data = {
            productId,
            quantity,
            pricePerItem,
            totalPrice,
            manufactured,
            sellBy,
            bestBefore,
            expires
        }

    }

}

export default {

    // Sends a post request to the backend with a new user object to store
    addNewUser: (user) => instance.post('/users', {
        ...user.data
    }, {
        withCredentials: true
    }),

    // Sends a post request to the backend with the user's login details
    signIn: (email, password) => instance.post('login', {email, password}, {
        withCredentials: true
    }),

    // Sends a post request to the backend to logout the user
    signOut: () => instance.post('/logout', {}, {
        withCredentials: true
    }),

    // Sends a get request to the backend asking for a the given user's details
    getUser: (userID) => {
        // Now sends cookies for backend to check
        return instance.get(`users/${userID}`, {
            withCredentials: true
        })
    },

    searchUsers: (query, orderBy, page) => {
        return instance.get(`/users/search?searchQuery=${query}&orderBy=${orderBy}&page=${page}`, {
            withCredentials: true
        })
    },

    // Sends a post request to the backend with a new business object to store
    addNewBusiness: (business) => instance.post('/businesses', {
        ...business.data
    }, {
        withCredentials: true
    }),

    sortProducts: (businessID, sortBy, page) => {
        return instance.get(`/businesses/${businessID}/products?orderBy=${sortBy}&page=${page}`, {
            withCredentials: true
        })
    },

    sortInventoryItems: (id, sortBy, page) => {
        return instance.get(`/businesses/${id}/inventory?orderBy=${sortBy}&page=${page}`, {
            withCredentials: true
        })
    },

    // The API spec states this should be /users/{id}/makeadmin. But we decided to implement it as
    // /users/{id}/makeAdmin for readability purposes.
    makeAdmin: (userId) => {
        return instance.put(`/users/${userId}/makeAdmin`, {}, {
            withCredentials: true
        })
    },

    // The API spec states this should be /users/{id}/revokeadmin. But we decided to implement it as
    // /users/{id}/revokeAdmin for readability purposes.
    revokeAdmin: (userId) => {
        return instance.put(`/users/${userId}/revokeAdmin`, {}, {
            withCredentials: true
        })
    },

    getBusiness: (businessID) => {
        return instance.get(`/businesses/${businessID}`, {
            withCredentials: true
        })
    },

    makeAdministrator: (businessesId, userId) => {
        return instance.put(`/businesses/${businessesId}/makeAdministrator`, {
            userId
        }, {
            withCredentials: true
        })
    },

    removeAdministrator: (businessesId, userId) => {
        return instance.put(`/businesses/${businessesId}/removeAdministrator`, {
            userId
        }, {
            withCredentials: true
        })
    },
    // Sends a PUT request to modify a product from some given business ID
    modifyProduct: (productId, businessId, newProduct) => {
        return instance.put(`/businesses/${businessId}/products/${productId}`, {
            ...newProduct.data
        }, {
            withCredentials: true
        })
    },
    // Sends a post request to the backend with a new product object to store
    addNewProduct: (businessID, product) => {
        return instance.post('/businesses/' + businessID + '/products', {
            ...product.data
        }, {
            withCredentials: true})
    },

    sortListings: (businessId, sortBy, page) => {
        return instance.get(`/businesses/${businessId}/listings?orderBy=${sortBy}&page=${page}`, {
            withCredentials: true,
        })
    },

    addNewInventoryItem: (id, inventoryItem) => {
        return instance.post(`/businesses/${id}/inventory/`, {
            ...inventoryItem.data
        }, {
            withCredentials: true
        })
    },

    addNewBusinessListing: (businessId, listing) => {
        return instance.post(`/businesses/${businessId}/listings`, {
            ...listing.data
        }, {
            withCredentials: true
        })
    },

    getEveryInventoryItem: (businessID) => {
        return instance.get(`/businesses/${businessID}/inventoryAll`, {
            withCredentials: true
        })
    },

    getDetailForACard: (id) => {
        return instance.get(`/cards/${id}`, {
            withCredentials: true
        })
    },

}
