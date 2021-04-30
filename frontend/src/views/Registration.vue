<!--This file creates the Registration page.-->
<!--It contains the container displaying the mandatory and optional fields that a person must submit to register an
    account with the app, provided in the form.-->
<!--Bootstrap has been used for creating and styling the elements.-->
<!--It is currently fully responsive.-->

<template>
  <div>

    <!--body excluding footer-->
    <div class="container all-but-footer text-font">

      <!--logo without text, centred above form-->
      <div class="row justify-content-center">
        <div class="col-3 m-3">
          <img id="logo" src="../../public/logo_only_med.png" class="img-fluid" alt="logo">
        </div>
      </div>

      <div class="col-lg-8 mx-auto">

        <!-- registration form's container-->
        <div class="container shadow py-5 px-4 bg-white mb-5" id="register-form">
          <div class="row">
            <div class="col">
              <h3 class="display-6 m-2 mb-5 text-center">Register</h3>
            </div>
          </div>

          <!--registration form, needs validation-->
          <form id="registration" class="needs-validation mb-3 px-5" novalidate @submit.prevent>

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
                <ul>
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

              <!--password input field-->
              <div class="col my-2 my-lg-0">
                <label for="password">Password*</label>
                <div class="input-group">
                  <input id="password" name="password" tabindex="7" :type="togglePasswordInputType(showPassword)" v-model="password" v-on:focus="passwordWasTyped = true" :class="toggleInvalidClass(passwordErrorMsg)" :maxlength="config.password.maxLength" required>

                  <!--toggle password visibility-->
                  <span class="input-group-text green-search-button" @click="showPassword = !showPassword"
                        @keydown=" (event) => { if (event.keyCode === 13) this.showPassword = !showPassword}"
                        tabindex="8">
                    <i v-if="!showPassword" class="fas fa-eye"></i>
                    <i v-else class="fas fa-eye-slash"></i>
                    </span>

                  <div class="invalid-feedback">
                    {{passwordErrorMsg}}
                  </div>

                </div>
              </div>

            </div>

            <!--sixth row of form-->
            <div class="row my-lg-2">

              <!--confirm password input field-->
              <div class="col my-2 my-lg-0">
                <label for="confirm-password">Confirm Password*</label>
                <input id="confirm-password" name="password" tabindex="9" :type="togglePasswordInputType(showPassword)"
                       v-model="confirmPassword" :class="toggleInvalidClass(confirmPasswordErrorMsg)"
                       :maxlength="config.password.maxLength" required>
                <div class="invalid-feedback">
                  {{confirmPasswordErrorMsg}}
                </div>
              </div>

            </div>

            <!--seventh row of form-->
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

            <!--eighth row of form-->
            <div class="row my-lg-2">

                <!--street input field-->
                <div class="col my-2 my-lg-0">
                  <label for="streetNumber">Street Number</label>
                  <input :class="toggleInvalidClass(streetNumberErrorMsg)" tabindex="12" id="streetNumber"
                          name="streetNumber" ref="streetNumber" autocomplete="off">
                <div class="invalid-feedback">
                  {{streetNumberErrorMsg}}
                </div>
              </div>

              <!--suburb input field-->
              <div class="col my-2 my-lg-0">
                <label for="streetName">Street Name</label>
              <input :class="toggleInvalidClass(streetNameErrorMsg)" tabindex="13" id="streetName"
                     name="streetName" ref="streetName" autocomplete="off">
              <div class="invalid-feedback">
                {{streetNameErrorMsg}}
              </div>
            </div>
          </div>


            <!--ninth row of form-->
            <div class="row my-lg-2">

              <!--city input field-->
              <div class="col-lg-6 my-2 my-lg-0">
                <label for="city">City</label>
                <input :class="toggleInvalidClass(cityErrorMsg)" tabindex="14" name="city" id="city" ref="city"
                       autocomplete="off">
                <div class="invalid-feedback">
                  {{cityErrorMsg}}
                </div>
              </div>

              <!--postcode input field-->
              <div class="col-lg-6 my-2 my-lg-0">
              <label for="postcode">Postcode</label>
              <input :class="toggleInvalidClass(postcodeErrorMsg)" tabindex="15" name="postcode" id="postcode" ref="postcode"
                     autocomplete="off">
              <div class="invalid-feedback">
                {{postcodeErrorMsg}}
              </div>
            </div>

          </div>

          <!--tenth row of form-->
          <div class="row my-lg-2">

            <!--state input field-->
            <div class="col-lg-6 my-2 my-lg-0">
              <label for="region">State/Region</label>
              <input :class="toggleInvalidClass(regionErrorMsg)" tabindex="16" name="region" id="region" ref="region"
                     autocomplete="off">
              <div class="invalid-feedback">
                {{regionErrorMsg}}
              </div>
            </div>

            <!--country input field-->
            <div class="col-lg-6 my-2 my-lg-0">
              <label for="country">Country*</label>
              <input :class="toggleInvalidClass(countryErrorMsg)" tabindex="17" id="country" name="country"
                     ref="country" autocomplete="off" required>
              <div class="invalid-feedback">
                {{countryErrorMsg}}
              </div>
            </div>

          </div>

          <!--eleventh row of form-->
          <div class="row my-lg-2">

            <!--bio field-->
            <div class="col my-2 my-lg-0">
              <label for="bio">Bio</label>
              <textarea id="bio" name="bio" tabindex="18" rows="5" cols="70" v-model="bio"
                        :class="toggleInvalidClass(bioErrorMsg)" :maxlength="config.bio.maxLength"
                        style="resize: none"/>
              <div class="invalid-feedback">
                {{bioErrorMsg}}
              </div>
            </div>

          </div>

            <!--twelfth row of form-->
            <div class="row my-lg-2">

              <!--error message field-->
              <div class="col-lg-12 mt-2 my-lg-0 mx-auto">
                <div id="registration-error" ref="registration-error" v-if="errorMessageBubble" class="alert alert-danger"
                     role="alert">
                  <label>{{ errorMessageBubble }}</label>
                </div>
              </div>
            </div>

            <!--register button-->
            <div class="d-grid gap-2 d-lg-block">
              <button class="btn btn-lg btn-outline-primary green-button-transparent" type="button" tabindex="18" id="back-to-login-button" @click="$router.push('/')">Back to Login</button>
              <button id="register-button" tabindex="17" class="btn btn-lg float-lg-end green-button" type="button" @click="addNewUser($event)">Register</button>
            </div>

          </form>

        </div>

      </div>

    </div>

    <!--footer-->
    <FooterSecure></FooterSecure>

  </div>
</template>

<script>
import Api, {User} from "../Api";
import Cookies from 'js-cookie';
import FooterSecure from "../components/FooterSecure";
import AddressAPI from "../addressInstance";

export default {
  name: "Registration",
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

      // Confirm password related variables
      confirmPassword: "",
      confirmPasswordErrorMsg: "",

      // Phone number related variables
      phoneNumber: "",
      phoneNumberErrorMsg: "",

      // Home address related variables
      homeAddressErrorMsg: "",

      // Street number related variables
      streetNumberErrorMsg: "",

      // Street name related variables
      streetNameErrorMsg: "",

      // Postcode related variables
      postcodeErrorMsg: "",

      // City related variables
      cityErrorMsg: "",

      // State/ region related variables
      regionErrorMsg: "",

      // Country related variables
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

  /**
   * This is used for creating a prefilled user for testing and bypassing manually filling in the registration page
   */
  created() {
    if (this.DEBUG_MODE) {
      this.firstName = "Dan";
      this.lastName = "Ronen";
      this.dateOfBirth = "2008-02-02";
      this.email = "demo@example.com";
      this.password = "1234AaAa@";
      this.confirmPassword = "1234AaAa@";
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
     * This method parses the given date of birth input and separates it into a year, month and day, provided it meets
     * the expected format.
     *
     * @param dateString, string, the date to validate and separate.
     * @returns {{month: number, year: number, day: number}|null}, {year, month, day}, if the date meets the expected
     * format, else null.
     *
     */
    parseSelectedDate(dateString) {
      const verifyRegex = /^[0-9]{1,5}-[0-9]{1,2}-[0-9]{1,2}$/

      if (verifyRegex.test(dateString)) {
        const dateParts = dateString.split("-", 3);
        return {
          year: Number(dateParts[0]),
          month: Number(dateParts[1]),
          day: Number(dateParts[2])
        }
      } else {
        return null
      }
    },

    /**
     * This method validates the date of birth field input and creates a Date which represents the new user's
     * date of birth.
     *
     * @param selectedDate, string, the date of birth of the user.
     * @returns {Boolean|null}, returns true is the date is valid i.e. in the past and meets the expected format, else
     *                          null or false.
     */
    isValidDateOfBirth(selectedDate) {
      const todayDate = new Date();
      const year_13_ms = 1000 * 60 * 60 * 24 * 365 * 13;
      const data = this.parseSelectedDate(selectedDate);

      if (data) {
        const {year, month, day} = data;
        if (year && month && day) {
          const chosenDate = new Date(year, month, day);
          return todayDate - chosenDate >= year_13_ms;
        }
      }
      return null;
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
      }
      else if (!regex.test(inputVal)) {
        errorMessage = regexMessage;
      } else if (!this.between(inputVal.length, minLength, maxLength)) {
        errorMessage = `Input must be between ${minLength} and ${maxLength} characters long.`
      }
      return errorMessage;
    },

    /**
     * This method removes white space from the beginning and end of all the input field's input values.
     */
    trimTextInputFields () {
      this.firstName = this.firstName.trim();
      this.middleName = this.middleName.trim();
      this.lastName = this.lastName.trim();
      this.nickname = this.nickname.trim();
      this.bio = this.bio.trim();
      this.email = this.email.trim();
      this.$refs.country.value = this.$refs.country.value.trim();
      this.$refs.city.value = this.$refs.city.value.trim();
      this.$refs.postcode.value = this.$refs.postcode.value.trim();
      this.$refs.region.value = this.$refs.region.value.trim();
      this.$refs.streetNumber.value = this.$refs.streetNumber.value.trim();
      this.$refs.streetName.value = this.$refs.streetName.value.trim();
    },

    /**
     * This method creates a new user.
     * @param e, the current event.
     */
    addNewUser(e) {
      // Steps required for the function before starting processing.
      e.preventDefault()  // prevents page from reloading
      this.trimTextInputFields()
      let requestIsInvalid = false


      // ===================================== START OF INPUT FIELDS VALIDATION ========================================

      // First name error checking
      this.firstNameErrorMsg = this.getErrorMessage(
          this.config.firstName.name,
          this.firstName,
          this.config.firstName.minLength,
          this.config.firstName.maxLength,
          this.config.firstName.regexMessage,
          this.config.firstName.regex
      )
      if (this.firstNameErrorMsg) {
        requestIsInvalid = true
      }

      // Middle name error checking
      this.middleNameErrorMsg = this.getErrorMessage(
          this.config.middleName.name,
          this.middleName,
          this.config.middleName.minLength,
          this.config.middleName.maxLength,
          this.config.middleName.regexMessage,
          this.config.middleName.regex
      )
      if (this.middleNameErrorMsg) {
        requestIsInvalid = true
      }

      // Last name error checking
      this.lastNameErrorMsg = this.getErrorMessage(
          this.config.lastName.name,
          this.lastName,
          this.config.lastName.minLength,
          this.config.lastName.maxLength,
          this.config.lastName.regexMessage,
          this.config.lastName.regex
      )
      if (this.lastNameErrorMsg) {
        requestIsInvalid = true
      }

      // Nickname error checking
      this.nicknameErrorMsg = this.getErrorMessage(
          this.config.nickname.name,
          this.nickname,
          this.config.nickname.minLength,
          this.config.nickname.maxLength,
          this.config.nickname.regexMessage,
          this.config.nickname.regex
      )
      if (this.nicknameErrorMsg) {
        requestIsInvalid = true
      }

      // Bio error checking
      this.bioErrorMsg = this.getErrorMessage(
          this.config.bio.name,
          this.bio,
          this.config.bio.minLength,
          this.config.bio.maxLength,
      )
      if (this.bioErrorMsg) {
        requestIsInvalid = true
      }

      // Email error checking
      this.emailErrorMsg = this.getErrorMessage(
          this.config.email.name,
          this.email,
          this.config.email.minLength,
          this.config.email.maxLength,
          this.config.email.regexMessage,
          this.config.email.regex
      )
      if (this.emailErrorMsg) {
        requestIsInvalid = true
      }

      // Date of birth error checking
      requestIsInvalid = true
      if (!this.dateOfBirth) {
        this.dateOfBirthErrorMsg = "This field is required!"
      } else if (!this.isValidDateOfBirth(this.dateOfBirth)) {
        this.dateOfBirthErrorMsg = "Must be over 13, and not from the future."
      } else {
        this.dateOfBirthErrorMsg = "";
        requestIsInvalid = false
      }

      // Password error checking
      this.passwordErrorMsg = this.getErrorMessage(
          this.config.password.name,
          this.password,
          this.config.password.minLength,
          this.config.password.maxLength,
          this.config.password.regexStrongMessage,
          this.config.password.regexStrong,
      )
      if (this.passwordErrorMsg) {
        requestIsInvalid = true
      }

      // Confirm password error checking
      if (this.password !== this.confirmPassword) {
        this.confirmPasswordErrorMsg = "Confirmation password does not equal password field."
      } else {
        this.confirmPasswordErrorMsg = ""
      }
      if (this.confirmPasswordErrorMsg) {
        requestIsInvalid = true
      }

      // Phone number error checking
      this.phoneNumberErrorMsg = this.getErrorMessage(
          this.config.phoneNumber.name,
          this.phoneNumber,
          this.config.phoneNumber.minLength,
          this.config.phoneNumber.maxLength,
          this.config.phoneNumber.regexMessage,
          this.config.phoneNumber.regex
      )
      if (this.phoneNumberErrorMsg) {
        requestIsInvalid = true
      }

      // Home address error checking
      this.homeAddressErrorMsg = this.getErrorMessage(
          this.config.homeAddress.name,
          this.$refs.homeAddressInput.value,
          this.config.homeAddress.minLength,
          this.config.homeAddress.maxLength
      )
      if (this.homeAddressErrorMsg) {
        requestIsInvalid = true
      }

      // Street number error checking
      this.streetNumberErrorMsg = this.getErrorMessage(
          this.config.streetNumber.name,
          // Using v-model for this address input apparently does not update
          // when we insert from our autocomplete list so it has been changed to use $refs
          this.$refs.streetNumber.value,
          this.config.streetNumber.minLength,
          this.config.streetNumber.maxLength
      )
      if (this.streetNumberErrorMsg) {
        requestIsInvalid = true
      }

      // Street name error checking
      this.streetNameErrorMsg = this.getErrorMessage(
          this.config.streetName.name,
          // Using v-model for this address input apparently does not update
          // when we insert from our autocomplete list so it has been changed to use $refs
          this.$refs.streetName.value,
          this.config.streetName.minLength,
          this.config.streetName.maxLength
      )
      if (this.streetNameErrorMsg) {
        requestIsInvalid = true
      }

      // Postcode error checking
      this.postcodeErrorMsg = this.getErrorMessage(
          this.config.postcode.name,
          this.$refs.postcode.value,
          this.config.postcode.minLength,
          this.config.postcode.maxLength
      )
      if (this.postcodeErrorMsg) {
        requestIsInvalid = true
      }

      // City error checking
      this.cityErrorMsg = this.getErrorMessage(
          this.config.city.name,
          this.$refs.city.value,
          this.config.city.minLength,
          this.config.city.maxLength
      )
      if (this.cityErrorMsg) {
        requestIsInvalid = true
      }

      // Region error checking
      this.regionErrorMsg = this.getErrorMessage(
          this.config.region.name,
          this.$refs.region.value,
          this.config.region.minLength,
          this.config.region.maxLength
      )
      if (this.regionErrorMsg) {
        requestIsInvalid = true
      }

      // Country error checking
      this.countryErrorMsg = this.getErrorMessage(
          this.config.country.name,
          this.$refs.country.value,
          this.config.country.minLength,
          this.config.country.maxLength
      )
      if (this.countryErrorMsg) {
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
        city: this.$refs.city.value,
        region: this.$refs.region.value,
        country: this.$refs.country.value,
        postcode: this.$refs.postcode.value
      }


      // Wrapping up the user submitted fields into a class object (User).
      const userData = {
        firstName: this.firstName.charAt(0).toUpperCase() + this.firstName.slice(1),
        lastName: this.lastName.charAt(0).toUpperCase() + this.lastName.slice(1),
        middleName: this.middleName.charAt(0).toUpperCase() + this.middleName.slice(1),
        nickname: this.nickname.charAt(0).toUpperCase() + this.nickname.slice(1),
        bio: this.bio,
        email: this.email,
        dateOfBirth: this.dateOfBirth,
        phoneNumber: this.phoneNumber,

        /*
         * NOTE: Using v-model for this address input apparently does not update.
         *       When we insert from our autocomplete list so it has been changed to use $refs
         */

        homeAddress: addressData,
        password: this.password
      }


      const user = new User(userData)

      /*
       * Add the User to the database by sending an API request to the backend to store the user's information.
       * Raise any errors and ensure they are displayed on the UI.
       */
      Api.addNewUser(user
      ).then( (res) => {
            if (res.status === 201) {
              const {userId} = res.data;
              if (userId) {
                Cookies.set('userID', userId);
                this.$router.push('/profile');
              }
            }
          }
      ).catch((error) => {
        this.cannotProceed = true;
        if (error.response) {
          if (error.response.status === 400) {
            this.errorMessageBubble = '400 Bad request; invalid user data';
          } else if (error.response.status === 409) {
            this.emailErrorMsg = 'Email already in use'
          } else {
            this.errorMessageBubble = `${error.response.status} Unexpected error occurred!`;
          }
        } else if (error.request) {
          this.errorMessageBubble = 'Timeout occurred';
        } else {
          this.errorMessageBubble = 'Unexpected error occurred!';
        }
      })
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
      // Display the first 5 options returned
      let maxL = 5;
      // Clear the list after each request (before filtering)
      this.addressResultProperties = [];

      while ((numInList < maxL) && (index < fLength)) {
        let address = "";
        let { properties } = features[index];
        if (properties) {

          let {country, city, postcode, state, street, housenumber, name} = properties;

          if (name) {
            address += name + ", ";
          }

          if (housenumber) {
            address += housenumber;
          }

          if (street) {
            address += " " + street + ", ";
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

            let {country, city, postcode, state, street, housenumber} = self.addressResultProperties[id];

            if (housenumber) {
              document.getElementById('streetNumber').value = housenumber;
            }
            if (street) {
              document.getElementById('streetName').value = street;
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
     * @param DOM Element An optional element that won't be closed if given
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
  }
}

</script>

<!------------------------------------------ Registration Page Styling ------------------------------------------------>

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

/* NOTE: IntelliJ doesn't highlight this one as used even though it is used in toggle checkPasswordCriteria */
.text-red {
  color: red;
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

input:focus, textarea:focus, #register-button:focus, #back-to-login-button:focus {
  outline: none;
  box-shadow: 0 0 2px 2px #2eda77; /* Full freedom. (works also with border-radius) */
  border: 1px solid #1EBABC;
}

/*------------------------------------------------------------------------*/

</style>
