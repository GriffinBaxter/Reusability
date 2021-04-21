<template>
  <div @click="event => showModel(event)">

    <!--   Slot serves the children-->
    <slot></slot>

    <!-- Modal -->
    <div class="modal fade" ref="updateProductModel" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="updateProductModelTitle">Update Product {{product.data.id}}</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">

            <!-- Product ID -->
            <form class="needs-validation mb-3 px-5" novalidate @submit.prevent>
              <div class="row my-lg-2">
                <div class="col-lg-4 my-2 my-lg-0">
                  <label :for="'product-id-'+product.data.id">ID</label>
                  <input :id="'product-id-'+product.data.id" name="product-id" tabindex="1" type="text" v-model="newProduct.data.id"
                         :class="toggleInvalidClass(errorsMessages.id)" :maxlength="config.productID.maxLength">
                  <div class="invalid-feedback">
                    {{errorsMessages.id}}
                  </div>
                </div>
              </div>

<!--              Product Name-->
              <div class="row my-lg-2">
                <div class="col-lg-4 my-2 my-lg-0">
                  <label :for="'product-name-'+product.data.id">Name*</label>
                  <input :id="'product-name-'+product.data.id" name="product-name" tabindex="2" type="text" v-model="newProduct.data.name"
                         :class="toggleInvalidClass(errorsMessages.name)" :maxlength="config.productName.maxLength">
                  <div class="invalid-feedback">
                    {{errorsMessages.name}}
                  </div>
                </div>

<!--               Product Manufacturer-->
                <div class="row my-lg-2">
                  <div class="col-lg-4 my-2 my-lg-0">
                    <label :for="'product-manufacturer-'+product.data.id">Manufacturer</label>
                    <input :id="'product-manufacturer-'+product.data.id" name="product-manufacturer" tabindex="3" type="text" v-model="newProduct.data.manufacturer"
                           :class="toggleInvalidClass(errorsMessages.manufacturer)" :maxlength="config.manufacturer.maxLength">
                    <div class="invalid-feedback">
                      {{errorsMessages.manufacturer}}
                    </div>
                  </div>
                </div>

<!--            Product Recommended Retail Price-->
                <div class="row my-lg-2">
                  <div class="col-lg-4 my-2 my-lg-0">
                    <label :for="'product-price-'+product.data.id">Recommended Retail Price</label>
                    <input :id="'product-price-'+product.data.id" name="product-price" tabindex="4" type="number" v-model="newProduct.data.recommendedRetailPrice"
                           :class="toggleInvalidClass(errorsMessages.recommendedRetailPrice)" :maxlength="config.recommendedRetailPrice.maxLength">
                    <div class="invalid-feedback">
                      {{errorsMessages.recommendedRetailPrice}}
                    </div>
                  </div>
                </div>

<!--                Product Description-->
                <div class="row my-lg-2">
                  <div class="col-lg-4 my-2 my-lg-0">
                    <label :for="'product-description-'+product.data.id">Description</label>
                    <textarea :id="'product-description-'+product.data.id" name="product-description" tabindex="5" v-model="newProduct.data.description"
                           :class="toggleInvalidClass(errorsMessages.description)" :maxlength="config.description.maxLength" style="resize: none"/>
                    <div class="invalid-feedback">
                      {{errorsMessages.description}}
                    </div>
                  </div>
                </div>
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
            <button type="button" class="btn btn-primary" @click="event => updateProduct(event)">Save changes</button>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import { Modal } from 'bootstrap'
import Product from "../configs/Product"
import Api from "@/Api";


export default {
  name: "UpdateProductWrapper",
  props: {
    // Product details -- MUST BE V-MODEL
    productProp: {
      type: Product,
      required: true
    },

    // Business id used to not what business to update
    businessId: {
      type: Number,
      required: true
    }

  },
  computed: {
    product: {}
  },
  data() {
    return {
      // Used to display a modal that contains the form to edit the product
      modal: null,

      // Keeps track if the modal is showing
      isModalShowing: false,

      // Contains the config for checking the product parameters when updated
      config: Product.config,

      // Create the object that will store the data
      newProduct: new Product(this.product.data),

      // Create the object that stores the error messages
      errorsMessages: {
        id: "",
        name: "",
        description: "",
        manufacturer: "",
        recommendedRetailPrice: ""
      }
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
     * Prevents the default call onClick and updates the placeholder values before show the modal.
     *
     * @param event The event (i.e. click event) that triggered the call.
     */
    showModel(event) {
      // Prevent any default actions
      event.preventDefault();

      // If the modal is already showing prevent the placeholders from being updated.
      if (!this.$refs.updateProductModel.classList.contains("show")) {
        // Update the placeholders
        this.newProduct.data.id = this.product.data.id;
        this.newProduct.data.name = this.product.data.name;
        this.newProduct.data.description = this.product.data.description;
        this.newProduct.data.manufacturer = this.product.data.manufacturer;
        this.newProduct.data.recommendedRetailPrice = this.product.data.recommendedRetailPrice;
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

      // Process new ID
      this.errorsMessages.id = this.getErrorMessage("Id", this.newProduct.data.id, this.config.productID.minLength,
          this.config.productID.maxLength, this.config.productID.regexMessage, this.config.productID.regex);
      if (this.errorsMessages.id) {
        return;
      }

      // Process new name
      this.errorsMessages.name = this.getErrorMessage("Name", this.newProduct.data.name, this.config.productName.minLength,
          this.config.productName.maxLength, this.config.productName.regexMessage, this.config.productName.regex);
      if (this.errorsMessages.name) {
        return;
      }

      // Process new manufacturer
      this.errorsMessages.manufacturer = this.getErrorMessage("Manufacturer", this.newProduct.data.manufacturer, this.config.manufacturer.minLength,
          this.config.manufacturer.maxLength, this.config.manufacturer.regexMessage, this.config.productName.regex);
      if (this.errorsMessages.manufacturer) {
        return;
      }

      // Process new recommended retail price
      if (typeof this.newProduct.data.recommendedRetailPrice === "string") {
        this.errorsMessages.recommendedRetailPrice = this.getErrorMessage("Recommended Retail Price", this.newProduct.data.recommendedRetailPrice,
            this.config.recommendedRetailPrice.minLength, this.config.recommendedRetailPrice.maxLength, this.config.recommendedRetailPrice.regexMessage,
            this.config.recommendedRetailPrice.regex);
      } else {
        // Check if the recommended retail price is between 0 and positive infinity
        this.errorsMessages.recommendedRetailPrice = this.between(this.newProduct.data.recommendedRetailPrice, 0, Number.POSITIVE_INFINITY)
            ? ""
            : "Must be between 0 and positive infinity.";
      }
      // Cancel further processing if the price has an error message.
      if (this.errorsMessages.recommendedRetailPrice) {
        return;
      }

      // Process new description
      this.errorsMessages.description = this.getErrorMessage("Description", this.newProduct.data.description, this.config.description.minLength,
          this.config.description.maxLength, this.config.description.maxLength);
      if (this.errorsMessages.description) {
        return;
      }

      // Perfrom the update call
      Api.modifyProduct(this.product.data.id, this.businessId, this.newProduct)
        .then(
          res => {
            // This means that
            if (res.data.status === 200) {
              // TODO Update product. But figure out how computed variables work with V-MODEL!
              this.$emit("product-update")
            }
          }
        )
        .catch(
          error => {
            if (error.response) {
              // TODO Error codes
            } else if (error.request) {
              // TODO Timeout
            } else {
              // TODO Something went wrong and it ain't good :(
            }
          }
        )
    },
  },
  mounted() {
    // Create a modal and attach it to the updateProductModel reference.
    this.modal = new Modal(this.$refs.updateProductModel);
  }
}
</script>

<style scoped>

</style>