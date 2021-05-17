<template>
  <div>
    <!-- Used to create a card -->
    <button class="btn btn-outline-primary green-button" @click="showModal">Create Card</button>

    <!-- Create Card Modal -->
    <div class="modal fade" ref="createCardModal" id="create-card-modal">
      <div class="modal-dialog">
        <div class="modal-content">

          <!-- Modal header-->
          <div class="modal-header">
            <h5 class="modal-title">Create Card</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>

          <!-- Modal body -->
          <div class="modal-body">
            <form class="needs-validation" novalidate @submit.prevent>

              <!-- Modal error message -->
              <div class="row my-lg-2" v-if="modalError">
                <div class="col-12 mx-auto">
                  <div class="alert alert-danger">
                    {{modalError}}
                  </div>
                </div>
              </div>

              <!-- Select a section  -->
              <div class="row my-lg-2">
                <div class="col-md-6 my-2 my-lg-0">
                  <label for="section-selection" class="form-label">What section would you like to post your card?*</label>
                </div>
                <div class="col-md-6 my-2 my-lg-0">
                  <select id="section-selection" name="section-selection" :class="`form-select ${isInvalid(formError.sectionSelection)}`" v-model="sectionSelected" >
                    <option value="" disabled selected>Select section</option>
                    <option :value="sections.FOR_SALE">For Sale</option>
                    <option :value="sections.EXCHANGE">Exchange</option>
                    <option :value="sections.WANTED">Wanted</option>
                  </select>
                  <div class="invalid-feedback" v-if="formError.sectionSelection">
                    {{formError.sectionSelection}}
                  </div>
              </div>
              </div>

              <hr>


              <!-- Pre filled in information -->
              <div class="row my-lg-2">
                <!-- User's full name-->
                <div class="col-md-12 col-6 d-md-flex flex-md-row">
                    <label for="user-full-name" class="fw-bold me-md-2">Name:</label>
                    <p id="user-full-name">{{userFullName}}</p>
                </div>

                <!-- User's location details -->
                <div class="col-md-12 col-6 d-md-flex flex-md-row">
                    <label for="user-location" class="fw-bold me-md-2">Location:</label>
                    <p id="user-location">{{userLocation}}</p>
                </div>
              </div>

              <!-- Card title -->
              <div class="row my-lg-2">
                <div class="col-md-2 ">
                  <label for="card-title" class="fw-bold">Title:</label>
                </div>
                <div class="col-md">
                  <input id="card-title" class="form-control">
                </div>
              </div>

            </form>
          </div>

          <!-- Modal footer -->
          <div class="modal-footer">
            <button type="button" class="btn btn-primary order-1">Create</button>
            <button type="button" class="btn btn-secondary order-0" data-bs-dismiss="modal">Cancel</button>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import { Modal } from 'bootstrap';

export default {
  name: "CreateCardModal",
  data() {
    return {
      modal: null,

      modalError: "",

      /** Keeps track of the selected section */
      sectionSelected: "",
      /** Contains all the possible section values for the select field */
      sections: {
        FOR_SALE: "for-sale",
        WANTED: "wanted",
        EXCHANGE: "exchange"
      },

      /** Contains the user's full name to be displayed as a prefilled value*/
      userFullName: "Your full name",
      /** Contains the prefilled value of the user's address (only city and suburb)*/
      userLocation: "Your city, suburb",


      /** Error messages */
      formError: {
        sectionSelection: ""
      }
    }
  },
  methods: {
    showModal(event) {
      event.preventDefault();

      this.modal.show();
    },
    /**
     * Given a error message deetermines if to return the is-invalid class.
     *
     * @param errorMessage, string, the error message relating to invalid input of a field.
     * @returns {string}, which is 'is-invalid' if there is an error message. Otherwise an empty string.
     */
    isInvalid(errorMessage) {
      return errorMessage !== "" ? 'is-invalid' : "";
    }
  },
  mounted() {
    this.modal = new Modal(this.$refs.createCardModal);
  }
}
</script>

<style scoped>

</style>