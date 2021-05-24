<template>
  <!-- Modal -->
  <div class="modal fade" id="creationPopup" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1"
       aria-labelledby="creationPopupTitle" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">

        <!--title-->
        <div class="modal-header">
          <h2 id="creationPopupTitle">New Inventory Item</h2>
        </div>

        <!--modal body-->
        <div class="modal-body">
          <form class="row" id="inventoryItemCreation" @submit.prevent>

            <!--product id-->
            <div class="col-7 form-group py-1 px-3">
              <label for="product-id">Product ID*: </label>
              <input id="product-id" name="product-id" type="text" v-model="productId"
                     :class="inventoryValidationHelper.toggleInvalidClass(productIdErrorMsg)" :maxlength="config.productId.maxLength" required>
              <div class="invalid-feedback">
                {{ productIdErrorMsg }}
              </div>
            </div>

            <!--quantity-->
            <div class="col-5 form-group py-1 px-3">
              <label for="quantity">Quantity*: </label>
              <input id="quantity" name="quantity" type="number" v-model="quantity" min="0"
                     :class="inventoryValidationHelper.toggleInvalidClass(quantityErrorMsg)" :maxlength="config.quantity.maxLength" required>
              <div class="invalid-feedback">
                {{ quantityErrorMsg }}
              </div>
            </div>


            <!--price per item-->
            <div class="col-6 form-group py-1 px-3">
              <label for="price-per-item" v-if="currencyCode != ''">Price Per Item ({{ currencyCode }}): </label>
              <label for="price-per-item" v-else>Price Per Item: </label>
              <div class="input-group">
                <div class="input-group-prepend" v-if="currencySymbol != ''">
                  <span class="input-group-text">{{ currencySymbol }}</span>
                </div>
                <input id="price-per-item" name="price-per-item" type="number" step="0.01"
                       v-model="pricePerItem"
                       min="0" :class="inventoryValidationHelper.toggleInvalidClass(pricePerItemErrorMsg)"
                       :maxlength="config.pricePerItem.maxLength">
                <div class="invalid-feedback">
                  {{ pricePerItemErrorMsg }}
                </div>
              </div>
            </div>

            <!--total price-->
            <div class="col-6 form-group py-1 px-3">
              <label for="total-price" v-if="currencyCode != ''">Total Price ({{ currencyCode }}): </label>
              <label for="total-price" v-else>Total Price: </label>
              <div class="input-group">
                <div class="input-group-prepend" v-if="currencySymbol != ''">
                  <span class="input-group-text">{{ currencySymbol }}</span>
                </div>
                <input id="total-price" name="total-price" type="number" step="0.01" v-model="totalPrice"
                       min="0" :class="inventoryValidationHelper.toggleInvalidClass(totalPriceErrorMsg)" :maxlength="config.totalPrice.maxLength">
                <div class="invalid-feedback">
                  {{ totalPriceErrorMsg }}
                </div>
              </div>
            </div>

            <!--manufactured-->
            <div class="col-12 form-group py-1 px-3">
              <label for="manufactured">Manufactured: </label>
              <input id="manufactured" name="manufactured" type="date" v-model="manufactured"
                     :class="inventoryValidationHelper.toggleInvalidClass(manufacturedErrorMsg)">
              <div class="invalid-feedback">
                {{ manufacturedErrorMsg }}
              </div>
            </div>

            <!--sell by-->
            <div class="col-12 form-group py-1 px-3">
              <label for="sell-by">Sell By: </label>
              <input id="sell-by" name="sell-by" type="date" v-model="sellBy"
                     :class="inventoryValidationHelper.toggleInvalidClass(sellByErrorMsg)">
              <div class="invalid-feedback">
                {{ sellByErrorMsg }}
              </div>
            </div>

            <!--best before-->
            <div class="col-12 form-group py-1 px-3">
              <label for="best-before">Best Before: </label>
              <input id="best-before" name="best-before" type="date" v-model="bestBefore"
                     :class="inventoryValidationHelper.toggleInvalidClass(bestBeforeErrorMsg)">
              <div class="invalid-feedback">
                {{ bestBeforeErrorMsg }}
              </div>
            </div>

            <!--expires-->
            <div class="col-12 form-group py-1 px-3">
              <label for="expires">Expires*: </label>
              <input class="col-6" id="expires" name="expires" type="date" v-model="expires"
                     :class="inventoryValidationHelper.toggleInvalidClass(expiresErrorMsg)" required>
              <div class="invalid-feedback">
                {{ expiresErrorMsg }}
              </div>
            </div>
            <div class="text-center text-danger">
              {{ toastErrorMessage }}
            </div>

          </form>
        </div>

        <!--footer-->
        <div class="modal-footer justify-content-between">
          <button type="button" class="btn green-button-transparent" @click="dataReset(false)" data-bs-dismiss="modal">
            Cancel
          </button>
          <button type="button" class="btn green-button " @click="createNewInventoryItem()">Confirm</button>
        </div>

      </div>
    </div>
  </div>
</template>

<script>

import {Modal} from "bootstrap"; //uncommenting means the test do not run
import Api from "../../Api";
import InventoryItem from "../../configs/InventoryItem";
import {parseISO} from 'date-fns'
const inventoryValidationHelper = require('../../components/inventory/InventoryValidationHelper');

export default {
  name: 'InventoryItemCreation',
  data() {
    return {
      // A copy of the product config file for error checking.
      config: InventoryItem.config,
      modal: null,
      businessId: this.$route.params.id,

      // product Id related variables
      productId: "",
      productIdErrorMsg: "",

      // quantity related variables
      quantity: "",
      quantityErrorMsg: "",

      // price per item related variables
      pricePerItem: "",
      pricePerItemErrorMsg: "",

      // total price related variables
      totalPrice: "",
      totalPriceErrorMsg: "",

      // manufactured related variables
      manufactured: "",
      manufacturedErrorMsg: "",

      // sell by related variables
      sellBy: "",
      sellByErrorMsg: "",

      // best Before Id related variables
      bestBefore: "",
      bestBeforeErrorMsg: "",

      // expires related variables
      expires: "",
      expiresErrorMsg: "",

      toastErrorMessage: "",

      inventoryValidationHelper: inventoryValidationHelper
    }
  },
  props: {
    currencyCode: {
      type: String,
      default: "",
      required: false
    },
    currencySymbol: {
      type: String,
      default: "",
      required: false
    }
  },
  methods: {
    /**
     * This method removes white space from the beginning and end of all the input field's input values.
     */
    trimTextInputFields() {
      this.productID = this.productId.trim();
      this.quantity = this.quantity.trim();
      this.pricePerItem = this.pricePerItem.trim();
      this.totalPrice = this.totalPrice.trim();
      this.manufactured = this.manufactured.trim();
      this.sellBy = this.sellBy.trim();
      this.bestBefore = this.bestBefore.trim();
      this.expires = this.expires.trim();
    },

    /**
     * reset all input file
     */
    dataReset(createSuccess) {
      if (createSuccess) {
        this.$emit('updateInventoryItem')
      }

      this.modal.hide();
      // product Id related variables
      this.productId = "";
      this.productIdErrorMsg = "";

      // quantity related variables
      this.quantity = "";
      this.quantityErrorMsg = "";

      // price per item related variables
      this.pricePerItem = "";
      this.pricePerItemErrorMsg = "";

      // total price related variables
      this.totalPrice = "";
      this.totalPriceErrorMsg = "";

      // manufactured related variables
      this.manufactured = "";
      this.manufacturedErrorMsg = "";

      // sell by related variables
      this.sellBy = "";
      this.sellByErrorMsg = "";

      // best Before Id related variables
      this.bestBefore = "";
      this.bestBeforeErrorMsg = "";

      // expires related variables
      this.expires = "";
      this.expiresErrorMsg = "";
    },

    /**
     *
     */
    createNewInventoryItem() {
      // Steps required for the function before starting processing.
      // inventoryItem.preventDefault()  // prevents page from reloading
      this.trimTextInputFields()
      let requestIsInvalid = false

      // ===================================== START OF INPUT FIELDS VALIDATION ========================================

      // Product Id error checking
      this.productIdErrorMsg = inventoryValidationHelper.getErrorMessage(
          this.config.productId.name,
          this.productId,
          this.config.productId.minLength,
          this.config.productId.maxLength,
          this.config.productId.regexMessage,
          this.config.productId.regex
      )
      if (this.productIdErrorMsg) {
        requestIsInvalid = true
      }

      // Quantity error checking
      this.quantityErrorMsg = inventoryValidationHelper.getErrorMessage(
          this.config.quantity.name,
          this.quantity,
          this.config.quantity.minLength,
          this.config.quantity.maxLength,
          this.config.quantity.regexMessage,
          this.config.quantity.regex
      )
      if (this.quantity <= 0) {
        this.quantityErrorMsg = "At least one"
      }
      if (this.quantityErrorMsg) {
        requestIsInvalid = true
      }

      // Price per item error checking
      this.pricePerItemErrorMsg = inventoryValidationHelper.getErrorMessage(
          this.config.pricePerItem.name,
          this.pricePerItem,
          this.config.pricePerItem.minLength,
          this.config.pricePerItem.maxLength,
          this.config.pricePerItem.regexMessage,
          this.config.pricePerItem.regex
      )
      if (this.pricePerItemErrorMsg) {
        requestIsInvalid = true
      }

      // Total price error checking
      this.totalPriceErrorMsg = inventoryValidationHelper.getErrorMessage(
          this.config.totalPrice.name,
          this.totalPrice,
          this.config.totalPrice.minLength,
          this.config.totalPrice.maxLength,
          this.config.totalPrice.regexMessage,
          this.config.totalPrice.regex
      )
      if (this.totalPriceErrorMsg) {
        requestIsInvalid = true
      }
      // Manufacture date error checking
      if (!inventoryValidationHelper.isValidManufactureDate(this.manufactured)) {
        this.manufacturedErrorMsg = "Manufactured date must be prior to today's date";
        requestIsInvalid = true;
      } else {
        this.manufacturedErrorMsg = '';
      }

      // Sell by date error checking
      if (!inventoryValidationHelper.isValidSellByDate(this.sellBy, this.manufactured, this.expires)) {
        this.sellByErrorMsg = "Sell by date must be after today's date but not today's date, and after the manufacture date and before the expiry date (not including)";
        requestIsInvalid = true;
      } else {
        this.sellByErrorMsg = '';
      }

      // Best best date before error checking
      if (!inventoryValidationHelper.isValidBestBeforeDate(this.bestBefore, this.manufactured, this.expires)) {
        this.bestBeforeErrorMsg = "Best before date must be after today's date but not today's date, after the manufacture date and before expiry date";
        requestIsInvalid = true;
      } else {
        this.bestBeforeErrorMsg = '';
      }

      // Expiry date error checking
      if (!inventoryValidationHelper.isValidExpiryDate(this.expires, this.bestBefore, this.manufactured)) {
        this.expiresErrorMsg = "Expiry date of the inventory item is after today's date, after the manufacture date, and after or equal to the best before date";
        requestIsInvalid = true;
      } else {
        this.expiresErrorMsg = '';
      }

      // If at any stage an error has been discovered we cancel the procedure
      if (requestIsInvalid) {
        return
      }

      // Wrapping up the product submitted fields into a class object (InventoryItem).
      const inventoryItemData = {
        productId: this.productId,
        quantity: this.quantity,
        pricePerItem: this.pricePerItem,
        totalPrice: this.totalPrice,
        manufactured: parseISO(this.manufactured),
        sellBy: parseISO(this.sellBy),
        bestBefore: parseISO(this.bestBefore),
        expires: parseISO(this.expires)
      }
      const newInventoryItem = new InventoryItem(inventoryItemData);

      /**
       * Add the Product to the database by sending an API request to the backend to store the product's information.
       * Raise any errors and ensure they are displayed on the UI.
       */
      Api.addNewInventoryItem(this.businessId, newInventoryItem
      ).then((response) => {
            if (response.status === 201) {
              this.dataReset(true);
              this.toastErrorMessage = "";
            }
          }
      ).catch((error) => {
        this.cannotProceed = true;
        if (error.response) {
          if (error.response.status === 400) {
            this.toastErrorMessage = 'Invalid Product Data. Please check the product ID';
          } else if (error.response.status === 401) {
            this.toastErrorMessage = "You must be logged in to perform this action.";
          } else if (error.response.status === 403) {
            this.toastErrorMessage = 'User is not an administer of this business.';
          } else {
            this.toastErrorMessage = `${error.response.status} Unexpected error occurred!`;
          }
        } else if (error.request) {
          this.toastErrorMessage = 'Timeout occurred';
        } else {
          this.toastErrorMessage = 'Unexpected error occurred!';
        }
      })
    }

  },
  mounted() {
    this.modal = new Modal(document.getElementById("creationPopup"));
  }
};
</script>

<style scoped>

/* Styles the input and text area's borders to be green when they are focused/tabbed to */
input:focus, textarea:focus {
  outline: none;
  box-shadow: 0 0 2px 2px #1EBA8C;
  border: 1px solid #1EBABC;
}

</style>
