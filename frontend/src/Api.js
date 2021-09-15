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
    timeout: 10000
});

export default {

    // Retrieves the server URL as a string
    getServerURL: () => {
        return SERVER_URL
    },

    // Returns the server path .
    getServerResourcePath: (filepath) => {
        return `${SERVER_URL}/${filepath}`;
    },

    // Sends a post request to the backend with a new user object to store
    addNewUser: (user) => instance.post('/users', {
        ...user.data
    }, {
        withCredentials: true
    }),

    // Sends a put request to the backend with an userID and a edit user object to update user
    editUser: (id, user) => instance.put(`/users/${id}/profile`, {
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

    // Sends a get request to the backend asking for a list of users which match the search criteria.
    searchUsers: (query, orderBy, page) => {
        return instance.get(`/users/search?searchQuery=${query}&orderBy=${orderBy}&page=${page}`, {
            withCredentials: true
        })
    },

    // Sends a GET request to the backend asking for any businesses matching the given criteria
    searchBusinesses: (query, businessType, orderBy, page) => {
        return instance.get(`/businesses/search?searchQuery=${query}&businessType=${businessType}&orderBy=${orderBy}&page=${page}`, {
            withCredentials: true
        })
    },

    // Sends a post request to the backend with a new business object to store
    addNewBusiness: (business) => instance.post('/businesses', {
        ...business.data
    }, {
        withCredentials: true
    }),

    // Sends a get request to the backend asking for a sorted and paginated list of products matching the given search criteria for a business.
    searchProducts: (businessID, searchQuery, searchBy, barcode, sortBy, page) => {
        return instance.get(`/businesses/${businessID}/products?searchQuery=${searchQuery}&searchBy=${searchBy}&barcode=${barcode}&orderBy=${sortBy}&page=${page}`, {
            withCredentials: true
        })
    },

    // Sends a get request to the backend asking for a sorted list of inventory items for a business.
    sortInventoryItems: (id, sortBy, page, barcode) => {
        return instance.get(`/businesses/${id}/inventory?barcode=${barcode}&orderBy=${sortBy}&page=${page}`, {
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

    // Sends a get request to the backend asking for the details of a business.
    getBusiness: (businessID) => {
        return instance.get(`/businesses/${businessID}`, {
            withCredentials: true
        })
    },

    // Sends a put request to the backend to make a user an administrator of a business.
    makeAdministrator: (businessesId, userId) => {
        return instance.put(`/businesses/${businessesId}/makeAdministrator`, {
            userId
        }, {
            withCredentials: true
        })
    },

    // Sends a put request to the backend to remove a user as an administrator of a business.
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
            withCredentials: true
        })
    },

    // Sends a get request to the backend asking for a sorted list of listings belonging to a business.
    sortListings: (businessId, sortBy, page, barcode) => {
        return instance.get(`/businesses/${businessId}/listings?barcode=${barcode}&orderBy=${sortBy}&page=${page}`, {
            withCredentials: true,
        })
    },

    // Sends a post request to the backend with the details of a new inventory item.
    addNewInventoryItem: (id, inventoryItem) => {
        return instance.post(`/businesses/${id}/inventory/`, {
            ...inventoryItem.data
        }, {
            withCredentials: true
        })
    },

    // Sends a post request to the backend with the details of a new listing.
    addNewBusinessListing: (businessId, listing) => {
        return instance.post(`/businesses/${businessId}/listings`, {
            ...listing.data
        }, {
            withCredentials: true
        })
    },

    // Sends a get request to the backend to retrieve all inventory items for a businesses (no pagination or sorting).
    getEveryInventoryItem: (businessID) => {
        return instance.get(`/businesses/${businessID}/inventoryAll`, {
            withCredentials: true
        })
    },

    // Sends a get request to the backend to retrieve all products for a businesses (no pagination or sorting).
    getEveryProduct: (businessID) => {
        return instance.get(`/businesses/${businessID}/productAll`, {
            withCredentials: true
        })
    },

    // Sends a put request to the backend to update the details of an existing inventory item.
    modifyInventoryItem: (inventoryItemId, businessId, newInventoryItem) => {
        return instance.put(`/businesses/${businessId}/inventory/${inventoryItemId}`, {...newInventoryItem.data}, {
            withCredentials: true
        })
    },

    // Sends a get request to the backend to retrieve the specific details of a marketplace card.
    getDetailForACard: (id) => {
        return instance.get(`/cards/${id}`, {
            withCredentials: true
        })
    },

    // Sends a get request to the backend to retrieve all marketplace cards (for a specific section, and paginated and sorted).
    getAllCards: (section, sortBy, page) => {
        return instance.get(`/cards?section=${section}&orderBy=${sortBy}&page=${page}`, {
            withCredentials: true
        })
    },

    // Sends a post request to the backend containing the details of a new marketplace card.
    addNewCard: (newCard) => {
        return instance.post(`/cards`, newCard, {
            withCredentials: true
        })
    },

    // Sends a delete request to the backend to delete a marketplace card.
    deleteACard: (id) => {
        return instance.delete(`/cards/${id}`, {
            withCredentials: true
        })
    },

    // Sends a get request to the backend to retrieve the marketplace cards created by a specific user.
    getUsersCards: (userId) => {
        return instance.get(`/users/${userId}/cards`, {
            withCredentials: true
        })
    },

    getNotifications: () => {
        return instance.get(`/users/notifications`, {
            withCredentials: true
        })
    },

    getBusinessNotifications: (businessId) => {
        return instance.get(`/businesses/${businessId}/notifications`, {
            withCredentials: true
        })
    },


    editCard: (cardId, updatedCard) => {
        return instance.put(`/cards/${cardId}`, {...updatedCard}, {
            withCredentials: true
        })
    },

    // Sends a get request to the backend to search for keywords by partial (or complete) name.
    searchKeywords: (searchQuery) => {
        return instance.get(`/keywords/search?searchQuery=${searchQuery}`, {
            withCredentials: true
        })
    },

    addNewKeyword: (newKeyword) => {
        return instance.post(`/keywords`, {...newKeyword}, {
            withCredentials: true
        })
    },

    // Sends a delete request to the backend to delete the image.
    deleteImage: (query, imageId) => {
        return instance.delete(`/images/${imageId}${query}`, {
            withCredentials: true
        })
    },


    // Uploads an image to a given product
    uploadImage: (query, image) => {
        return instance.post(`/images${query}`,
            image, {
            withCredentials: true,
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        })
    },

    // Sets the primary image
    setPrimaryImage: (query, imageId) => {
        return instance.put(`/images/${imageId}/makePrimary${query}`,
            {}, {
                withCredentials: true
            })
    },

    // Extends a given card's display period by 2 weeks.
    extendCardDisplayPeriod: (id) => {
        return instance.put(`/cards/${id}/extenddisplayperiod`, {}, {
            withCredentials: true
        })
    },

    // System administrators can delete a keyword.  Keyword is removed from the list and from any cards it appears on.
    deleteExistingKeyword: (id) => {
        return instance.delete(`/keywords/${id}`, {
            withCredentials: true
        })
    },

    // Sends a GET request to the backend asking for any listings matching the given criteria (paginated)
    searchListings: (searchQuery, searchType, orderBy, page, businessTypes, barcode, minimumPrice, maximumPrice, fromDate, toDate) => {
        return instance.get(`/listings?searchQuery=${searchQuery}&searchType=${searchType}&orderBy=${orderBy}&page=${page}&businessTypes=${businessTypes}&barcode=${barcode}&minimumPrice=${minimumPrice}&maximumPrice=${maximumPrice}&fromDate=${fromDate}&toDate=${toDate}`, {
            withCredentials: true
        })
    },

    // Retrieving a specific listing by given id.
    getDetailForAListing: (businessId, listingId) => {
        return instance.get(`/businesses/${businessId}/listings/${listingId}`, {
            withCredentials: true
        })
    },

    // Change the status for current user to given listing.
    changeStatusOfAListing: (id) => {
        return instance.put(`/listings/${id}/bookmark`, {}, {
            withCredentials: true
        })
    },

    // Retrieve the bookmarked messages
    getBookmarkedMessage: () => {
        return instance.get(`/home/bookmarkMessages`, {
            withCredentials: true
        })
    },

    // Buy a listing
    buyListing: (id) => {
        return instance.put(`/listings/${id}/buy`, {}, {
            withCredentials: true
        })
    },

    deleteBookmarkMessage: (id) => {
        return instance.delete(`/home/bookmarkMessages/${id}`, {
            withCredentials: true
        })
    },

    // Sends a GET request to the backend asking for the sold listings of a business (paginated)
    getSoldListings: (businessId, page) => {
        return instance.get(`/businesses/${businessId}/soldListings?page=${page}`, {
            withCredentials: true
        })
    },

    deleteNotification: (id, notificationType) => {
        return instance.delete(`/users/notifications/${id}?type=${notificationType}`, {
            withCredentials: true
        })
    },

    // Sends a POST request to the backend to make a new conversation for a marketplace card.
    createConversation: (messagePayload) => {
        return instance.post(`/home/conversation`, messagePayload, { withCredentials: true})
    },

    // Sends a message to the backend with a card ID and user ID of the intended recipient
    sendMessage: (cardId, recipient, message) => {
        return instance.post(`/cards/${cardId}/message`, {
            recipient, message
        }, {
            withCredentials: true
        })
    },

    // retrieves conversation entities
    getConversations() {
        return instance.get(`/home/conversation`, {withCredentials: true})
    },

    // Sends a DELETE request to the backend to delete a conversation with the given id.
    deleteConversation: (id) => {
        return instance.delete(`/users/conversation/${id}`, {
            withCredentials: true
        })
    },

    // retrieves a specific conversation
    getConversation(id) {
        return instance.get(`/home/conversation/${id}/messages`, {withCredentials: true})
    },

    sendReply: (conversationId, message) => {
        return instance.post(`/home/conversation/${conversationId}`, message, {withCredentials: true})
    }
}
