<template>
  <div>
    <div id="main">
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
                <h3 class="display-6 m-2 mb-5 text-center">Edit Business Profile</h3>
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
                  <select id="business-type" name="business-type" tabindex="2" :class="toggleInvalidSelectClass(businessTypeErrorMsg)"
                          v-model="businessType" required>
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
                         @input="input()" @keydown="addressKeyDown($event)" :class="toggleInvalidClass(businessAddressErrorMsg)"
                         :maxlength="config.businessAddress.maxLength" autocomplete="off">
                  <div class="invalid-feedback">
                    {{businessAddressErrorMsg}}
                  </div>
                </div>
              </div>
              <div class="row my-lg-2">
                <div class="col my-2 my-lg-0">
                  <label for="streetNumber">Street Number</label>
                  <input :class="toggleInvalidClass(businessStreetNumberErrorMsg)" tabindex="4" id="streetNumber"
                         name="streetNumber" ref="streetNumber" autocomplete="off">
                  <div class="invalid-feedback">
                    {{businessStreetNumberErrorMsg}}
                  </div>
                </div>

                <div class="col my-2 my-lg-0">
                  <label for="streetName">Street Name</label>
                  <input :class="toggleInvalidClass(businessStreetNameErrorMsg)" tabindex="5" id="streetName"
                         name="streetName" ref="streetName" autocomplete="off">
                  <div class="invalid-feedback">
                    {{businessStreetNameErrorMsg}}
                  </div>
                </div>
              </div>

              <div class="row my-lg-2">
                <div class="col-lg-6 my-2 my-lg-0">
                  <label for="suburb">Suburb</label>
                  <input :class="toggleInvalidClass(businessSuburbErrorMsg)" tabindex="6" name="suburb" id="suburb"
                         ref="suburb" autocomplete="off">
                  <div class="invalid-feedback">
                    {{businessSuburbErrorMsg}}
                  </div>
                </div>

                <div class="col-lg-6 my-2 my-lg-0">
                  <label for="city">City</label>
                  <input :class="toggleInvalidClass(businessCityErrorMsg)" tabindex="7" name="city" id="city" ref="city"
                         autocomplete="off">
                  <div class="invalid-feedback">
                    {{businessCityErrorMsg}}
                  </div>
                </div>
              </div>

              <div class="row my-lg-2">
                <div class="col-lg-6 my-2 my-lg-0">
                  <label for="region">State/Region</label>
                  <input :class="toggleInvalidClass(businessRegionErrorMsg)" tabindex="8" name="region" id="region" ref="region"
                         autocomplete="off">
                  <div class="invalid-feedback">
                    {{businessRegionErrorMsg}}
                  </div>
                </div>

                <div class="col-lg-6 my-2 my-lg-0">
                  <label for="postcode">Postcode</label>
                  <input :class="toggleInvalidClass(businessPostcodeErrorMsg)" tabindex="9" id="postcode" name="postcode"
                         ref="postcode" autocomplete="off">
                  <div class="invalid-feedback">
                    {{businessPostcodeErrorMsg}}
                  </div>
                </div>
              </div>

              <div class="row my-lg-2">
                <div class="col my-2 my-lg-0">
                  <label for="country">Country*</label>
                  <input :class="toggleInvalidClass(businessCountryErrorMsg)" tabindex="10" id="country" name="country"
                         ref="country" autocomplete="off" required>
                  <div class="invalid-feedback">
                    {{businessCountryErrorMsg}}
                  </div>
                </div>
              </div>

              <div class="row my-lg-2">
                <div class="col my-2 my-lg-0">
                  <label for="description">Description</label>
                  <textarea id="description" name="description" tabindex="11" rows="5" cols="70" v-model="description"
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

              <div class="d-grid gap-2 d-lg-block">
                <button class="btn btn-lg btn-outline-primary green-button-transparent" type="button" tabindex="13" @click="toBusinessProfile()">Cancel Changes</button>
                <button id="register-button" tabindex="12" class="btn btn-lg float-lg-end green-button" type="button" @click="editBusiness($event)">Save Changes</button>
              </div>

            </form>
          </div>
        </div>
      </div>
    </div>


    <Footer></Footer>
  </div>
</template>

<script>
import Footer from "../components/main/Footer";
import Business from "../configs/Business";
import AddressAPI from "../addressInstance";
import {getAddressConcatenation} from "../views/helpFunction";

export default {
  name: "EditBusinessProfile",
  components: {
    Footer,
  },
  data() {
    return {
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
        { bType: 'NON PROFIT ORGANISATION', value: 'Non Profit Organisation' }
      ],
      businessTypeErrorMsg: "",

      // Business address related variables
      businessAddressErrorMsg: "",

      // Business street number related variables
      businessStreetNumberErrorMsg: "",

      // Business street name related variables
      businessStreetNameErrorMsg: "",

      // Business suburb related variables
      businessSuburbErrorMsg: "",

      // Business city related variables
      businessCityErrorMsg: "",

      // Business Postcode related variables
      businessPostcodeErrorMsg: "",

      // Business State/ region related variables
      businessRegionErrorMsg: "",

      // Business Country related variables
      businessCountryErrorMsg: "",

      // Description related variables
      description: "",
      descriptionErrorMsg: "",

      currencyCode: "",
      currencySymbol: "",

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
     * This method toggles the appearance of the error message for select boxes, where the is-invalid
     * class is added to the messages if an error message needs to be presented to the user.
     *
     * @param errorMessage, string, the error message relating to invalid input of a field.
     * @returns {[string]}, classList, a list containing the classes for an invalid message.
     */
    toggleInvalidSelectClass(errorMessage) {
      let classList = ['form-select']
      if (errorMessage) {
        classList.push('is-invalid')
      }
      return classList
    },

    /**
     * This method returns the currently logged in administrator to the business profile of the business
     * they are currently acting as.
     */
    toBusinessProfile() {
      const id = this.$route.params.id
      this.$router.push({name: "BusinessProfile", params: {id}})
    },

    /**
     * Address API requests.
     * An asynchronous function that calls the Komoot Photon API with the given address input.
     * Upon success, the filterResponse function is called with the response data.
     */
    async request() {
      let input = document.getElementById('home-address').value;
      if (input.length > 4) { // Starts on 5th char
        await AddressAPI.addressQuery(input).then((response) => {
          this.addresses = this.filterResponse(response.data);
        })
            .catch((error) => console.log(error))
      } else {
        this.addresses = [];
      }
    },

    /**
     * Filters the response data from the Komoot API by extracting the relevant fields and storing them
     * both as a string to be shown in the autocomplete dropdown box, and unchanged in the addressResultProperties
     * variable to allow for the individual parts of the address to be entered into the correct fields
     * when a user clicks on an autocomplete option.
     * @param data The request result from sent back by the Komoot Photon API
     * @returns {array} A list of addresses to suggest to the user
     */
    filterResponse (data) {
      let {features} = data;
      let autoCompleteOptions = [];
      let index = 0;
      let numInList = 0;
      let fLength = features.length;
      // Display the first 8 options returned
      let maxL = 8;
      // Clear the list after each request (before filtering)
      this.addressResultProperties = [];

      while ((numInList < maxL) && (index < fLength)) {
        let { properties } = features[index];
        if (properties) {
          let address = getAddressConcatenation(properties);
          if (!autoCompleteOptions.includes(address.trim())) {
            // Add to both the string to display and the variable for later use.
            autoCompleteOptions.push(address.trim());
            this.addressResultProperties.push(properties);
            numInList++;
          }
        }
        index++;
      }
      return autoCompleteOptions;
    },
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
</style>