<template>
  <div class="container">
    <div class="row justify-content-center ">
      <div class="col-3 m-3 text-center logo-container">
        <img id="logo" src="../../public/logo_only_med.png" class="img-fluid" alt="logo">
        <p class="company-name-main-login company-name-main-font">REUSABILITY</p>
        <p class="company-name-sub-heading"> - Share & Save - </p>
      </div>
    </div>
    <div id="password-container"
         class="container col-xl-5 col-lg-6 col-md-8 col-sm-12 my-2 my-lg-0 mt-4 py-3 px-4"
    >

      <!--text-->
      <div class="row">
        <div class="col">
          <h3 class="m-4 text-center">Reset Password</h3>
        </div>
      </div>

      <!--fourth row of form-->
      <div class="row my-lg-2">

        <!--password validation information-->
        <div class="col my-2 my-lg-0">
          <h6>Password must meet the following conditions:</h6>
          <ul>
            <li :class="checkPasswordCriteria(password, config.password.regexContainLowerCase, passwordWasTyped)">
              One lowercase letter</li>
            <li :class="checkPasswordCriteria(password, config.password.regexContainUpperCase, passwordWasTyped)">
              One uppercase letter</li>
            <li :class="checkPasswordCriteria(password, config.password.regexContainNumber, passwordWasTyped)">
              One number</li>
            <li :class="checkPasswordCriteria(password, config.password.regexContainSymbol, passwordWasTyped)">
              One of the following: !, @, #, $, %, ^, & and or*</li>
            <li :class="checkPasswordCriteria(password, config.password.regexContainLength, passwordWasTyped)">
              At least 8 characters in length</li>
          </ul>
        </div>

      </div>

      <!--fifth row of form-->
      <div class="row my-lg-2">

        <!--password input field-->
        <div class="col my-2 my-lg-0">
          <label for="password">New Password*</label>
          <div class="input-group">
            <input id="password" name="password" tabindex="7" :type="togglePasswordInputType(showPassword)" v-model="password"
                   v-on:focus="passwordWasTyped = true" :class="toggleInvalidClass(passwordErrorMsg)" autocomplete="new-password">

            <!--toggle password visibility-->
            <span class="input-group-text green-search-button" id="show-password" @click="showPassword = !showPassword"
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

      <!--sixth row of form-->
      <div class="row my-lg-2">

        <!--confirm password input field-->
        <div class="col my-2 my-lg-0 mb-4">
          <label for="confirm-password">Confirm New Password*</label>
          <input id="confirm-password" name="password" tabindex="9" :type="togglePasswordInputType(showPassword)"
                 v-model="confirmPassword" :class="toggleInvalidClass(confirmPasswordErrorMsg)"
                 :maxlength="config.password.maxLength" autocomplete="new-password" required>
          <div class="invalid-feedback">
            {{confirmPasswordErrorMsg}}
          </div>
        </div>
      </div>

      <div class="row">
        <div class="col d-flex justify-content-center" >
          <button class="btn btn-lg my-sm-4 mb-4 green-button"
                  type="submit" tabindex="4">Change Password</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {toggleInvalidClass} from "../../src/validationUtils";
import {togglePasswordInputType, checkPasswordCriteria} from "../../src/passwordUtil";
import User from "../configs/User"

export default {
  name: "ResetPassword",
  data() {
    return {
      // A copy of the user config file for error checking.
      config: User.config,
      // Password related variables
      password: "",
      passwordErrorMsg: "",
      passwordWasTyped: false, // Allows for the styling to only start applying after the user has interacted
                               // with the password
      showPassword: false, // Used for toggling the password visibility

      // Confirm password related variables
      confirmPassword: "",
      confirmPasswordErrorMsg: "",
    }
  },
  methods: {
    toggleInvalidClass: toggleInvalidClass,
    togglePasswordInputType: togglePasswordInputType,
    checkPasswordCriteria: checkPasswordCriteria,
  }
}
</script>

<style scoped>

/* NOTE: IntelliJ doesn't highlight this one as used even though it is used in toggle checkPasswordCriteria */
.text-red {
  color: red;
}

.logo-container{
  width: 400px;

}

#logo {
  width: 200px;

}

.company-name-main-font {
  font-size: 40px;
  margin-bottom: 0;
}

#password-container {
  background-color: #fff;
  border-radius: 1rem;
  box-shadow: 0 .5rem 1rem rgba(0,0,0,.15)!important;
  width: 90vw;
  max-width: 500px;
}

</style>
