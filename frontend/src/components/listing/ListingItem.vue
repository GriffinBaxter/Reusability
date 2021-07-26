<template>
  <div class="card border-secondary mb-3 px-2 py-2 shadow">
    <div class="row">
      <div class="row" id="mainContent">
        <div class="col-12 col-sm-3">
          <div class="ratio ratio-4x3">
            <div v-if="this.$props.images.length > 0" id="carousel-item-images" class="carousel slide" data-bs-ride="carousel">
              <div v-if="this.$props.images.length > 1" class="carousel-indicators">
                <button v-for="(image, index) of this.$props.images" v-bind:key="image.id" type="button" data-bs-target="#carousel-product-images" :data-bs-slide-to="index" :class="image.isPrimary ? 'active' : ''"></button>
              </div>
              <div class="carousel-inner">
                <div v-for="image of this.$props.images" v-bind:key="image.id" :class="image.isPrimary ? 'carousel-item active' : 'carousel-item'">
                  <img :src="getImageSrc(image.filename)" class="d-block w-100" width="230px" height="230px" alt="listing item image">
                </div>
              </div>
              <div v-if="this.$props.images.length > 1">
                <button class="carousel-control-prev" type="button" data-bs-target="#carousel-item-images" data-bs-slide="prev">
                  <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                  <span class="visually-hidden">Previous</span>
                </button>
                <button class="carousel-control-next" type="button" data-bs-target="#carousel-item-images" data-bs-slide="next">
                  <span class="carousel-control-next-icon" aria-hidden="true"></span>
                  <span class="visually-hidden">Next</span>
                </button>
              </div>
            </div>
            <div v-else>
              <img class="card-img mt-4" :src="require('../../../public/default-product.jpg')" id="listing-item-image" alt="listing item image">
            </div>
          </div>
        </div>
        <div class="col">
          <div class="card-body px-3" id="card-body">
            <div class="row">
              <div class="col">
                <h4 class="card-title">{{quantity}}x {{ productName }}</h4>
              </div>
              <div class="col d-flex align-items-end flex-column col-5" v-if="!(moreInfo.length===0)">
                <p align="right">{{moreInfo}}</p>
              </div>
            </div>
            <div class="row py-2" id="main-body">
              <div class="col-md-3">
                <p class="card-text">
                  {{ productId }}
                </p>
              </div>
              <div class="col d-flex align-items-end flex-column">
                <p>{{ description }}</p>
                <p class="card-text mt-auto" id="price">Price: {{ currencySymbol }}{{ price }} {{ currencyCode }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
      <hr class="mt-2 mb-2" id="lineBreak">
      <div class="row">
        <div class="col-4 text-center">
          Listing Date: {{ listDate }}
        </div>
        <div class="col-4 text-center">
          Closing Date: {{ closeDate }}
        </div>
        <div class="col-4 text-center">
          Expires: {{ expires }}
        </div>
      </div>
    </div>
  </div>

</template>

<script>
import Api from "../../Api"

export default {
  name: "ListingItem",
  props: {
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
    quantity: {
      type: Number,
      // eslint-disable-next-line vue/require-valid-default-prop
      default: "None",
      required: true
    },
    price: {
      type: Number,
      // eslint-disable-next-line vue/require-valid-default-prop
      default: "None",
      required: false
    },
    listDate: {
      type: String,
      default: "None",
      required: true
    },
    closeDate: {
      type: String,
      default: "None",
      required: true
    },
    bestBefore: {
      type: String,
      default: "None",
      required: false
    },
    expires: {
      type: String,
      default: "None",
      required: true
    },
    moreInfo: {
      type: String,
      default: "",
      required: false
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
    },
    images: {
      type: Array,
      default: null,
      required: false
    }
  },
  methods: {
    getImageSrc(filename) {
      return Api.getServerURL() + "/" + filename;
    },
  },
  mounted() {
  }
}
</script>

<style scoped>

#listing-item-image, #carousel-item-images {
  object-fit: cover;
  width: 100%;
}

#lineBreak {
  width: 90%;
  margin-left: 5%;
  margin-right: 5%;
}

#mainContent {
  margin-left: 1px;
}

</style>
