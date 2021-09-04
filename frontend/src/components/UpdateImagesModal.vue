<template>

  <!-- Modal -->
  <div class="modal fade" ref="_updateImagesModal" tabindex="-1" aria-labelledby="updateImagesModal" aria-hidden="true" id="update-product-images-modal">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <h5 v-if="location === 'Product'" class="modal-title" id="updateImagesModalTitle_Product">Update Product {{value.data.id}}'s Images</h5>
          <h5 v-else-if="location === 'Business'" class="modal-title" id="updateImagesModalTitle_Business">Update {{value.data.name}}'s Images</h5>
          <h5 v-else-if="location === 'User'" class="modal-title" id="updateImagesModalTitle_User">Update {{value.data.firstName}}'s Images</h5>
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

            <div class="row">
              <!-- Primary Image -->
              <div class="col-lg-4">
                <h5>Primary Image:</h5>
                <img class="card-img px-5 px-lg-0 mb-3" :src="require('../../public/default-product.jpg')" id="primary-image" alt="primary image">
              </div>

              <div class="col-lg-8">
                <!-- Upload -->
                  <button class="btn green-button" @click="onUploadClick">Upload image</button>
                  <input type="file" id="imageUpload" ref="image" @change="getImage" name="img"
                         accept="image/png, image/gif, image/jpeg">
                <hr>
                <!-- Images -->
                <div class="row">
                  <div v-if="images.length === 0">
                    No Images Uploaded
                  </div>
                  <div class="row">
                    <div class="col-3" v-for="image in images" v-bind:key="image.id">
                      <img v-if="selectedImage === image.id" class="img-fluid rounded border border-primary border-2" :src="getImageSrc(image.filename)" @click="setSelected(image.id)" alt="product image">
                      <img v-else-if="image.id === primaryImage" class="img-fluid rounded border border-warning border-2" :src="getImageSrc(image.filename)" @click="setSelected(image.id)" alt="product image">
                      <img v-else class="img-fluid rounded" :src="getImageSrc(image.filename)" @click="setSelected(image.id)" alt="product image">
                    </div>
                  </div>
                </div>
                <!-- Buttons -->
                <div class="actionButtons" v-if="selectedImage != null">
                  <hr>
                  <button class="btn btn-danger" id="delete-button" @click="deleteSelectedImage()">Delete Image</button>
                  <button v-if="selectedImage !== primaryImage" class="btn btn-outline-success float-end"
                  @click="setPrimarySelectedImage()">Set Primary Image</button>
                </div>
              </div>
            </div>
          </form>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-outline-primary order-0 green-button-transparent" data-bs-dismiss="modal">Close</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Product from "../configs/Product";
import {Modal} from "bootstrap";
import Api from "../Api"

export default {
  name: "UpdateImagesModal",
  props: {

    // Product/Business/User details -- MUST BE V-MODEL therefore MUST BE NAMED VALUE!
    value: {
      type: Object,
      required: true
    },

    // id used to know what business product belongs to update
    id: {
      type: Number,
      required: false
    },

    // Current location ("Product", "Business", "User")
    location: {
      type: String,
      required: true
    }

  },
  data() {
    return {
      modal: null,

      selectedImage: null,
      primaryImage: 0,
      primaryImageFilename: "",

      // if an error occurs when a user performs an action then the appropriate error message needs to be displayed.
      formErrorModalMessage: "",

      images: [],

      // Create the object that will store the data
      currentData: null
    }
  },
  methods: {
    /**
     * Prevents the default call onClick and updates the placeholder values before show the modal.
     * @param event The event (i.e. click event) that triggered the call.
     */
    showModel(event) {
      // Prevent any default actions
      event.preventDefault();

      // If the modal is already showing prevent the placeholders from being updated.
      if (!this.$refs._updateImagesModal.classList.contains("show")) {
        // Update the placeholders
        this.currentData.data.id = this.value.data.id;
        this.currentData.data.images = this.value.data.images;
      }

      if (this.location === "User") {
        this.currentData.data.firstName = this.value.data.firstName
      } else if (this.location === "Business") {
        this.currentData.data.name = this.value.data.name;
      }

      for (let image of this.currentData.data.images) {
        if (image.isPrimary) {
          this.primaryImage = image.id;
          this.primaryImageFilename = image.filename;
        }
      }

      this.images = this.currentData.data.images;

      if (this.images.length > 0) {
        document.getElementById("primary-image").src = this.getImageSrc(this.primaryImageFilename);
      } else {
        document.getElementById("primary-image").src = require('../../public/default-product.jpg');
      }

      this.selectedImage = null;
      // Show the modal
      this.modal.show();
    },

    setSelected(id) {
      if (this.selectedImage === id) {
        this.selectedImage = null;
      } else {
        this.selectedImage = id;
      }
    },

    getImageSrc(filename) {
      return Api.getServerURL() + "/" + filename;
    },

    /**
     * When a user selects an image and clicks delete then a call is made to the backend for the selected image
     * to delete it.
     */
    deleteSelectedImage() {
      if (this.location === "Product"){
        Api.deleteProductImage(this.businessId, this.currentData.data.id , this.selectedImage).then(
            response => {
              if (response.status === 200) {
                location.reload();
              } else {
                this.formErrorModalMessage = "Sorry, something went wrong...";
              }
            }
        ).catch((error) => {
          if (error.request && !error.response) {
            this.$router.push({path: '/timeout'});
          } else if (error.response.status === 403) {
            this.formErrorModalMessage = "Sorry, you do not have permission to delete this image.";
          } else if (error.response.status === 406) {
            this.formErrorModalMessage = "Sorry, something went wrong...";
          } else {
            this.$router.push({path: '/timeout'});
            console.log(error.message);
          }
        })
      } else if (this.location === "Business") {
        console.log("To be implemented")
      } else if (this.location === "User") {
        console.log("To be implemented")
      }
    },

    /**
     * Sets the selected image to the primary image.
     */
    setPrimarySelectedImage() {
      if (this.location === "Product") {
        Api.setPrimaryImage(this.businessId, this.currentData.data.id, this.selectedImage).then(
            response => {
              if (response.status === 200) {
                location.reload();
              } else {
                this.formErrorModalMessage = "Sorry, something went wrong...";
              }
            }
        ).catch((error) => {
          if (error.request && !error.response) {
            this.$router.push({path: '/timeout'});
          } else if (error.response.status === 403) {
            this.formErrorModalMessage = "Sorry, you do not have permission to change the primary image.";
          } else if (error.response.status === 406) {
            this.formErrorModalMessage = "Sorry, something went wrong...";
          } else {
            this.$router.push({path: '/timeout'});
            console.log(error.message);
          }
        })
      } else if (this.location === "Business") {
        console.log("To be implemented")
      } else if (this.location === "User") {
        console.log("To be implemented")
      }
    },

    /**
     * Gets the currently selected image for uploading.
     */
    getImage() {
      let file = this.$refs.image.files[0];

      let image = new FormData();
      image.append("images", file)

      if (this.location === "Product") {
        Api.uploadProductImage(this.$props.businessId, this.currentData.data.id, image)
            .then(() => {
              location.reload();
            }).catch((error) => {
          this.formErrorModalMessage = "Sorry, the file you uploaded is not a valid image.";
          console.log(error.message);
        })
      } else if (this.location === "Business") {
        console.log("To be implemented")
      } else if (this.location === "User") {
        console.log("To be implemented")
      }
    },

    onUploadClick() {
      this.$refs.image.click();
    }
  },
  mounted() {
    // Create a modal and attach it to the updateProductModel reference.
    this.modal = new Modal(this.$refs._updateImagesModal);

    if (this.location === "Product") {
      this.currentData = new Product(this.value.data)
    } else if (this.location === "Business" || this.location === "User") {
      this.currentData = this.value
    }
        // temp
    this.primaryImage = 0;
  }
}
</script>
