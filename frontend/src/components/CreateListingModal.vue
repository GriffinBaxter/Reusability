<template>
  <!-- Modal -->
  <div class="modal fade" id="listingCreationPopup" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1"
       aria-labelledby="creationPopupTitle" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">

        <!--title-->
        <div class="modal-header">
          <h2 class="text-center">New Listing</h2>
        </div>

        <!--modal body-->
        <div class="modal-body">
          <form id="ListingCreation" @submit.prevent>
            <!--Inventory Item Select-->
            <div class="row">
              <div class="col form-group py-1 px-3">
                <label for="productDataList" class="form-control-label">Inventory ID*: </label>
                <input :class="toggleInvalidClass(inventoryIdErrorMsg)" @input="autofillData()" list="productDataList" id="productInput" name="productDataList" ref="productInput" required/>
                <datalist id="productDataList" style="overflow-y: auto!important">
                  <option v-for="item in allInventoryItems" v-bind:key="item.id" :value="item.product.id + ' (Expires: ' + item.expires + ')' + ' ID: ' + item.id">Quantity: {{item.quantity}} Price: (${{item.totalPrice}})</option>
                </datalist>
                <div class="invalid-feedback">
                  {{ inventoryIdErrorMsg }}
                </div>
              </div>
            </div>
            <!--quantity-->
            <div class="row">
              <div class="col-sm-6 form-group py-1 px-3">
                <label for="quantity">Quantity*: </label>
                <input id="quantity" name="quantity" type="number" ref="quantity" v-model="quantity" min="0"
                       :class="toggleInvalidClass(quantityErrorMsg)" :maxlength="config.quantity.maxLength" required>
                <div class="invalid-feedback">
                  {{ quantityErrorMsg }}
                </div>
              </div>


              <!--Price-->
              <div class="col-sm-6 form-group py-1 px-3">
                <label for="price">Price*: </label>
                <div class="input-group">
                  <div class="input-group-prepend">
                    <span class="input-group-text">$</span>
                  </div>
                  <input id="price" name="price" type="number" ref="price" step="0.01"
                         v-model="price"
                         min="0" :class="toggleInvalidClass(priceErrorMsg)"
                         :maxlength="config.price.maxLength">
                  <div class="invalid-feedback">
                    {{ priceErrorMsg }}
                  </div>
                </div>
              </div>
            </div>

            <!--More-Info-->
            <div class="row form-group py-1 px-3">
              <label for="more-info">More info: </label>
              <input id="more-info" name="more-info" type="text" step="0.01" v-model="moreInfo"
                     min="0" :class="toggleInvalidClass(moreInfoErrorMsg)" :maxlength="config.moreInfo.maxLength">
              <div class="invalid-feedback">
                {{ moreInfoErrorMsg }}
              </div>
            </div>

            <!--Close Date-->
            <div class="row form-group py-1 px-3">
              <label for="closes">Close Date: </label>
              <input id="closes" name="closes" type="datetime-local" v-model="closes"
                     :class="toggleInvalidClass(closesErrorMsg)">
              <div class="invalid-feedback">
                {{ closesErrorMsg }}
              </div>
            </div>


          </form>
          <div class="text-center text-danger">
            {{creationErrorMessage}}
          </div>
        </div>


        <!--footer-->
        <div class="modal-footer justify-content-between">
          <button type="button" class="btn green-button-transparent" @click="dataReset()" data-bs-dismiss="modal">Cancel</button>
          <button type="button" class="btn green-button" @click="createNewInventoryItem()">Confirm</button>
        </div>

      </div>
    </div>
  </div>

</template>

<script>
import Api from "@/Api";
import Listing from "@/configs/Listings";

import {Modal} from "bootstrap";
const datefns = require('date-fns');

export default {
  name: "CreateListing",
  data() {
    return {
      config: Listing.config,
      inventoryItems: [],
      allInventoryItems: [], // Stores all inventory items (for new listing dropdown)
      currentInventoryItem: null,
      inventoryItemIds: [], // Stores all inventory item
      modal: null,

      // Inventory Id related variables
      inventoryId: "",
      inventoryIdErrorMsg: "",
      // Quantity related variables
      quantity: "",
      quantityErrorMsg: "",
      // Price related variables
      price: "",
      priceErrorMsg: "",
      // MoreInfo related variables
      moreInfo: "",
      moreInfoErrorMsg: "",
      // Closes related variables
      closes: "",
      closesErrorMsg: "",
      businessId: this.$route.params.id,
      creationErrorMessage: ""
    }
  },
  methods: {
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
     * Resets data on the page
     */
    dataReset() {
      this.inventoryId = "";
      this.inventoryIdErrorMsg = "";
      this.quantity = "";
      this.quantityErrorMsg = "";
      this.price = "";
      this.priceErrorMsg = "";
      this.moreInfo = "";
      this.moreInfoErrorMsg = "";
      this.closes = "";
      this.closesErrorMsg = "";
    },
    closeModal() {
      this.modal.hide();
    },
    /**
     * This method removes white space from the beginning and end of all the input field's input values.
     */
    trimTextInputFields() {
      this.$refs.quantity.value = this.$refs.quantity.value.trim();
      this.$refs.price.value = this.$refs.price.value.trim();
      this.moreInfo = this.moreInfo.trim();
      this.closes = this.closes.trim();
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
      let errorMessage = "";
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
     *  Checks the ID of the current input value, then finds the inventory item with that ID (in allInventoryItems) to autofill
     *  that item's quantity and price in the quantity and price input fields.
     * */
    autofillData() {
      // Datalists are not flexible enough to allow nice event handlers and formatting so
      // changing this to a custom dropdown would be ideal for future sprints.
      const value = this.$refs.productInput.value;
      if (!value) return;

      let result = null;

      let i = 0;
      let itemNotFound = true;
      while (i < this.allInventoryItems.length && itemNotFound) {
        let itemID = this.allInventoryItems[i].id;
        // This split depends on the formatting in the :value for the <option> inside the productDataList.
        let inputID = parseInt(value.split(' ')[4]);
        if (itemID === inputID) {
          result = this.allInventoryItems[i];
          itemNotFound = false;
        }
        i += 1;
      }

      if (result !== null) {
        this.currentInventoryItem = result;
        this.quantity = result.quantity;
        this.price = result.totalPrice;

        const newDateTime = new Date(result.expires);
        this.closes = datefns.format(new Date(newDateTime.getFullYear(), newDateTime.getMonth(), newDateTime.getDate()), "yyyy-MM-dd'T'HH:mm:ss.SSS");
      }
    },

    /**
     * This method parses the given date and separates it into a year, day and month, provided it meets
     * the expected format.
     *
     * Note that the date format is yyyy-MM-dd (e.g. '2029-12-30') to use the compareAsc() in the date validation methods.
     * So, this must be consistent!
     *
     * @param dateString, string, the date to validate and separate.
     * @returns {{year: string, day: string, month: string, hour: string, minute: string, seconds: string}|null}, {year, day, month}, if the date meets the expected
     * format, else null.
     */
    parseSelectedDate(dateString) {
      const newDate = datefns.parseISO(dateString);

      if (datefns.isValid(newDate)) {
        return {
          year: datefns.getYear(newDate),
          month: datefns.getMonth(newDate),
          day: datefns.getDate(newDate),
          hour: datefns.getHours(newDate),
          minute: datefns.getMinutes(newDate),
          seconds: datefns.getSeconds(newDate)
        }
      } else {
        return null;
      }

    },
    /**
     * This function will check the validity of the sell by date of an inventory item i.e. that the sell by date of the
     * inventory item is after to today's date but not today's date, and after the manufacture date and before the expiry date (not including).
     *
     * @return true if the date meets the above conditions, otherwise false
     */
    isValidCloseDate(selectedCloseDate) {
      let isValid = false;
      const closeDate = this.parseSelectedDate(selectedCloseDate);
      if (closeDate === null) {
        return isValid;
      }
      const closeDateYear = closeDate.year
      const closeDateMonth = closeDate.month;
      const closeDateDay = closeDate.day

      const todayDate = datefns.endOfToday();

      // Compare the two dates and return 1 if the first date is after the second, -1 if the first date is before the
      // second or 0 if dates are equal.

      const comparisonWithTodayValue = datefns.compareAsc(new Date(closeDateYear, closeDateMonth, closeDateDay), todayDate)
      const isAfterTodayAndNotToday = (comparisonWithTodayValue === 1);

      if (isAfterTodayAndNotToday) {
        isValid = true;
      }

      return isValid
    },

    async getAllInventoryItems() {
      await Api.getEveryInventoryItem(this.businessId).then((response) => {
        this.allInventoryItems = [...response.data];
      }).catch((error) => {
        if (error.response) {
          if (error.response.status === 400) {
            this.toastErrorMessage = 'Invalid listing data';
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
    },

    /**
     * Creates the new Inventory Item
     */
    async createNewInventoryItem() {
      let requestIsInvalid = false;

      if (this.currentInventoryItem === null) {
        this.inventoryIdErrorMsg = "Please enter an ID";
        requestIsInvalid = true;
      } else {
        this.inventoryId = this.currentInventoryItem.id;
        this.inventoryIdErrorMsg = "";
      }

      this.trimTextInputFields();

      // Quantity error checking
      this.quantityErrorMsg = this.getErrorMessage(
          this.config.quantity.name,
          this.$refs.quantity.value,
          this.config.quantity.minLength,
          this.config.quantity.maxLength,
          this.config.quantity.regexMessage,
          this.config.quantity.regex
      )
      if (this.$refs.quantity <= 0) {
        this.quantityErrorMsg = "At least one"
      }
      if (this.quantityErrorMsg) {
        requestIsInvalid = true
      }

      // Price per item error checking
      this.priceErrorMsg = this.getErrorMessage(
          this.config.price.name,
          this.$refs.price.value,
          this.config.price.minLength,
          this.config.price.maxLength,
          this.config.price.regexMessage,
          this.config.price.regex
      )
      if (this.priceErrorMsg) {
        requestIsInvalid = true
      }

      this.moreInfoErrorMsg = this.getErrorMessage(
          this.config.moreInfo.name,
          this.moreInfo,
          this.config.moreInfo.minLength,
          this.config.moreInfo.maxLength,
          this.config.moreInfo.regexMessage,
          this.config.moreInfo.regex
      )

      if (!this.isValidCloseDate(this.closes)) {
        requestIsInvalid = true;
        this.closesErrorMsg = "Date must be valid and in the future";
      } else {
        this.closesErrorMsg = "";
      }

      // If at any stage there is an issue with the request cancel the request
      if (requestIsInvalid) {
        return;
      }

      const listingItemData = {
        inventoryItemId: this.inventoryId,
        quantity: parseInt(this.quantity),
        price: parseFloat(this.price),
        moreInfo: this.moreInfo,
        closes: this.closes
      };

      const newListingItem = new Listing(listingItemData);

      await Api.addNewBusinessListing(this.businessId, newListingItem).then((response) => {
        if (response.status === 201) {
          this.creationErrorMessage = "";
          this.dataReset();
          this.$emit('updateListings');
          this.closeModal();
          this.currentInventoryItem = null;
        }
      }).catch((error) => {

        this.cannotProceed = true;
        if (error.response) {
          if (error.response.status === 400) {
            this.creationErrorMessage = 'There are not enough inventory items. Please check the quantity.';
          } else if (error.response.status === 403) {
            this.creationErrorMessage = 'User is not an administer of this business.';
          } else {
            this.creationErrorMessage = `${error.response.status} Unexpected error occurred!`;
          }
        } else if (error.request) {
          this.creationErrorMessage = 'Timeout occurred';
        } else {
          this.creationErrorMessage = 'Unexpected error occurred!';
        }

      })
    },
    /**
     * Creates test data TEMP
     */
    testData() {
      const product = {id:"WATT-420-BEANS"};
      const item = {id:1, product:product, quantity:5, totalPrice:5.19};
      this.inventoryItems.push(item);
      const anotherProduct = {id:"FOOT-LETTUCE"};
      const anotherItem = {id:2, product:anotherProduct, quantity:3, totalPrice:2.59};
      this.inventoryItems.push(anotherItem);
      this.inventoryItems.push({id:7, product:anotherProduct, quantity:2});
      this.inventoryItems.push({id:234, product:product, quantity:3})
    }
  },
  mounted() {
    this.getAllInventoryItems().then(() => {});
    this.modal = new Modal(document.getElementById("listingCreationPopup"));
  }
}
</script>

<style scoped>

/* Styles the input and textarea's borders to be green when they are focused/tabbed to */
input:focus, textarea:focus {
  outline: none;
  box-shadow: 0 0 2px 2px #1EBA8C;
  border: 1px solid #1EBABC;
}

</style>
