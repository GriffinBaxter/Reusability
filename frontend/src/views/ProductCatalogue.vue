<template>
  <div>
    <navbar></navbar>

  <div id="outerContainer" class="container">

    <div id="body" class="container all-but-footer">

      <div class="row mt-3">
        <h2 align="center">Product Catalogue</h2>
        <h6 align="center">{{ addedMessage }}</h6>
      </div>

      <div class="row mb-3">
        <div class="col">
          <button id="create-product-button" type="button" class="btn btn-md btn-primary float-end mt-4" tabindex="2"
                  @click="modal.show()">Create Product</button>
        </div>
      </div>

      <Table table-id="product-catalogue-id" null-string-value="N/A" :table-tab-index="0" :table-headers="tableHeaders" :table-data="tableData"
             :max-rows-per-page="rowsPerPage" :total-rows="totalRows" :current-page-override="currentPage" :order-by-override="tableOrderBy" :table-data-is-page="true"
             @update-current-page="event => updatePage(event)" @order-by-header-index="event => orderProducts(event)" @row-selected="event => showDetails(event.index)"></Table>

    </div>

    <div v-if="showModal">
      <transition name="fade">
        <div class="modal-mask">
          <div class="modal-wrapper">
            <div class="modal-dialog modal-">
              <div class="modal-content">
                <div class="modal-body">
                  <product-modal
                      v-bind:product-id="productId"
                      v-bind:product-name="productName"
                      v-bind:description="description"
                      v-bind:manufacturer="manufacturer"
                      v-bind:recommended-retail-price="recommendedRetailPrice"
                      v-bind:created="created" />
                </div>
                <div class="modal-footer">
                  <button class="btn btn-outline-primary float-end" id="closeModalButton" @click="showModal = false">Close</button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </transition>
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
    <Footer></Footer>

  </div>
</template>

<script>
import { Modal } from 'bootstrap'
import Api, { Product } from '../Api';
import Cookies from 'js-cookie';
import Navbar from "../components/Navbar";
import Footer from "../components/Footer";
import ProductModal from "@/components/ProductModal";
import Table from "@/components/Table";

export default {
  name: "ProductCatalogue",
  components: {
    Table,
    ProductModal,
    Navbar,
    Footer
  },

  data() {
    return {
      // Table variables
      // A list of the table headers
      tableHeaders: ["Product ID", "Name", "Manufacturer", "Recommended Retail Price", "Created"],
      // A list of the ordering by headers, which is used with talking to the backend
      tableOrderByHeaders: ["productId", "name", "recommendedRetailPrice", "manufacturer", "created"],
      // A list of all the data points belonging to the table
      tableData: [],
      // Used to tell the table what is the current ordering (for visual purposes).
      tableOrderBy: {orderBy: null, isAscending: true},
      // Stores the URL string that is used by the requestProducts() to order the products
      orderByString: "",
      // A list of Product object that store the products
      productList: [],
      // These variables are used to control and update the table.
      rowsPerPage: 5,
      currentPage: 0,
      totalRows: 0,


      // Product modal varaibles
      productId: null,
      productName: null,
      description: null,
      manufacturer: null,
      recommendedRetailPrice: 0,
      created: null,
      showModal: false,
      small: false,

      modal: null,

      // Used for having pre-filled input fields
      DEBUG_MODE: false,

      // A copy of the product config file for error checking.
      config: Product.config,

      businessId: 0,
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

      // Message to display that product has been added to catalogue
      addedMessage: "",
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
    showDetails(productIndex) {
      let product = this.productList[productIndex];
      this.productId = product.id;
      this.productName = product.name;
      this.description = product.description;
      this.manufacturer = product.manufacturer;
      this.recommendedRetailPrice = product.recommendedRetailPrice;
      this.created = product.created;
      this.showModal = true;
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
      this.$router.push({path: `/businessProfile/${this.businessId}/productCatalogue`, query: {"orderBy": this.orderBy, "page": (this.currentPage).toString()}})
      this.requestProducts();
    },
    /**
     * Parses the orderByString and returns the resulted Objects.
     * @return {{orderBy: null | String, isAscending: boolean}} This contains the {orderBy, isAscending} properties of the this.orderByString .
     * Emulates a click when the product presses enter on a column header.
     *
     * @param event The keydown event
     */
    parseOrderBy() {
      let orderBy = null;
      let isAscending = true;

      // If the last 3 letters are ASC then we can assume the orderBy is the other component of that orderByString.
      // This also means isAscending is true.
      if (this.orderByString.slice(this.orderByString.length-3) === 'ASC') {
        orderBy = this.orderByString.slice(0, this.orderByString.length-3);

      // If the last 4 letters are DESC then we can assume the orderBy is the other component of the orderByString
      // This also means that isAscending is false.
      } else if (this.orderByString.slice(this.orderByString.length-4) === 'DESC') {
        orderBy = this.orderByString.slice(0, this.orderByString.length-4)
        isAscending = false;
      }
    },

      // If we found a valid orderBy compare it against he allowed orderBy headers in tableOrderByHeaders
      if (orderBy !== null) {
        orderBy = this.tableOrderByHeaders.indexOf(orderBy);

        // If the orderBy is returned as -1. This means that no header was found!
        // So we say it is unordered.
        if (orderBy === -1) {
          orderBy = null;
        }
      }

      return {orderBy, isAscending};
    },
    /**
     * Requests a list of products matching the given business ID from the back-end.
     * If successful it updates the Table to contain the new data points.
     * If successful it sets the productList variable to the response data.
     *
     * @return {Promise}
     */
    async requestProducts() {

      // Getting all the information necssary from the route update (params and query).
      this.businessId = parseInt(this.$route.params.id);
      this.orderByString = this.$route.query["orderBy"] || "productIdASC";
      this.currentPage = parseInt(this.$route.query["page"]) || 0;

      // Perfrom the call to sort the products and get them back.
      await Api.sortProducts(this.businessId, this.orderByString, this.currentPage).then(response => {

        // Parsing the orderby string to get the orderBy and isAscending components to update the table.
        const {orderBy, isAscending} = this.parseOrderBy();
        this.tableOrderBy = {orderBy: orderBy, isAscending: isAscending};

        this.productList = [...response.data];
        let newtableData = [];

        // No results
        if (this.productList.length <= 0) {
          this.currentPage = 1;
          this.maxPage = 1;
          this.totalRows = 0;
        // Generate the tableData to be placed in the table & get the total number of rows.
        } else {
          this.totalRows = parseInt(response.headers["total-rows"]);

          for (let i = 0; i < this.productList.length; i++ ) {
            newtableData.push(this.productList[i].id);
            newtableData.push(this.productList[i].name);
            newtableData.push(this.productList[i].manufacturer);
            newtableData.push(this.productList[i].recommendedRetailPrice);
            newtableData.push(this.productList[i].created);
          }

          this.tableData = newtableData;
        }

      }).catch((error) => {
        if (error.request && !error.response) {
          this.$router.push({path: '/timeout'});
        } else if (error.response.status === 400) {
          this.$router.push({path: '/pageDoesNotExist'});
        } else if (error.response.status === 401) {
          this.$router.push({path: '/invalidtoken'});
        } else if (error.response.status === 403) {
          this.$router.push({path: '/forbidden'});
        } else if (error.response.status === 406) {
          this.$router.push({path: '/noBusiness'});
        } else {
          this.$router.push({path: '/timeout'});
          console.log(error.message);
        }
      })
    },

    /**
     * Updates the URL and calls the requestProducts() to update the table.
     * @param event This contains the {orderBy, isAscending} components of the new desired ordering.
     */
    orderProducts(event) {
      this.orderByString = `${this.tableOrderByHeaders[event.orderBy]}${event.isAscending ? 'ASC' : 'DESC'}`
      this.$router.push({path: `/businessProfile/${this.businessId}/productCatalogue`, query: {"orderBy": this.orderByString, "page": (this.currentPage).toString()}});
      if (this.currentPage > 1) {
        this.currentPage -= 1;
        this.$router.push({path: `/businessProfile/${this.businessId}/productCatalogue`, query: {"orderBy": this.orderBy, "page": (this.currentPage).toString()}})
        this.requestProducts()
      }
    },

    /**
     * Goes to the next page and updates the rows.
     *
     */
    nextPage() {
      this.addedMessage = ""; // Clear product added message, so it doesn't stick around.

      if (this.currentPage < this.maxPage) {
        this.currentPage += 1;
        this.$router.push({path: `/businessProfile/${this.businessId}/productCatalogue`, query: {"orderBy": this.orderBy, "page": (this.currentPage).toString()}})
        this.requestProducts()
      }
    },

    /**
     * Orders the products based on the given booleans for each column, and updates the display
     * @param id Boolean, whether to order by productId
     * @param name Boolean, whether to order by name
     * @param manufacturer Boolean, whether to order by manufacturer
     * @param recommendedRetailPrice Boolean, whether to order by RRP
     * @param created Boolean, whether to order by created date
     */
    orderProducts(id, name, manufacturer, recommendedRetailPrice, created) {

      this.addedMessage = ""; // Clear product added message, so it doesn't stick around.

      if (id) {
        this.disableIcons()
        if (this.productIdAscending) {
          this.orderBy = "productIdASC"
          document.getElementById('productIdIcon').setAttribute('class', 'fas fa-chevron-up float-end');
        } else {
          this.orderBy = "productIdDESC"
          document.getElementById('productIdIcon').setAttribute('class', 'fas fa-chevron-down float-end');
        }

        this.productIdAscending = !this.productIdAscending;
        this.nameAscending = false;
        this.manufacturerAscending = false;
        this.recommendedRetailPriceAscending = false;
        this.createdAscending = false;

        this.buildRows();
      } else if (name) {
        this.disableIcons()
        if (this.nameAscending) {
          this.orderBy = "nameASC"
          document.getElementById('nameIcon').setAttribute('class', 'fas fa-chevron-up float-end');
        } else {
          this.orderBy = "nameDESC"
          document.getElementById('nameIcon').setAttribute('class', 'fas fa-chevron-down float-end');
        }

        this.productIdAscending = false;
        this.nameAscending = !this.nameAscending;
        this.manufacturerAscending = false;
        this.recommendedRetailPriceAscending = false;
        this.createdAscending = false;

        this.buildRows();
      } else if (manufacturer) {
        this.disableIcons()
        if (this.manufacturerAscending) {
          this.orderBy = "manufacturerASC"
          document.getElementById('manufacturerIcon').setAttribute('class', 'fas fa-chevron-up float-end');
        } else {
          this.orderBy = "manufacturerDESC"
          document.getElementById('manufacturerIcon').setAttribute('class', 'fas fa-chevron-down float-end');
        }

        this.productIdAscending = false;
        this.nameAscending = false;
        this.manufacturerAscending = !this.manufacturerAscending;
        this.recommendedRetailPriceAscending = false;
        this.createdAscending = false;

        this.buildRows();
      } else if (recommendedRetailPrice) {
        this.disableIcons()
        if (this.recommendedRetailPriceAscending) {
          this.orderBy = "recommendedRetailPriceASC"
          document.getElementById('recommendedRetailPriceIcon').setAttribute('class', 'fas fa-chevron-up float-end');
        } else {
          this.orderBy = "recommendedRetailPriceDESC"
          document.getElementById('recommendedRetailPriceIcon').setAttribute('class', 'fas fa-chevron-down float-end');
        }

        this.productIdAscending = false;
        this.nameAscending = false;
        this.manufacturerAscending = false;
        this.recommendedRetailPriceAscending = !this.recommendedRetailPriceAscending;
        this.createdAscending = false;

        this.buildRows();
      } else if (created) {
        this.disableIcons()
        if (this.createdAscending) {
          this.orderBy = "createdASC";
          document.getElementById('createdIcon').setAttribute('class', 'fas fa-chevron-up float-end');
        } else {
          this.orderBy = "createdDESC";
          document.getElementById('createdIcon').setAttribute('class', 'fas fa-chevron-down float-end');
        }
        this.productIdAscending = false;
        this.nameAscending = false;
        this.manufacturerAscending = false;
        this.recommendedRetailPriceAscending = false;
        this.createdAscending = !this.createdAscending;
      }


      this.$router.push({path: `/businessProfile/${this.businessId}/productCatalogue`, query: {"orderBy": this.orderBy, "page": (this.currentPage).toString()}});
      this.requestProducts();

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

      const product = new Product(productData);

      /*
       * Add the Product to the database by sending an API request to the backend to store the product's information.
       * Raise any errors and ensure they are displayed on the UI.
       */
      Api.addNewProduct(this.businessId, product
      ).then((res) => {
            if (res.status === 201) {
              this.modal.hide();

              // Set message so user knows product has been added.
              this.addedMessage = "Product With ID: " + this.productID + ", Added to Catalogue";

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

              this.requestProducts().then(
                  () => this.buildRows()
              ).catch(
                  (e) => console.log(e)
              )
            }
          }
      ).catch((error) => {
        this.cannotProceed = true;
        if (error.response) {
          if (error.response.status === 400) {
            this.toastErrorMessage = '400 Bad request; invalid product data';
          } else if (error.response.status === 403) {
            this.toastErrorMessage = 'User is not an administer of this business.';
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
    if (currentID) {
      this.requestProducts().then(
          () => {}
      ).catch(
          (e) => console.log(e)
      )
    } else {
      this.$router.push({name: 'Login'});
    }
  }
}
</script>

<style scoped>

.modal-content {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 99;

  width: 100%;
  max-width: 1000px;
  background-color: #FFFFFF;
  border-radius: 10px;

  padding: 10px;
}

.modal-mask {
  position: fixed;
  z-index: 9998;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, .5);
  display: table;
  transition: opacity .3s ease;
}

.all-but-footer {
  min-height: calc(100vh - 240px);
}
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

h6 {
  color: #1EBA8C;
}

.modal {
  background: rgba(17, 78, 60, 0.4);
}

.modal-content {
  box-shadow: 0 0 1px 1px #404040;
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
