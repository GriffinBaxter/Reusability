<template>
  <transition name="modal-fade">
    <div class="modal-backdrop" @click="$emit('close')">
      <div class="modal-dialog" role="document" @click.stop="">
        <div class="modal-content">
          <div class="modal-header text-center">
            <h3 class="col-12 modal-title text-center">Create Product</h3>
          </div>
          <div class="modal-body">
            <!--create product form, needs validation-->
            <form id="create" novalidate @submit.prevent>
              <div class="form-group">
                  <label for="product-id">Product ID*</label>
                  <input id="product-id" name="product-id" tabindex="1" type="text" v-model="productID"
                         :class="toggleInvalidClass(productIDErrorMsg)" :maxlength="config.productID.maxLength" required>
              </div>
              <div class="form-group">
                  <label for="product-name">Product Name*</label>
                  <input id="product-name" name="product-name" tabindex="2" type="text" v-model="productName"
                         :class="toggleInvalidClass(productNameErrorMsg)" :maxlength="config.productName.maxLength" required>
              </div>
              <div class="form-group">
                  <label for="product-price">Recommended Retail Price</label>
                  <input id="product-price" name="product-price" tabindex="3" type="text" v-model="recommendedRetailPrice"
                         :class="toggleInvalidClass(recommendedRetailPriceErrorMsg)"
                         :maxlength="config.recommendedRetailPrice.maxLength">
              </div>
              <div class="form-group">
                  <label for="description">Description</label>
                  <textarea id="description" name="description" tabindex="4" rows="5" cols="70" v-model="description"
                            :maxlength="config.description.maxLength" :class="toggleInvalidClass(descriptionErrorMsg)"
                            style="resize: none"/>
              </div>
            </form>
           </div>
          <div class="modal-footer">
            <!--cancel and save buttons-->
              <button id="cancel-button" tabindex="6" class="btn btn-lg btn-outline-primary green-button-transparent"
                      type="button" @click="close" aria-label="Close modal"> Cancel </button>
              <button id="save-button" tabindex="5" class="btn btn-lg btn-primary float-lg-end green-button"
                      type="button" @click="close" aria-label="Close modal"> Save </button>
          </div>
          </div>
        </div>
      </div>
  </transition>
</template>

<script>
import {Product} from "@/Api";

export default {
  name: 'CreateProductModal',

  data() {
    return {

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

      // Toast related variables
      toastErrorMessage: "",
      cannotProceed: false,

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

    close() {
      this.$emit('close');
    },
  },
};
</script>

<style>

.modal-backdrop {
  position: fixed;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  background-color: rgba(8, 50, 37, 0.3);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-dialog {
  background: #ffffff;
  max-width: 600px;
  position: relative;
  display: flex;
  flex-direction: column;
  margin-left: auto;
  margin-right: auto;
  align-content: center;
  justify-content: center;
  border-radius: 15px;
}

.modal-content {
  border-radius: 15px;
}

form {
  display: flex;
  flex-direction: column;
  margin-left: auto;
  margin-right: auto;
  max-width: 600px;
  align-content: center;
  justify-content: center;
}

label {
  text-align: left;
  display: flex;
  flex-direction: column;
}

.modal-fade-enter,
.modal-fade-leave-to {
  opacity: 0;
}

.modal-fade-enter-active,
.modal-fade-leave-active {
  transition: opacity .5s ease;
}

/*--------------------- Hide arrows from input numbers ---------------------*/
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

input:focus, textarea:focus, #cancel-button:focus, #save-button:focus {
  outline: none;
  box-shadow: 0 0 2px 2px #2eda77; /* Full freedom. (works also with border-radius) */
  border: 1px solid #1EBABC;
}
</style>