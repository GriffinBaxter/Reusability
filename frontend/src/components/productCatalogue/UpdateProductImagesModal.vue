<template>

  <!-- Modal -->
  <div class="modal fade" ref="_updateProductImagesModal" tabindex="-1" aria-labelledby="updateProductImagesModal" aria-hidden="true" id="update-product-images-modal">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="updateProductImagesModalTitle">Update Product {{value.data.id}}'s Images</h5>
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
                <img class="card-img px-5 px-lg-0 mb-3" :src="getImageSrc(primaryImageFilename)" id="primary-image">
              </div>

              <div class="col-lg-8">
                <!-- Upload -->
                <div class="row">
                  <label for="imageUpload">Upload Image:</label>
                  <input type="file" id="imageUpload">
                </div>
                <hr>
                <!-- Images -->
                <div class="row">
                  <div v-if="images.length === 0">
                    No images Uploaded
                  </div>
                  <div class="row">
                    <div class="col-3" v-for="image in images" v-bind:key="image.id">
                      <img v-if="selectedImage === image.id" class="img-fluid rounded border border-primary border-2" :src="getImageSrc(image.filename)" @click="setSelected(image.id)">
                      <img v-else-if="image.id === primaryImage" class="img-fluid rounded border border-warning border-2" :src="getImageSrc(image.filename)" @click="setSelected(image.id)">
                      <img v-else class="img-fluid rounded" :src="getImageSrc(image.filename)" @click="setSelected(image.id)">
                    </div>
                  </div>
                </div>
                <!-- Buttons -->
                <div v-if="selectedImage != null">
                  <hr>
                  <button class="btn btn-danger">Delete Image</button>
                  <button v-if="selectedImage != primaryImage" class="btn btn-outline-success float-end">Set Primary Image</button>
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
import Product from "../../configs/Product";
import {Modal} from "bootstrap";
import Api from "@/Api";

export default {
  name: "UpdateProductImagesModal",
  props: {

    // Product details -- MUST BE V-MODEL therefore MUST BE NAMED VALUE!
    value: {
      type: Product,
      required: true
    },

    // Business id used to know what business to update
    businessId: {
      type: Number,
      required: true
    }
  },
  data() {
    return {
      modal: null,

      selectedImage: null,
      primaryImage: 0,
      primaryImageFilename: "",

      formErrorModalMessage: "",

      // temp image values
      images: [
        {
          src:require("../../../public/apples.jpg"),
          id:0
        },
        {
          src:require("../../../public/clothes.jpg"),
          id:1
        }
      ],

      // Create the object that will store the data
      currentProduct: new Product(this.value.data)
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
      if (!this.$refs._updateProductImagesModal.classList.contains("show")) {
        // Update the placeholders
        this.currentProduct.data.id = this.value.data.id;
        this.currentProduct.data.name = this.value.data.name;
        this.currentProduct.data.description = this.value.data.description;
        this.currentProduct.data.manufacturer = this.value.data.manufacturer;
        this.currentProduct.data.recommendedRetailPrice = this.value.data.recommendedRetailPrice;
        this.currentProduct.data.images = this.value.data.images;
      }

      for (let image of this.currentProduct.data.images) {
        if (image.isPrimary) {
          this.primaryImage = image.id
          this.primaryImageFilename = image.filename;
        }
      }
      
      this.images = this.currentProduct.data.images;

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
    }
  },
  mounted() {
    // Create a modal and attach it to the updateProductModel reference.
    this.modal = new Modal(this.$refs._updateProductImagesModal);

    // temp
    this.primaryImage = 0;
  }
}
</script>

<style scoped>

</style>