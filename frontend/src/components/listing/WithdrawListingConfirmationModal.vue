<template>
  <!-- Modal -->
  <div class="modal fade" ref="_withdrawListingConfirmationModal" tabindex="-1"
       aria-labelledby="withdrawListingConfirmationModal" aria-hidden="true" id="withdraw-listing-confirmation-modal">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="currencyChangeModalTitle">
            Are you sure you would like to withdraw this listing from {{ businessName }}?
          </h5>
        </div>

        <div class="modal-body">
          <!-- Modal form content wrapper-->
          <form class="needs-validation mb-3 px-2" novalidate @submit.prevent>

            <!-- Information -->
            <div class="row">
              <h5>{{ productName }}</h5>
              <br>
              <b>Quantity:</b> {{ quantity }}
              <br><br>
              <b>Price:</b> {{ currencySymbol }}{{ price }} {{ currencyCode }}
            </div>
          </form>
        </div>
        <div class="modal-footer">
          <button class="btn btn-outline-primary green-button-transparent me-auto" data-bs-dismiss="modal">
            Cancel
          </button>
          <button type="button" class="btn btn-danger">Withdraw Listing</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {Modal} from "bootstrap";

export default {
  name: "WithdrawListingConfirmationModal",
  props: {
    businessName: {
      type: String,
      required: true
    },
    productName: {
      type: String,
      required: true
    },
    quantity: {
      type: String,
      required: true
    },
    price: {
      type: String,
      required: true
    },
    currencySymbol: {
      type: String,
      required: true
    },
    currencyCode: {
      type: String,
      required: true
    },
  },
  data() {
    return {
      modal: null,
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
  },
  /**
   * When mounted create the instance of the modal.
   */
  mounted() {
    // Create a modal and attach it to the withdrawListingConfirmationModal reference.
    this.modal = new Modal(this.$refs._withdrawListingConfirmationModal);
  }
}
</script>

<style scoped>

.row {
  display: block;
  text-align: center;
}

b {
  padding: 0;
}

.modal-dialog {
  max-width: 550px;
}

</style>
