<!--This file creates the Edit Profile page.-->
<!--It contains the container displaying the mandatory and optional fields that a person must submit to edit an
    account with the app, provided in the form.-->
<!--Bootstrap has been used for creating and styling the elements.-->
<!--It is currently fully responsive.-->

<template>
  <div>
    <div id="main">
      <!--body excluding footer-->
      <div class="container all-but-footer text-font">

        <!--logo without text, centred above form-->
        <div class="row justify-content-center">
          <div class="col-3 m-3">
            <img id="logo" src="../../public/logo_only_med.png" class="img-fluid" alt="logo">
          </div>
        </div>

        <div class="col-lg-8 mx-auto">

          <!-- Edit Profile form's container-->
          <div class="container shadow py-5 px-4 bg-white mb-5" id="edit-profile-form">
            <div class="row">
              <div class="col">
                <h3 class="display-6 m-2 mb-5 text-center">Edit Profile</h3>
              </div>
            </div>

            <!-- Edit Profile form, needs validation-->
            <form id="editProfile" class="needs-validation mb-3 px-5" novalidate @submit.prevent>

              <!--first row of form-->
              <div class="row my-lg-2">

                <!--first name input field-->
                <div class="col-lg-4 my-2 my-lg-0">
                  <label for="first-name">First Name*</label>
                  <input id="first-name" name="first-name" tabindex="1" type="text" v-model="firstName"
                         :class="toggleInvalidClass(firstNameErrorMsg)" :maxlength="config.firstName.maxLength"
                         required> <!-- Add max length attached to the max length in the config-->
                  <div class="invalid-feedback">
                    {{firstNameErrorMsg}}
                  </div>
                </div>

                <!--middle name input field-->
                <div class="col-lg-4 my-2 my-lg-0">
                  <label for="middle-name">Middle Name</label>
                  <input id="middle-name" name="middle-name" tabindex="2" type="text" v-model="middleName"
                         :class="toggleInvalidClass(middleNameErrorMsg)" :maxlength="config.middleName.maxLength">
                  <div class="invalid-feedback">
                    {{middleNameErrorMsg}}
                  </div>
                </div>

                <!--last name input field-->
                <div class="col-lg-4 my-2 my-lg-0">
                  <label for="last-name">Last Name*</label>
                  <input id="last-name" name="last-name" tabindex="3" type="text" v-model="lastName"
                         :class="toggleInvalidClass(lastNameErrorMsg)" :maxlength="config.lastName.maxLength" required>
                  <div class="invalid-feedback">
                    {{lastNameErrorMsg}}
                  </div>
                </div>

              </div>

              <!--second row of form-->
              <div class="row my-lg-2">

                <!--date of birth input field-->
                <div class="col-lg-4 my-2 my-lg-0">
                  <label for="date-of-birth">Date of Birth*</label>
                  <input id="date-of-birth" name="date-of-birth" tabindex="4" type="date" v-model="dateOfBirth"
                         :max="getMaxDateOfBirth()" :class="toggleInvalidClass(dateOfBirthErrorMsg)" required>
                  <div class="invalid-feedback">
                    {{dateOfBirthErrorMsg}}
                  </div>
                </div>

                <!--nickname input field-->
                <div class="col-lg my-2 my-lg-0">
                  <label for="nickname">Nickname</label>
                  <input id="nickname" name="nickname" type="text" tabindex="5" v-model="nickname"
                         :class="toggleInvalidClass(nicknameErrorMsg)" :maxlength="config.nickname.maxLength">
                  <div class="invalid-feedback">
                    {{nicknameErrorMsg}}
                  </div>
                </div>

              </div>

              <!--third row of form-->
              <div class="row my-lg-2 ">

                <!--email input field-->
                <div class="col my-2 my-lg-0">
                  <label for="email">Email*</label>
                  <input id="email" name="email" type="email" tabindex="6" v-model="email"
                         :class="toggleInvalidClass(emailErrorMsg)" :maxlength="config.email.maxLength" required>
                  <div class="invalid-feedback">
                    {{emailErrorMsg}}
                  </div>
                </div>

              </div>

              <!--fourth row of form-->
              <div class="row my-lg-2">

                <!--password validation information-->
                <div class="col my-2 my-lg-0">
                  <h6>Password must meet the following conditions:</h6>
                  <ul class="m-0">
                    <li :class="checkPasswordCriteria(password, config.password.regexContainLowerCase)">
                      One lowercase letter</li>
                    <li :class="checkPasswordCriteria(password, config.password.regexContainUpperCase)">
                      One uppercase letter</li>
                    <li :class="checkPasswordCriteria(password, config.password.regexContainNumber)">
                      One number</li>
                    <li :class="checkPasswordCriteria(password, config.password.regexContainSymbol)">
                      One of the following: !, @, #, $, %, ^, & and or*</li>
                    <li :class="checkPasswordCriteria(password, config.password.regexContainLength)">
                      At least 8 characters in length</li>
                  </ul>
                </div>

              </div>

              <!--fifth row of form-->
              <div class="row my-lg-2">

                <!--current password input field-->
                <div class="col my-2 my-lg-0">
                  <label for="current-password">Current Password (Only required when changing password)</label>
                  <input id="current-password" name="password" tabindex="7"
                         v-model="currentPassword" :class="toggleInvalidClass(currentPasswordErrorMsg)"
                         :maxlength="config.password.maxLength" required>
                  <div class="invalid-feedback">
                    {{currentPasswordErrorMsg}}
                  </div>
                </div>

              </div>


              <!--sixth row of form-->
              <div class="row my-lg-2">

                <!--password input field-->
                <div class="col my-2 my-lg-0">
                  <label for="password">Password</label>
                  <div class="input-group">
                    <input id="password" name="password" tabindex="8" :type="togglePasswordInputType(showPassword)" v-model="password" v-on:focus="passwordWasTyped = true" :class="toggleInvalidClass(passwordErrorMsg)">

                    <!--toggle password visibility-->
                    <span class="input-group-text green-search-button" @click="showPassword = !showPassword"
                          @keydown=" (event) => { if (event.keyCode === 13) this.showPassword = !showPassword}"
                          tabindex="8">
                    <i v-if="!showPassword" class="fas fa-eye" aria-hidden="true"></i>
                    <i v-else class="fas fa-eye-slash" aria-hidden="true"></i>
                    </span>

                    <div class="invalid-feedback">
                      {{passwordErrorMsg}}
                    </div>

                  </div>
                </div>

              </div>

              <!--seventh row of form-->
              <div class="row my-lg-2">

                <!--confirm password input field-->
                <div class="col my-2 my-lg-0">
                  <label for="confirm-password">Confirm Password</label>
                  <input id="confirm-password" name="password" tabindex="9" :type="togglePasswordInputType(showPassword)"
                         v-model="confirmPassword" :class="toggleInvalidClass(confirmPasswordErrorMsg)"
                         :maxlength="config.password.maxLength" required>
                  <div class="invalid-feedback">
                    {{confirmPasswordErrorMsg}}
                  </div>
                </div>

              </div>

              <!--eighth row of form-->
              <div class="row my-lg-2">

                <!--phone number input field-->
                <div class="col-lg-4 my-2 my-lg-0">
                  <label for="phone-number">Phone Number</label>
                  <input id="phone-number" name="phone-number" tabindex="10" type="text" placeholder="+64 123 132 132" v-model="phoneNumber"
                         :class="toggleInvalidClass(phoneNumberErrorMsg)" required>
                  <div class="invalid-feedback">
                    {{phoneNumberErrorMsg}}
                  </div>
                </div>

                <!--home address input field, allows for autocompletion via prompt-->
                <div class="col my-2 my-lg-0">
                  <label for="home-address">Address Autofill (Optional)</label>
                  <input id="home-address" name="home-address" tabindex ="11" ref="homeAddressInput" type="text" @input="input()"
                         @keydown="addressKeyDown($event)" :class="toggleInvalidClass(homeAddressErrorMsg)"
                         :maxlength="config.homeAddress.maxLength" autocomplete="off">
                  <div class="invalid-feedback">
                    {{homeAddressErrorMsg}}
                  </div>
                </div>

              </div>

              <!--ninth row of form-->
              <div class="row my-lg-2">

                <!--street number input field-->
                <div class="col my-2 my-lg-0">
                  <label for="streetNumber">Street Number</label>
                  <input :class="toggleInvalidClass(streetNumberErrorMsg)" tabindex="12" id="streetNumber" v-model="streetNumber"
                         name="streetNumber" ref="streetNumber" autocomplete="off">
                  <div class="invalid-feedback">
                    {{streetNumberErrorMsg}}
                  </div>
                </div>

                <!--street name input field-->
                <div class="col my-2 my-lg-0">
                  <label for="streetName">Street Name</label>
                  <input :class="toggleInvalidClass(streetNameErrorMsg)" tabindex="13" id="streetName" v-model="streetName"
                         name="streetName" ref="streetName" autocomplete="off">
                  <div class="invalid-feedback">
                    {{streetNameErrorMsg}}
                  </div>
                </div>
              </div>


              <!--tenth row of form-->
              <div class="row my-lg-2">

                <!--suburb input field-->
                <div class="col-lg-6 my-2 my-lg-0">
                  <label for="suburb">Suburb</label>
                  <input :class="toggleInvalidClass(suburbErrorMsg)" tabindex="14" name="suburb" id="suburb" v-model="suburb"
                         ref="suburb" autocomplete="off">
                  <div class="invalid-feedback">
                    {{suburbErrorMsg}}
                  </div>
                </div>

                <!--city input field-->
                <div class="col-lg-6 my-2 my-lg-0">
                  <label for="city">City</label>
                  <input :class="toggleInvalidClass(cityErrorMsg)" tabindex="15" name="city" id="city" ref="city" v-model="city"
                         autocomplete="off">
                  <div class="invalid-feedback">
                    {{cityErrorMsg}}
                  </div>
                </div>
              </div>

              <!--eleventh row of form-->
              <div class="row my-lg-2">

                <!--state input field-->
                <div class="col-lg-6 my-2 my-lg-0">
                  <label for="region">State/Region</label>
                  <input :class="toggleInvalidClass(regionErrorMsg)" tabindex="16" name="region" id="region" ref="region" v-model="region"
                         autocomplete="off">
                  <div class="invalid-feedback">
                    {{regionErrorMsg}}
                  </div>
                </div>

                <!--postcode input field-->
                <div class="col-lg-6 my-2 my-lg-0">
                  <label for="postcode">Postcode</label>
                  <input :class="toggleInvalidClass(postcodeErrorMsg)" tabindex="17" name="postcode" id="postcode" ref="postcode" v-model="postcode"
                         autocomplete="off">
                  <div class="invalid-feedback">
                    {{postcodeErrorMsg}}
                  </div>
                </div>

              </div>

              <!--twelfth row of form-->
              <div class="row my-lg-2">
                <!--country input field-->
                <div class="col my-2 my-lg-0">
                  <label for="country">Country*</label>
                  <input :class="toggleInvalidClass(countryErrorMsg)" tabindex="18" id="country" name="country" v-model="country"
                         ref="country" autocomplete="off" required>
                  <div class="invalid-feedback">
                    {{countryErrorMsg}}
                  </div>
                </div>

              </div>

              <!--thirteenth row of form-->
              <div class="row my-lg-2">

                <!--bio field-->
                <div class="col my-2 my-lg-0">
                  <label for="bio">Bio</label>
                  <textarea id="bio" name="bio" tabindex="19" rows="5" cols="70" v-model="bio"
                            :class="toggleInvalidClass(bioErrorMsg)" :maxlength="config.bio.maxLength"
                            style="resize: none"/>
                  <div class="invalid-feedback">
                    {{bioErrorMsg}}
                  </div>
                </div>

              </div>

              <!--fourteenth row of form-->
              <div class="row my-lg-2">

                <!--error message field-->
                <div class="col-lg-12 mt-2 my-lg-0 mx-auto">
                  <div id="edit-profile-error" ref="edit-profile-error" v-if="errorMessageBubble" class="alert alert-danger"
                       role="alert">
                    <label>{{ errorMessageBubble }}</label>
                  </div>
                </div>
              </div>

              <!-- Save changes button-->
              <div class="d-grid gap-2 d-lg-block">
                <button class="btn btn-lg btn-outline-primary green-button-transparent" type="button" tabindex="21" id="back-to-login-button" @click="toProfile()">Cancel Changes</button>
                <button id="save-button" tabindex="20" class="btn btn-lg float-lg-end green-button" type="button" @click="editUser($event)">Save Changes</button>
              </div>

            </form>

          </div>

        </div>

      </div>

    </div>
    <!--footer-->
    <FooterSecure></FooterSecure>

  </div>
</template>

<script>
import User from "../configs/User"
import Cookies from 'js-cookie';
import FooterSecure from "../components/main/FooterSecure";
import AddressAPI from "../addressInstance";
import Api from "../Api";

export default {
  name: "EditProfile",
  components: {
    FooterSecure,
  },

  data() {
    return {

      // Used for having pre-filled input fields
      DEBUG_MODE: false,

      // A copy of the user config file for error checking.
      config: User.config,

      // First name related variables
      firstName: "",
      firstNameErrorMsg: "",

      // Middle name related variables
      middleName: "",
      middleNameErrorMsg: "",

      // Last name related variables
      lastName: "",
      lastNameErrorMsg: "",

      // Nickname related variables
      nickname: "",
      nicknameErrorMsg: "",

      // Bio related variables
      bio: "",
      bioErrorMsg: "",

      // Email related variables
      email: "",
      emailErrorMsg: "",

      // Data of birth related variables
      dateOfBirth: "",
      dateOfBirthErrorMsg: "",

      // Password related variables
      password: "",
      passwordErrorMsg: "",
      passwordWasTyped: false, // Allows for the styling to only start applying after the user has interacted
                               // with the password
      showPassword: false, // Used for toggling the password visibility

      // Current password related variables
      currentPassword: "",
      currentPasswordErrorMsg: "",

      // Confirm password related variables
      confirmPassword: "",
      confirmPasswordErrorMsg: "",

      // Phone number related variables
      phoneNumber: "",
      phoneNumberErrorMsg: "",

      // Home address related variables
      homeAddressErrorMsg: "",

      // Street number related variables
      streetNumber: "",
      streetNumberErrorMsg: "",

      // Street name related variables
      streetName: "",
      streetNameErrorMsg: "",

      // Suburb related variables
      suburb: "",
      suburbErrorMsg: "",

      // Postcode related variables
      postcode: "",
      postcodeErrorMsg: "",

      // City related variables
      city: "",
      cityErrorMsg: "",

      // State/ region related variables
      region: "",
      regionErrorMsg: "",

      // Country related variables
      country: "",
      countryErrorMsg: "",

      // Error message related variables
      errorMessageBubble: "",
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
     * This method toggles the appearance of the password field, where the password will be shown if showPassword is
     * true, else it is hidden.
     * @param showPassword, whether the password should be displayed.
     * @returns {string}, String, the visibility of the password.
     */
    togglePasswordInputType(showPassword) {
      if (showPassword) {
        return 'text'
      } else {
        return 'password'
      }
    },

    /**
     * This method checks the password against the given criteria and determines whether it meets the criteria.
     * If it does, the colour is changed from black to red.
     *
     * @param password, string, the current input of the password field.
     * @param regex, string, the password criteria that the password is checked against.
     * @returns {[string]}, classList, a List containing a String of classes for the password criteria to used.
     */
    checkPasswordCriteria(password, regex) {

      let classList = ['small']
      if (!regex.test(password) && this.passwordWasTyped) {
        classList.push('text-red');
      }
      return classList;
    },

    /**
     * This method edits a user.
     * @param e, the current event.
     */
    editUser(e) {
      // Steps required for the function before starting processing.
      e.preventDefault()  // prevents page from reloading
      this.trimTextInputFields()


    },

    /**
     * This method returns the user to the current url IDs profile
     */
    toProfile() {
      const id = this.$route.params.id
      this.$router.push({name: "Profile", params: {id}})
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
        let address = "";
        let { properties } = features[index];
        if (properties) {

          let {country, city, postcode, state, street, housenumber, name, district} = properties;

          if (name) {
            address += name + ", ";
          }

          if (housenumber) {
            address += housenumber;
          }

          if (street) {
            address += " " + street + ", ";
          }

          if (district) {
            address += " " + district + ", ";
          }

          if (city) {
            address += city + ", ";
          }

          if (postcode) {
            address += postcode + ", ";
          }

          if (state) {
            address += state + ", ";
          }

          if (country) {
            address += country;
          }

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
      let inputValue = this.$refs.homeAddressInput.value;

      const self = this;
      // Close any already open lists of autocompleted values
      this.closeAllLists();
      if (!inputValue) { return false;}
      this.autocompleteFocusIndex = -1;
      // Create a outer DIV element that will contain the items from the request
      const outerDiv = document.createElement("div");
      outerDiv.style.width = this.$refs.homeAddressInput.getBoundingClientRect().width.toString() + 'px';
      outerDiv.setAttribute("id", this.$refs.homeAddressInput.id + "autocomplete-list");
      outerDiv.setAttribute("class", "autocomplete-items");
      // Append the DIV element as a child of the autocomplete container
      this.$refs.homeAddressInput.parentNode.appendChild(outerDiv);

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
            document.getElementById('home-address').value = "";
            const id = event.target.id;

            let {country, city, postcode, state, street, housenumber, district} = self.addressResultProperties[id];

            if (housenumber) {
              document.getElementById('streetNumber').value = housenumber;
            }
            if (street) {
              document.getElementById('streetName').value = street;
            }

            if (district) {
              document.getElementById('suburb').value = district;
            }

            if (city) {
              document.getElementById('city').value = city;
            }

            if (postcode) {
              document.getElementById('postcode').value = postcode;
            }

            if (state) {
              document.getElementById('region').value = state;
            }

            if (country) {
              document.getElementById('country').value = country;
            }

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
        if (element !== itemElements[i] && element !== this.$refs.homeAddressInput) {
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

      let elementList = document.getElementById(this.$refs.homeAddressInput.id + "autocomplete-list");
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
     * This method determines the maximum possible date of birth.
     *
     * @returns {string}, the maximum possible date of birth in the expected String format of e.g. 2021-03-15.
     */
    getMaxDateOfBirth() {
      const todayDate = new Date();
      const year_13_ms = 1000 * 60 * 60 * 24 * 365 * 13;

      const maxDate = new Date(todayDate - year_13_ms);

      let day = maxDate.getDate();
      let month = maxDate.getMonth();
      let year = maxDate.getFullYear();

      if (month < 10) {
        month = `0${month.toString()}`
      }

      if (day < 10) {
        day = `0${day.toString()}`
      }

      return `${year.toString()}-${month.toString()}-${day.toString()}`
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
     * Retrieves the current url id's details and autofills details into the appropriate fields
     * @param id User id to retrieve
     */
    retrieveUser(id) {
      Api.getUser(id).then((res) => {
        this.bio = res.data.bio
        this.dateOfBirth = res.data.dateOfBirth
        this.email = res.data.email
        this.phoneNumber = res.data.phoneNumber
        // Names
        this.firstName = res.data.firstName
        this.middleName = res.data.middleName
        this.lastName = res.data.lastName
        this.nickname = res.data.nickname
        // Address
        this.streetNumber = res.data.homeAddress.streetNumber
        this.streetName = res.data.homeAddress.streetName
        this.suburb = res.data.homeAddress.suburb
        this.city = res.data.homeAddress.city
        this.region = res.data.homeAddress.region
        this.postcode = res.data.homeAddress.postcode
        this.country = res.data.homeAddress.country
      })
    }
  },
  beforeCreate() {
    const currentID = Cookies.get('userID');
    if (currentID) {
      // this.getLoginRole(currentID);
       const id = this.$route.params.id

      if (currentID !== id) {
        this.$router.push({name: "Profile", params: {id}})
      }
    }
  },
  mounted() {
    const id = this.$route.params.id
    this.retrieveUser(id);
  }
}

</script>

<!------------------------------------------ Edit Profile Page Styling ------------------------------------------------>

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

#edit-profile-form {
  border-radius: 2%;
}

/* NOTE: IntelliJ doesn't highlight this one as used even though it is used in toggle checkPasswordCriteria */
.text-red {
  color: red;
}

/*--------------------- Hide arrows from input numbers ---------------------*/
input:focus, textarea:focus, #save-button:focus, #back-to-login-button:focus {
  outline: none;
  box-shadow: 0 0 2px 2px #2eda77; /* Full freedom. (works also with border-radius) */
  border: 1px solid #1EBABC;
}
/*------------------------------------------------------------------------*/
</style>
