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
                <input :class="toggleInvalidClass(inventoryIdErrorMsg)" :maxlength="config.inventoryItemId.maxLength" tabindex="1" list="productDataList" id="productInput" name="productDataList" required/>
                <datalist id="productDataList" style="overflow-y: auto!important">
                  <option v-for="item in allInventoryItems" v-bind:key="item.id" :value="item.product.id + ' ' + item.expires">Quantity: {{item.quantity}} Price: (${{item.totalPrice}}) Expiration Date: {{item.expires}}</option>
                </datalist>
<!--                <select id="inventoryId" class="form-select mdb-select md-form" searchable="Search here.." tabindex="1" data-live-search="true" :class="toggleInvalidClass(inventoryIdErrorMsg)">-->
<!--                  <option value="" disabled selected>Select an Item</option>-->
<!--                  <option v-for="item in inventoryItems" v-bind:key="item.id" :value="item.id">{{item.product.id}} x{{item.quantity}} (${{item.totalPrice}})</option>-->
<!--                </select>-->
                <div class="invalid-feedback">
                  {{ inventoryIdErrorMsg }}
                </div>
              </div>
            </div>
            <!--quantity-->
            <div class="row">
              <div class="col-sm-6 form-group py-1 px-3">
                <label for="quantity">Quantity*: </label>
                <input id="quantity" name="quantity" tabindex="2" type="number" v-model="quantity" min="0"
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
                  <input id="price" name="price" tabindex="3" type="number" step="0.01"
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
              <input id="more-info" name="more-info" tabindex="4" type="text" step="0.01" v-model="moreInfo"
                     min="0" :class="toggleInvalidClass(moreInfoErrorMsg)" :maxlength="config.moreInfo.maxLength">
              <div class="invalid-feedback">
                {{ moreInfoErrorMsg }}
              </div>
            </div>

            <!--Close Date-->
            <div class="row form-group py-1 px-3">
              <label for="closes">Close Date: </label>
              <input id="closes" name="closes" tabindex="5" type="date" v-model="closes"
                     :class="toggleInvalidClass(closesErrorMsg)">
              <div class="invalid-feedback">
                {{ closesErrorMsg }}
              </div>
            </div>


          </form>
        </div>


        <!--footer-->
        <div class="modal-footer">
          <button type="button" class="btn btn-light" @click="dataReset()" data-bs-dismiss="modal">Cancel</button>
          <button type="button" class="btn btn-success" @click="createNewInventoryItem()">Confirm</button>
        </div>

      </div>
    </div>
  </div>

</template>

<script>
import Api, {Listing} from "@/Api";
const datefns = require('date-fns');

export default {
  name: "CreateListing",
  props: {
    businessId: {
      type: Number,
      required: true
    }
  },
  data() {
    return {
      config: Listing.config,
      inventoryItems: [],
      allInventoryItems: [], // Stores all inventory items (for new listing dropdown)

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
    /**
     * This method removes white space from the beginning and end of all the input field's input values.
     */
    trimTextInputFields() {
      this.quantity = this.quantity.trim();
      this.price = this.price.trim();
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
      const closeDateMonth = closeDate.month-1; // 1 month must be taken off
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
      await Api.getEveryInventoryItem(2).then((response) => {
        this.allInventoryItems = [...response.data];
      }).catch((error) => {
        if (error.response) {
          if (error.response.status === 400) {
            this.toastErrorMessage = '400 Bad request; invalid listing data';
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

    autofillDetails(event) {
      console.log(event);
    },

    /**
     * Creates the new Inventory Item
     */
    async createNewInventoryItem() { // TODO
      let requestIsInvalid = false;

      this.inventoryId = document.getElementById("productInput").value;
      this.trimTextInputFields();

      // Inventory Item Error Checking
      //if (this.inventoryId.length === 0) {
      //  requestIsInvalid = true;
      //  this.inventoryIdErrorMsg = "Must select an item from the inventory"
      //} else {
      //  this.inventoryIdErrorMsg = "";
      //}

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
      this.priceErrorMsg = this.getErrorMessage(
          this.config.price.name,
          this.price,
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
      //console.log(listingItemData);
      const newListingItem = new Listing(listingItemData);

      await Api.addNewBusinessListing(this.businessId, newListingItem).then((response) => {
        if (response.status === 201) {
          this.dataReset();
        }
      }).catch((error) => {

        this.cannotProceed = true;
        if (error.response) {
          if (error.response.status === 400) {
            this.toastErrorMessage = '400 Bad request; invalid listing data';
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
    // Adds test data
    this.getAllInventoryItems().then(() => console.log(this.allInventoryItems));
    //this.testData();
  }
}
</script>

<style scoped>

</style>
