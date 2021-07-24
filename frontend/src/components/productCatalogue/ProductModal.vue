<template>
    <div class="row">
      <div class="col-md-3">
        <div id="carousel-product-images" class="carousel slide" data-bs-ride="carousel">
          <div class="carousel-indicators">
            <button v-for="(image, index) of this.$props.images" v-bind:key="image.id" type="button" data-bs-target="#carousel-product-images" :data-bs-slide-to="index" :class="image.isPrimary ? 'active' : ''"></button>
          </div>
          <div class="carousel-inner">
            <div v-for="image of this.$props.images" v-bind:key="image.id" :class="image.isPrimary ? 'carousel-item active' : 'carousel-item'">
              <img :src="getImageSrc(image.filename)" class="d-block w-100" width="230px" height="230px">
            </div>
          </div>
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
      <div class="col-md-9">
        <div class="card-body px-3">
          <h2 class="card-title">{{ productName }}</h2>
          <br>
          <p class="card-text">
            <b>Product ID:</b> {{ productId }}
            <br>
            <b>Manufacturer:</b> {{ manufacturer }}
            <br>
            <b>RRP:</b> {{ currencySymbol }}{{ recommendedRetailPrice }} {{ currencyCode }}
            <br>
            <b>Description:</b>
            <br>
            {{ description }}
          </p>
          <hr>
          <div class="row">
            <p class="card-text">
              <b>Created:</b> {{ created }}
            </p>
          </div>
        </div>
      </div>
    </div>

</template>


<script>
import Api from "@/Api";

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
      console.log(this.$props.images)
      return Api.getServerURL() + "/" + filename;
    },
  },
  mounted() {
  }
}
</script>

<style scoped>

#carousel-product-images {
  object-fit: cover;
  width: 230px;
  height: 230px;
}

</style>
