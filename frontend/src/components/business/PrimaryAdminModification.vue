<template>
  <!-- Modal -->
  <div class="modal fade"
       ref="_primaryAdminModification"
       tabindex="-1"
       aria-labelledby="primaryAdminModification"
       aria-hidden="true"
       id="primary-admin-modification-modal">
    <div class="modal-dialog modal-dialog-scrollable">
      <div class="modal-content">

        <div class="modal-header text-center" style="position: relative">
          <div class="col">
            <!-- Business image -->
            <img class="rounded-circle img-fluid business-image"
                 :src="require('../../../public/sample_business_logo.jpg')"
                 alt="Profile Image"/>

            <!-- Business info-->
            <h5>{{ businessName }}</h5>
            <div class="text-secondary">{{ description }}</div>
          </div>
          <button type="button" class="btn-close close-button" data-bs-dismiss="modal" aria-label="Close"/>
        </div>

        <!-- admins -->
        <div class="modal-body">
          <div class="row administrators"
               v-for="admin in adminList"
               v-bind:key="admin.id" style="">
            <div class="col-3">
              <img class="rounded-circle img-fluid user-image"
                   :src="require('../../../public/sample_business_logo.jpg')"
                   alt="Profile Image"/>
            </div>
            <div class="col-9">
              <p style="margin: 0 0 0 0">Name: {{ admin.firstName + " " + admin.lastName }}</p>
              <p style="margin: 0 0 0 0">Email: {{ admin.email }}</p>
            </div>
          </div>
        </div>

        <div class="modal-footer"></div>

      </div>
    </div>
  </div>
</template>

<script>
import {Modal} from "bootstrap";
import Api from "@/Api";

export default {
  name: "PrimaryAdminModification",
  props: {
    businessName: {
      type: String,
      required: true
    },

    description: {
      type: String,
      required: true
    },

    adminList: {
      type: Array,
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
     * get SRC if file been given, otherwise return default image SRC
     *
     * @param filename image file name
     * @returns {string|*} image address
     */
    getImageSrc(filename) {
      if (filename === "") {
        return require('../../../public/default-image.jpg')
      }
      return Api.getServerURL() + "/" + filename;
    },
    /**
     * open modal
     * @param event
     */
    showModel(event) {
      // Prevent any default actions
      event.preventDefault();

      // Show the modal
      this.modal.show();
    }
  },
  mounted() {
    this.modal = new Modal(this.$refs._primaryAdminModification);
  }

}
</script>

<style scoped>
.administrators:hover {
  color: #1EBA8C !important;
  cursor: pointer;
  border: 1px solid;
  box-shadow: 2px 2px 5px #1EBA8C;
}

.administrators {
  color: #969696;
  margin: 8px 20px 8px 20px;
  border: 1px solid;
  border-radius: 5px;
  box-shadow: 2px 2px 5px gray;
}

.business-image {
  width: 200px;
  height: 200px;
  margin-left: auto;
  margin-right: auto;
}

.user-image {
  width: 48px;
  height: 48px;
  float: right;
}

.close-button {
  position: absolute;
  right: 20px;
  top: 20px;
}
</style>