<!--This file creates the Login page.-->
<!--It contains the container for allowing the user to login or directs them to the Register page.-->
<!--It contains the company logo and name as well the SecureFooter which only contains the copyright information for Team-->
<!--400.-->
<!--It is currently fully responsive, EXCEPT for the text of the logo.-->

<!--TODO fix bug with logo's text's responsiveness.-->

<template>
  <div>

    <!--login container-->
    <div id="login" class="container text-font all-but-footer">

      <!--logo, with text-->
      <div class="row justify-content-center">
        <div class="col-3 m-3 text-center">
          <img id="logo" src="../../public/logo_only_med.png" class="img-fluid" alt="logo">
          <p class="company-name-main">REUSABILITY</p>
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
                  <label for="email-input" class="form-label">Email Address</label>
                  <input type="email" class="form-control" id="email-input" ref="eInput" tabindex="1">
                </div>
              </div>

              <!--password input field-->
              <div class="row">
                <div class="col mb-1">
                  <label for="password-input" class="form-label">Password</label>
                  <input type="password" class="form-control" id="password-input" ref="pInput" tabindex="2">
                </div>
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
                  <router-link class="btn btn-lg m-sm-4 mb-4 green-button-transparent" to="/registration" tag="button" type="button" tabindex="4" >Register</router-link>
                </div>

                <!--login button-->
                <div class="col">
                  <button class="btn btn-lg float-end m-sm-4 mb-4 green-button" @click="login()" type="submit" id="loginButton" tabindex="3">Sign In</button>
                </div>
              </div>

            </form>

          </div>
        </div>
      </div>

    </div>
      <FooterSecure class="footer"></FooterSecure>
  </div>
</template>

<script>
import Api from '../Api';
import Cookies from 'js-cookie';
import FooterSecure from "@/components/FooterSecure";

export default {
  name: "Login",
  components: {
    FooterSecure,
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
        Cookies.set('userID', response.data.userId)
        // Also grab JSESSIONID when we have agreed on an implementation with the backend team.
        this.$router.push({ name: 'Home' })
        this.$refs.errorLbl.style.visibility = "hidden";
      })
      .catch((error) => {
        if (error.response) {
          this.$refs.errorLbl.innerText = 'Failed login attempt, email or password incorrect.';
          this.$refs.errorLbl.style.visibility = "visible";
        } else if (error.request) {
          this.$refs.errorLbl.innerText = 'Connection Timeout.';
          this.$refs.errorLbl.style.visibility = "visible";
        } else {
          this.$refs.errorLbl.innerText = 'Connection error.';
          this.$refs.errorLbl.style.visibility = "visible";
        }
      })
    }
  }
}

</script>

<!-------------------------------------------- Invalid Token Page Styling --------------------------------------------->

<style scoped>

/**
 * Makes error message invisible
 */
#error-label {
  visibility: hidden;
}

.footer {
  position: absolute;
  bottom: 0;
  width: 100%;
}

input:focus, textarea:focus {
  outline: none;
  box-shadow: 0 0 2px 2px #2eda77; /* Full freedom. (works also with border-radius) */
  border: 1px solid #1EBABC;
}
</style>