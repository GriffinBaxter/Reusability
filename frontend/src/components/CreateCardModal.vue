<template>
  <div>
    <!-- Used to create a card -->
    <button id="open-create-card-modal-button" class="btn green-button-transparent col-12" @click="showModal">Create Card</button>

    <!-- Create Card Modal -->
    <div class="modal fade" ref="createCardModal" id="create-card-modal" tabindex="0">
      <div class="modal-dialog">
        <div class="modal-content">

          <!-- Modal header-->
          <div class="modal-header">
            <h5 class="modal-title">Create Card</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close" tabindex="-1"  ></button>
          </div>

          <EditCreateCardModal ref="editCreateModal"/>

          <!-- Modal footer -->
          <div class="modal-footer">
            <button id="create-card-button" type="button" class="btn green-button order-1" @click="createNewCard">Create</button>
            <button type="button" class="btn btn-secondary order-0" data-bs-dismiss="modal">Cancel</button>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import {Modal} from 'bootstrap';
import EditCreateCardModal from "./marketplace/EditCreateCards";

export default {
  name: "CreateCardModal",
  components: {
    EditCreateCardModal
  },
  data() {
    return {
      modal: null,
    }
  },
  methods: {
    showModal(event) {
      event.preventDefault();

      this.$refs.editCreateModal.resetData()
      this.modal.show()
    },
    hideModal() {
      this.modal.hide()
    },
    createNewCard(event) {
      event.preventDefault()
      this.$refs.editCreateModal.createNewCard();
    }
  },
  mounted() {
    this.modal = new Modal(this.$refs.createCardModal)
  }
}
</script>

<style >

</style>
