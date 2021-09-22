<!--This file creates the Login page.-->
<!--It contains the container for allowing the user to login or directs them to the Register page.-->
<!--It contains the company logo and name as well the SecureFooter which only contains the copyright information for Team-->
<!--400.-->
<!--It is currently fully responsive, EXCEPT for the text of the logo.-->

<template>
  <div>
    <div id="main">
    <!--login container-->
    <div id="login" class="container text-font all-but-footer">

      <!--logo, with text-->
      <div class="row justify-content-center ">
        <div class="col-3 m-3 text-center logo-container">
          <img id="logo" src="../../public/logo_only_med.png" class="img-fluid" alt="logo">
          <p class="company-name-main-login company-name-main-font">REUSABILITY</p>
          <p class="company-name-sub-heading"> - Share & Save - </p>
        </div>
      </div>

      <!--form content-->
      <div class="row justify-content-center">
        <div class="col-xl-5 col-lg-6 col-md-8 col-sm-12 my-2 my-lg-0">
          <div id="form-container" class="container shadow ">

            <!--login header-->
            <div class="row">
              <div class="col">
                <h3 class="m-4 text-center">Login</h3>
              </div>
            </div>

            <!--email input field-->
            <form @submit.prevent>
              <div class="row">
                <div class="col mb-4">
                  <label for="email-input" class="form-label">Email Address*</label>
                  <input type="email" class="form-control" id="email-input" ref="eInput" tabindex="1">
                </div>
              </div>

              <div class="row my-lg-2">
                <!--password input field-->
                <div class="col my-2 my-lg-0">
                  <label for="password-input" class="form-label">Password</label>
                  <div class="input-group">
                    <input autocomplete="on" class="form-control" name="password" :type="togglePasswordInputType(showPassword)" tabindex="2" id="password-input" ref="pInput">

                    <!--toggle password visibility-->
                    <span class="input-group-text green-search-button" @click="showPassword = !showPassword"
                          @keydown=" (event) => { if (event.keyCode === 13) this.showPassword = !showPassword}"
                          tabindex="3">
                      <i v-if="!showPassword" class="fas fa-eye" aria-hidden="true"></i>
                      <i v-else class="fas fa-eye-slash" aria-hidden="true"></i>
                      </span>

                  </div>
                </div>

            </div>

              <!--forgot password location-->
              <div class="row">
                <router-link to="/forgotPassword" style="color: #1EBA8C; text-decoration: none; background-color: transparent;" tabindex="6">Forgot password?</router-link>
              </div>

              <!--error message location-->
              <div class="row">
                <div class="col mb-2 mb-md-0">
                  <label for="loginButton" id="error-label" ref="errorLbl" class="text-danger mt-2">Failed login attempt, email or password incorrect.</label>
                </div>
              </div>

              <div class="row">
                <!--register button-->
                <div class="col-5">
                  <router-link class="btn btn-lg m-sm-4 mb-4 green-button-transparent" to="/registration" tag="button" type="button" tabindex="5" >Register</router-link>
                </div>

                <!--login button-->
                <div class="col">
                  <button class="btn btn-lg float-end m-sm-4 mb-4 green-button" @click="login()" type="submit" id="loginButton" tabindex="4">Sign In</button>
                </div>
              </div>

            </form>

          </div>
        </div>
      </div>

    </div>
    </div>
    <Footer/>

  </div>
</template>

<script>
import Api from '../Api';
import Cookies from 'js-cookie';
import Footer from "../components/main/Footer";

export default {
  name: "Login",
  components: {
    Footer
  },

  data() {
    return {
      showPassword: false, // Used for toggling the password visibility
    }
  },

  methods: {

    /**
     * Uses Axios to send a login request to the back-end using the inputted email and password.
     * Also displays an appropriate error message if the credentials are incorrect, or the connection fails.
     * If the login is successful, the user is router to their profile page.
     */
    login() {

      const email = this.$refs.eInput.value;
      const pass = this.$refs.pInput.value;
      // Backend will hash + salt password before storing it.
      Api.signIn(email, pass).then((response) => {
        Cookies.remove('actAs', { sameSite: 'strict' });
        Cookies.set('userID', response.data.userId, { sameSite:'strict'})
        // Also grab JSESSIONID when we have agreed on an implementation with the backend team.
        this.$router.push({ name: 'Home' })
        this.$refs.errorLbl.style.visibility = "hidden";
      })
      .catch((error) => {
        if (error.response) {
          if (this.hasExceedLoginAttempts()) {
            this.$refs.errorLbl.innerText = 'Exceeded login attempts. Your account is now locked. Please wait X minutes for your account to be unlocked. ';
          } else {
            this.$refs.errorLbl.innerText = 'Failed login attempt, email or password incorrect.';
          }
        } else if (error.request) {
          this.$refs.errorLbl.innerText = 'Connection Timeout.';
        } else {
          this.$refs.errorLbl.innerText = 'Connection error.';
        }
        this.$refs.errorLbl.style.visibility = "visible";
      })
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
     * Check if the user has exceeded the number of attempts for logging into their account.
     * @return Boolean, true if the number of attempts exceeds the limit, otherwise false.
     */
    hasExceedLoginAttempts() {
      // TODO
      return true;
    }
  }
}

</script>

<!------------------------------------------------ Login Page Styling --------------------------------------------->

<style scoped>

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

/**
 * Makes error message invisible
 */
#error-label {
  visibility: hidden;
}

.all-but-footer {
  padding-bottom: 50px;
}

input:focus, textarea:focus {
  outline: none;
  box-shadow: 0 0 2px 2px #2eda77; /* Full freedom. (works also with border-radius) */
  border: 1px solid #1EBABC;
}



</style>
