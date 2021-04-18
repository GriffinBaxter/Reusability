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
          <div class="modal-header">
            <h3 class="modal-title" id="createProductModalLabel">Create Product</h3>
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
          <div class="modal-footer">
            <button type="button" class="btn btn-md btn-outline-secondary green-button-transparent" @click="modal.hide()" tabindex="7">Cancel</button>
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