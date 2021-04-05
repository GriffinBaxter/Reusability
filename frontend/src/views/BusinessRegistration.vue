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
        <form id="registration" class="needs-validation mb-3 px-5" novalidate @submit.prevent>
        <div class="row my-lg-2">
          <div class="col my-2 my-lg-0">
            <label for="business-name">Name*</label>
            <input id="business-name" name="business-name" tabindex="1" type="text" v-model="businessName"
                   :maxlength="config.businessName.maxLength" required class="form-control">
          </div>
        </div>
        <div class="row my-lg-2">
          <div class="col-lg-4 my-2 my-lg-0">
            <label for="business-type">Type*</label>
            <select id="business-type" name="business-type" tabindex="2" v-model="businessType" required class="form-control">
              <option value="" disabled>Please Select Business Type</option>
              <option v-for="option in types" :key="option.bType" :value="option.value">
                {{ option.value }}
              </option>
            </select>
          </div>

          <div class="col my-2 my-lg-0">
            <label for="business-address">Address Autofill (Optional)</label>
            <input id="business-address" name="business-address" ref="businessAddressInput" type="text" tabindex="3"
                   :maxlength="config.businessAddress.maxLength" autocomplete="off" class="form-control">
          </div>
        </div>
        <div class="row my-lg-2">
          <div class="col-lg-6 my-2 my-lg-0">
            <label for="streetAddress">Street Address</label>
            <input tabindex="4" id="streetAddress"
                   name="streetAddress" ref="streetAddress" required autocomplete="off" class="form-control">
          </div>
          <div class="col-lg-6 my-2 my-lg-0">
            <label for="suburb">Suburb</label>
            <input tabindex="5" name="suburb" id="suburb" ref="suburb"
                   autocomplete="off" required class="form-control">
          </div>
        </div>
        <div class="row my-lg-2">
          <div class="col-lg-6 my-2 my-lg-0">
            <label for="city">City</label>
            <input tabindex="6" name="city" id="city" ref="city"
                   autocomplete="off" required class="form-control">
          </div>

          <div class="col-lg-6 my-2 my-lg-0">
            <label for="region">State/Region</label>
            <input tabindex="7" name="region" id="region" ref="region"
                   autocomplete="off" required class="form-control">
          </div>
        </div>

        <div class="row my-lg-2">
          <div class="col-lg-6 my-2 my-lg-0">
            <label for="country">Country*</label>
            <input tabindex="8" id="country" name="country"
                   ref="country" autocomplete="off" required class="form-control">
          </div>

          <div class="col-lg-6 my-2 my-lg-0">
            <label for="postcode">Postcode</label>
            <input tabindex="9" id="postcode" name="postcode"
                   ref="postcode" autocomplete="off" required class="form-control">
          </div>
        </div>

        <div class="row my-lg-2">
          <div class="col my-2 my-lg-0">
            <label for="description">Description</label>
            <textarea id="description" name="description" tabindex="10" rows="5" cols="70" v-model="description"
                      :maxlength="config.description.maxLength" class="form-control"
                      style="resize: none"/>
          </div>
        </div>

        </form>
      </div>
    </div>
    <Footer/>
  </div>
</template>

<script>
import {Business} from "../Api";
import Footer from "../components/Footer";

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
      types: [
        { bType: 'ACCOMMODATION AND FOOD SERVICES', value: 'Accommodation and Food Services' },
        { bType: 'RETAIL TRADE', value: 'Retail Trade' },
        { bType: 'CHARITABLE ORGANISATION', value: 'Charitable Organisation' },
        { bType: 'NON-PROFIT ORGANISATION', value: 'Non-Profit Organisation' }
      ],

      // Business type related variables
      businessType: "",

      // Description related variables
      description: "",

      // Address autocompletion related variables
      address: "",
      addresses: [],
      autocompleteFocusIndex: 0,
      addressResultProperties: []
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

/*------------------ Hide arrows from input numbers ---------------------*/
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

input:focus, textarea:focus, #register-button:focus{
  outline: none;     /* oranges! yey */
  box-shadow: 0 0 2px 2px #1EBA8C; /* Full freedom. (works also with border-radius) */
  border: 1px solid #1EBABC;
}
/*------------------------------------------------------------------------*/

</style>