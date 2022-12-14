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

            <!-- Autofill success message card-->
            <div class="row my-lg-2">
              <div class="col-12 mx-auto">
                <div v-if="autofilled" id="autofillSuccessMessage" class="alert alert-success text-center">
                  <label>Product information has been autofilled</label>
                </div>
              </div>
            </div>

            <!-- Autofill error message card-->
            <div class="row my-lg-2">
              <div class="col-12 mx-auto">
                <div v-if="autofillError" id="autofillErrorMessage" class="alert alert-danger text-center">
                  <label>Could not autofill, product may not exist in database</label>
                </div>
              </div>
            </div>

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
                  <label :for="'product-id-'+value.data.id">ID*</label>
                  <input :id="'product-id-'+value.data.id" name="product-id" type="text" v-model="newProduct.data.id"
                         :class="toggleInvalidClass(errorsMessages.id)" :maxlength="config.productID.maxLength">
                  <div class="invalid-feedback">
                    {{errorsMessages.id}}
                  </div>
                </div>
              </div>
              <hr>
              <!--product barcode-->
              <div class="form-group">
                <label for="barcode-checkbox-in-update-modal">Edit Barcode?&nbsp;</label>
                <input type="checkbox" id="barcode-checkbox-in-update-modal" name="barcode-checkbox-in-update-modal" v-model="editBarcode">
                <br>
                <div v-if="editBarcode">
                  <br>
                  <label for="product-barcode">Barcode (EAN or UPC)</label>
                  <input id="product-barcode" class="input-styling" name="product-barcode" type="text" v-model="newProduct.data.barcode"
                         :class="toggleInvalidClass(errorsMessages.barcode)" :maxlength="config.barcode.maxLength">
                  <div class="invalid-feedback">
                    {{ errorsMessages.barcode }}
                  </div>
                  <br><br>
                  <div class="row">
                    <div class="col">
                      <button class="btn green-button-transparent" @click="onUploadClick">Scan by uploading image</button>
                      <input type="file" id="imageUpload" ref="image" @change="getBarcodeStatic"
                             name="img" accept="image/png, image/gif, image/jpeg">
                    </div>
                    <div class="col">
                      <button v-if="liveStreamAvailable && !liveStreaming" class="btn green-button-transparent"
                              @click="getBarcodeLiveStream">
                        Scan using camera
                      </button>
                      <button v-if="liveStreaming && !barcodeFound" class="btn green-button-transparent"
                              @click="
                              liveStreaming = false;
                              removeCameraError();
                              ">
                        Stop scanning  (barcode not found)
                      </button>
                    </div>
                  </div>
                  <button v-if="liveStreaming && barcodeFound" class="btn green-button"
                          @click="liveStreaming = false">
                    Save Scanned Barcode
                  </button>
                  <br>
                  <div v-if="liveStreaming"><br></div>
                  <div id="editLiveStreamCamera" style="padding-bottom: 6px"></div>
                  <button id="autofill-button" type="button"
                          :class="`btn green-button ${getErrorMessage(
                              config.barcode.name,
                              newProduct.data.barcode,
                              config.barcode.minLength,
                              config.barcode.maxLength,
                              config.barcode.regexMessage,
                              config.barcode.regex) === '' ? '': 'disabled'}`"
                          @click="autofillProductFromBarcode()">Autofill Empty Fields
                  </button>
                </div>
              </div>
              <hr>
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
import Product from "../../configs/Product"
import Api from "../../Api";
import {autofillProductFromBarcode, getBarcodeLiveStream, getBarcodeStatic} from "../../barcodeUtils";
import {toggleInvalidClass} from "../../validationUtils";


export default {
  name: "UpdateProductModal",
  props: {

    // Product details -- MUST BE V-MODEL therefore MUST BE NAMED VALUE!
    value: {
      type: Product,
      required: true
    },

    // Business id used to know what business to update
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
        recommendedRetailPrice: "",
        barcode: ""
      },

      // Keeps track if there is an error or not in the form
      inputError: false,

      editBarcode: false,
      liveStreamAvailable: false,
      liveStreaming: false,
      barcodeFound: false,

      barcode: "",
      barcodeErrorMsg: "",
      productName: "",
      manufacturer: "",
      description: "",
      autofilled: false,
      autofillError: false

    }
  },
  methods: {
    toggleInvalidClass: toggleInvalidClass,
    /**
     * Emits an event that updates the v-model prop value.
     * @param value The new value of the value prop.
     */
    updateValue(value) {
      this.$emit('input', value);
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
        this.newProduct.data.barcode = this.value.data.barcode;

        // Reset all the error messages
        this.autofilled = false;
        this.autofillError = false;
        this.errorsMessages.id = "";
        this.errorsMessages.name = "";
        this.errorsMessages.manufacturer = "";
        this.errorsMessages.recommendedRetailPrice = "";
        this.errorsMessages.description = "";
        this.errorsMessages.barcode = "";
      }

      this.editBarcode = this.newProduct.data.barcode !== null && this.newProduct.data.barcode !== "";
      this.barcode = ""
      this.liveStreaming = false;
      this.barcodeFound = false;

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
      } else if (inputVal !== null) {
        if (!this.between(inputVal.length, minLength, maxLength)) {
          return `Input must be between ${minLength} and ${maxLength} characters long.`
        }
      } else if (minLength >= 1) {
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
     * Given an update event we perform a validation and perform the call to the API to modify the product.
     * @param event An update button event.
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

      // Process new barcode
      if (this.editBarcode) {
        this.errorsMessages.barcode = this.getErrorMessage(
            this.config.barcode.name,
            this.newProduct.data.barcode,
            this.config.barcode.minLength,
            this.config.barcode.maxLength,
            this.config.barcode.regexMessage,
            this.config.barcode.regex)
      } else {
        this.newProduct.data.barcode = '';
        this.errorsMessages.barcode = '';
      }
      if (this.errorsMessages.barcode) {
        this.inputError = true;
      }

      // If there is an input don't bother making the request to the backend
      if (this.inputError) {
        return;
      }


      // Perform the update call
      Api.modifyProduct(this.value.data.id, this.businessId, this.newProduct)
          .then(
              res => {
                // This means that the modification was successful
                if (res.data.status === 200) {
                  this.updateValue(new Product(this.newProduct.data));
                  // Custom event so that ProductCatalogue.vue knows edit was a success and can alert the user.
                  this.$root.$emit('edits');
                  this.modal.hide();
                  this.formErrorModalMessage = "";
                }
              }
          )
          .catch(
              error => {
                if (error.response) {

                  // There was something wrong with the user data!
                  if (error.response.status === 400) {
                    if (error.response.data.message !== "") {
                      this.formErrorModalMessage = "Error: " + error.response.data.message;
                    } else {
                      this.formErrorModalMessage = "Some of the information you have entered is invalid.";
                    }

                    // Invalid token was used
                  } else if (error.response.status === 403) {
                    this.formErrorModalMessage = "You do not have permission to perform this action!"
                    this.$router.push({path: "/invalidtoken"})

                    // We got an error code back but unsure of its meaning
                  } else {
                    this.formErrorModalMessage = "Sorry, something went wrong..."
                  }


                } else if (error.request) {
                  // Timeout occurred
                  this.formErrorModalMessage = "Server timeout"
                  this.$router.push({path: "/timeout"})
                } else {
                  // Something went wrong but with send the request itself.
                  this.formErrorModalMessage = "Sorry, something went wrong..."
                }
              }
          )
    },

    onUploadClick() {
      this.$refs.image.click();
    },

    /**
     * Retrieves the barcode number (EAN or UPC) from an uploaded image.
     */
    getBarcodeStatic() {
      this.formErrorModalMessage = "";
      this.barcode = this.newProduct.data.barcode;

      let outerThis = this;
      getBarcodeStatic(this, function () {
        outerThis.newProduct.data.barcode = outerThis.barcode;
        outerThis.formErrorModalMessage = outerThis.barcodeErrorMsg;
      });
    },

    /**
     * Retrieves the barcode number (EAN or UPC) from a live camera feed, based on the most commonly occurring barcode
     * per each frame scan.
     */
    getBarcodeLiveStream() {
      this.formErrorModalMessage = "";
      this.barcode = this.newProduct.data.barcode;

      let outerThis = this;
      getBarcodeLiveStream(this, 375, "#editLiveStreamCamera", function() {
        outerThis.newProduct.data.barcode = outerThis.barcode;
      });
    },

    /**
     * Removes the camera error message after stopping the scanning.
     */
    removeCameraError() {
      document.querySelector('#editLiveStreamCamera').innerHTML = ""
    },

    /**
     * Autofills data from the barcode, using the OpenFoodFacts API.
     */
    autofillProductFromBarcode() {
      this.autofillError = false;
      this.autofilled = false;
      this.barcode = this.newProduct.data.barcode;
      this.productName = this.newProduct.data.name;
      this.manufacturer = this.newProduct.data.manufacturer;
      this.description = this.newProduct.data.description;

      let outerThis = this;
      autofillProductFromBarcode(outerThis, function () {
        outerThis.newProduct.data.name = outerThis.productName;
        outerThis.newProduct.data.manufacturer = outerThis.manufacturer;
        outerThis.newProduct.data.description = outerThis.description;
      });
    }
  },
  mounted() {
    // Create a modal and attach it to the updateProductModel reference.
    this.modal = new Modal(this.$refs._updateProductModel);

    this.liveStreamAvailable = navigator.mediaDevices && typeof navigator.mediaDevices.getUserMedia === 'function';
  }
}
</script>

<style scoped>

/* Styles the input and textarea's borders to be green when they are focused/tabbed to */
input:focus, textarea:focus, button:focus{
  outline: none;
  box-shadow: 0 0 2px 2px #1EBA8C;
  border: 1px solid #1EBABC;
}

label, input {
  display: inline-block;
  vertical-align: middle;
}

</style>
