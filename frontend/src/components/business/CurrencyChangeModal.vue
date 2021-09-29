<template>
  <!-- Modal -->
  <div class="modal fade" ref="_currencyChangeModal" tabindex="-1" aria-labelledby="currencyChangeModal" aria-hidden="true" id="currency-change-modal">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="currencyChangeModalTitle">Would you like to change the currency associated with {{businessName}}?</h5>
        </div>

        <div class="modal-body">
          <!-- Modal form content wrapper-->
          <form class="needs-validation mb-3 px-2" novalidate @submit.prevent>
            <!-- Error message card-->
            <div class="row my-lg-2">
              <div class="col-12 mx-auto">
                <div v-if="formErrorModalMessage" class="alert alert-danger">
                  <label>{{formErrorModalMessage}}</label>
                </div>
              </div>
            </div>

            <!-- Information -->
            <div class="row">
              Since you have changed the country of your business you have the option to update the currency associated
              with your business.
              <br> <br>
              Note: the currency symbol and code will be changed, but prices will not be converted.
            </div>
          </form>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-danger me-auto" data-bs-dismiss="modal">Cancel</button>
          <button class="btn btn-outline-primary green-button-transparent" id="keep-currency" @click="keepCurrency()">Keep Current Currency</button>
          <button class="btn green-button" id="update-currency" @click="updateCurrency()">Update Currency</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {Modal} from "bootstrap";
import CurrencyAPI from "../../currencyInstance";

export default {
  name: "CurrencyChangeModal",
  props: {
    // the name of the business
    businessName: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      modal: null,

      // if an error occurs when a user performs an action then the appropriate error message needs to be displayed.
      formErrorModalMessage: "",
    }
  },
  methods: {
    /**
     * Prevents the default call onClick and shows the modal.
     * @param event The event (i.e. click event) that triggered the call.
     */
    showModal(event) {
      // Prevent any default actions
      event.preventDefault();
      // Show the modal
      this.modal.show();
    },
    /**
     * Emit a custom event so that the EditBusinessProfile page knows that the user
     * does not want to change the currency for the business.
     */
    keepCurrency() {
      this.$emit('currencyChange', null, null);
      this.modal.hide();
    },
    /**
     * Make a call to REST Countries API to retrieve currency code and symbol for country of business.
     * Then emit a custom event containing the retrieved data so that the EditBusinessProfile page can update
     * currency information accordingly.
     */
    updateCurrency() {
      CurrencyAPI.currencyQuery(this.$parent.$refs.country.value).then((response) => {
        const code = Object.keys(response.data[0].currencies)[0];
        const symbol = response.data[0].currencies[code].symbol;
        this.$emit('currencyChange', code, symbol);
        this.modal.hide();
      }).catch((error) => console.log(error))
    }
  },
  /**
   * When mounted create the instance of the modal.
   */
  mounted() {
    // Create a modal and attach it to the currencyChangeModal reference.
    this.modal = new Modal(this.$refs._currencyChangeModal);
  }
}
</script>
