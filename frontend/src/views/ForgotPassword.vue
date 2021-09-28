<template>
  <div>
    <div id="main">
      <!--forgot password container-->
      <div id="forgot-password" class="container text-font all-but-footer">

        <!--logo, with text-->
        <div class="row justify-content-center">
          <div class="col-3 m-3 text-center logo-container">
            <img id="logo" src="../../public/logo_only_med.png" class="img-fluid" alt="logo">
            <p class="company-name-main-login company-name-main-font">REUSABILITY</p>
            <p class="company-name-sub-heading"> - Share & Save - </p>
          </div>
        </div>

        <!--form content-->
        <div class="row justify-content-center">
          <div class="col col-lg-6 col-md-8 col-sm-12 my-2 my-lg-0">
            <div id="form-container" class="container shadow">

              <!--password reset header-->
              <div class="row">
                <div class="col">
                  <h3 class="m-4 text-center">Password Reset</h3>
                </div>
              </div>

              <!--email input field-->
              <form @submit.prevent>
                <div class="row">
                  <div class="col mb-4">
                    <label for="email-input" class="form-label">Email Address*</label>
                    <input type="email" class="form-control" id="email-input" ref="email" tabindex="1">
                  </div>
                  <label id="error-message">{{ errorMessage }}</label>
                  <label id="success-message">{{ successMessage }}</label>
                </div>

                <div class="row">
                  <!--cancel button-->
                  <div class="col">
                    <router-link class="btn btn-lg mt-4 mb-4 green-button-transparent" to="/" tag="button" type="button" tabindex="5">Back to Login</router-link>
                  </div>

                  <!--send email button-->
                  <div class="col">
                    <button :class="`btn btn-lg float-end mt-4 mb-4 green-button ${canSendEmail ? '': 'disabled'}`"
                            @click="sendEmail()" type="submit" id="sendEmailButton" tabindex="4">
                      Send Password Reset Email
                    </button>
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
import Footer from "../components/main/Footer";
import Api from "@/Api";

export default {
  name: "ForgotPassword",
  components: {
    Footer
  },
  data() {
    return {
      // the error message to show when an email is not sent
      errorMessage: "",
      successMessage: "",
      canSendEmail: true
    }
  },
  methods: {
    sendEmail() {
      this.canSendEmail = false;
      this.errorMessage = "";
      this.successMessage = "";
      // TODO: add loading indicator here
      Api.forgotPasswordSendEmail(this.$refs.email.value.trim()).then(() => {
        this.successMessage = "A password reset link from reusability.help@gmail.com has successfully been sent to " +
            "your email. Make sure to check your spam folder if it's not in your inbox.";
      })
      .catch((error) => {
        if (error.response && error.response.status === 406) {
          this.errorMessage = "Email does not belong to a registered user.";
        } else {
          this.errorMessage = "Email was unable to be sent (the email address may not exist).";
        }
        this.canSendEmail = true;
      })
    }
  }
}
</script>

<style scoped>

#error-message {
  color: red;
}

#success-message {
  color: #19b092;
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

.all-but-footer {
  padding-bottom: 50px;
}

input:focus, textarea:focus {
  outline: none;
  box-shadow: 0 0 2px 2px #2eda77; /* Full freedom. (works also with border-radius) */
  border: 1px solid #1EBABC;
}

</style>
