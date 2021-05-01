<template>

    <!-- Modal -->
    <div class="modal fade" ref="_updateProductModel" tabindex="-1" aria-labelledby="updateProductModel" aria-hidden="true" id="update-product-modal">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="updateProductModelTitle">Update Product {{value.data.id}}</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">

            <!-- Modal form content wrapper-->
            <form class="needs-validation mb-3 px-5" novalidate @submit.prevent>

              <!-- Error message card-->
              <div class="row my-lg-2">
                <div class="col-12 mx-auto">
                  <div v-if="formErrorModalMessage" class="alert alert-danger">
                    <label>{{formErrorModalMessage}}</label>
                  </div>
                </div>
              </div>

              <!-- Product ID -->
              <div class="row my-lg-2">
                <div class="col-12 my-2 my-lg-0">
                  <label :for="'product-id-'+value.data.id">ID</label>
                  <input :id="'product-id-'+value.data.id" name="product-id" type="text" v-model="newProduct.data.id"
                         :class="toggleInvalidClass(errorsMessages.id)" :maxlength="config.productID.maxLength">
                  <div class="invalid-feedback">
                    {{errorsMessages.id}}
                  </div>
                </div>
              </div>

              <!--              Product Name-->
              <div class="row my-lg-2">
                <div class="col-12 my-2 my-lg-0">
                  <label :for="'product-name-'+value.data.id">Name*</label>
                  <input :id="'product-name-'+value.data.id" name="product-name" type="text" v-model="newProduct.data.name"
                         :class="toggleInvalidClass(errorsMessages.name)" :maxlength="config.productName.maxLength">
                  <div class="invalid-feedback">
                    {{errorsMessages.name}}
                  </div>
                </div>
              </div>

              <!--               Product Manufacturer-->
              <div class="row my-lg-2">
                <div class="col-12 my-2 my-lg-0">
                  <label :for="'product-manufacturer-'+value.data.id">Manufacturer</label>
                  <input :id="'product-manufacturer-'+value.data.id" name="product-manufacturer" type="text" v-model="newProduct.data.manufacturer"
                         :class="toggleInvalidClass(errorsMessages.manufacturer)" :maxlength="config.manufacturer.maxLength">
                  <div class="invalid-feedback">
                    {{errorsMessages.manufacturer}}
                  </div>
                </div>
              </div>

              <!--            Product Recommended Retail Price-->
              <div class="row my-lg-2">
                <div class="col-12 my-2 my-lg-0">
                  <label :for="'product-price-'+value.data.id">Recommended Retail Price</label>
                  <input :id="'product-price-'+value.data.id" name="product-price" type="number" v-model="newProduct.data.recommendedRetailPrice"
                         :class="toggleInvalidClass(errorsMessages.recommendedRetailPrice)" min="0">
                  <div class="invalid-feedback">
                    {{errorsMessages.recommendedRetailPrice}}
                  </div>
                </div>
              </div>

              <!--                Product Description-->
              <div class="row my-lg-2">
                <div class="col-12 my-2 my-lg-0">
                  <label :for="'product-description-'+value.data.id">Description</label>
                  <textarea :id="'product-description-'+value.data.id" name="product-description" v-model="newProduct.data.description"
                            :class="toggleInvalidClass(errorsMessages.description)" :maxlength="config.description.maxLength" style="resize: none"/>
                  <div class="invalid-feedback">
                    {{errorsMessages.description}}
                  </div>
                </div>
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-primary order-1 green-button" @click="event => updateProduct(event)">Save changes</button>
            <button type="button" class="btn btn-outline-primary order-0 green-button-transparent" data-bs-dismiss="modal">Close</button>
          </div>
        </div>
      </div>
    </div>

</template>

<script>
import { Modal } from 'bootstrap'
import Product from "../configs/Product"
import Api from "../Api";


export default {
  name: "UpdateProductModal",
  props: {

    // Product details -- MUST BE V-MODEL therefore MUST BE NAMED VALUE!
    value: {
      type: Product,
      required: true
    },

    // Business id used to not what business to update
    businessId: {
      type: Number,
      required: true
    }
  },
  data() {
    return {
      // Used to display a modal that contains the form to edit the product
      modal: null,

      // This defines the error message regarding different responses from the request to Axios
      formErrorModalMessage: "",

      // Contains the config for checking the product parameters when updated
      config: Product.config,

      // Create the object that will store the data
      newProduct: new Product(this.value.data),

      // Create the object that stores the error messages
      errorsMessages: {
        id: "",
        name: "",
        description: "",
        manufacturer: "",
        recommendedRetailPrice: ""
      },

      // Keeps track if there is an error or not in the form
      inputError: false

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
     * This method toggles the appearance of the error message, where the is-invalid class is added to the messages
     * if an error message needs to be presented to the user.
     *
     * @param errorMessage, string, the error message relating to invalid input of a field.
     * @returns {[string]}, classList, a list containing the classes for an invalid message.
     */
    toggleInvalidClass(errorMessage) {
      let classList = ['form-control']
      if (errorMessage !== "") {
        classList.push('is-invalid')
      }
      return classList
    },
    /**
     * Prevents the default call onClick and updates the placeholder values before show the modal.
     *
     * @param event The event (i.e. click event) that triggered the call.
     */
    showModel(event) {
      // Prevent any default actions
      event.preventDefault();

      // If the modal is already showing prevent the placeholders from being updated.
      if (!this.$refs._updateProductModel.classList.contains("show")) {
        // Update the placeholders
        this.newProduct.data.id = this.value.data.id;
        this.newProduct.data.name = this.value.data.name;
        this.newProduct.data.description = this.value.data.description;
        this.newProduct.data.manufacturer = this.value.data.manufacturer;
        this.newProduct.data.recommendedRetailPrice = this.value.data.recommendedRetailPrice;

        // Reset all the error messages
        this.errorsMessages.id = "";
        this.errorsMessages.name = "";
        this.errorsMessages.manufacturer = "";
        this.errorsMessages.recommendedRetailPrice = "";
        this.errorsMessages.description = "";

      }
      // Show the modal
      this.modal.show();
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
      if (inputVal === "" && minLength >= 1) {
        return "Please enter input";
      } else if (!regex.test(inputVal)) {
        return regexMessage;
      } else if (!this.between(inputVal.length, minLength, maxLength)) {
        return `Input must be between ${minLength} and ${maxLength} characters long.`
      }
      return "";
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
     *
     * @param event
     */
    updateProduct(event) {
      // Prevent any default actions
      event.preventDefault();
      this.inputError = false;

      // Process new ID
      this.errorsMessages.id = this.getErrorMessage("Id", this.newProduct.data.id, this.config.productID.minLength,
          this.config.productID.maxLength, this.config.productID.regexMessage, this.config.productID.regex);
      if (this.errorsMessages.id) {
        this.inputError = true;
      }

      // Process new name
      this.errorsMessages.name = this.getErrorMessage("Name", this.newProduct.data.name, this.config.productName.minLength,
          this.config.productName.maxLength, this.config.productName.regexMessage, this.config.productName.regex);
      if (this.errorsMessages.name) {
        this.inputError = true;
      }

      // Process new manufacturer
      this.errorsMessages.manufacturer = this.getErrorMessage("Manufacturer", this.newProduct.data.manufacturer, this.config.manufacturer.minLength,
          this.config.manufacturer.maxLength, this.config.manufacturer.regexMessage, this.config.productName.regex);
      if (this.errorsMessages.manufacturer) {
        this.inputError = true;
      }

      // Process new recommended retail price
      if (typeof this.newProduct.data.recommendedRetailPrice === "string") {
        this.errorsMessages.recommendedRetailPrice = this.getErrorMessage("Recommended Retail Price", this.newProduct.data.recommendedRetailPrice,
            this.config.recommendedRetailPrice.minLength, this.config.recommendedRetailPrice.maxLength, this.config.recommendedRetailPrice.regexMessage,
            this.config.recommendedRetailPrice.regex);

        // If it is undefined (i.e. it passes regex but is still invalid) throw an error message
        if (!Number.parseFloat(this.newProduct.data.recommendedRetailPrice)) {
          this.errorsMessages.recommendedRetailPrice = "Must be a float."
          this.inputError = true;
        } else {
          // This means that it is passable
          this.newProduct.data.recommendedRetailPrice = Number.parseFloat(this.newProduct.data.recommendedRetailPrice);
          this.errorsMessages.recommendedRetailPrice = "";
        }

      } else {
        // Check if the recommended retail price is between 0 and positive infinity
        this.errorsMessages.recommendedRetailPrice = this.between(this.newProduct.data.recommendedRetailPrice, 0, Number.POSITIVE_INFINITY)
            ? ""
            : "Must be between 0 and positive infinity.";
      }
      // Cancel further processing if the price has an error message.
      if (this.errorsMessages.recommendedRetailPrice) {
        this.inputError = true;
      }

      // Process new description
      this.errorsMessages.description = this.getErrorMessage("Description", this.newProduct.data.description, this.config.description.minLength,
          this.config.description.maxLength, this.config.description.maxLength);
      if (this.errorsMessages.description) {
        this.inputError = true;
      }


      // If there is an input don't bother making the request to the backend
      if (this.inputError) {
        return;
      }


      // Perfrom the update call
      Api.modifyProduct(this.value.data.id, this.businessId, this.newProduct)
          .then(
              res => {
                // This means that the modification was successfull
                if (res.data.status === 200) {
                  this.updateValue(new Product(this.newProduct.data));
                  this.modal.hide();
                }
              }
          )
          .catch(
              error => {
                if (error.response) {

                  // There was something wrong with the user data!
                  if (error.response.status === 400) {
                    // There is no way to tell if this is caused by the product ID already existing or not, which is not very helpful
                    // when this happens!
                    this.formErrorModalMessage = "Some of the information you have entered is invalid."

                    // Invalid token was used
                  } else if (error.response.status === 403) {
                    this.formErrorModalMessage = "You do not have permission to perform this action!"
                    this.$router.push({path: "/invalidtoken"})

                    // We got an error code back but unsure of its meaning
                  } else {
                    this.formErrorModalMessage = "Sorry, something went wrong..."
                  }


                } else if (error.request) {
                  // Timeout occured
                  this.formErrorModalMessage = "Server timeout"
                  this.$router.push({path: "/timeout"})
                } else {
                  // Something went wrong but with send the request itself.
                  this.formErrorModalMessage = "Sorry, something went wrong..."
                }
              }
          )
    },
  },
  mounted() {
    // Create a modal and attach it to the updateProductModel reference.
    this.modal = new Modal(this.$refs._updateProductModel);
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