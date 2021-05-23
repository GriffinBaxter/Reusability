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
              <div id="autofill-container" @click="autofillClick" @keyup="keyPressedOnInput" ref="autofill-container">
                <label for="autofill-input">Product ID*: </label>
                <input type="text" id="autofill-input" ref="autofill-input" class="form-control" v-model="autofillInput">
                <span class="iconSpan">
                    <i class="fas fa-angle-down"></i>
                  </span>
                <ul class="autofill-options hidden-all" id="autofill-list" ref="autofill-list">
                  <li v-for="product in allProducts" v-bind:key="product.id" v-bind:id="'li-product-' + product.id" tabindex="-1" v-bind:value="product.id"><strong>{{ product.id }}</strong><br>{{ product.name + ' RRP: ' + currencySymbol + product.recommendedRetailPrice + ' ' + currencyCode}}</li>
                </ul>
              </div>
            </div>

            <!--quantity-->
            <div class="col-5 form-group py-1 px-3">
              <label for="quantity">Quantity*: </label>
              <input id="quantity" name="quantity" type="number" v-model="quantity" min="0"
                     :class="toggleInvalidClass(quantityErrorMsg)" :maxlength="config.quantity.maxLength" @input="updatePriceFromQuantity" required>
              <div class="invalid-feedback">
                {{ quantityErrorMsg }}
              </div>
            </div>


            <!--price per item-->
            <div class="col-6 form-group py-1 px-3">
              <label for="price-per-item">Price Per Item ({{ currencyCode }}): </label>
              <div class="input-group">
                <div class="input-group-prepend">
                  <span class="input-group-text">{{ currencySymbol }}</span>
                </div>
                <input id="price-per-item" name="price-per-item" type="number" step="0.01"
                       v-model="pricePerItem"
                       min="0" :class="toggleInvalidClass(pricePerItemErrorMsg)"
                       :maxlength="config.pricePerItem.maxLength" @input="updatePriceFromQuantity">
                <div class="invalid-feedback">
                  {{ pricePerItemErrorMsg }}
                </div>
              </div>
            </div>

            <!--total price-->
            <div class="col-6 form-group py-1 px-3">
              <label for="total-price">Total Price ({{ currencyCode }}): </label>
              <div class="input-group">
                <div class="input-group-prepend">
                  <span class="input-group-text">{{ currencySymbol }}</span>
                </div>
                <input id="total-price" name="total-price" type="number" step="0.01" v-model="totalPrice"
                       min="0" :class="toggleInvalidClass(totalPriceErrorMsg)" :maxlength="config.totalPrice.maxLength">
                <div class="invalid-feedback">
                  {{ totalPriceErrorMsg }}
                </div>
              </div>
            </div>

            <!--manufactured-->
            <div class="col-12 form-group py-1 px-3">
              <label for="manufactured">Manufactured: </label>
              <input id="manufactured" name="manufactured" type="date" v-model="manufactured"
                     :class="toggleInvalidClass(manufacturedErrorMsg)">
              <div class="invalid-feedback">
                {{ manufacturedErrorMsg }}
              </div>
            </div>

            <!--sell by-->
            <div class="col-12 form-group py-1 px-3">
              <label for="sell-by">Sell By: </label>
              <input id="sell-by" name="sell-by" type="date" v-model="sellBy"
                     :class="toggleInvalidClass(sellByErrorMsg)">
              <div class="invalid-feedback">
                {{ sellByErrorMsg }}
              </div>
            </div>

            <!--best before-->
            <div class="col-12 form-group py-1 px-3">
              <label for="best-before">Best Before: </label>
              <input id="best-before" name="best-before" type="date" v-model="bestBefore"
                     :class="toggleInvalidClass(bestBeforeErrorMsg)">
              <div class="invalid-feedback">
                {{ bestBeforeErrorMsg }}
              </div>
            </div>

            <!--expires-->
            <div class="col-12 form-group py-1 px-3">
              <label for="expires">Expires*: </label>
              <input class="col-6" id="expires" name="expires" type="date" v-model="expires"
                     :class="toggleInvalidClass(expiresErrorMsg)" required>
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
import {endOfToday, format, compareAsc} from 'date-fns';
import Autofill from '../autofill';

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

      allProducts: [],
      autofillInput: '',
      autofillState: 'initial',
      currentProduct: null
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
     * This method removes white space from the beginning and end of all the input field's input values.
     */
    trimTextInputFields() {
      this.productID = this.productId.trim();
      this.manufactured = this.manufactured.trim();
      this.sellBy = this.sellBy.trim();
      this.bestBefore = this.bestBefore.trim();
      this.expires = this.expires.trim();
    },
    async getAllProducts() {
      await Api.getEveryProduct(this.businessId).then((response) => {
        this.allProducts = [...response.data];
      }).catch((error) => {
        if (error.response) {
          if (error.response.status === 400) {
            this.toastErrorMessage = 'Invalid data';
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
     * Click event handler for the inventory ID input. Will toggle the autofill options display when needed and
     * also calls fillData, if applicable.
     *
     * This function is based off of the example code found on Julie Grundy's custom select element tutorial on 24ways.org:
     * https://24ways.org/2019/making-a-better-custom-select-element/
     */
    autofillClick() {
      const currentFocus = document.activeElement;
      const input = this.$refs["autofill-input"];
      switch (this.autofillState) {
        case "initial":
          Autofill.toggleList('open', this.$refs["autofill-list"]);
          this.autofillState = 'opened';
          break;
        case 'opened':
          if (currentFocus === input) {
            Autofill.toggleList('closed', this.$refs["autofill-list"]);
            this.autofillState = 'initial';
          } else if (currentFocus.tagName === 'LI') {
            this.fillData(currentFocus);
            Autofill.toggleList('closed', this.$refs["autofill-list"]);
            this.autofillState = 'closed';
          }
          break;
        case 'filtered':
          if (currentFocus.tagName === 'LI') {
            this.fillData(currentFocus);
            Autofill.toggleList('closed', this.$refs["autofill-list"]);
            this.autofillState = 'closed';
          }
          break;
        case 'closed':
          Autofill.toggleList('open', this.$refs["autofill-list"]);
          this.autofillState = 'filtered';
          break;
      }
    },
    /**
     * Sets the value of the product ID input to the ID of the given item.
     *
     * This function is based off of the example code found on Julie Grundy's custom select element tutorial on 24ways.org:
     * https://24ways.org/2019/making-a-better-custom-select-element/
     */
    fillData(currentItem) {
      let finalProduct = null;
      for (let product of this.allProducts) {
        if (product.id === currentItem.getAttribute('value')) {
          finalProduct = product;
        }
      }
      this.currentProduct = finalProduct;
      this.autofillInput = finalProduct.id;
      this.quantity = 1;
      this.pricePerItem = finalProduct.recommendedRetailPrice;
      this.totalPrice = finalProduct.recommendedRetailPrice * this.quantity;
    },
    /**
     * Handles keyboard input when navigating autofill dropdown menu
     *
     * This function is adapted from the example code found on Julie Grundy's custom select element tutorial on 24ways.org:
     * https://24ways.org/2019/making-a-better-custom-select-element/
     */
    keyPressedOnInput(event) {
      const key = event.key;
      const currentFocus = document.activeElement;
      const input = this.$refs["autofill-input"];

      switch (key) {
        case 'Enter':
          if (this.autofillState === 'initial') {
            // If state = initial, toggle open and set state to opened
            Autofill.toggleList('open', this.$refs["autofill-list"]);
            this.autofillState = 'opened';
          } else if (this.autofillState === 'opened' && currentFocus.tagName === 'LI') {
            // If state = opened and focus on list, fill data and set state to closed
            this.fillData(currentFocus)
            Autofill.toggleList('closed', this.$refs["autofill-list"])
            this.autofillState = 'closed';
          } else if (this.autofillState === 'opened' && currentFocus === input) {
            // If state = opened and focus on input, close it
            Autofill.toggleList('closed', this.$refs["autofill-list"])
            this.autofillState = 'closed';
          } else if (this.autofillState === 'filtered' && currentFocus.tagName === 'LI') {
            // If state = filtered and focus on list, fill data and set state to closed
            this.fillData(currentFocus)
            Autofill.toggleList('closed', this.$refs["autofill-list"])
            this.autofillState = 'closed';
          } else if (this.autofillState === 'filtered' && currentFocus === input) {
            // If state = filtered and focus on input, set state to opened
            Autofill.toggleList('open', this.$refs["autofill-list"])
            this.autofillState = 'opened';
          } else {
            // If state = closed, set state to filtered. I.e. open but keep existing input.
            Autofill.toggleList('open', this.$refs["autofill-list"])
            this.autofillState = ('filtered');
          }
          break;
        case 'Escape':
          if (this.autofillState === 'opened' || this.autofillState === 'filtered') {
            // Close the list
            Autofill.toggleList('closed', this.$refs["autofill-list"]);
            this.autofillState = 'initial';
          }
          break;
        case 'ArrowDown':
          if (this.autofillState === 'initial' || this.autofillState === 'closed') {
            // If state = initial or closed, set state to opened and moveFocus to first
            Autofill.toggleList('open', this.$refs["autofill-list"])
            Autofill.moveFocus(input, 'forward', this.$refs["autofill-input"], this.$refs["autofill-list"].children, document.activeElement)
            this.autofillState = 'opened';
          } else {
            // If state = opened/filtered and focus on input/list, moveFocus to first/next
            Autofill.toggleList('open', this.$refs["autofill-list"])
            Autofill.moveFocus(currentFocus, 'forward', this.$refs["autofill-input"], this.$refs["autofill-list"].children, document.activeElement)
          }
          break;
        case 'ArrowUp':
          if (this.autofillState === 'initial' || this.autofillState === 'closed') {
            // If state = initial, set state to opened and moveFocus to last
            // If state = closed, set state to opened and moveFocus to last
            Autofill.toggleList('Open', this.$refs["autofill-list"])
            Autofill.moveFocus(input, 'back', this.$refs["autofill-input"], this.$refs["autofill-list"].children, document.activeElement)
            this.autofillState = 'opened';
          } else {
            // If state = opened/filtered and focus on input/list, moveFocus to last/previous
            Autofill.moveFocus(currentFocus, 'back', this.$refs["autofill-input"], this.$refs["autofill-list"].children, document.activeElement)
          }
          break;
        default:
          if (this.autofillState === 'initial') {
            // If state = initial, toggle open, filter and set state to filtered
            Autofill.toggleList('open', this.$refs["autofill-list"])
            Autofill.filterOptions(this.$refs["autofill-input"].value, this.$refs["autofill-list"].children, this.autofillState);
            this.autofillState = 'filtered';
          } else if (this.autofillState === 'opened') {
            // If state = opened, filter and set state to filtered
            Autofill.filterOptions(this.$refs["autofill-input"].value, this.$refs["autofill-list"].children, this.autofillState);
            this.autofillState = 'filtered';
          } else if (this.autofillState === 'closed') {
            // If state = closed, filter and set state to filtered
            Autofill.filterOptions(this.$refs["autofill-input"].value, this.$refs["autofill-list"].children, this.autofillState);
            this.autofillState = 'filtered';
          } else { // Already filtered
            Autofill.filterOptions(this.$refs["autofill-input"].value, this.$refs["autofill-list"].children, this.autofillState);
          }
          break;
      }
    },
    /**
     * Updates the total price when the quantity input or price per item are modified.
     * */
    updatePriceFromQuantity() {
      if (!isNaN(this.quantity) && !isNaN(this.pricePerItem)) {
        this.totalPrice = this.quantity * this.pricePerItem;
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
     * @returns {{year: string, day: string, month: string}|null}, {year, day, month}, if the date meets the expected
     * format, else null.
     */
    parseSelectedDate(dateString) {

      const verifyRegex = /^[0-9]{1,5}-[0-9]{1,3}-[0-9]{1,3}$/

      if (verifyRegex.test(dateString)) {
        const dateParts = dateString.split("-", 3);

        const year = dateParts[0];
        let month = dateParts[1];
        let day = dateParts[2];

        month = (month.length === 1) ? `0${month}` : month;
        day = (day.length === 1) ? `0${day}` : day;

        return {
          year: year,
          month: month,
          day: day,
        }
      } else {
        return null
      }
    },

    /**
     * This function will check the validity of the manufactured date of an inventory item i.e. that the manufactured
     * date of the inventory item is prior to today's date
     * @return true if the date is before today's date or today's date, otherwise false.
     */
    isValidManufactureDate(selectedManufacturedDate) {

      const selectedDate = this.parseSelectedDate(selectedManufacturedDate);

      if (selectedDate === null) {
        return true
      } else {

        const givenDateYear = selectedDate.year
        const givenDateMonth = selectedDate.month
        const givenDateDay = selectedDate.day
        const todayDateYear = format(endOfToday(new Date()), 'yyyy');
        const todayDateMonth = format(endOfToday(new Date()), 'MM');
        const todayDateDay = format(endOfToday(new Date()), 'dd');

        // Compare the two dates and return 1 if the first date is after the second, -1 if the first date is before the
        // second or 0 if dates are equal.
        const comparisonValue = compareAsc(new Date(givenDateYear, givenDateMonth, givenDateDay), new Date(todayDateYear, todayDateMonth, todayDateDay))

        return ((comparisonValue === -1) || (comparisonValue === 0));

      }

    },

    /**
     * This function will check the validity of the sell by date of an inventory item i.e. that the sell by date of the
     * inventory item is after to today's date but not today's date, and after the manufacture date and before the expiry date (not including).
     *
     * @return true if the date meets the above conditions, otherwise false
     */
    isValidSellByDate(selectedSellByDate, selectedManufacturedDate, selectedExpiryDate) {

      let isValid = false;
      const sellByDate = this.parseSelectedDate(selectedSellByDate);
      const manufacturedDate = this.parseSelectedDate(selectedManufacturedDate);
      const expiryDate = this.parseSelectedDate(selectedExpiryDate);

      if (sellByDate === null) {
        return true;
      }
      const sellByDateYear = sellByDate.year
      const sellByDateMonth = sellByDate.month
      const sellByDateDay = sellByDate.day

      if (manufacturedDate !== null) {
        const manufacturedDateYear = manufacturedDate.year
        const manufacturedDateMonth = manufacturedDate.month
        const manufacturedDateDay = manufacturedDate.day

        const comparisonWithManufacturedValue = compareAsc(new Date(sellByDateYear, sellByDateMonth, sellByDateDay), new Date(manufacturedDateYear, manufacturedDateMonth, manufacturedDateDay))
        const isAfterManufactureDateAndNotManufactureDate = (comparisonWithManufacturedValue === 1);

        if (!isAfterManufactureDateAndNotManufactureDate) {
          return isValid;
        }
      }
      if (expiryDate !== null) {
        const expiredDateYear = expiryDate.year
        const expiredDateMonth = expiryDate.month
        const expiredDateDay = expiryDate.day

        const comparisonWithExpiryValue = compareAsc(new Date(sellByDateYear, sellByDateMonth, sellByDateDay), new Date(expiredDateYear, expiredDateMonth, expiredDateDay))
        const isBeforeExpiryAndNotExpiryDate = (comparisonWithExpiryValue === -1);

        if (!isBeforeExpiryAndNotExpiryDate) {
          return isValid;
        }
      }

      const todayDateYear = format(endOfToday(new Date()), 'yyyy');
      const todayDateMonth = format(endOfToday(new Date()), 'MM');
      const todayDateDay = format(endOfToday(new Date()), 'dd');

      // Compare the two dates and return 1 if the first date is after the second, -1 if the first date is before the
      // second or 0 if dates are equal.

      const comparisonWithTodayValue = compareAsc(new Date(sellByDateYear, sellByDateMonth, sellByDateDay), new Date(todayDateYear, todayDateMonth, todayDateDay))
      const isAfterTodayAndNotToday = (comparisonWithTodayValue === 1);

      if (isAfterTodayAndNotToday) {
        isValid = true;
      }

      return isValid

    },

    /**
     * This function will check the validity of the best before date of an inventory item i.e. that the best before date
     * of the inventory item is after today's date but not today's date (and implicitly after the manufacture date)
     * and before expiry date.
     *
     * @return true if the date meets the above conditions, otherwise false
     */
    isValidBestBeforeDate(selectedBestBeforeDate, selectedManufacturedDate, selectedExpiryDate) {

      let isValid = false;
      const bestBeforeDate = this.parseSelectedDate(selectedBestBeforeDate);
      const manufacturedDate = this.parseSelectedDate(selectedManufacturedDate);
      const expiryDate = this.parseSelectedDate(selectedExpiryDate);

      if (bestBeforeDate === null) {
        return true
      }

      const bestBeforeDateYear = bestBeforeDate.year
      const bestBeforeDateMonth = bestBeforeDate.month
      const bestBeforeDateDay = bestBeforeDate.day

      if (manufacturedDate !== null) {
        const manufacturedDateYear = manufacturedDate.year
        const manufacturedDateMonth = manufacturedDate.month
        const manufacturedDateDay = manufacturedDate.day

        const comparisonWithManufacturedValue = compareAsc(new Date(bestBeforeDateYear, bestBeforeDateMonth, bestBeforeDateDay), new Date(manufacturedDateYear, manufacturedDateMonth, manufacturedDateDay))
        const isAfterManufactureDateAndNotManufactureDate = (comparisonWithManufacturedValue === 1);

        if (!isAfterManufactureDateAndNotManufactureDate) {
          return isValid;
        }
      }
      if (expiryDate !== null) {
        const expiredDateYear = expiryDate.year
        const expiredDateMonth = expiryDate.month
        const expiredDateDay = expiryDate.day

        const comparisonWithExpiryValue = compareAsc(new Date(bestBeforeDateYear, bestBeforeDateMonth, bestBeforeDateDay), new Date(expiredDateYear, expiredDateMonth, expiredDateDay))
        const isBeforeExpiryAndNotExpiryDate = (comparisonWithExpiryValue === -1);

        if (!isBeforeExpiryAndNotExpiryDate) {
          return isValid;
        }
      }

      const todayDateYear = format(endOfToday(new Date()), 'yyyy');
      const todayDateMonth = format(endOfToday(new Date()), 'MM');
      const todayDateDay = format(endOfToday(new Date()), 'dd');

      // Compare the two dates and return 1 if the first date is after the second, -1 if the first date is before the
      // second or 0 if dates are equal.

      const comparisonWithTodayValue = compareAsc(new Date(bestBeforeDateYear, bestBeforeDateMonth, bestBeforeDateDay), new Date(todayDateYear, todayDateMonth, todayDateDay))
      const isAfterTodayAndNotToday = (comparisonWithTodayValue === 1);

      if (isAfterTodayAndNotToday) {
        isValid = true;
      }

      return isValid

    },

    /**
     * This function will check the validity of the expires date of an inventory item i.e. that the expiry date
     * of the inventory item is after today's date, after the manufacture date, and after or equal to the best before
     * date.
     *
     * @return true if the date meets the above conditions, otherwise false
     */
    isValidExpiryDate(selectedExpiryDate, selectedBestBeforeDate, selectedManufacturedDate) {

      let isValid = false;
      const manufacturedDate = this.parseSelectedDate(selectedManufacturedDate);
      const expiryDate = this.parseSelectedDate(selectedExpiryDate);

      if (expiryDate === null) {
        return isValid
      }

      const expiredDateYear = expiryDate.year
      const expiredDateMonth = expiryDate.month
      const expiredDateDay = expiryDate.day

      if (manufacturedDate !== null) {
        const manufacturedDateYear = manufacturedDate.year
        const manufacturedDateMonth = manufacturedDate.month
        const manufacturedDateDay = manufacturedDate.day

        const comparisonWithManufacturedValue = compareAsc(new Date(expiredDateYear, expiredDateMonth, expiredDateDay), new Date(manufacturedDateYear, manufacturedDateMonth, manufacturedDateDay))
        const isAfterManufactureDateAndNotManufactureDate = (comparisonWithManufacturedValue === 1);

        if (!isAfterManufactureDateAndNotManufactureDate) {
          return isValid;
        }
      }


      const todayDateYear = format(endOfToday(new Date()), 'yyyy');
      const todayDateMonth = format(endOfToday(new Date()), 'MM');
      const todayDateDay = format(endOfToday(new Date()), 'dd');

      // Compare the two dates and return 1 if the first date is after the second, -1 if the first date is before the
      // second or 0 if dates are equal.

      const comparisonWithTodayValue = compareAsc(new Date(expiredDateYear, expiredDateMonth, expiredDateDay), new Date(todayDateYear, todayDateMonth, todayDateDay))
      const isAfterTodayAndNotToday = (comparisonWithTodayValue === 1);

      if (isAfterTodayAndNotToday) {
        isValid = true;
      }

      return isValid

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

      // Autofill related variables
      this.autofillInput = '';
      this.autofillState = 'initial'
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
      this.productIdErrorMsg = this.getErrorMessage(
          this.config.productId.name,
          this.autofillInput,
          this.config.productId.minLength,
          this.config.productId.maxLength,
          this.config.productId.regexMessage,
          this.config.productId.regex
      )
      if (this.productIdErrorMsg) {
        requestIsInvalid = true
      }

      // Quantity error checking
      this.quantityErrorMsg = this.getErrorMessage(
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
      this.pricePerItemErrorMsg = this.getErrorMessage(
          this.config.pricePerItem.name,
          this.pricePerItem.toString(),
          this.config.pricePerItem.minLength,
          this.config.pricePerItem.maxLength,
          this.config.pricePerItem.regexMessage,
          this.config.pricePerItem.regex
      )
      if (this.pricePerItemErrorMsg) {
        requestIsInvalid = true
      }

      // Total price error checking
      this.totalPriceErrorMsg = this.getErrorMessage(
          this.config.totalPrice.name,
          this.totalPrice.toString(),
          this.config.totalPrice.minLength,
          this.config.totalPrice.maxLength,
          this.config.totalPrice.regexMessage,
          this.config.totalPrice.regex
      )
      if (this.totalPriceErrorMsg) {
        requestIsInvalid = true
      }
      // Manufacture date error checking
      if (!this.isValidManufactureDate(this.manufactured)) {
        this.manufacturedErrorMsg = "Manufactured date must be prior to today's date";
        requestIsInvalid = true;
      } else {
        this.manufacturedErrorMsg = '';
      }

      // Sell by date error checking
      if (!this.isValidSellByDate(this.sellBy, this.manufactured, this.expires)) {
        this.sellByErrorMsg = "Sell by date must be after today's date but not today's date, and after the manufacture date and before the expiry date (not including)";
        requestIsInvalid = true;
      } else {
        this.sellByErrorMsg = '';
      }

      // Best best date before error checking
      if (!this.isValidBestBeforeDate(this.bestBefore, this.manufactured, this.expires)) {
        this.bestBeforeErrorMsg = "Best before date must be after today's date but not today's date, after the manufacture date and before expiry date";
        requestIsInvalid = true;
      } else {
        this.bestBeforeErrorMsg = '';
      }

      // Expiry date error checking
      if (!this.isValidExpiryDate(this.expires, this.bestBefore, this.manufactured)) {
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
        productId: this.autofillInput,
        quantity: this.quantity,
        pricePerItem: this.pricePerItem,
        totalPrice: this.totalPrice,
        manufactured: this.manufactured,
        sellBy: this.sellBy,
        bestBefore: this.bestBefore,
        expires: this.expires
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
    this.getAllProducts().then(() => {});
    this.modal = new Modal(document.getElementById("creationPopup"));

    // Global event listener to toggle autofill list display
    let self = this;
    document.addEventListener('click', function(event) {
      if (!event.target.closest('#autofill-container') && self.autofillState !== 'closed') {
        Autofill.toggleList('closed', self.$refs["autofill-list"]);
        self.autofillState = 'initial';
      }
    })
  }
};
</script>

<style scoped>

/* Styles the input and textarea's borders to be green when they are focused/tabbed to */
input:focus, textarea:focus {
  outline: none;
  box-shadow: 0 0 2px 2px #1EBA8C;
  border: 1px solid #1EBABC;
}

/*********************************************************************
                          Autofill styling

       This CSS is a modified version of the examples found on
    Julie Grundy's tutorial for creating a custom select element:

    https://24ways.org/2019/making-a-better-custom-select-element/
 *********************************************************************/

#autofill-container {
  position: relative;
}

#autofill-input::-ms-expand {
  display: none;
}

.autofill-options {
  border: 1px solid lightgray;
  border-radius: 0 0 0.25em 0.25em;
  line-height: 1.25;
  padding: 0;
  list-style-type: none;
  cursor: pointer;
  z-index: 2;
  position: absolute;
  width: 100%;
  background-color: #ffffff;
}

.autofill-options li {
  padding: 1em;
}

.autofill-options li:hover, .autofill-options li:focus {
  background: #1EBA8C;
  color: #fff;
}

.hidden-all {
  display: none;
}

.iconSpan {
  position: absolute;
  top: 2em;
  right: 0.75em;
  z-index: 20;
  background: transparent;
}

/*********************************************************************/

</style>
