<template>
  <!-- Modal -->
  <div class="modal fade" ref="_deleteConversationModal" tabindex="-1" aria-labelledby="deleteConversationModal" aria-hidden="true" id="delete-conversation-modal">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="deleteConversationModalTitle">Delete your conversation with {{userName}}?</h5>
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
              Once you have deleted your copy of the conversation it can't be undone.
            </div>
          </form>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-outline-primary order-0 green-button-transparent" data-bs-dismiss="modal">Cancel</button>
          <button class="btn btn-danger" id="delete-button" @click="deleteConversation()">Delete</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {Modal} from "bootstrap";
import Api from "../../Api"

export default {
  name: "DeleteConversationModal",
  props: {
    // the name of the other user in the conversation.
    userName: {
      type: String,
      required: true
    },
    // the id of the conversation to delete.
    id: {
      type: Number,
      required: true
    },
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
     * Makes a DELETE call to the backend deleting the conversation with the given id.
     * If the deletion is successful, a custom event 'conversationSuccessfullyDeleted' is emitted so the Navbar
     * knows to update the messages. The event is emitted to the Navbar to resolve the issue with the modal being
     * unavailable when it is a component of Messages.
     */
    deleteConversation() {
      Api.deleteConversation(this.id).then(
          (response) => {
            if (response.status === 200) {
              this.modal.hide();
              this.$emit('conversationSuccessfullyDeleted');
            } else {
              this.formErrorModalMessage = "Sorry, something went wrong...";
            }
          }
      ).catch((error) => {
        if (error.request && !error.response) {
          this.$router.push({path: '/timeout'});
        } else if (error.response.status === 403) {
          this.formErrorModalMessage = "Sorry, you do not have permission to delete this conversation.";
        } else if (error.response.status === 406) {
          this.formErrorModalMessage = "Sorry, something went wrong...";
        } else {
          this.$router.push({path: '/timeout'});
          console.log(error.message);
        }
      })
    }
  },
  /**
   * When mounted create the instance of the modal.
   */
  mounted() {
    // Create a modal and attach it to the deleteConversationModal reference.
    this.modal = new Modal(this.$refs._deleteConversationModal);
  }
}
</script>

<style scoped>

</style>