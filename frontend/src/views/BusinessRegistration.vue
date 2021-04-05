<template>
  <div class="container">
    <div class="row justify-content-center">
      <div class="col-3 m-3">
        <img id="logo" src="../../public/logo_only_med.png" class="img-fluid" alt="logo">
      </div>
    </div>
    <div class="col-lg-8 mx-auto">
      <div class="container shadow py-5 px-4 bg-white mb-5" id="register-form">
        <div class="row">
          <div class="col">
            <h3 class="display-6 m-2 mb-5 text-center">Business Registration</h3>
          </div>
        </div>
        <form id="registration" class="needs-validation mb-3 px-5" novalidate @submit.prevent>
        <div class="row my-lg-2">
          <div class="col my-2 my-lg-0">
            <label for="business-name">Name*</label>
            <input id="business-name" name="business-name" tabindex="1" type="text" v-model="businessName"
                   :class="toggleInvalidClass(businessNameErrorMsg)" :maxlength="config.businessName.maxLength" required>
            <div class="invalid-feedback">
              {{businessNameErrorMsg}}
            </div>
          </div>
        </div>
        <div class="row my-lg-2">
          <div class="col-lg-4 my-2 my-lg-0">
            <label for="business-type">Type*</label>
            <select id="business-type" name="business-type" tabindex="2" :class="toggleInvalidClass(businessTypeErrorMsg)" v-model="businessType" required>
              <option value="" disabled>Select Business Type</option>
              <option v-for="option in types" :key="option.bType" :value="option.value">
                {{ option.value }}
              </option>
            </select>
            <div class="invalid-feedback">
              {{businessTypeErrorMsg}}
            </div>
          </div>

          <div class="col my-2 my-lg-0">
            <label for="business-address">Address Autofill (Optional)</label>
            <input id="business-address" name="business-address" ref="businessAddressInput" type="text" tabindex="3"
                   :class="toggleInvalidClass(businessAddressErrorMsg)"
                   :maxlength="config.businessAddress.maxLength" autocomplete="off">
            <div class="invalid-feedback">
              {{businessAddressErrorMsg}}
            </div>
          </div>
        </div>
        <div class="row my-lg-2">
          <div class="col-lg-6 my-2 my-lg-0">
            <label for="streetAddress">Street Address</label>
            <input tabindex="4" id="streetAddress" :class="toggleInvalidClass(businessStreetAddressErrorMsg)"
                   name="streetAddress" ref="streetAddress" required autocomplete="off">
            <div class="invalid-feedback">
              {{businessStreetAddressErrorMsg}}
            </div>
          </div>
          <div class="col-lg-6 my-2 my-lg-0">
            <label for="suburb">Suburb</label>
            <input :class="toggleInvalidClass(businessSuburbErrorMsg)" tabindex="5" name="suburb" id="suburb" ref="suburb"
                   autocomplete="off" required>
            <div class="invalid-feedback">
              {{businessSuburbErrorMsg}}
            </div>
          </div>
        </div>
        <div class="row my-lg-2">
          <div class="col-lg-6 my-2 my-lg-0">
            <label for="city">City</label>
            <input :class="toggleInvalidClass(businessCityErrorMsg)" tabindex="6" name="city" id="city" ref="city"
                   autocomplete="off">
            <div class="invalid-feedback">
              {{businessCityErrorMsg}}
            </div>
          </div>

          <div class="col-lg-6 my-2 my-lg-0">
            <label for="region">State/Region</label>
            <input :class="toggleInvalidClass(businessRegionErrorMsg)" tabindex="7" name="region" id="region" ref="region"
                   autocomplete="off" required>
            <div class="invalid-feedback">
              {{businessRegionErrorMsg}}
            </div>
          </div>
        </div>

        <div class="row my-lg-2">
          <div class="col-lg-6 my-2 my-lg-0">
            <label for="country">Country*</label>
            <input :class="toggleInvalidClass(businessCountryErrorMsg)" tabindex="8" id="country" name="country"
                   ref="country" autocomplete="off" required>
            <div class="invalid-feedback">
              {{businessCountryErrorMsg}}
            </div>
          </div>

          <div class="col-lg-6 my-2 my-lg-0">
            <label for="postcode">Postcode</label>
            <input :class="toggleInvalidClass(businessPostcodeErrorMsg)" tabindex="9" id="postcode" name="postcode"
                   ref="postcode" autocomplete="off" required>
            <div class="invalid-feedback">
              {{businessPostcodeErrorMsg}}
            </div>
          </div>
        </div>

        <div class="row my-lg-2">
          <div class="col my-2 my-lg-0">
            <label for="description">Description</label>
            <textarea id="description" name="description" tabindex="10" rows="5" cols="70" v-model="description"
                      :maxlength="config.description.maxLength" :class="toggleInvalidClass(descriptionErrorMsg)"
                      style="resize: none"/>
            <div class="invalid-feedback">
              {{descriptionErrorMsg}}
            </div>
          </div>
        </div>

          <div class="row my-lg-2">
            <div class="col-lg-12 mt-2 my-lg-0 mx-auto">
              <div id="registration-error" ref="registration-error" v-if="toastErrorMessage" class="alert alert-danger"
                   role="alert">
                <label>{{ toastErrorMessage }}</label>
              </div>
            </div>
          </div>

        </form>
      </div>
    </div>
    <Footer/>
  </div>
</template>

<script>
import {Business} from "../Api";
import Footer from "../components/Footer";

export default {
  name: "BusinessRegistration",
  components: {
    Footer,
  },

  data() {
    return {

      // Used for having pre-filled input fields
      DEBUG_MODE: false,

      // A copy of the user config file for error checking.
      config: Business.config,

      // Business name related variables
      businessName: "",
      businessNameErrorMsg: "",

      // Business type related variables
      businessType: "",
      types: [
        { bType: 'ACCOMMODATION AND FOOD SERVICES', value: 'Accommodation and Food Services' },
        { bType: 'RETAIL TRADE', value: 'Retail Trade' },
        { bType: 'CHARITABLE ORGANISATION', value: 'Charitable Organisation' },
        { bType: 'NON-PROFIT ORGANISATION', value: 'Non-Profit Organisation' }
      ],
      businessTypeErrorMsg: "",

      // Business address related variables
      businessAddressErrorMsg: "",

      // Business street address related variables
      businessStreetAddressErrorMsg: "",

      // Business suburb related variables
      businessSuburbErrorMsg: "",

      // Business city related variables
      businessCityErrorMsg: "",

      // Business State/ region related variables
      businessRegionErrorMsg: "",

      // Business Country related variables
      businessCountryErrorMsg: "",

      // Business Postcode related variables
      businessPostcodeErrorMsg: "",

      // Description related variables
      description: "",
      descriptionErrorMsg: "",

      // Toast related variables
      toastErrorMessage: "",
      cannotProceed: false,

      // Address autocompletion related variables
      address: "",
      addresses: [],
      autocompleteFocusIndex: 0,
      addressResultProperties: []
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
    trimBusinessTextInputFields() {
      this.businessName = this.businessName.trim();
      this.businessType = this.businessType.trim();
      this.description = this.description.trim();
      this.$refs.country.value = this.$refs.country.value.trim();
      this.$refs.city.value = this.$refs.city.value.trim();
      this.$refs.suburb.value = this.$refs.suburb.value.trim();
      this.$refs.region.value = this.$refs.region.value.trim();
      this.$refs.streetAddress.value = this.$refs.streetAddress.value.trim();
      this.$refs.postcode.value = this.$refs.postcode.value.trim();
    },

    /**
     * This method creates a new business.
     * @param e, the current event.
     */
    addNewBusiness(e) {
      // Steps required for the function before starting processing.
      e.preventDefault()  // prevents page from reloading
      this.trimBusinessTextInputFields()
      let requestIsInvalid = false

      // ===================================== START OF INPUT FIELDS VALIDATION ========================================

      // Business name error checking
      this.businesstNameErrorMsg = this.getErrorMessage(
          this.config.businessName.name,
          this.businessName,
          this.config.businessName.minLength,
          this.config.businessName.maxLength,
          this.config.businessName.regexMessage,
          this.config.businessName.regex
      )
      if (this.businessNameErrorMsg) {
        requestIsInvalid = true
      }

      // Description error checking
      this.descriptionErrorMsg = this.getErrorMessage(
          this.config.description.name,
          this.bio,
          this.config.description.minLength,
          this.config.description.maxLength,
      )
      if (this.descriptionErrorMsg) {
        requestIsInvalid = true
      }

      // Business Type error checking
      const businessTypes = [
        'ACCOMMODATION AND FOOD SERVICES',
        'RETAIL TRADE',
        'CHARITABLE ORGANISATION',
        'NON-PROFIT ORGANISATION']
      if (this.businessType in businessTypes) {
        this.businessTypeErrorMsg = "";
        requestIsInvalid = false
      } else {
        this.businessTypeErrorMsg = "This field is required!"
        requestIsInvalid = true
      }

      // Business address error checking
      this.businessAddressErrorMsg = this.getErrorMessage(
          this.config.businessAddress.name,
          this.$refs.businessAddressInput.value,
          this.config.businessAddress.minLength,
          this.config.businessAddress.maxLength
      )
      if (this.businessAddressErrorMsg) {
        requestIsInvalid = true
      }

      // Street address error checking
      this.businessStreetAddressErrorMsg = this.getErrorMessage(
          this.config.streetAddress.name,
          // Using v-model for this address input apparently does not update
          // when we insert from our autocomplete list so it has been changed to use $refs
          this.$refs.streetAddress.value,
          this.config.streetAddress.minLength,
          this.config.streetAddress.maxLength
      )
      if (this.businessStreetAddressErrorMsg) {
        requestIsInvalid = true
      }

      // Suburb error checking
      this.businessSuburbErrorMsg = this.getErrorMessage(
          this.config.suburb.name,
          this.$refs.suburb.value,
          this.config.suburb.minLength,
          this.config.suburb.maxLength
      )
      if (this.businessSuburbErrorMsg) {
        requestIsInvalid = true
      }

      // City error checking
      this.businessCityErrorMsg = this.getErrorMessage(
          this.config.city.name,
          this.$refs.city.value,
          this.config.city.minLength,
          this.config.city.maxLength
      )
      if (this.businessCityErrorMsg) {
        requestIsInvalid = true
      }

      // Region error checking
      this.businessRegionErrorMsg = this.getErrorMessage(
          this.config.region.name,
          this.$refs.region.value,
          this.config.region.minLength,
          this.config.region.maxLength
      )
      if (this.businessRegionErrorMsg) {
        requestIsInvalid = true
      }

      // Country error checking
      this.businessCountryErrorMsg = this.getErrorMessage(
          this.config.country.name,
          this.$refs.country.value,
          this.config.country.minLength,
          this.config.country.maxLength
      )
      if (this.businessCountryErrorMsg) {
        requestIsInvalid = true
      }

      // Postcode error checking
      this.businessPostcodeErrorMsg = this.getErrorMessage(
          this.config.postcode.name,
          this.$refs.postcode.value,
          this.config.postcode.minLength,
          this.config.postcode.maxLength
      )
      if (this.businessPostcodeErrorMsg) {
        requestIsInvalid = true
      }

      // ====================================== END OF INPUT FIELDS VALIDATION =========================================

      // If at any stage an error has been discovered we cancel the procedure
      if (requestIsInvalid) {
        return
      }
    }
  }
}
</script>

<style scoped>
form {
  display: flex;
  flex-direction: column;
  margin-left: auto;
  margin-right: auto;
  max-width: 950px;
  align-content: center;
  justify-content: center;
}

label {
  text-align: left;
  display: flex;
  flex-direction: column;
}

#register-form {
  border-radius: 2%;
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

input:focus, textarea:focus, #register-button:focus{
  outline: none;     /* oranges! yey */
  box-shadow: 0 0 2px 2px #1EBA8C; /* Full freedom. (works also with border-radius) */
  border: 1px solid #1EBABC;
}
/*------------------------------------------------------------------------*/

</style>