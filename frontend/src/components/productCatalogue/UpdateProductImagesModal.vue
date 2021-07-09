<template>

  <!-- Modal -->
  <div class="modal fade" ref="_updateProductImages" tabindex="-1" aria-labelledby="updateProductImages" aria-hidden="true" id="update-product-images">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="btn btn-outline-primary order-0 green-button-transparent" data-bs-dismiss="modal">Back</button>
          <h5 class="modal-title" id="updateProductImagesTitle">Update Product {{value.data.id}} Images</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">

          <!-- Modal form content wrapper-->
          <form class="needs-validation mb-3 px-5" novalidate @submit.prevent>

            <!-- Error message card-->
            <div class="row my-lg-2">
              <div class="col-12 mx-auto">
                <div v-if="formErrorModalMessage" class="alert alert-danger">
                  <label>{{formErrorModalMessage}}</label>
                </div>
              </div>
            </div>

            <!-- Primary Image -->
            <div class="col">
              <img>
            </div>

            <div class="col">
              <!-- Upload -->
              <div class="row">
                <button>Upload Image</button>
              </div>
              <!-- Images -->
              <div class="row">
                <ol>
                  <li v-for="image in images" v-bind:key="image.index"></li>
                </ol>
              </div>
              <!-- Buttons -->
              <div class="row">
                <button class="btn btn-danger">Delete Image</button>
                <button class="btn btn-success">Set Primary Image</button>
              </div>
            </div>

          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Product from "@/configs/Product";

export default {
  name: "UpdateProductImagesModal",
  props: {

    // Product details -- MUST BE V-MODEL therefore MUST BE NAMED VALUE!
    value: {
      type: Product,
      required: true
    },

    // Business id used to not what business to update
    businessId: {
      type: Number,
      required: true
    }
  },
  data() {
    return {
      formErrorModalMessage: "",
      images: []
    }
  },

}
</script>

<style scoped>

</style>