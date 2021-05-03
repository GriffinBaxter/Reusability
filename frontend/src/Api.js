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


export default {

  // Sends a post request to the backend with a new user object to store
  addNewUser: (user) => instance.post('/users', {...user.data}, { withCredentials: true}),

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
    return instance.get(`/businesses/${businessID}/products?orderBy=${sortBy}&page=${page}`,{
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
    return instance.get(`/businesses/${businessID}`,{
      withCredentials: true
    })
  },

  makeAdministrator: (businessesId, userId) => {
    return instance.put(`/businesses/${businessesId}/makeAdministrator`, {
      userId},{
      withCredentials: true
    })
  },

  removeAdministrator: (businessesId, userId) => {
    return instance.put(`/businesses/${businessesId}/removeAdministrator`, {
      userId},{
      withCredentials: true
    })
  },

  // Sends a post request to the backend with a new product object to store
  addNewProduct: (businessID, product) => {
    return instance.post('/businesses/'+businessID+'/products', {...product.data}, {withCredentials: true})
  },
  // Sends a PUT request to modify a product from some given business ID
  modifyProduct: (productId, businessId, newProduct) => {
    return instance.put(`/businesses/${businessId}/products/${productId}`, {...newProduct.data}, {
      withCredentials: true
    })
  }

  // Usage examples from original file:
  //
  // // (C)reate
  // createNew: (firstName, lastName) => instance.post('students', {firstName, lastName}),
  // // (R)ead
  // getAll: () => instance.get('students', {
  //   transformResponse: [function (data) {
  //     return data? JSON.parse(data)._embedded.students : data;
  //   }]
  // }),
  // // (U)pdate
  // updateForId: (id, firstName, lastName) => instance.put('students/'+id, {firstName, lastName}),
  // // (D)elete
  // removeForId: (id) => instance.delete('students/'+id)

}
