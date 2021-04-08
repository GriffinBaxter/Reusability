<template>
  <div>
  <div id="login" class="container all-but-footer">
    <div class="row justify-content-center">
      <div class="col-3 m-3">
        <img id="logo" src="../../public/logo_only_med.png" class="img-fluid" alt="logo">
      </div>
    </div>
    <div class="row justify-content-center">
      <div class="col-xl-5 col-lg-6 col-md-8 col-sm-12 my-2 my-lg-0">
        <div id="formContainer" class="container shadow ">
          <div class="row">
            <div class="col">
              <h3 class="m-4 text-center">Login</h3>
            </div>
          </div>
          <form @submit.prevent>
            <div class="row">
              <div class="col mb-4">
                <label for="emailInput" class="form-label">Email Address</label>
                <input type="email" class="form-control" id="emailInput" ref="eInput" tabindex="1">
              </div>
            </div>
            <div class="row">
              <div class="col mb-1">
                <label for="passwordInput" class="form-label">Password</label>
                <input type="password" class="form-control" id="passwordInput" ref="pInput" tabindex="2">
              </div>
            </div>
            <div class="row">
              <div class="col mb-2 mb-md-0">
                <label for="loginButton" id="errorLabel" ref="errorLbl" class="text-danger mt-2">Failed login attempt, email or password incorrect.</label>
              </div>
            </div>
            <div class="row">
              <div class="col-5">
                <router-link class="btn btn-lg btn-outline-primary m-sm-4 mb-4" to="/registration" tag="button" type="button" tabindex="4">Register</router-link>
              </div>
              <div class="col">
                <button class="btn btn-lg float-end m-sm-4 mb-4 green-button" @click="login()" type="submit" id="loginButton" tabindex="3">Sign In</button>
              </div>
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
import Api from '../Api';
import Cookies from 'js-cookie';
import Footer from '../components/Footer';

export default {
  name: "Login",
  components: {
    Footer
  },
  methods: {
    login() {
      /*
      Uses Axios to send a login request to the back-end using the inputted email and password.
      Also displays an appropriate error message if the credentials are incorrect,
      or the connection fails.

      If the login is successful, the user is router to their profile page.
       */
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

<style scoped>

#errorLabel {
  visibility: hidden;
}

.all-but-footer {
  min-height: calc(100vh - 200px);
  margin-bottom: 60px;
}

</style>
