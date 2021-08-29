<template>
  <!-- Modal body -->
  <div class="modal fade" ref="messageModal" id="message-modal" tabindex="0">
    <div class="modal-dialog">
      <div class="modal-content">
        <!-- Modal header-->
        <div class="modal-header">
          <h5 class="modal-title">Message</h5>
        </div>

        <div class="modal-body">
          <h6>Card: {{this.card}}</h6>
          <h6>Recipient: {{this.creator}}</h6>
          <textarea v-model="message" style="min-width: 100%" :maxlength="300" id="message-body"></textarea>
          <!-- Modal error message -->
          <div class="row my-lg-2" v-if="modalError">
            <div class="col-12 mx-auto">
              <div class="alert alert-danger">
                {{modalError}}
              </div>
            </div>
          </div>
        </div>

        <div class="modal-footer">
          <button id="cancel-message-button"
                  type="button"
                  class="btn btn-outline-danger"
                  style="float:left"
                  @click="reset"
                  data-bs-dismiss="modal"
                  aria-label="Cancel">Cancel</button>
          <button id="send-button"
                  type="button"
                  class="btn btn-outline-success"
                  style="float:right"
                  @click="sendMessage"
                  aria-label="Send">Send</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

import {Modal} from "bootstrap";
import Api from "../../Api";

export default {
  name: "MessageModal",
  data() {
    return {
      modal: null,
      message: "",
      modalError: "",
      creator: "",
      creatorId: "",
      card: "",
      cardId: ""
    }
  },
  methods: {
    /**
     * Validates the message and then calls the API to send the message before closing the modal if it's successful.
     * If any errors are found, they are displayed on the modal and the modal is not closed.
     */
    sendMessage() {
      this.modalError = "";
      if (this.message.length < 1 || this.message.length > 300) {
        this.modalError = "Message length must be between 1 and 300 characters long"
      } else {
        Api.sendMessage(this.cardId, this.creatorId, this.message).then(() => {
          this.modal.hide();
          this.reset();
        }).catch((error) => {
          if (error.require && !error.response) {
            this.$router.push({path: '/timeout'});
          } else if (error.response.status === 400) {
            this.modalError = error.response.data;
          } else if (error.response.status === 401) {
            this.$router.push({path: '/invalidtoken'});
          } else if (error.response.status === 406) {
            this.$router.push({path: '/noCard'});
          } else {
            this.$router.push({path: '/timeout'});
            console.log(error.message);
          }
        });
      }
    },
    /**
     * Resets all the fields of the modal - triggered by clicking the "Cancel" button or when the API call is successful
     */
    reset() {
      this.modalError = "";
      this.message = "";
    },
    showModal(creator, creatorId, card, cardId) {
      this.creator = creator;
      this.creatorId = creatorId;
      this.card = card;
      this.cardId = cardId;
      this.modal.show();
    },
  },
  mounted() {
    this.modal = new Modal(this.$refs.messageModal);
  }
}
</script>

<style scoped>

</style>