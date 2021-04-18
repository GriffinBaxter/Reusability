<template>
  <div class="container">
    <div class="row justify-content-center">
      <div class="col-3 m-3">
        <img id="logo" src="../../public/logo_only_med.png" class="img-fluid" alt="logo">
      </div>
    </div>

    <!--button to open create product modal-->
    <button id="create-product-button" type="button" class="btn btn-lg btn-primary float-lg-end" @click="modal.show()">Create Product</button>

    <!--create product modal-->
    <div class="modal fade" ref="CreateProductModal" tabindex="-1" aria-hidden="true">
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
          <div class="modal-header text-center">
            <h3 class="modal-title w-100" id="createProductModalLabel">Create Product</h3>
            <button type="button" class="btn-close" @click="modal.hide()" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <!--create product form, needs validation-->
            <form id="create" novalidate @submit.prevent>
              <!--product id-->
              <div class="form-group">
                <label for="product-id">Product ID*</label>
                <input id="product-id" name="product-id" tabindex="1" type="text" v-model="productID"
                       :class="toggleInvalidClass(productIDErrorMsg)" :maxlength="config.productID.maxLength" required>
              </div>
              <!--product name-->
              <div class="form-group">
                <label for="product-name">Product Name*</label>
                <input id="product-name" name="product-name" tabindex="2" type="text" v-model="productName"
                       :class="toggleInvalidClass(productNameErrorMsg)" :maxlength="config.productName.maxLength" required>
              </div>
              <!--recommended retail price-->
              <div class="form-group">
                <label for="product-price">Recommended Retail Price(RRP)</label>
                <input id="product-price" name="product-price" tabindex="3" type="text" v-model="recommendedRetailPrice"
                       :class="toggleInvalidClass(recommendedRetailPriceErrorMsg)"
                       :maxlength="config.recommendedRetailPrice.maxLength">
              </div>
              <!--manufacturer-->
              <div class="form-group">
                <label for="manufacturer">Manufacturer</label>
                <input id="manufacturer" name="manufacturer" tabindex="4" type="text" v-model="manufacturer"
                       :class="toggleInvalidClass(manufacturerErrorMsg)" :maxlength="config.manufacturer.maxLength" required>
              </div>
              <!--description-->
              <div class="form-group">
                <label for="description">Description</label>
                <textarea id="description" name="description" tabindex="5" rows="5" cols="70" v-model="description"
                          :maxlength="config.description.maxLength" :class="toggleInvalidClass(descriptionErrorMsg)"
                          style="resize: none"/>
              </div>
            </form>
          </div>
          <div class="modal-footer justify-content-between">
            <button type="button" class="btn btn-md btn-outline-secondary green-button-transparent mr-auto" @click="modal.hide()" tabindex="7">Cancel</button>
            <button type="button" class="btn btn-md btn-primary float-lg-end green-button" tabindex="6">Save</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { Modal } from 'bootstrap'
import {Product} from "@/Api";

export default {
  name: "Products",
  data: () => ({
    modal: null,

    // Used for having pre-filled input fields
    DEBUG_MODE: false,

    // A copy of the user config file for error checking.
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
  }),
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
      let errorMessage = ""; //TODO: remove after testing and just have ""
      if (inputVal === "" && minLength >= 1) {
        errorMessage = "Please enter input";
      }
      else if (!regex.test(inputVal)) {
        errorMessage = regexMessage;
      } else if (!this.between(inputVal.length, minLength, maxLength)) {
        errorMessage = `Input must be between ${minLength} and ${maxLength} characters long.`
      }
      return errorMessage;
    },

    /**
     * This method removes white space from the beginning and end of all the input field's input values.
     */
    trimTextInputFields () {
      this.productID = this.productID.trim();
      this.productName = this.productName.trim();
      this.recommendedRetailPrice = this.recommendedRetailPrice.trim();
      this.manufacturer= this.manufacturer.trim();
      this.description = this.description.trim();
    },

  },
  mounted() {
    this.modal = new Modal(this.$refs.CreateProductModal)
  }
};
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

input:focus, textarea:focus, #create-product-button:focus{
  outline: none;     /* oranges! yey */
  box-shadow: 0 0 2px 2px #1EBA8C; /* Full freedom. (works also with border-radius) */
  border: 1px solid #1EBABC;
}
/*------------------------------------------------------------------------*/

</style>