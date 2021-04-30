<template>
  <!-- Modal -->
  <div class="modal fade" id="creationPopup" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1"
       aria-labelledby="creationPopupTitle" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">

        <!--title-->
        <div class="modal-header">
          <h2 align="central" id="creationPopupTitle">New Inventory Item</h2>
        </div>

        <!--modal body-->
        <div class="modal-body">
          <form class="row" id="inventoryItemCreation" @submit.prevent>

            <!--product id-->
            <div class="col-7 form-group py-1 px-3">
              <label for="product-id">Product ID*: </label>
              <input id="product-id" name="product-id" tabindex="1" type="text" v-model="productId"
                     :class="toggleInvalidClass(productIdErrorMsg)" :maxlength="config.productId.maxLength" required>
              <div class="invalid-feedback">
                {{ productIdErrorMsg }}
              </div>
            </div>

            <!--quantity-->
            <div class="col-5 form-group py-1 px-3">
              <label for="quantity">Quantity*: </label>
              <input id="quantity" name="quantity" tabindex="2" type="number" v-model="quantity" min="0"
                     :class="toggleInvalidClass(quantityErrorMsg)" :maxlength="config.quantity.maxLength" required>
              <div class="invalid-feedback">
                {{ quantityErrorMsg }}
              </div>
            </div>


            <!--price per item-->
            <div class="col-6 form-group py-1 px-3">
              <label for="price-per-item">Price Per Item: </label>
              <input id="price-per-item" name="price-per-item" tabindex="3" type="number" v-model="pricePerItem" min="0"
                     :class="toggleInvalidClass(pricePerItemErrorMsg)" :maxlength="config.pricePerItem.maxLength">
              <div class="invalid-feedback">
                {{ pricePerItemErrorMsg }}
              </div>
            </div>

            <!--total price-->
            <div class="col-6 form-group py-1 px-3">
              <label for="total-price">Total Price: </label>
              <input id="total-price" name="total-price" tabindex="4" type="number" v-model="totalPrice" min="0"
                     :class="toggleInvalidClass(totalPriceErrorMsg)" :maxlength="config.totalPrice.maxLength">
              <div class="invalid-feedback">
                {{ totalPriceErrorMsg }}
              </div>
            </div>

            <!--manufactured-->
            <div class="col-12 form-group py-1 px-3">
              <label for="manufactured">Manufactured: </label>
              <input id="manufactured" name="manufactured" tabindex="5" type="date" v-model="manufactured"
                     :class="toggleInvalidClass(manufacturedErrorMsg)">
              <div class="invalid-feedback">
                {{ manufacturedErrorMsg }}
              </div>
            </div>

            <!--sell by-->
            <div class="col-12 form-group py-1 px-3">
              <label for="sell-by">Sell By: </label>
              <input id="sell-by" name="sell-by" tabindex="6" type="date" v-model="sellBy"
                     :class="toggleInvalidClass(sellNyErrorMsg)">
              <div class="invalid-feedback">
                {{ sellNyErrorMsg }}
              </div>
            </div>

            <!--best before-->
            <div class="col-12 form-group py-1 px-3">
              <label for="best-before">Best Before: </label>
              <input id="best-before" name="best-before" tabindex="7" type="date" v-model="bestBefore"
                     :class="toggleInvalidClass(bestBeforeErrorMsg)">
              <div class="invalid-feedback">
                {{ bestBeforeErrorMsg }}
              </div>
            </div>

            <!--expires-->
            <div class="col-12 form-group py-1 px-3">
              <label for="expires">Expires*: </label>
              <input class="col-6" id="expires" name="expires" tabindex="8" type="date" v-model="expires"
                     :class="toggleInvalidClass(expiresErrorMsg)" required>
              <div class="invalid-feedback">
                {{ expiresErrorMsg }}
              </div>
            </div>

          </form>
        </div>


        <!--footer-->
        <div class="modal-footer">
          <button type="button" class="btn btn-light" data-bs-dismiss="modal">Cancel</button>
          <button type="button" class="btn btn-success" @click="createNewInventoryItem()">Confirm</button>
        </div>

      </div>
    </div>
  </div>
</template>

<script>

import {InventoryItem} from "../Api";
const endOfToday = require('date-fns/endOfToday');
const format = require('date-fns/format');
const compareAsc = require('date-fns/compareAsc');

export default {
  name: 'InventoryItemCreation',
  data() {
    return {
      // A copy of the product config file for error checking.
      config: InventoryItem.config,

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
      sellNyErrorMsg: "",

      // best Before Id related variables
      bestBefore: "",
      bestBeforeErrorMsg: "",

      // expires related variables
      expires: "",
      expiresErrorMsg: "",
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
      this.quantity = this.quantity.trim();
      this.pricePerItem = this.pricePerItem.trim();
      this.totalPrice = this.totalPrice.trim();
      this.manufactured = this.manufactured.trim();
      this.sellBy = this.sellBy.trim();
      this.bestBefore = this.bestBefore.trim();
      this.expires = this.expires.trim();
    },

    /**
     * This method parses the given date and separates it into a year, day and month, provided it meets
     * the expected format.
     *
     * Note that the date format is yyyy-dd-MM (e.g. '2029-04-30') to use the compareAsc() in the date validation methods.
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
        let day = dateParts[1];
        let month = dateParts[2];

        month = (month.length === 1) ? `0${month}`: month;
        day = (day.length === 1) ? `0${day}`: day;

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

      console.log(selectedDate)

      const givenDateYear = selectedDate.year
      const givenDateMonth = selectedDate.month
      const givenDateDay = selectedDate.day

      const todayDateYear = format(endOfToday(new Date()), 'yyyy');
      const todayDateMonth = format(endOfToday(new Date()), 'MM');
      const todayDateDay = format(endOfToday(new Date()), 'dd');

      // Compare the two dates and return 1 if the first date is after the second, -1 if the first date is before the
      // second or 0 if dates are equal.
      const comparisonValue = compareAsc(new Date(givenDateYear, givenDateDay, givenDateMonth), new Date(todayDateYear, todayDateDay, todayDateMonth))

      return ((comparisonValue === -1) || (comparisonValue === 0)) ? true : false;

    },

    /**
     * This function will check the validity of the sell by date of an inventory item i.e. that the sell by date of the
     * inventory item is after to today's date but not today's date, and before the expiry date.
     *
     * @return true if the date meets the above conditions, otherwise false
     */
    isValidSellByDate(selectedSellByDate, selectedManufacturedDate, selectedExpiryDate) {



    },

    /**
     * This function will check the validity of the best before date of an inventory item i.e. that the best before date
     * of the inventory item is after to today's date but not today's date, and before expiry date.
     *
     * @return true if the date meets the above conditions, otherwise false
     */
    isValidBestBeforeDate(selectedSellByDate, selectedManufacturedDate, selectedExpiryDate) {


    },

    /**
     * This function will check the validity of the expires date of an inventory item i.e. that the expiry date
     * of the inventory item is after today's date, after the manufacture date, and after or equal to the best before
     * date.
     *
     * @return true if the date meets the above conditions, otherwise false
     */
    isValidExpiryDate(selectedManufacturedDate, selectedBestBeforeDate) {


    },



    createNewInventoryItem() {
      // Steps required for the function before starting processing.
      // inventoryItem.preventDefault()  // prevents page from reloading
      this.trimTextInputFields()
      let requestIsInvalid = false

      // ===================================== START OF INPUT FIELDS VALIDATION ========================================

      // Product Id error checking
      this.productIdErrorMsg = this.getErrorMessage(
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
      this.quantityErrorMsg = this.getErrorMessage(
          this.config.quantity.name,
          this.quantity,
          this.config.quantity.minLength,
          this.config.quantity.maxLength,
          this.config.quantity.regexMessage,
          this.config.quantity.regex
      )
      if (this.quantityErrorMsg) {
        requestIsInvalid = true
      }

      // Price per item error checking
      this.pricePerItemErrorMsg = this.getErrorMessage(
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
      this.totalPriceErrorMsg = this.getErrorMessage(
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

      // Manufactured error checking
      //TODO:check this.manufactured == ""
      console.log(this.isADayAfter(this.manufactured))
      if (this.isADayAfter(this.manufactured) === false) {
        this.manufacturedErrorMsg = "If fill in, must be a date before or today.";
      }
      if (this.totalPriceErrorMsg) {
        requestIsInvalid = true;
      }

      // Sell by error checking
      console.log(this.isADayAfter(this.sellBy))
      if (this.isADayAfter(this.sellBy) === false) {
        this.sellByErrorMsg = "If fill in, must be a date before or today.";
      }
      if (this.sellByErrorMsg) {
        requestIsInvalid = true;
      }

      // No checking for best before

      // Expires by error checking
      if (this.isADayBefore(this.expires) !== true) {
        this.expiresErrorMsg = "Must be a date after or today.";
      }
      if (this.expiresErrorMsg) {
        requestIsInvalid = true
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
        manufactured: this.manufactured,
        sellBy: this.sellBy,
        bestBefore: this.bestBefore,
        expires: this.expires
      }
      const newInventoryItem = new InventoryItem(inventoryItemData);

      console.log(newInventoryItem);

    }

  },
};
</script>

<style>
</style>