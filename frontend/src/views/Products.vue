<template>
  <div id="outerContainer" class="container">

    <div class="row mb-3">
      <div class="col">
        <!--button to open create product modal-->
        <button id="create-product-button" type="button" class="btn btn-md btn-primary float-end mt-4" tabindex="2"
                @click="modal.show()">Create Product</button>
      </div>
    </div>

    <div class="row mb-3">
      <div class="col py-2 header-col col-hover rounded-3 me-2 text-center" tabindex="3" @keydown="orderEnter($event)"
           @click="orderProducts(true, false , false, false, false, false)">
        <b>ProductID</b>
        <i id="productIdIcon"></i>
      </div>
      <div class="col py-2 header-col col-hover rounded-3 me-2 text-center" tabindex="4" @keydown="orderEnter($event)"
           @click="orderProducts(false, true , false, false, false, false)">
        <b>Name</b>
        <i id="nameIcon"></i>
      </div>
      <div class="col py-2 header-col col-hover rounded-3 me-2 text-center" tabindex="5" @keydown="orderEnter($event)"
           @click="orderProducts(false, false , true, false, false, false)">
        <b>Description</b>
        <i id="descriptionIcon"></i>
      </div>
      <div class="col py-2 header-col col-hover rounded-3 text-center" tabindex="6" @keydown="orderEnter($event)"
           @click="orderProducts(false, false , false, true, false, false)">
        <b>Manufacturer</b>
        <i id="manufacturerIcon"></i>
      </div>
      <div class="col py-2 header-col col-hover rounded-3 text-center" tabindex="7" @keydown="orderEnter($event)"
           @click="orderProducts(false, false , false, true, false, false)">
        <b>Recommended Retail Price</b>
        <i id="recommendedRetailPriceIcon"></i>
      </div>
      <div class="col py-2 header-col col-hover rounded-3 text-center" tabindex="8" @keydown="orderEnter($event)"
           @click="orderProducts(false, false , false, true, false, false)">
        <b>Created</b>
        <i id="createdIcon"></i>
      </div>
    </div>

    <div class="row">
      <div class="col">
        <!-- Avert your eyes for this... -->
        <nav aria-label="product-table-navigation" id="pagination-nav" class="float-end" v-if="maxPage > 1">
          <ul class="pagination" id="pagination-ul">

            <li :class="toggleDisableClass('page-item', currentPage-1 <= 0)">
              <a class="page-link" href="#" @click.prevent="previousPage()">Previous</a>
            </li>

            <li class="page-item" v-if="maxPage > 2 && currentPage >= maxPage">
              <a class="page-link" href="#" @click="updatePage($event, currentPage-2)">{{ currentPage - 2 }}</a>
            </li>

            <li class="page-item" v-if="currentPage-1 > 0">
              <a class="page-link" href="#" @click="updatePage($event, currentPage-1)">{{ currentPage - 1 }}</a>
            </li>

            <li class="page-item active" aria-current="page">
              <a class="page-link" href="#" @click="(e) => e.preventDefault()">{{ currentPage }}</a>
            </li>

            <li class="page-item" v-if="currentPage+1 <= maxPage">
              <a class="page-link" href="#" @click="updatePage($event, currentPage+1)">{{ currentPage + 1 }}</a>
            </li>

            <li class="page-item" v-if="maxPage > 2 && currentPage <= 1">
              <a class="page-link" href="#" @click="updatePage($event, currentPage+2)">{{ currentPage + 2 }}</a>
            </li>

            <li :class="toggleDisableClass('page-item', currentPage+1 > maxPage)" id="next-button">
              <a class="page-link" href="#" @click.prevent="nextPage()">Next</a>
            </li>
          </ul>

        </nav>


      </div>
    </div>

    <!--create product modal-->
    <div class="modal fade" ref="CreateProductModal" tabindex="-1" aria-hidden="true">
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
          <div class="modal-header text-center">
            <h3 class="modal-title w-100" id="createProductModalLabel">Create Product</h3>
            <button type="button" class="btn-close" @click="closeCreateProductModal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <!--create product form, needs validation-->
            <form id="create" novalidate @submit.prevent>
              <!--product id-->
              <div class="form-group">
                <label for="product-id">Product ID*</label>
                <input id="product-id" name="product-id" type="text" v-model="productID"
                       :class="toggleInvalidClass(productIDErrorMsg)" :maxlength="config.productID.maxLength" required>
                <div class="invalid-feedback">
                  {{productIDErrorMsg}}
                </div>
              </div>
              <!--product name-->
              <div class="form-group">
                <label for="product-name">Product Name*</label>
                <input id="product-name" name="product-name" type="text" v-model="productName"
                       :class="toggleInvalidClass(productNameErrorMsg)" :maxlength="config.productName.maxLength" required>
                <div class="invalid-feedback">
                  {{productNameErrorMsg}}
                </div>
              </div>
              <!--recommended retail price-->
              <div class="form-group">
                <label for="product-price">Recommended Retail Price(RRP)</label>
                <input id="product-price" name="product-price" type="text" v-model="recommendedRetailPrice"
                       :class="toggleInvalidClass(recommendedRetailPriceErrorMsg)"
                       :maxlength="config.recommendedRetailPrice.maxLength">
                <div class="invalid-feedback">
                  {{recommendedRetailPriceErrorMsg}}
                </div>
              </div>
              <!--manufacturer-->
              <div class="form-group">
                <label for="manufacturer">Manufacturer</label>
                <input id="manufacturer" name="manufacturer" type="text" v-model="manufacturer"
                       :class="toggleInvalidClass(manufacturerErrorMsg)" :maxlength="config.manufacturer.maxLength" required>
                <div class="invalid-feedback">
                  {{manufacturerErrorMsg}}
                </div>
              </div>
              <!--description-->
              <div class="form-group">
                <label for="description">Description</label>
                <textarea id="description" name="description" rows="5" cols="70" v-model="description"
                          :maxlength="config.description.maxLength" :class="toggleInvalidClass(descriptionErrorMsg)"
                          style="resize: none"/>
                <div class="invalid-feedback">
                  {{descriptionErrorMsg}}
                </div>
              </div>
              <!--toast error-->
              <div class="form-group">
              <div id="registration-error" ref="registration-error" v-if="toastErrorMessage" class="alert alert-danger"
                   role="alert">
                <label>{{ toastErrorMessage }}</label>
              </div>
              </div>
            </form>
          </div>
          <div class="modal-footer justify-content-between">
            <button id="cancel-button" type="button" class="btn btn-md btn-outline-secondary green-button-transparent mr-auto"
                    @click="closeCreateProductModal">Cancel</button>
            <button id="creation-button" type="button" class="btn btn-md btn-outline-primary float-lg-end green-button"
                    @click="addNewProduct($event)">Save</button>
          </div>
        </div>
      </div>
    </div>


  </div>
</template>

<script>
import { Modal } from 'bootstrap'
import Api, { Product } from '../Api';
import Cookies from 'js-cookie';

export default {
  name: "Products",
  data() {
    return {
      productIdAscending: false,
      nameAscending: false,
      descriptionAscending: false,
      manufacturerAscending: false,
      recommendedRetailPriceAscending: false,
      createdAscending: false,
      rowsPerPage: 5,
      currentPage: 1,
      maxPage: 2,
      productList: [],
      small: false,

      modal: null,

      // Used for having pre-filled input fields
      DEBUG_MODE: false,

      // A copy of the product config file for error checking.
      config: Product.config,

      // Product id related variables
      productID: "",
      productIDErrorMsg: "",

      // Product name related variables
      productName: "",
      productNameErrorMsg: "",

      // Recommended retail price related variables
      recommendedRetailPrice: "",
      recommendedRetailPriceErrorMsg: "",

      // Product description related variables
      description: "",
      descriptionErrorMsg: "",

      // Product manufacturer related variables
      manufacturer: "",
      manufacturerErrorMsg: "",

      // Toast related variables
      toastErrorMessage: "",
      cannotProceed: false,
    }
  },
  methods: {

    /**
     * Toggles the disabling of pagination buttons.
     *
     * @param baseClasses Base classes to add
     * @param condition Given condition for toggling
     * @returns {array} A list classes to apply
     */
    toggleDisableClass(baseClasses, condition) {
      const classList = [baseClasses]
      if (condition) {
        classList.push('disabled')
      }
    },

    /**
     * This method toggles the appearance of the error message, where the is-invalid class is added to the messages
     * if an error message needs to be presented to the user.
     *
     * @param errorMessage, string, the error message relating to invalid input of a field.
     * @returns {[string]}, classList, a list containing the classes for an invalid message.
     */
    toggleInvalidClass(errorMessage) {
      let classList = ['form-control']
      if (errorMessage) {
        classList.push('is-invalid')
      }
      return classList
    },

    /**
     * Updates the display to show the new page when a product clicks to move to a different page.
     *
     * @param event The click event
     * @param newPageNum The page to move to
     */
    updatePage(event, newPageNum) {
      event.preventDefault();
      this.currentPage = newPageNum;
      this.buildRows();
    },

    /**
     * Emulates a click when the product presses enter on a column header.
     *
     * @param event The keydown event
     */
    orderEnter(event) {
      if (event.keyCode === 13) {
        event.target.click();
      }
    },

    /**
     * Requests a list of products matching the given business ID from the back-end.
     * If successful it sets the productList variable to the response data.
     *
     * @return {Promise}
     */
    async requestProducts(businessID) {

      await Api.sortProducts(businessID).then(response => {
        this.productList = [...response.data];
        console.log(this.productList);
        // Order by productId alphabetically by default
        this.productList.sort(function (a, b) {
          if (a.id < b.id) {
            return -1;
          }
          if (a.id > b.id) {
            return 1;
          }
          return 0;
        });
        this.maxPage = Math.ceil(this.productList.length / this.rowsPerPage)
      }).catch((error) => {
        if (error.request && !error.response) {
          this.$router.push({path: '/timeout'});
        } else if (error.response.status === 401) {
          this.$router.push({path: '/invalidtoken'});
        } else {
          this.$router.push({path: '/timeout'});
          console.log(error.message);
        }
      })
    },

    /**
     * Goes to the previous page and updates the rows.
     *
     */
    previousPage() {
      if (this.currentPage > 1) {
        this.currentPage -= 1;
        this.buildRows()
      }
    },

    /**
     * Goes to the next page and updates the rows.
     *
     */
    nextPage() {
      if (this.currentPage < this.maxPage) {
        this.currentPage += 1;
        this.buildRows()
      }
    },

    /**
     * Orders the products based on the given booleans for each column, and updates the display
     * @param id Boolean, whether to order by productId
     * @param name Boolean, whether to order by name
     * @param description Boolean, whether to order by description
     * @param manufacturer Boolean, whether to order by manufacturer
     * @param recommendedRetailPrice Boolean, whether to order by RRP
     * @param created Boolean, whether to order by created date
     */
    orderProducts(id, name, description, manufacturer, recommendedRetailPrice, created) {

      if (id) {
        this.disableIcons()
        if (this.productIdAscending) {
          this.productList.sort(function (a, b) {
            if (a.id > b.id) {
              return -1;
            }
            if (a.id < b.id) {
              return 1;
            }
            return 0;
          })
          document.getElementById('productIdIcon').setAttribute('class', 'fas fa-chevron-up float-end');
        } else {
          this.productList.sort(function (a, b) {
            if (a.id < b.id) {
              return -1;
            }
            if (a.id > b.id) {
              return 1;
            }
            return 0;
          })
          document.getElementById('productIdIcon').setAttribute('class', 'fas fa-chevron-down float-end');
        }

        this.productIdAscending = !this.productIdAscending;
        this.nameAscending = false;
        this.descriptionAscending = false;
        this.manufacturerAscending = false;
        this.recommendedRetailPriceAscending = false;
        this.createdAscending = false;

        this.buildRows();
      } else if (name) {
        this.disableIcons()
        if (this.nameAscending) {
          this.productList.sort(function (a, b) {
            if (a.name > b.name) {
              return -1;
            }
            if (a.name < b.name) {
              return 1;
            }
            return 0;
          })
          document.getElementById('nameIcon').setAttribute('class', 'fas fa-chevron-up float-end');
        } else {
          this.productList.sort(function (a, b) {
            if (a.name < b.name) {
              return -1;
            }
            if (a.name > b.name) {
              return 1;
            }
            return 0;
          })
          document.getElementById('nameIcon').setAttribute('class', 'fas fa-chevron-down float-end');
        }

        this.productIdAscending = false;
        this.nameAscending = !this.nameAscending;
        this.descriptionAscending = false;
        this.manufacturerAscending = false;
        this.recommendedRetailPriceAscending = false;
        this.createdAscending = false;

        this.buildRows();
      } else if (description) {
        this.disableIcons()
        if (this.descriptionAscending) {
          this.productList.sort(function (a, b) {
            if (a.description > b.description) {
              return -1;
            }
            if (a.description < b.description) {
              return 1;
            }
            return 0;
          })
          document.getElementById('descriptionIcon').setAttribute('class', 'fas fa-chevron-up float-end');
        } else {
          this.productList.sort(function (a, b) {
            if (a.description < b.description) {
              return -1;
            }
            if (a.description > b.description) {
              return 1;
            }
            return 0;
          })
          document.getElementById('descriptionIcon').setAttribute('class', 'fas fa-chevron-down float-end');
        }

        this.productIdAscending = false;
        this.nameAscending = false;
        this.descriptionAscending = !this.descriptionAscending;
        this.manufacturerAscending = false;
        this.recommendedRetailPriceAscending = false;
        this.createdAscending = false;

        this.buildRows();
      } else if (manufacturer) {
        this.disableIcons()
        if (this.manufacturerAscending) {
          this.productList.sort(function (a, b) {
            if (a.manufacturer > b.manufacturer) {
              return -1;
            }
            if (a.manufacturer < b.manufacturer) {
              return 1;
            }
            return 0;
          })
          document.getElementById('manufacturerIcon').setAttribute('class', 'fas fa-chevron-up float-end');
        } else {
          this.productList.sort(function (a, b) {
            if (a.manufacturer < b.manufacturer) {
              return -1;
            }
            if (a.manufacturer > b.manufacturer) {
              return 1;
            }
            return 0;
          })
          document.getElementById('manufacturerIcon').setAttribute('class', 'fas fa-chevron-down float-end');
        }

        this.productIdAscending = false;
        this.nameAscending = false;
        this.descriptionAscending = false;
        this.manufacturerAscending = !this.manufacturerAscending;
        this.recommendedRetailPriceAscending = false;
        this.createdAscending = false;

        this.buildRows();
      } else if (recommendedRetailPrice) {
        this.disableIcons()
        if (this.recommendedRetailPriceAscending) {
          this.productList.sort(function (a, b) {
            if (a.recommendedRetailPrice > b.recommendedRetailPrice) {
              return -1;
            }
            if (a.recommendedRetailPrice < b.recommendedRetailPrice) {
              return 1;
            }
            return 0;
          })
          document.getElementById('recommendedRetailPriceIcon').setAttribute('class', 'fas fa-chevron-up float-end');
        } else {
          this.productList.sort(function (a, b) {
            if (a.recommendedRetailPrice < b.recommendedRetailPrice) {
              return -1;
            }
            if (a.recommendedRetailPrice > b.recommendedRetailPrice) {
              return 1;
            }
            return 0;
          })
          document.getElementById('recommendedRetailPriceIcon').setAttribute('class', 'fas fa-chevron-down float-end');
        }

        this.productIdAscending = false;
        this.nameAscending = false;
        this.descriptionAscending = false;
        this.manufacturerAscending = false;
        this.recommendedRetailPriceAscending = !this.recommendedRetailPriceAscending;
        this.createdAscending = false;

        this.buildRows();
      } else if (created) {
        this.disableIcons()
        if (this.created) {
          this.productList.sort(function (a, b) {
            if (a.created > b.created) {
              return -1;
            }
            if (a.created < b.created) {
              return 1;
            }
            return 0;
          })
          document.getElementById('createdIcon').setAttribute('class', 'fas fa-chevron-up float-end');
        } else {
          this.productList.sort(function (a, b) {
            if (a.created < b.created) {
              return -1;
            }
            if (a.created > b.created) {
              return 1;
            }
            return 0;
          })
          document.getElementById('createdIcon').setAttribute('class', 'fas fa-chevron-down float-end');
        }

        this.productIdAscending = false;
        this.nameAscending = false;
        this.descriptionAscending = false;
        this.manufacturerAscending = false;
        this.recommendedRetailPriceAscending = false;
        this.createdAscending = !this.createdAscending;

        this.buildRows();
      }

    },

    /**
     * Disables all ascending or descending icons in the top column headers.
     */
    disableIcons() {
      document.getElementById('productIdIcon').setAttribute('class', '');
      document.getElementById('nameIcon').setAttribute('class', '');
      document.getElementById('descriptionIcon').setAttribute('class', '');
      document.getElementById('manufacturerIcon').setAttribute('class', '');
      document.getElementById('recommendedRetailPriceIcon').setAttribute('class', '');
      document.getElementById('createdIcon').setAttribute('class', '');
    },

    /**
     * Dynamically builds the rows of products from the stored productList.
     */
    buildRows() {
      this.clearRows();
      let limit = this.rowsPerPage + (this.currentPage - 1) * this.rowsPerPage;
      let startIndex = (this.currentPage - 1) * this.rowsPerPage;
      const outerContainer = document.getElementById('outerContainer');
      const lastChild = outerContainer.lastChild;

      if (limit > this.productList.length) {
        limit = this.productList.length
      }

      if (this.productList.length > 0) {

        // 6 is the last index of the permanent items
        let tabIndex = 8;


        for (let i = startIndex; i < limit; i++) {
          // Check breakpoint
          // let width = window.innerWidth;

          let classInput = 'row mb-2 justify-content-center';
          let t = true;
          if (t) {
            classInput = 'col text-center';
          }

          const productRow = document.createElement("div");
          if (i % 2 === 0) {
            productRow.setAttribute("class", "row mb-3 py-4 shadow-sm row-colour productRows");
          } else {
            productRow.setAttribute("class", "row mb-3 py-4 shadow-sm row-colour-dark productRows");
          }
          productRow.setAttribute("tabIndex", `${tabIndex}`);
          productRow.setAttribute("id", `${this.productList[i].id}`);

          const productIdCol = document.createElement("div");
          productIdCol.setAttribute("class", `${classInput}`);
          productIdCol.setAttribute("id", `${i}-productId`);
          productIdCol.innerHTML = this.productList[i].id;
          productRow.appendChild(productIdCol);

          const nameCol = document.createElement("div");
          nameCol.setAttribute("class", `${classInput}`);
          nameCol.setAttribute("id", `${i}-name`);
          nameCol.innerText = this.productList[i].name;
          productRow.appendChild(nameCol);

          productRow.appendChild(nameCol);

          const descriptionCol = document.createElement("div");
          descriptionCol.setAttribute("class", `${classInput}`);
          descriptionCol.setAttribute("id", `${i}-description`);
          descriptionCol.innerText = this.productList[i].description;
          productRow.appendChild(descriptionCol);

          const manufacturerCol = document.createElement("div");
          manufacturerCol.setAttribute("class", `${classInput}`);
          manufacturerCol.setAttribute("id", `${i}-manufacturer`);
          manufacturerCol.innerText = this.productList[i].manufacturer;
          productRow.appendChild(manufacturerCol);

          const recommendedRetailPriceCol = document.createElement("div");
          recommendedRetailPriceCol.setAttribute("class", `${classInput}`);
          recommendedRetailPriceCol.setAttribute("id", `${i}-recommendedRetailPrice`);
          recommendedRetailPriceCol.innerText = this.productList[i].recommendedRetailPrice;
          productRow.appendChild(recommendedRetailPriceCol);

          const createdCol = document.createElement("div");
          createdCol.setAttribute("class", `${classInput}`);
          createdCol.setAttribute("id", `${i}-created`);
          createdCol.innerText = this.productList[i].created;
          productRow.appendChild(createdCol);

          outerContainer.insertBefore(productRow, lastChild);

          tabIndex += 1;

        }
      }

      let showingStart = this.productList.length ? startIndex + 1 : 0;

      const showingString = `Showing ${showingStart}-${limit} of ${this.productList.length} results`;
      const showingRow = document.createElement('div');
      showingRow.setAttribute("class", "row");
      showingRow.setAttribute("id", `showingRow`);
      const showingCol = document.createElement('div');
      showingCol.setAttribute("class", "col");
      showingCol.innerText = showingString;
      showingRow.appendChild(showingCol);

      outerContainer.insertBefore(showingRow, lastChild);

    },

    /**
     * Removes all rows of products from the page.
     */
    clearRows() {
      let allRows = document.getElementsByClassName("productRows");
      // Not sure why i-->0 works when i >0; i-- doesn't
      for (let i = allRows.length; i-- > 0;) {
        allRows[i].parentNode.removeChild(allRows[i]);
      }
      if (document.contains(document.getElementById('showingRow'))) {
        document.getElementById('showingRow').remove();
      }
    },

    /**
     * This method checks whether the given value, val, is within the given lower and upper bounds, inclusive.
     *
     * @param val, int, the value to be tested for being within the range.
     * @param min, int, the minimum value in the range.
     * @param max, int, the maximum value in the range.
     * @returns Boolean, true if within range, false is not within range.
     */
    between(val, min, max) {
      return min <= val && val <= max;
    },

    /**
     * This method determines the error message to be generated for a given input value based on the field type and
     * its associated validity (determined by a regex).
     *
     * @param name, string, name of the input field.
     * @param inputVal, string, the value entered in the stated field.
     * @param minLength, number, the minimum allowed length of the inputVal.
     * @param maxLength, number, the maximum allowed length of the inputVal.
     * @param regexMessage, string, the tailored message about the expected syntax for the inputVal if it does not
     *                              meet the regex given.
     * @param regex, string, the allowed format for the given input field.
     * @returns {string}, errorMessage, the message that needs to be raised if the inputVal does not meet the regex.
     */
    getErrorMessage(name, inputVal, minLength, maxLength, regexMessage = "", regex = /^[\s\S]*$/) {
      let errorMessage = ""; //TODO: remove after testing and just have ""
      if (inputVal === "" && minLength >= 1) {
        errorMessage = "Please enter input";
      } else if (!regex.test(inputVal)) {
        errorMessage = regexMessage;
      } else if (!this.between(inputVal.length, minLength, maxLength)) {
        errorMessage = `Input must be between ${minLength} and ${maxLength} characters long.`
      }
      return errorMessage;
    },

    /**
     * This method removes white space from the beginning and end of all the input field's input values.
     */
    trimTextInputFields() {
      this.productID = this.productID.trim();
      this.productName = this.productName.trim();
      this.recommendedRetailPrice = this.recommendedRetailPrice.trim();
      this.manufacturer = this.manufacturer.trim();
      this.description = this.description.trim();
    },

    /**
     * This method creates a new product.
     * @param e, the current event.
     */
    addNewProduct(e) {
      // Steps required for the function before starting processing.
      e.preventDefault()  // prevents page from reloading
      this.trimTextInputFields()
      let requestIsInvalid = false

      // ===================================== START OF INPUT FIELDS VALIDATION ========================================

      // Product id error checking
      this.productIDErrorMsg = this.getErrorMessage(
          this.config.productID.name,
          this.productID,
          this.config.productID.minLength,
          this.config.productID.maxLength,
          this.config.productID.regexMessage,
          this.config.productID.regex
      )
      if (this.productIDErrorMsg) {
        requestIsInvalid = true
      }

      // Product name error checking
      this.productNameErrorMsg = this.getErrorMessage(
          this.config.productName.name,
          this.productName,
          this.config.productName.minLength,
          this.config.productName.maxLength,
          this.config.productName.regexMessage,
          this.config.productName.regex
      )
      if (this.productNameErrorMsg) {
        requestIsInvalid = true
      }

      // Recommended retail price error checking
      this.recommendedRetailPriceErrorMsg = this.getErrorMessage(
          this.config.recommendedRetailPrice.name,
          this.recommendedRetailPrice,
          this.config.recommendedRetailPrice.minLength,
          this.config.recommendedRetailPrice.maxLength,
          this.config.recommendedRetailPrice.regexMessage,
          this.config.recommendedRetailPrice.regex
      )
      if (this.recommendedRetailPriceErrorMsg) {
        requestIsInvalid = true
      }

      // Product manufacturer error checking
      this.manufacturerErrorMsg = this.getErrorMessage(
          this.config.manufacturer.name,
          this.manufacturer,
          this.config.manufacturer.minLength,
          this.config.manufacturer.maxLength,
          this.config.manufacturer.regexMessage,
          this.config.manufacturer.regex
      )
      if (this.manufacturerErrorMsg) {
        requestIsInvalid = true
      }

      // Description error checking
      this.descriptionErrorMsg = this.getErrorMessage(
          this.config.description.name,
          this.description,
          this.config.description.minLength,
          this.config.description.maxLength,
      )
      if (this.descriptionErrorMsg) {
        requestIsInvalid = true
      }

      // If at any stage an error has been discovered we cancel the procedure
      if (requestIsInvalid) {
        return
      }

      // Wrapping up the product submitted fields into a class object (Product).
      const productData = {
        id: this.productID,
        name: this.productName,
        description: this.description,
        manufacturer: this.manufacturer,
        recommendedRetailPrice: this.recommendedRetailPrice,
      }

      const businessID = this.$route.params.id;
      const product = new Product(productData);

      /*
       * Add the Product to the database by sending an API request to the backend to store the product's information.
       * Raise any errors and ensure they are displayed on the UI.
       */
      Api.addNewProduct(businessID, product
      ).then((res) => {
            if (res.status === 201) {
              this.modal.hide()
              this.$router.push('/businesses/'+businessID+'/products');
            }
          }
      ).catch((error) => {
        this.cannotProceed = true;
        if (error.response) {
          if (error.response.status === 400) {
            this.toastErrorMessage = '400 Bad request; invalid product data';
          } else if (error.response.status === 403) {
            this.toastErrorMessage = 'User is not an administer of this business.'
          } else {
            this.toastErrorMessage = `${error.response.status} Unexpected error occurred!`;
          }
        } else if (error.request) {
          this.toastErrorMessage= 'Timeout occurred';
        } else {
          this.toastErrorMessage = 'Unexpected error occurred!';
        }
      })
    },

    /**
     * When the cancel or close (x) button are clicked in the create product modal this method is called.
     * This method resets the error messages and input field values and then closes the modal.
     * If the values are not reset then next time the modal is opened the modal will still display the error messages,
     * and currently stored values for the input fields.
     */
    closeCreateProductModal(){
      // Reset product id related variables
      this.productID = "";
      this.productIDErrorMsg = "";

      // Reset product name related variables
      this.productName = "";
      this.productNameErrorMsg = "";

      // Reset recommended retail price related variables
      this.recommendedRetailPrice = "";
      this.recommendedRetailPriceErrorMsg = "";

      // Reset product description related variables
      this.description = "";
      this.descriptionErrorMsg = "";

      // Reset product manufacturer related variables
      this.manufacturer = "";
      this.manufacturerErrorMsg = "";

      // Reset toast related variables
      this.toastErrorMessage = "";
      this.cannotProceed = false;

      this.modal.hide();
    }
  },

  mounted() {

    // When mounted create instance of modal
    this.modal = new Modal(this.$refs.CreateProductModal)

    /**
     * When mounted, initiate population of page.
     * If cookies are invalid or not present, redirect to login page.
     */
    const currentID = Cookies.get('userID');
    const businessID = this.$route.params.id;
    if (currentID) {
      this.requestProducts(businessID).then(
          () => this.buildRows()
      ).catch(
          (e) => console.log(e)
      )
      //this.orderProducts();
    } else {
      this.$router.push({name: 'Login'});
    }
  }
}

</script>

<style scoped>

#create-product-button {
  background-color: #1EBA8C;
  border-color: #1EBA8C;
}

#create-product-button:hover {
  background-color: transparent;
  color: #1EBA8C;
}

#create-product-button:focus {
  background-color: transparent;
  color: #1EBA8C;
}

.modal {
  background: rgba(17, 78, 60, 0.4);
}

@media only screen and (max-device-width: 600px) {
  .modal-dialog {
    margin-left: 5%;
    margin-right: 5%;
  }
}

form {
  display: flex;
  flex-direction: column;
  margin-left: auto;
  margin-right: auto;
  align-content: center;
  justify-content: center;
}

label {
  text-align: left;
  display: flex;
  flex-direction: column;
}


/*------------------ Hide arrows from input numbers ---------------------*/
/* Chrome, Safari, Edge, Opera */
input::-webkit-outer-spin-button,
input::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

/* Firefox */
input[type=number] {
  -moz-appearance: textfield;
}

input:focus, textarea:focus, button:focus, #create-product-button:focus{
  outline: none;     /* oranges! yey */
  box-shadow: 0 0 2px 2px #1EBA8C; /* Full freedom. (works also with border-radius) */
  border: 1px solid #1EBABC;
}
/*------------------------------------------------------------------------*/

</style>