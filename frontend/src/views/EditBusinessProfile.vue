<template>
  <div>
    <div id="main">
      <div class="container">
        <!-- Currency change modal -->
        <CurrencyChangeModal ref="currencyChangeModal" :business-name="businessName" v-on:currencyChange="currencyChange"/>

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
                    <option v-for="option in types" :key="option.text" :value="option.text">
                      {{ option.text }}
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
                <button id="register-button" tabindex="12" class="btn btn-lg float-lg-end green-button" type="button" @click="processEdit($event)">Save Changes</button>
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
import Business, {BusinessTypes, convertToFrontendBusinessType} from "../configs/Business";
import AddressAPI from "../addressInstance";
import {getAddressConcatenation} from "../views/helpFunction";
import {getErrorMessage} from "../components/inventory/InventoryValidationHelper";
import Api from "../Api";
import CurrencyChangeModal from "../components/business/CurrencyChangeModal";
import {toggleInvalidClass, toggleInvalidSelectClass} from "../validationUtils";
import Cookies from "js-cookie";
import {UserRole} from "../configs/User";

export default {
  name: "EditBusinessProfile",
  components: {
    Footer,
    CurrencyChangeModal
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
        { text: 'Accommodation and Food Services' },
        { text: 'Retail Trade'},
        { text: 'Charitable Organisation'},
        { text: 'Non Profit Organisation'}
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

      // Address autocompletion related variables
      address: "",
      addresses: [],
      autocompleteFocusIndex: 0,
      addressResultProperties: [],

      // Address related variables
      // streetNumber: "",
      // streetName: "",
      // suburb: "",
      // city: "",
      // region: "",
      // postcode: "",
      // country: "",

      // Currency change details.
      currencyCode: "",
      currencySymbol: "",
      originalCountry: "",

      // Used to keep track of whether logged in user is a business or global admin.
      // true if admin, false otherwise. Initially not set.
      isAdministrator: false
    }
  },
  methods: {
    toggleInvalidClass: toggleInvalidClass,
    toggleInvalidSelectClass: toggleInvalidSelectClass,

    /**
     * This method returns the currently logged in administrator to the business profile of the business
     * they are currently acting as.
     */
    toBusinessProfile() {
      const id = this.$route.params.id;
      this.$router.push({name: "BusinessProfile", params: {id}});
    },

    /**
     * Address API requests.
     * An asynchronous function that calls the Komoot Photon API with the given address input.
     * Upon success, the filterResponse function is called with the response data.
     */
    async request() {
      let input = document.getElementById('business-address').value;
      if (input.length > 4) { // Starts on 5th char
        await AddressAPI.addressQuery(input).then((response) => {
          this.addresses = this.filterResponse(response.data);
        }).catch((error) => console.log(error))
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
     * @param element Element An optional element that won't be closed if given
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
    },

    /**
     * Retrieves the business info for the current url and autofill the form with existing business data.
     * @param id Id of business to retrieve from backend.
     */
    async retrieveBusiness(id) {
      await Api.getBusiness(id).then((res) => {
        // currency change information
        this.originalCountry = res.data.address.country;
        this.currencyCode = res.data.currencyCode;
        this.currencySymbol = res.data.currencySymbol;
        // autofill form
        this.autoFillBusinessData(res.data);
      }).catch((err) => {
        if (err.response) {
          if (err.response.status === 406) {
            this.$router.push({name: "NoBusiness"});
          } else if (err.response.status === 401) {
            this.$router.push({name: "InvalidToken"});
          } else {
            console.log(err.response);
          }
        } else {
          console.log(err);
        }
      })
    },

    /**
     * Sets business fields if they exist (this is done to prevent setting inputs as undefined)
     * @param business an object containing all data for current business.
     */
    autoFillBusinessData(business) {
      if (business.name !== null) { this.businessName = business.name; }
      if (business.businessType !== null) { this.businessType = convertToFrontendBusinessType(business.businessType); }
      if (business.description !== null) { this.description = business.description; }
      if (business.address.streetNumber !== null) { this.$refs.streetNumber.value = business.address.streetNumber; }
      if (business.address.streetName !== null) { this.$refs.streetName.value = business.address.streetName; }
      if (business.address.suburb !== null) { this.$refs.suburb.value = business.address.suburb; }
      if (business.address.city !== null) { this.$refs.city.value = business.address.city; }
      if (business.address.region !== null) { this.$refs.region.value = business.address.region; }
      if (business.address.postcode !== null) { this.$refs.postcode.value = business.address.postcode; }
      if (business.address.country !== null) { this.$refs.country.value = business.address.country; }
    },

    /**
     * This method checks the validation of all input fields. If a user has changed the country for a business
     * then the currency changes are processed (using helper methods). Once all fields are valid then a
     * PUT request will be made to the backend using another helper method.
     *
     * @param e, the current event.
     */
    processEdit(e) {
      // prevents page from reloading
      e.preventDefault();
      // remove whitespace from input fields.
      this.trimTextInputFields();
      // get error messages if input is invalid.
      this.getErrorMessages();
      // if an error message exists then return.
      if (this.checkInvalidRequest()) { return; }
      // if there has been a country change for the business, then the user may wish to update currency information.
      // currency change options are performed in a modal so we need to return.
      if (this.checkCurrencyChange()) { return; }
      // make PUT call to backend.
      this.editBusiness();
    },

    /**
     * This method removes white space from the beginning and end of all the input field's input values.
     */
    trimTextInputFields () {
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
     * This method gets the inputs from the the form and validates them based on the criteria in the Business config file.
     * If a field is not valid then an error message is set in the modal.
     */
    getErrorMessages() {
      // Business type error checking.
      if (BusinessTypes.includes(this.businessType)) {
        this.businessTypeErrorMsg = "";
      } else {
        this.businessTypeErrorMsg = "This field is required!";
      }
      // Business name error checking
      this.businessNameErrorMsg = getErrorMessage(
          this.config.businessName.name,
          this.businessName,
          this.config.businessName.minLength,
          this.config.businessName.maxLength,
          this.config.businessName.regexMessage,
          this.config.businessName.regex
      );
      // Description error checking
      this.descriptionErrorMsg = getErrorMessage(
          this.config.description.name,
          this.description,
          this.config.description.minLength,
          this.config.description.maxLength,
      );
      // Business address error checking
      this.businessAddressErrorMsg = getErrorMessage(
          this.config.businessAddress.name,
          this.$refs.businessAddressInput.value,
          this.config.businessAddress.minLength,
          this.config.businessAddress.maxLength
      );
      // Street number error checking
      this.businessStreetNumberErrorMsg = getErrorMessage(
          this.config.streetNumber.name,
          // Using v-model for this address input apparently does not update
          // when we insert from our autocomplete list so it has been changed to use $refs
          this.$refs.streetNumber.value,
          this.config.streetNumber.minLength,
          this.config.streetNumber.maxLength
      );
      // Street name error checking
      this.businessStreetNameErrorMsg = getErrorMessage(
          this.config.streetName.name,
          // Using v-model for this address input apparently does not update
          // when we insert from our autocomplete list so it has been changed to use $refs
          this.$refs.streetName.value,
          this.config.streetName.minLength,
          this.config.streetName.maxLength
      );
      // Suburb error checking
      this.businessSuburbErrorMsg = getErrorMessage(
          this.config.suburb.name,
          this.$refs.suburb.value,
          this.config.suburb.minLength,
          this.config.suburb.maxLength
      );
      // Postcode error checking
      this.businessPostcodeErrorMsg = getErrorMessage(
          this.config.postcode.name,
          this.$refs.postcode.value,
          this.config.postcode.minLength,
          this.config.postcode.maxLength
      );
      // City error checking
      this.businessCityErrorMsg = getErrorMessage(
          this.config.city.name,
          this.$refs.city.value,
          this.config.city.minLength,
          this.config.city.maxLength
      );
      // Region error checking
      this.businessRegionErrorMsg = getErrorMessage(
          this.config.region.name,
          this.$refs.region.value,
          this.config.region.minLength,
          this.config.region.maxLength
      );
      // Country error checking
      this.businessCountryErrorMsg = getErrorMessage(
          this.config.country.name,
          this.$refs.country.value,
          this.config.country.minLength,
          this.config.country.maxLength
      );
    },

    /**
     * If an error message exists then the input fields have not been correctly filled out and changes
     * must not be made to the business.
     * @return boolean true if changes are invalid, false otherwise.
     */
    checkInvalidRequest() {
      return (
          this.businessTypeErrorMsg || this.businessNameErrorMsg || this.descriptionErrorMsg ||
          this.businessAddressErrorMsg || this.businessStreetNumberErrorMsg || this.businessStreetNameErrorMsg ||
          this.businessSuburbErrorMsg || this.businessPostcodeErrorMsg || this.businessCityErrorMsg ||
          this.businessRegionErrorMsg || this.businessCountryErrorMsg
      );
    },

    /**
     * When a user edits the country for a business, they will need to be notified about the currency change.
     * This is done by opening a modal with currency change information.
     * @return boolean false if there has not been a country change, true if country has been changed.
     */
    checkCurrencyChange() {
      // if the user has not changed the country for the business then the user does not have to be notified about
      // currency change.
      if (this.originalCountry === this.$refs.country.value) { return false; }
      // user needs to be notified about currency change using modal.
      this.$refs.currencyChangeModal.showModal(event);
      return true;
    },

    /**
     * This method is called when a custom event "currencyChange" is emitted from the CurrencyChangeModal.
     * The currency code and symbol are set to updated values if the user decided to update them.
     * Once the currency changes have been processed then a call can be made to the backend to update the business.
     *
     * @param code null if user decided not to update, otherwise it is the currency code matching currency of new country.
     * @param symbol null if user decided not to update, otherwise it is the currency symbol matching currency of new country.
     */
    currencyChange(code, symbol) {
      // only change currency code and symbol if they have been updated using modal (i.e. they are not null)
      if (code && symbol) {
        this.currencyCode = code;
        this.currencySymbol = symbol;
      }
      // update business since currency changes have now been processed.
      this.editBusiness();
    },

    /**
     * Sends an EditBusiness object to the backend to update the user's business
     *
     * PRECONDITIONS:
     *     1. all fields are validated
     */
    async editBusiness() {

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
        primaryAdministratorId: null,
        name: this.businessName,
        description: this.description,
        // NOTE: Using v-model for this address input apparently does not update.
        // When we insert from our autocomplete list so it has been changed to use $refs
        address: addressData,
        businessType: this.businessType,
        currencySymbol: this.currencySymbol,
        currencyCode: this.currencyCode
      }

      const id = this.$route.params.id;

      // Add the Business to the database by sending an API request to the backend to store the business' information.
      // Raise any errors and ensure they are displayed on the UI.
      await Api.editBusiness(id, new Business(businessData)).then( (res) => {
        if (res.status === 200) {
          if (id) {
            this.$router.push('/businessProfile/' + id);
          }
        }
      }).catch((error) => {
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

    /**
     * This method retrieves the details of the currently logged in user. If they are a business or global administrator
     * then they are able to edit the current business.
     *
     * @param userId the id of the currently logged in user.
     * @param businessId the id of the current business.
     * @return {Promise<void>} a promise containing the results of retrieving the logged in user details from the backend.
     */
    async checkIsAdmin(userId, businessId) {
      await Api.getUser(userId).then(response => {
        response.data.businessesAdministered.forEach(business => {
          if (business.id.toString() === businessId) { this.isAdministrator = true; }
        });
        this.isAdministrator = this.isAdministrator ? true :
            (response.data.role === UserRole.DEFAULTGLOBALAPPLICATIONADMIN || response.data.role === UserRole.GLOBALAPPLICATIONADMIN);
        // if user is not acting as a business, or acting for another business
        const actAs = Cookies.get('actAs');
        if (actAs !== undefined && businessId !== actAs) { this.isAdministrator = false; }
      }).catch((error) => {
        if (error.request && !error.response) {
          this.$router.push({path: '/timeout'});
        } else if (error.response !== undefined && error.response.status === 406) {
            this.$router.push({path: '/noUser'});
        } else if (error.response !== undefined && error.response.status === 401) {
            this.$router.push({path: '/invalidtoken'});
        } else {
          this.$router.push({path: '/noUser'});
          console.log(error.message);
        }
      })
    },
  },
  async mounted() {
    // check currently logged in user is a business or global administrator.
    // if not an administrator then redirect them to forbidden page.
    // if an administrator then autofill business data (continue loading page).
    const userId = Cookies.get("userID");
    const businessId = this.$route.params.id;
    await this.checkIsAdmin(userId, businessId);
    if (this.isAdministrator) {
      await this.retrieveBusiness(businessId);
    } else {
      await this.$router.push({name: "Forbidden"});
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
