<template>
  <!-- Modal -->
  <div class="modal fade"
       ref="_primaryAdminModification"
       tabindex="-1"
       aria-labelledby="primaryAdminModification"
       data-bs-backdrop="static"
       aria-hidden="true"
       id="primary-admin-modification-modal">
    <div class="modal-dialog modal-dialog-scrollable">
      <div class="modal-content">

        <div class="modal-header text-center" style="position: relative">
          <div class="col">
            <!-- Business image -->
            <img class="rounded-circle img-fluid business-image"
                 :src="getPrimaryImageSrc(businessInfo.images)"
                 alt="Profile Image"/>

            <!-- Business info-->
            <h5>{{ businessInfo.name }}</h5>
            <div class="text-secondary">{{ businessInfo.description }}</div>
          </div>
          <button type="button"
                  class="btn-close close-button"
                  data-bs-dismiss="modal"
                  aria-label="Close"
                  @click="selectedUser = null"/>
        </div>

        <!-- admins -->
        <div class="modal-body" v-if="selectedUser === null">
          <div :id="'primaryAdminCandidate_' + admin.id"
               class="row administrators"
               v-for="admin in adminList"
               v-bind:key="admin.id"
               @click="selectedUser = admin">
            <div class="col-3">
              <img class="rounded-circle img-fluid user-image"
                   :src="getPrimaryImageSrc(admin.images)"
                   alt="Admin Image"/>
            </div>
            <div class="col-9">
              <p style="margin: 0 0 0 0">Name: {{ admin.firstName + " " + admin.lastName }}</p>
              <p style="margin: 0 0 0 0">Email: {{ admin.email }}</p>
            </div>
          </div>

          <div id="noAdminInListMessage" v-if="adminList.length === 0">
            You are the only administrator for this business.
          </div>
        </div>

        <div class="modal-body" v-else>
          <h6> Are you sure you want to make
            <strong>{{ selectedUser.firstName }} {{ selectedUser.lastName }}</strong>
            the primary administrator?</h6>

          <button id="backButton"
                  type="button"
                  class="btn btn-outline-success"
                  @click="selectedUser = null">
            Back
          </button>

          <button id="confirmButton"
                  type="button"
                  class="btn btn-danger"
                  @click="setPrimaryAdmin(selectedUser.id)">
            Confirm
          </button>
        </div>

        <div class="modal-footer"/>

      </div>
    </div>
  </div>
</template>

<script>
import {Modal} from "bootstrap";
import Api from "../../Api";
import Business from "../../configs/Business";

export default {
  name: "PrimaryAdminModification",
  props: {
    businessInfo: {
      type: Object,
      required: true
    },

    adminList: {
      type: Array,
      required: true
    }
  },
  data() {
    return {
      modal: null,
      selectedUser: null,
    }
  },
  methods: {
    /**
     * get primary image SRC for a user if given images not null, otherwise return default image SRC
     *
     * @param images images for a user
     * @returns {string|*} primary image address
     */
    getPrimaryImageSrc(images) {
      if (images === undefined || images.length === 0) {
        return require('../../../public/default-image.jpg')
      }
      const primaryImage = images.filter((image) => image.isPrimary)
      return Api.getServerURL() + '/' + primaryImage[0].thumbnailFilename;
    },
    /**
     * try to set the user which has given id to a primary admin
     */
    setPrimaryAdmin(id) {
      const url = document.URL;
      const urlID = url.substring(url.lastIndexOf('/') + 1);

      const businessData = {
        primaryAdministratorId: id,
        name: this.businessInfo.name,
        description: this.businessInfo.description,
        address: this.businessInfo.address,
        businessType: this.businessInfo.businessType,
        currencySymbol: this.businessInfo.currencySymbol,
        currencyCode: this.businessInfo.currencyCode
      }
      Api.editBusiness(urlID, new Business(businessData))
          .then(() => {
            location.reload()
          })
          .catch((error) => {
            if (error.response) {
              if (error.response.status === 400) {
                this.toastErrorMessage = '400 Bad request; invalid business data';
              } else if (error.response.status === 409) {
                this.businessNameErrorMsg = 'Business with name already exists';
              } else {
                this.toastErrorMessage = `${error.response.status} Unexpected error occurred!`;
              }
            } else if (error.request) {
              this.toastErrorMessage = 'Timeout occurred';
            } else {
              this.toastErrorMessage = 'Unexpected error occurred!';
            }
          });
      this.modal.hide();
    },
    /**
     * open modal
     * @param event
     */
    showModel(event) {
      // Prevent any default actions
      event.preventDefault();
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

#backButton {
  margin-left: 75px;
}

#confirmButton {
  margin-right: 75px;
  float: right;
}
</style>