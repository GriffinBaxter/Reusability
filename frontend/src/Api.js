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

// TODO: Only for registration --- NEEDS ADAPTION FOR FUTURE STORIES
export class User {

    // This is a config for the user requirement details
    static config = {
        firstName: {
            name: "First name",
            minLength: 2,
            maxLength: 255,
            regexMessage: "Must be alphanumeric (spaces, -, ' optional)",
            regex: /^[a-zA-Z '-]+$/
        },
        middleName: {
            name: "Middle name",
            minLength: 0,
            maxLength: 255,
            regexMessage: "Must be alphanumeric (spaces, -, ' optional)",
            regex: /^[a-zA-Z '-]*$/
        },
        lastName: {
            name: "Last name",
            minLength: 2,
            maxLength: 255,
            regexMessage: "Must be alphanumeric (spaces, -, ' optional)",
            regex: /^[a-zA-Z '-]+$/
        },
        nickname: {
            name: "Nickname",
            minLength: 0,
            maxLength: 255,
            regexMessage: "Must be alphanumeric (spaces, -, ' optional)",
            regex: /^[a-zA-Z '-]*$/
        },
        bio: {
            name: "Bio",
            minLength: 0,
            maxLength: 600
        },
        email: {
            name: "Email",
            minLength: 5,
            maxLength: 30,
            regex: /^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/,
            regexMessage: "Invalid email. Expected format is example123@gmail.com."

        },
        dateOfBirth: {
            name: "Date of birth",
            minAgeMs: 13 * 365 * 24 * 60 * 60 * 1000
        },

        phoneNumber: {
            name: "Phone number",
            minLength: 0,
            maxLength: 15,
            regex: /^[+0-9 ]*$/,
            regexMessage: "Invalid phone number. Must only contain numbers, +, and spaces."
        },
        homeAddress: {
            name: "Home address",
            minLength: 0,
            maxLength: 255,
            regex: /^[a-zA-Z0-9 '#,.&()-]+$/,
            regexMessage: "Must only contain alphanumeric characters, numbers, spaces, or '#,.&()[]-]+$",

        },

        streetNumber: {
            name: "Street number",
            minLength: 0,
            maxLength: 255
        },

        streetName: {
            name: "Street name",
            minLength: 0,
            maxLength: 255
        },

        city: {
            name: "City",
            minLength: 0,
            maxLength: 255
        },

        postcode: {
            name: "Postcode",
            minLength: 0,
            maxLength: 255
        },

        region: {
            name: "Region",
            minLength: 0,
            maxLength: 255
        },

        country: {
            name: "Country",
            minLength: 1,
            maxLength: 255
        },

        password: {
            name: "Password",
            minLength: 8,
            maxLength: 30,
            regexStrong: new RegExp("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])(?=.{8,30})"),
            regexStrongMessage: "Invalid password format",
            regexContainLowerCase: /^[\s\S]*[a-z][\s\S]*$/,
            regexContainUpperCase: /^[\s\S]*[A-Z][\s\S]*$/,
            regexContainNumber: /^[\s\S]*[0-9][\s\S]*$/,
            regexContainLength: /^[\s\S]{8,}$/,
            regexContainSymbol: /^[\s\S]*[!@#$%^&*][\s\S]*$/

            // Regex resource: https://www.thepolyglotdeveloper.com/2015/05/use-regex-to-test-password-strength-in-javascript/
        }
    };

    constructor({
                    firstName,
                    lastName,
                    middleName,
                    nickname,
                    bio,
                    email,
                    dateOfBirth,
                    phoneNumber,
                    homeAddress,
                    password
                }) {
        this.data = {
            firstName,
            lastName,
            middleName,
            nickname,
            bio,
            email,
            dateOfBirth,
            phoneNumber,
            homeAddress,
            password
        }

    }

}

export class Business {

    // This is a config for the business requirement details
    static config = {
        businessName: {
            name: "Name",
            minLength: 1,
            maxLength: 100,
            regex: /^[a-zA-Z0-9 '#,.&()-]+$/,
            regexMessage: "Must only contain alphanumeric characters, numbers, spaces, or '#,.&()[]-]+$",
        },
        description: {
            name: "Description",
            minLength: 0,
            maxLength: 600
        },
        businessType: {
            name: "Business type",
        },
        businessAddress: {
            name: "Business address",
            minLength: 0,
            maxLength: 255,
            regex: /^[a-zA-Z0-9 '#,.&()-]+$/,
            regexMessage: "Must only contain alphanumeric characters, numbers, spaces, or '#,.&()[]-]+$",
        },
        streetNumber: {
            name: "Street number",
            minLength: 0,
            maxLength: 255
        },
        streetName: {
            name: "Street name",
            minLength: 0,
            maxLength: 255
        },
        city: {
            name: "City",
            minLength: 0,
            maxLength: 255,
        },
        postcode: {
            name: "Postcode",
            minLength: 0,
            maxLength: 255
        },
        region: {
            name: "Region",
            minLength: 0,
            maxLength: 255
        },
        country: {
            name: "Country",
            minLength: 1,
            maxLength: 255,
            regexMessage: "Must be alphanumeric (spaces, -, ' optional)",
            regex: /^[a-zA-Z '-]+$/
        },
    };

    constructor({primaryAdministratorId, name, description, address, businessType}) {
        this.data = {
            primaryAdministratorId,
            name,
            description,
            address,
            businessType
        }

    }

}

export class Product {

    // This is a config for the product requirement details
    static config = {
        productID: {
            name: "Product ID",
            minLength: 3,
            maxLength: 15,
            regex: /^[A-Z0-9-]+$/,
            regexMessage: "Must only contain uppercase alphanumeric characters, numbers, or -",
        },
        productName: {
            name: "Product name",
            minLength: 1,
            maxLength: 100,
            regex: /^[a-zA-Z0-9 '#,.&()-]+$/,
            regexMessage: "Must only contain alphanumeric characters, numbers, spaces or '#,.&()-"
        },
        description: {
            name: "Description",
            minLength: 0,
            maxLength: 600
        },
        manufacturer: {
            name: "manufacturer",
            minLength: 0,
            maxLength: 100,
            regex: /^[a-zA-Z0-9 '#,.&()-]*$/,
            regexMessage: "Must only contain alphanumeric characters, numbers, spaces or '#,.&()-"
        },
        recommendedRetailPrice: {
            name: "Recommended retail price",
            minLength: 0,
            maxLength: 16,
            regex: /^(?:[1-9]\d*|0)?(?:\.\d+)?$/,
            regexMessage: "Must be a positive double precision floating point number e.g 1.00"
        },
    };

    constructor({id, name, description, manufacturer, recommendedRetailPrice}) {
        this.data = {
            id,
            name,
            description,
            manufacturer,
            recommendedRetailPrice,
        }

    }

}

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
    addNewUser: (user) => instance.post('/users', {...user.data}, {withCredentials: true}),

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
    addNewBusiness: (business) => instance.post('/businesses', {...business.data}, {withCredentials: true}),

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

    // Sends a post request to the backend with a new product object to store
    addNewProduct: (businessID, product) => {
        return instance.post('/businesses/' + businessID + '/products', {...product.data}, {withCredentials: true})
    },

    addNewInventoryItem: (id, inventoryItem) => {
        return instance.post(`/businesses/${id}/inventory/`, {
            ...inventoryItem.data
        }, {
            withCredentials: true
        })
    }

}
