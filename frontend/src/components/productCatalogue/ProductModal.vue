<template>
    <div class="row">
      <div class="col-md-3">
        <div v-if="this.$props.images.length > 0" id="carousel-product-images" class="carousel slide" data-bs-ride="carousel">
          <div v-if="this.$props.images.length > 1" class="carousel-indicators">
            <button v-for="(image, index) of this.$props.images" v-bind:key="image.id" type="button" data-bs-target="#carousel-product-images" :data-bs-slide-to="index" :class="image.isPrimary ? 'active' : ''"></button>
          </div>
          <div class="carousel-inner">
            <div v-for="image of this.$props.images" v-bind:key="image.id" :class="image.isPrimary ? 'carousel-item active' : 'carousel-item'">
              <img alt="product-image" :src="getImageSrc(image.filename)" class="d-block w-100" width="230px" height="230px">
            </div>
          </div>
          <div v-if="this.$props.images.length > 1">
            <button class="carousel-control-prev" type="button" data-bs-target="#carousel-product-images" data-bs-slide="prev">
              <span class="carousel-control-prev-icon" aria-hidden="true"></span>
              <span class="visually-hidden">Previous</span>
            </button>
            <button class="carousel-control-next" type="button" data-bs-target="#carousel-product-images" data-bs-slide="next">
              <span class="carousel-control-next-icon" aria-hidden="true"></span>
              <span class="visually-hidden">Next</span>
            </button>
          </div>
        </div>
        <div v-else>
          <img alt="default-product" class="card-img mt-4" :src="require('../../../public/default-product.jpg')" id="product-image">
        </div>
      </div>
      <div class="col-md-9">
        <div class="card-body px-3">
          <h2 class="card-title">{{ productName }}</h2>
          <br>
          <p class="card-text">
            <strong>Product ID:</strong> {{ productId }}
            <br>
            <strong>Manufacturer:</strong> {{ manufacturer }}
            <br>
            <strong>RRP:</strong> {{ currencySymbol }}{{ recommendedRetailPrice }} {{ currencyCode }}
            <br>
            <strong>Description:</strong>
            <br>
            {{ description }}
          </p>
          <hr>
          <div class="row">
            <p class="card-text">
              <strong>Created:</strong> {{ created }}
            </p>
          </div>
        </div>
      </div>
    </div>

</template>


<script>
import Api from "../../Api"

export default {
  name: "ProductModal",
  props: {
    images: {
      type: Array,
      default: null,
      required: false
    },
    productName: {
      type: String,
      default: "NAME",
      required: true
    },
    productId: {
      type: String,
      default: "ID",
      required: true
    },
    description: {
      type: String,
      default: "",
      required: false
    },
    manufacturer: {
      type: String,
      default: "N/A",
      required: false
    },
    recommendedRetailPrice: {
      type: Number,
      // eslint-disable-next-line vue/require-valid-default-prop
      default: "--",
      required: false
    },
    created: {
      type: String,
      default: "N/A",
      required: true
    },
    currencyCode: {
      type: String,
      default: "",
      required: false
    },
    currencySymbol: {
      type: String,
      default: "",
      required: false
    }
  },
  methods: {
    getImageSrc(filename) {
      return Api.getServerURL() + "/" + filename;
    },
  }
}
</script>

<style scoped>

#carousel-product-images {
  object-fit: cover;
  width: 230px;
  height: 230px;
}

#product-image {
  object-fit: cover;
  width: 230px;
  height: 230px;
}

</style>
