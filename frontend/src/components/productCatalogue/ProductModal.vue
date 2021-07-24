<template>
    <div class="row">
      <div class="col-md-3">
        <img class="card-img mt-4" :src="require('../../../public/default-product.jpg')" id="product-image">
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
  mounted() {
    document.getElementById("product-image").src = require('../../../public/default-product.jpg');
    if (this.images.length > 0) {
      for (let image of this.images) {
        if (image.isPrimary) {
          document.getElementById("product-image").src = Api.getServerURL() + "/" + image.filename;
          break;
        }
      }
    }
  }
}
</script>

<style scoped>
img {
  object-fit: cover;
  width: 230px;
  height: 230px;
}
</style>
