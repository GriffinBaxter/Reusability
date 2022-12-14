<template>

  <!-- Modal -->
  <div class="modal fade" ref="_updateImagesModal" tabindex="-1" aria-labelledby="updateImagesModal" aria-hidden="true"
       id="update-product-images-modal">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <h5 v-if="location === 'Product'" class="modal-title" id="updateImagesModalTitle_Product">Update Product
            {{ value.data.id }}'s Images</h5>
          <h5 v-else-if="location === 'Business'" class="modal-title" id="updateImagesModalTitle_Business">Update
            {{ value.data.name }}'s Images</h5>
          <h5 v-else-if="location === 'User'" class="modal-title" id="updateImagesModalTitle_User">Update
            {{ value.data.firstName }}'s Images</h5>
        </div>

        <div class="modal-body">

          <!-- Modal form content wrapper-->
          <form class="needs-validation mb-3 px-2" novalidate @submit.prevent>

            <!-- Error message card-->
            <div class="row my-lg-2">
              <div class="col-12 mx-auto">
                <div v-if="formErrorModalMessage" class="alert alert-danger">
                  <label>{{ formErrorModalMessage }}</label>
                </div>
              </div>
            </div>

            <div class="row">
              <!-- Primary Image -->
              <div class="col-lg-4">
                <h5>Primary Image:</h5>
                <img class="card-img px-5 px-lg-0 mb-3" :src="require('../../public/default-image.jpg')"
                     id="primary-image" alt="primary image">
              </div>

              <div class="col-lg-8">
                <!-- Upload -->
                <button class="btn green-button" @click="onUploadClick">Upload image</button>
                <input type="file" id="imageUpload" ref="image" @change="uploadImage" name="img"
                       accept="image/png, image/gif, image/jpeg">
                <hr>
                <!-- Images -->
                <div class="row">
                  <div v-if="images.length === 0">
                    No Images Uploaded
                  </div>
                  <div class="row">
                    <div class="col-3" v-for="image in images" v-bind:key="image.id">
                      <img v-if="selectedImage === image.id" class="img-fluid rounded border border-primary border-2"
                           :src="getImageSrc(image.filename)" @click="setSelected(image.id)" alt="product image">
                      <img v-else-if="image.id === primaryImage"
                           class="img-fluid rounded border border-warning border-2" :src="getImageSrc(image.filename)"
                           @click="setSelected(image.id)" alt="product image">
                      <img v-else class="img-fluid rounded" :src="getImageSrc(image.filename)"
                           @click="setSelected(image.id)" alt="product image">
                    </div>
                  </div>
                </div>
                <!-- Buttons -->
                <div class="actionButtons" v-if="selectedImage != null">
                  <hr>
                  <button class="btn btn-danger" id="delete-button" @click="deleteSelectedImage()">Delete Image</button>
                  <button v-if="selectedImage !== primaryImage" class="btn btn-outline-success float-end"
                          @click="setPrimarySelectedImage()">Set Primary Image
                  </button>
                </div>
              </div>
            </div>
          </form>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-outline-primary order-0 green-button-transparent"
                  data-bs-dismiss="modal">Close
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {Modal} from "bootstrap";
import Api from "../Api"
import Product from "../configs/Product"

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
      this.formErrorModalMessage = "";

      // Prevent any default actions
      event.preventDefault();
      this.currentData = this.value;

      if (this.location === "User") {
        this.currentData.data.firstName = this.value.data.firstName
      } else if (this.location === "Business") {
        this.currentData.data.name = this.value.data.name;
      }

      let primaryImageFilename;
      for (let image of this.currentData.data.images) {
        if (image.isPrimary) {
          this.primaryImage = image.id;
          primaryImageFilename = image.filename;
        }
      }

      this.images = this.currentData.data.images;
      if (this.images.length > 0) {
        document.getElementById("primary-image").src = this.getImageSrc(primaryImageFilename);
      } else {
        document.getElementById("primary-image").src = require('../../public/default-image.jpg');
      }

      this.selectedImage = null;
      // Show the modal
      this.modal.show();
    },

    /**
     * Sets/unsets the selected image
     * @param id Id of image to select/unselect
     */
    setSelected(id) {
      if (this.selectedImage === id) {
        this.selectedImage = null;
      } else {
        this.selectedImage = id;
      }
    },

    /**
     * Gets the full image path from the server URL
     * @param filename Name of file to get full path for
     */
    getImageSrc(filename) {
      return Api.getServerURL() + "/" + filename;
    },

    /**
     * get query which will use for image api call
     */
    getQueryForParams() {
      let query;
      switch (this.location) {
        case "User":
          query = "?uncheckedImageType=USER_IMAGE&userId=" + this.$props.id;
          break
        case "Business":
          query = "?uncheckedImageType=BUSINESS_IMAGE&businessId=" + this.$props.id;
          break
        case "Product":
          query = "?uncheckedImageType=PRODUCT_IMAGE&businessId=" + this.$props.id + "&productId=" + this.currentData.data.id;
          break
        default:
          query = "";
          console.log("Location error!")
      }
      return query;
    },

    /**
     * When a user selects an image and clicks delete then a call is made to the backend for the selected image
     * to delete it.
     */
    deleteSelectedImage() {
      Api.deleteImage(this.getQueryForParams(), this.selectedImage)
          .then(() => {
            this.removeImage(this.selectedImage);
          })
          .catch((error) => {
            this.handleError(error);
          })
    },

    /**
     * Removes the selected image from the list of images
     * If image is primary, it also updates the primary image
     * @param imageId Id of image to remove
     */
    removeImage(imageId) {
      this.removeActiveCarouselElements();

      this.selectedImage = null;

      let images = this.currentData.data.images
      for (let i=0; i < images.length; i++) {
        if (images[i].id === imageId) {
          if (this.location !== "Product") {
            let index = (i + 1) % images.length
            if (i !== 0 || images.length > 1) {
              document.getElementById("image-carousel").children[index].classList.add("active");
            }
          }
          images.splice(i, 1);
          break;
        }
      }

      // if image is primary sets primary image to the first item in the list
      if (this.primaryImage === imageId) {
        this.removeActiveCarouselElements();

        if (this.currentData.data.images.length > 0) {
          this.primaryImage = this.currentData.data.images[0].id;
          this.currentData.data.images[0].isPrimary = true;
          document.getElementById("primary-image").src = this.getImageSrc(this.currentData.data.images[0].filename);
          this.$emit("updatePrimary", this.currentData.data.images[0].thumbnailFilename);

        } else {
          this.$emit("updatePrimary", null);
          this.primaryImage = null;
          document.getElementById("primary-image").src = require('../../public/default-image.jpg');
        }
      }

      if (this.location === "Product") {
        this.updateValue(new Product(this.currentData.data));
      }
    },


    /**
     * Sets the selected image to the primary image.
     */
    setPrimarySelectedImage() {
      Api.setPrimaryImage(this.getQueryForParams(), this.selectedImage).then(
          response => {
            if (response.status === 200) {
              this.setPrimary(this.selectedImage);
            }
          }
      ).catch((error) => {
        this.handleError(error);
      })
    },
    /**
     * Sets the selected image to the primary image
     * @param imageId Id of image to set as primary
     */
    setPrimary(imageId) {
      this.removeActiveCarouselElements();

      this.primaryImage = imageId;
      for (const [index, image] of this.currentData.data.images.entries()) {
        if (image.id === imageId) {
          // updates the new primary image
          image.isPrimary = true;
          document.getElementById("primary-image").src = this.getImageSrc(image.filename);

          if (this.location !== "Product") {
            document.getElementById("image-carousel").children[index].classList.add("active");
          }

          // Update Navbar image (BusinessProfile/Profile)
          this.$emit("updatePrimary", image.thumbnailFilename);
        } else if (image.isPrimary) {
          // updates the old primary image
          image.isPrimary = false;
        }
      }
      if (this.location === "Product") {
        this.updateValue(new Product(this.currentData.data));
      }
    },

    /**
     * This method handles the error that is received from the backend when something goes wrong.
     * @param error the error to handle
     */
    handleError(error) {
      if (error.request && !error.response) {
        this.$router.push({path: '/timeout'});
      } else if (error.response.status === 403) {
        this.formErrorModalMessage = "Sorry, you do not have permission to perform this action.";
      } else if (error.response.status === 406) {
        this.formErrorModalMessage = "Sorry, something went wrong...";
      } else {
        this.$router.push({path: '/timeout'});
        console.log(error.message);
      }
    },

    /**
     * Gets the currently selected image for uploading.
     */
    uploadImage() {
      let file = this.$refs.image.files[0];

      let image = new FormData();
      image.append("images", file)

      Api.uploadImage(this.getQueryForParams(), image)
          .then((res) => {
            this.addImage(res)
          }).catch((error) => {
        this.formErrorModalMessage = "Sorry, the file you uploaded is not a valid image.";
        console.log(error.message);
      })
    },
    /**
     * Adds the new image to the list of stored images
     * If list is currently empty also sets image to primary image
     * @param res response from upload image API call
     */
    addImage(res) {
      // If image is the only image set primary as default
      if (this.currentData.data.images.length === 0) {
        res.data.isPrimary = true;
        this.primaryImage = res.data.id;
        this.currentData.data.images.push(res.data);
        document.getElementById("primary-image").src = this.getImageSrc(res.data.filename);
        this.$emit("updatePrimary", res.data.thumbnailFilename)
      } else {
        this.currentData.data.images.push(res.data);
      }
      if (this.location === "Product") {
        this.updateValue(new Product(this.currentData.data));
      }
    },

    updateValue(value) {
      this.$emit('input', value);
    },

    onUploadClick() {
      this.$refs.image.click();
    },

    /**
     * Makes all elements of the image carousel inactive, for use before setting a new active image (irrelevant to
     * product images).
     */
    removeActiveCarouselElements() {
      if (this.location !== "Product") {
        for (let child of document.getElementById("image-carousel").children) {
          child.classList.remove("active");
        }
      }
    }
  },
  mounted: function () {
    // Create a modal and attach it to the updateProductModel reference.
    this.modal = new Modal(this.$refs._updateImagesModal);

    this.currentData = this.value;
  }
}
</script>
