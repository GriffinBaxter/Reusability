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
            <button class="btn btn-lg btn-outline-primary green-button-transparent" type="button" tabindex="13" @click="$router.push('/profile')">Back to Profile</button>
            <button id="register-button" tabindex="12" class="btn btn-lg float-lg-end green-button" type="button" @click="addNewBusiness($event)">Register</button>
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
import Api from "../Api";
import Business from "../configs/Business"
import Footer from "../components/main/Footer";
import AddressAPI from "../addressInstance";
import Cookies from 'js-cookie';

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
    trimBusinessTextInputFields() {
      this.businessName = this.businessName.trim();
      this.businessType = this.businessType.trim();
      this.description = this.description.trim();
      this.$refs.country.value = this.$refs.country.value.trim();
      this.$refs.city.value = this.$refs.city.value.trim();
      this.$refs.postcode.value = this.$refs.postcode.value.trim();
      this.$refs.region.value = this.$refs.region.value.trim();
      this.$refs.streetNumber.value = this.$refs.streetNumber.value.trim();
      this.$refs.streetName.value = this.$refs.streetName.value.trim();
      this.$refs.suburb.value = this.$refs.suburb.value.trim();
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

      // Business Type error checking
      const businessTypes = [
        'ACCOMMODATION AND FOOD SERVICES',
        'RETAIL TRADE',
        'CHARITABLE ORGANISATION',
        'NON PROFIT ORGANISATION']
      if (businessTypes.includes(this.businessType.toUpperCase())) {
        this.businessTypeErrorMsg = "";
      } else {
        this.businessTypeErrorMsg = "This field is required!"
        requestIsInvalid = true
      }

      // Business name error checking
      this.businessNameErrorMsg = this.getErrorMessage(
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
          this.description,
          this.config.description.minLength,
          this.config.description.maxLength,
      )
      if (this.descriptionErrorMsg) {
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

      // Street number error checking
      this.businessStreetNumberErrorMsg = this.getErrorMessage(
          this.config.streetNumber.name,
          // Using v-model for this address input apparently does not update
          // when we insert from our autocomplete list so it has been changed to use $refs
          this.$refs.streetNumber.value,
          this.config.streetNumber.minLength,
          this.config.streetNumber.maxLength
      )
      if (this.businessStreetNumberErrorMsg) {
        requestIsInvalid = true
      }

      // Street name error checking
      this.businessStreetNameErrorMsg = this.getErrorMessage(
          this.config.streetName.name,
          // Using v-model for this address input apparently does not update
          // when we insert from our autocomplete list so it has been changed to use $refs
          this.$refs.streetName.value,
          this.config.streetName.minLength,
          this.config.streetName.maxLength
      )
      if (this.businessStreetNameErrorMsg) {
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

      // ====================================== END OF INPUT FIELDS VALIDATION =========================================

      // If at any stage an error has been discovered we cancel the procedure
      if (requestIsInvalid) {
        return
      }

      const addressData = {
        streetNumber: this.$refs.streetNumber.value,
        streetName: this.$refs.streetName.value,
        suburb: this.$refs.suburb.value,
        city: this.$refs.city.value,
        region: this.$refs.region.value,
        country: this.$refs.country.value,
        postcode: this.$refs.postcode.value
      }

      // Wrapping up the business submitted fields into a class object (Business).
      const businessData = {
        primaryAdministratorId: Cookies.get('userID'),
        name: this.businessName,
        description: this.description,
        /*
         * NOTE: Using v-model for this address input apparently does not update.
         *       When we insert from our autocomplete list so it has been changed to use $refs
         */
        address: addressData,
        businessType: this.businessType
      }

      const business = new Business(businessData)
      /*
       * Add the Business to the database by sending an API request to the backend to store the business' information.
       * Raise any errors and ensure they are displayed on the UI.
       */
      Api.addNewBusiness(business
      ).then( (res) => {
            if (res.status === 201) {
              const businessId = res.data.businessId;
              if (businessId) {
                Cookies.set('actAs', businessId, { sameSite:'strict'});
                this.$router.push('/businessProfile/' + businessId);
              }
            }
          }
      ).catch((error) => {
        this.cannotProceed = true;
        if (error.response) {
          if (error.response.status === 400) {
            this.toastErrorMessage = '400 Bad request; invalid business data';
          } else if (error.response.status === 409) {
            this.businessNameErrorMsg = 'Business with name already exists';
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

    /*
     * Address API requests
     */
    async request() {
      /*
      An asynchronous function that calls the Komoot Photon API with the given address input.
      Upon success, the filterResponse function is called with the response data.
      */
      let input = document.getElementById('business-address').value;
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
          let address = this.getAddressConcatenation(properties);
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

    /**
     * This method converts the components of the address received from the Komoot Photon API
     * to a single line string.
     * @return address a string representation of the address returned by the Komoot Photon API
     */
    getAddressConcatenation(properties) {
      let address = "";

      let {country, city, postcode, state, street, housenumber, name, district} = properties;

      if (name) { address += name + ", "; }
      if (housenumber) { address += housenumber; }
      if (street) { address += " " + street + ", "; }
      if (district) { address += " " + district + ", "; }
      if (city) { address += city + ", "; }
      if (postcode) { address += postcode + ", "; }
      if (state) { address += state + ", "; }
      if (country) { address += country; }

      return address;
    },

    /**
     * This function is based on the example code snippet found on w3schools for a simple autocomplete dropdown menu:
     * https://www.w3schools.com/howto/howto_js_autocomplete.asp
     *
     * An asynchronous function that is called whenever the user enters a character into the address autocomplete
     * input. It first calls the request function and awaits the response. Then it loops over the filtered result,
     * creating the custom dropdown menu using each address string in the addresses array.
     *
     * It also adds a 'click' event listener to each of the address divs in the dropdown list that enters each part
     * of the address into the correct input on the page. (Using the addressResultProperties array)
     *
     * @returns {Promise<boolean>} Async implied promise
     */
    async input() {
      // Populate the addresses array by making a request to the API
      await this.request();
      // Get the current address input
      let inputValue = this.$refs.businessAddressInput.value;

      const self = this;
      // Close any already open lists of autocompleted values
      this.closeAllLists();
      if (!inputValue) { return false;}
      this.autocompleteFocusIndex = -1;
      // Create a outer DIV element that will contain the items from the request
      const outerDiv = document.createElement("div");
      outerDiv.style.width = this.$refs.businessAddressInput.getBoundingClientRect().width.toString() + 'px';
      outerDiv.setAttribute("id", this.$refs.businessAddressInput.id + "autocomplete-list");
      outerDiv.setAttribute("class", "autocomplete-items");
      // Append the DIV element as a child of the autocomplete container
      this.$refs.businessAddressInput.parentNode.appendChild(outerDiv);

      for (let i = 0; i < this.addresses.length; i++) {
        // Check if the input contains one of the return addresses exactly and whether the current address is empty
        if (!this.addresses.includes(inputValue) && this.addresses[i] !== '') {
          // Create an inner DIV element to hold the address
          let innerDiv = document.createElement("div");
          innerDiv.innerHTML += this.addresses[i];
          innerDiv.id= i.toString();

          // Insert the value into the input when the user clicks on an item
          innerDiv.addEventListener("click", function(event) {
            // Insert the value for the autocomplete text field
            document.getElementById('business-address').value = "";
            const id = event.target.id;

            self.setAddressElementsById(self.addressResultProperties[id]);

            // Close the list of autocompleted values,
            // (or any other open lists of autocompleted values:
            self.closeAllLists();
          });

          outerDiv.appendChild(innerDiv);
        }
      }
      // Close all lists when the user clicks somewhere else on the document
      document.addEventListener("click", function (event) {
        self.closeAllLists(event.target);
      });

    },

    /**
     * This methods sets the values of the address related fields.
     */
    setAddressElementsById(addressComponents) {
      let {country, city, postcode, state, street, housenumber, district} = addressComponents;

      if (housenumber) { document.getElementById('streetNumber').value = housenumber; }
      if (street) { document.getElementById('streetName').value = street; }
      if (district) { document.getElementById('suburb').value = district; }
      if (city) { document.getElementById('city').value = city; }
      if (postcode) { document.getElementById('postcode').value = postcode; }
      if (state) { document.getElementById('region').value = state; }
      if (country) { document.getElementById('country').value = country; }
    },

    /**
     * This function is based on the example code snippet found on w3schools for a simple autocomplete dropdown menu:
     * https://www.w3schools.com/howto/howto_js_autocomplete.asp
     *
     * This function removes all of the autocomplete dropdown items except the one passed to it.
     * @param DOM Element An optional element that won't be closed if given
     */
    closeAllLists(element) {
      // Close all autocomplete lists in the document, except the one passed as an argument
      let itemElements = document.getElementsByClassName("autocomplete-items");
      for (let i = 0; i < itemElements.length; i++) {
        if (element !== itemElements[i] && element !== this.$refs.businessAddressInput) {
          itemElements[i].parentNode.removeChild(itemElements[i]);
        }
      }
    },


    /**
     * This function is based on the example code snippet found on w3schools for a simple autocomplete dropdown menu:
     * https://www.w3schools.com/howto/howto_js_autocomplete.asp
     *
     * This function is an event listener for key-presses to allow for navigation of the dropdown box by keyboard.
     *
     * @param event The keydown event
     */
    addressKeyDown(event) {

      let elementList = document.getElementById(this.$refs.businessAddressInput.id + "autocomplete-list");
      if (elementList) elementList = elementList.getElementsByTagName("div");
      if (event.keyCode === 40) {
        // If the arrow DOWN key is pressed, increase the autocompleteFocusIndex variable
        this.autocompleteFocusIndex++;
        // and mark the new item as active
        this.addActive(elementList);
      } else if (event.keyCode === 38) {
        // If the arrow UP key is pressed, decrease the autocompleteFocus variable
        this.autocompleteFocusIndex--;
        // and mark the new item as active
        this.addActive(elementList);
      } else if (event.keyCode === 13) {
        // If the ENTER key is pressed, prevent the form from being submitted
        event.preventDefault();
        if (this.autocompleteFocusIndex > -1) {
          // and simulate a click on the active item (to insert it into the input)
          if (elementList) elementList[this.autocompleteFocusIndex].click();
        }
      }
    },


    /**
     * This function is based on the example code snippet found on w3schools for a simple autocomplete dropdown menu:
     * https://www.w3schools.com/howto/howto_js_autocomplete.asp
     *
     * This function marks the currently active item as active with the appropriate CSS.
     *
     */
    addActive(elementList) {
      // A function to mark an item as active with CSS.
      if (!elementList) return false;
      // Start by removing the "active" class on all items
      this.removeActive(elementList);
      if (this.autocompleteFocusIndex >= elementList.length) this.autocompleteFocusIndex = 0;
      if (this.autocompleteFocusIndex < 0) this.autocompleteFocusIndex = (elementList.length - 1);
      // Add class "autocomplete-active" to the given item
      elementList[this.autocompleteFocusIndex].classList.add("autocomplete-active");
    },


    /**
     * This function is based on the example code snippet found on w3schools for a simple autocomplete dropdown menu:
     * https://www.w3schools.com/howto/howto_js_autocomplete.asp
     *
     * This function removes the 'autocomplete-active' CSS class from all items in the given element list.
     *
     * @param elementList A list of elements to remove.
     */
    removeActive(elementList) {
      // A function to remove the "active" class from all autocomplete items
      for (let i = 0; i < elementList.length; i++) {
        elementList[i].classList.remove("autocomplete-active");
      }
    }
  },

  /**
   * When mounted, initiate population of page.
   * If cookies are invalid or not present, redirect to login page.
   */
  mounted() {
    const currentID = Cookies.get('userID');
    if (!currentID) {
      this.$router.push({name: 'Login'});
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
</style>
