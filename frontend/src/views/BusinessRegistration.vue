<template>
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
        <div class="row my-lg-2">
          <div class="col-lg-4 my-2 my-lg-0">
            <label>Name*</label>
          </div>
        </div>
        <div class="row my-lg-2">
          <div class="col-lg-4 my-2 my-lg-0">
            <label>Type*</label>
          </div>

          <div class="col my-2 my-lg-0">
            <label>Address Autofill (Optional)</label>
          </div>
        </div>
        <div class="row my-lg-2">
          <div class="col-lg-6 my-2 my-lg-0">
            <label>Street Address</label>
          </div>
          <div class="col-lg-6 my-2 my-lg-0">
            <label>Suburb</label>
          </div>
        </div>
        <div class="row my-lg-2">
          <div class="col-lg-6 my-2 my-lg-0">
            <label>City</label>
          </div>

          <div class="col-lg-6 my-2 my-lg-0">
            <label>State/Region</label>
          </div>
        </div>

        <div class="row my-lg-2">
          <div class="col-lg-6 my-2 my-lg-0">
            <label>Country*</label>
          </div>

          <div class="col-lg-6 my-2 my-lg-0">
            <label>Postcode</label>
          </div>
        </div>

        <div class="row my-lg-2">
          <div class="col my-2 my-lg-0">
            <label>Description</label>
          </div>
        </div>
      </div>
    </div>
    <Footer/>
  </div>
</template>

<script>
import Footer from "../components/Footer";

export default {
  name: "BusinessRegistration",
  components: {
    Footer,
  },
}
</script>

<style scoped>

label {
  text-align: left;
  display: flex;
  flex-direction: column;
}

#register-form {
  border-radius: 2%;
}

</style>