<template>
  <div>
    <!-- Edit Card Modal -->
    <div class="modal fade" ref="editCardModal" id="edit-card-modal" tabindex="0">
      <div class="modal-dialog">
        <div class="modal-content">

          <!-- Modal header-->
          <div class="modal-header">
            <h5 class="modal-title">Edit Card</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close" tabindex="-1"  ></button>
          </div>

          <EditCreateCardModal ref="editCreateModal"/>

          <!-- Modal footer -->
          <div class="modal-footer">
            <button id="edit-card-button" type="button" class="btn green-button order-1" >Save</button>
            <button type="button" class="btn btn-secondary order-0" data-bs-dismiss="modal">Cancel</button>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import {Modal} from 'bootstrap';
import EditCreateCardModal from "./EditCreateCards";

export default {
  name: "EditCardModal",
  components: {EditCreateCardModal},
  data() {
    return {
      modal: null,
    }
  },
  methods: {
    showModal(id) {
      this.$refs.editCreateModal.setData(id)
      this.modal.show();
    },
    hideModal() {
      this.modal.hide();
    }
  },
  mounted() {
    this.modal = new Modal(this.$refs.editCardModal);
  }
}
</script>

<style >

</style>
