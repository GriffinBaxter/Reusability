<template>
  <!-- Modal -->
  <div class="modal fade" ref="_updateInventoryItemModal" id="updateInventoryItemModal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1"
       aria-labelledby="updateInventoryItemModalTitle" aria-hidden="false">
    <div class="modal-dialog">
      <div class="modal-content">

        <!-- Title -->
        <div class="modal-header">
          <h2 id="updateInventoryItemModalTitle">Update Inventory Item</h2>
        </div>

        <!-- Modal Body -->
        <div class="modal-body">
          <form class="row" id="inventoryItemUpdate" novalidate @submit.prevent>

            <!-- Error message card-->
            <div class="row my-lg-2">
              <div class="col-12 mx-auto">
                <div v-if="formErrorModalMessage" class="alert alert-danger">
                  <label>{{formErrorModalMessage}}</label>
                </div>
              </div>
            </div>

            <!-- Product ID -->
            <div class="col-7 form-group py-1 px-3">
              <label for="product-id">Product ID*: </label>
              <input id="product-id" name="product-id" tabindex="1" type="text"
                     v-model="newInventoryItem.data.productId" :class="inventoryValidationHelper.toggleInvalidClass(productIdErrorMsg)"
                     :maxlength="config.productId.maxLength" required>
              <div class="invalid-feedback">
                {{ productIdErrorMsg }}
              </div>
            </div>

            <!-- Quantity -->
            <div class="col-5 form-group py-1 px-3">
              <label for="quantity">Quantity*: </label>
              <input id="quantity" name="quantity" tabindex="2" type="number" min="0"
                     v-model="newInventoryItem.data.quantity" :class="inventoryValidationHelper.toggleInvalidClass(quantityErrorMsg)"
                     :maxlength="config.quantity.maxLength" required>
              <div class="invalid-feedback">
                {{ quantityErrorMsg }}
              </div>
            </div>


            <!-- Price Per Item -->
            <div class="col-6 form-group py-1 px-3">
              <label for="price-per-item">Price Per Item ({{ currencyCode }}): </label>
              <div class="input-group">
                <div class="input-group-prepend">
                  <span class="input-group-text">{{ currencySymbol }}</span>
                </div>
                <input id="price-per-item" name="price-per-item" tabindex="3" type="number" step="0.01"
                       v-model="newInventoryItem.data.pricePerItem" :class="inventoryValidationHelper.toggleInvalidClass(pricePerItemErrorMsg)"
                       min="0" :maxlength="config.pricePerItem.maxLength">
                <div class="invalid-feedback">
                  {{ pricePerItemErrorMsg }}
                </div>
              </div>
            </div>

            <!-- Total Price -->
            <div class="col-6 form-group py-1 px-3">
              <label for="total-price">Total Price ({{ currencyCode }}): </label>
              <div class="input-group">
                <div class="input-group-prepend">
                  <span class="input-group-text">{{ currencySymbol }}</span>
                </div>
                <input id="total-price" name="total-price" tabindex="4" type="number" step="0.01"
                       v-model="newInventoryItem.data.totalPrice" :class="inventoryValidationHelper.toggleInvalidClass(totalPriceErrorMsg)"
                       :maxlength="config.totalPrice.maxLength">
                <div class="invalid-feedback">
                  {{ totalPriceErrorMsg }}
                </div>
              </div>
            </div>

            <!-- Manufactured -->
            <div class="col-12 form-group py-1 px-3">
              <label for="manufactured">Manufactured: </label>
              <input id="manufactured" name="manufactured" tabindex="5" type="date"
                     v-model="newInventoryItem.data.manufactured" :class="inventoryValidationHelper.toggleInvalidClass(manufacturedErrorMsg)">
              <div class="invalid-feedback">
                {{ manufacturedErrorMsg }}
              </div>
            </div>

            <!-- Sell By -->
            <div class="col-12 form-group py-1 px-3">
              <label for="sell-by">Sell By: </label>
              <input id="sell-by" name="sell-by" tabindex="6" type="date"
                     v-model="newInventoryItem.data.sellBy" :class="inventoryValidationHelper.toggleInvalidClass(sellByErrorMsg)">
              <div class="invalid-feedback">
                {{ sellByErrorMsg }}
              </div>
            </div>

            <!-- Best Before -->
            <div class="col-12 form-group py-1 px-3">
              <label for="best-before">Best Before: </label>
              <input id="best-before" name="best-before" tabindex="7" type="date"
                     v-model="newInventoryItem.data.bestBefore" :class="inventoryValidationHelper.toggleInvalidClass(bestBeforeErrorMsg)">
              <div class="invalid-feedback">
                {{ bestBeforeErrorMsg }}
              </div>
            </div>

            <!-- Expires -->
            <div class="col-12 form-group py-1 px-3">
              <label for="expires">Expires*: </label>
              <input class="col-6" id="expires" name="expires" tabindex="8" type="date"
                     v-model="newInventoryItem.data.expires" :class="inventoryValidationHelper.toggleInvalidClass(expiresErrorMsg)"
                     required>
              <div class="invalid-feedback">
                {{ expiresErrorMsg }}
              </div>
            </div>

          </form>
        </div>


        <!--footer-->
        <div class="modal-footer justify-content-between">
          <button id="cancelButton" type="button" class="btn green-button-transparent" data-bs-dismiss="modal">Cancel</button>
          <button id="saveButton" type="button" class="btn green-button" @click="updateInventoryItem($event)">Save changes</button>
        </div>

      </div>
    </div>
  </div>
</template>

<script>

import { Modal } from 'bootstrap'
import InventoryItem from "../../configs/InventoryItem";
const inventoryValidationHelper = require('../../components/inventory/InventoryValidationHelper');
import Api from "../../Api";

export default {
  name: "UpdateInventoryItemModal",
  props: {
    value: {
      type: Object,
      required: false
    },
    businessId: {
      type: Number,
      required: true
    },
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
  data() {
    return {
      // Used to display a modal that contains the form to edit the product
      modal: null,
      // This defines the error message regarding different responses from the request to Axios
      formErrorModalMessage: "",

      // The config for checking the inventory item parameters when updated
      config: InventoryItem.config,

      // The new inventory item
      newInventoryItem: new InventoryItem(
          {
            productId: "DEFAULT",
            quantity: 1,
            pricePerItem: 0,
            totalPrice: 0,
            manufactured: "2021-01-01",
            sellBy: "2021-01-01",
            bestBefore: "2021-01-01",
            expires: "2021-01-01"
          }
      ),

      // Error messages
      productIdErrorMsg: "",
      quantityErrorMsg: "",
      pricePerItemErrorMsg: "",
      totalPriceErrorMsg: "",
      manufacturedErrorMsg: "",
      sellByErrorMsg: "",
      bestBeforeErrorMsg: "",
      expiresErrorMsg: "",

      inventoryValidationHelper: inventoryValidationHelper
    }
  },
  methods: {
    /**
     * Emits an event that updates the v-model prop value.
     * @param value The new value of the value prop.
     */
    updateValue(value) {
      this.$emit('input', value)
    },

    /**
     * Updates the placeholder values before showing the modal.
     *
     */
    showModal() {
      // If the modal is already showing prevent the placeholders from being updated.
      if (!this.$refs._updateInventoryItemModal.classList.contains("show")) {
        // Update the placeholders
        this.newInventoryItem.data.productId = this.value.productId;
        this.newInventoryItem.data.quantity = this.value.quantity;
        this.newInventoryItem.data.pricePerItem = this.value.pricePerItem;
        this.newInventoryItem.data.totalPrice = this.value.totalPrice;
        this.newInventoryItem.data.manufactured = this.value.manufactured;
        this.newInventoryItem.data.sellBy = this.value.sellBy;
        this.newInventoryItem.data.bestBefore = this.value.bestBefore;
        this.newInventoryItem.data.expires = this.value.expires;

        // Reset all the error messages
        this.productIdErrorMsg = "";
        this.quantityErrorMsg = "";
        this.pricePerItemErrorMsg = "";
        this.totalPriceErrorMsg = "";
        this.manufacturedErrorMsg = "";
        this.sellByErrorMsg = "";
        this.bestBeforeErrorMsg = "";
        this.expiresErrorMsg = "";
      }

      // Show the modal
      this.modal.show();
    },

    /**
     * This function validates all of the inventory item fields and sets requestIsInvalid to true if any errors
     * are found in the values. This also sets the error messages based on the value, minimum and maximum length,
     * regex message, and regex.
     *
     * @return requestIsInvalid, true if any error in the data is found
     */
    validateFields() {
      let requestIsInvalid = false;

      if (this.newInventoryItem.data.pricePerItem == null) {
        this.newInventoryItem.data.pricePerItem = "";
      }
      if (this.newInventoryItem.data.totalPrice == null) {
        this.newInventoryItem.data.totalPrice = "";
      }

      // Product Id error checking
      this.productIdErrorMsg = inventoryValidationHelper.getErrorMessage(
          this.config.productId.name,
          this.newInventoryItem.data.productId,
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
          this.newInventoryItem.data.quantity.toString(),
          this.config.quantity.minLength,
          this.config.quantity.maxLength,
          this.config.quantity.regexMessage,
          this.config.quantity.regex
      )
      if (this.newInventoryItem.data.quantity <= 0) {
        this.quantityErrorMsg = "Must be a quantity of at least 1"
      }
      if (this.quantityErrorMsg) {
        requestIsInvalid = true
      }

      // Price per item error checking
      this.pricePerItemErrorMsg = inventoryValidationHelper.getErrorMessage(
          this.config.pricePerItem.name,
          this.newInventoryItem.data.pricePerItem.toString(),
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
          this.newInventoryItem.data.totalPrice.toString(),
          this.config.totalPrice.minLength,
          this.config.totalPrice.maxLength,
          this.config.totalPrice.regexMessage,
          this.config.totalPrice.regex
      )
      if (this.totalPriceErrorMsg) {
        requestIsInvalid = true
      }

      // Manufacture date error checking
      if (!inventoryValidationHelper.isValidManufactureDate(this.newInventoryItem.data.manufactured)) {
        this.manufacturedErrorMsg = "Manufactured date must be prior to today's date";
        requestIsInvalid = true;
      } else {
        this.manufacturedErrorMsg = '';
      }

      // Sell by date error checking
      if (!inventoryValidationHelper.isValidSellByDate(this.newInventoryItem.data.sellBy, this.newInventoryItem.data.manufactured, this.newInventoryItem.data.expires)) {
        this.sellByErrorMsg = "Sell by date must be after today's date but not today's date, and after the manufacture date and before the expiry date (not including)";
        requestIsInvalid = true;
      } else {
        this.sellByErrorMsg = '';
      }

      // Best best date before error checking
      if (!inventoryValidationHelper.isValidBestBeforeDate(this.newInventoryItem.data.bestBefore, this.newInventoryItem.data.manufactured, this.newInventoryItem.data.expires)) {
        this.bestBeforeErrorMsg = "Best before date must be after today's date but not today's date, after the manufacture date and before expiry date";
        requestIsInvalid = true;
      } else {
        this.bestBeforeErrorMsg = '';
      }

      // Expiry date error checking
      if (!inventoryValidationHelper.isValidExpiryDate(this.newInventoryItem.data.expires, this.newInventoryItem.data.bestBefore, this.newInventoryItem.data.manufactured)) {
        this.expiresErrorMsg = "Expiry date of the inventory item is after today's date, after the manufacture date, and after or equal to the best before date";
        requestIsInvalid = true;
      } else {
        this.expiresErrorMsg = '';
      }

      return requestIsInvalid;
    },

    /**
     * Given an update event we perform a validation and perform the call to the API to modify the inventory item.
     * @param event An update button event.
     */
    updateInventoryItem(event) {
      // Prevent any default actions
      event.preventDefault();

      // If any errors with the input data is found in the validateFields method, then it is true and the update is aborted
      if (!this.validateFields()) {
        Api.modifyInventoryItem(this.value.id, this.businessId, this.newInventoryItem)
            .then(
                res => {
                  if (res.data.status === 200) {
                    this.updateValue(new InventoryItem(this.newInventoryItem.data));
                    this.modal.hide();
                    this.$router.go();
                  }
                }
            )
            .catch(
                error => {
                  if (error.response) {
                    // There was something wrong with the user data!
                    if (error.response.status === 400) {
                      if (error.response.data.message !== "") {
                        this.formErrorModalMessage = error.response.data.message;
                      } else {
                        this.formErrorModalMessage = "Some of the information you have entered is invalid."
                      }
                    } else if (error.response.status === 403) {
                      this.formErrorModalMessage = "You do not have permission to perform this action!"
                      this.$router.push({path: "/forbidden"})

                    } else if (error.response.status === 401) {
                      this.formErrorModalMessage = "You must be logged in to perform this action."
                      this.$router.push({name: 'Login'})

                    } else {
                      this.formErrorModalMessage = "Sorry, something went wrong..."
                    }


                  } else if (error.request) {
                    this.formErrorModalMessage = "Server timeout"
                    this.$router.push({path: "/timeout"})
                  } else {
                    this.formErrorModalMessage = "Sorry, something went wrong..."
                  }
                }
            )
      }

    },

  },
  mounted() {
    // Create a modal and attach it to the updateInventoryItemModal reference.
    this.modal = new Modal(this.$refs._updateInventoryItemModal);
  }
}
</script>

<style scoped>

input:focus, textarea:focus, button:focus{
  outline: none;
  box-shadow: 0 0 2px 2px #1EBA8C; /* Full freedom. (works also with border-radius) */
  border: 1px solid #1EBABC;
}
</style>