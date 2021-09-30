<template>
  <div class="card border-secondary mb-3 px-2 py-2 shadow">
    <div class="row">
      <div class="row" id="mainContent">
        <div class="col-12 col-sm-2">
          <div v-if="this.$props.images.length > 0">
            <div id="carousel-item-images" class="carousel slide" data-bs-ride="carousel">
              <div v-if="this.$props.images.length > 1" class="carousel-indicators">
                <button v-for="(image, index) of this.$props.images" v-bind:key="image.id" type="button" data-bs-target="#carousel-product-images" :data-bs-slide-to="index" :class="image.isPrimary ? 'active' : ''"></button>
              </div>
              <div class="carousel-inner">
                <div v-for="image of this.$props.images" v-bind:key="image.id" :class="image.isPrimary ? 'carousel-item active ratio ratio-1x1' : 'carousel-item ratio ratio-1x1'">
                  <img :src="getImageSrc(image.thumbnailFilename)" class="card-img" alt="listing image">
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
          </div>
          <div class="ratio ratio-1x1" v-else>
            <img class="card-img" :src="require('../../../public/default-image.jpg')" id="listing-item-image" alt="listing default image">
          </div>
        </div>
        <div class="col">
          <div class="card-body px-2" id="card-body">
            <div class="row">
              <div class="col">
                <h4>{{quantity}}x {{ productName }}</h4>
              </div>
              <div class="col d-flex align-items-end flex-column col-5" v-if="!(moreInfo.length===0)">
                <p style="text-align: right">{{moreInfo}}</p>
              </div>
            </div>
            <div class="row py-2" id="main-body">
              <div class="col-md-3">
                <p class="card-text">
                  {{ productId }}
                </p>
                <h6 class="card-text mt-auto" id="price">Price: {{ currencySymbol }}{{ price }} {{ currencyCode }}</h6>
              </div>
              <div class="col d-flex align-items-end flex-column">
                <p>{{ description }}</p>
                <p v-if="barcode != null && barcode !== ''" class="card-text">
                  Barcode: {{ barcode }}
                </p>
                <label v-if="closed" id="closed-label" class="card-text" style="color: red">
                  CLOSED
                </label>
                <button v-else-if="isAdmin" id="withdraw-button" style="border: none; background: none;" @click="deleteListing($event, listingId)">
                  <svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" fill="currentColor" class="bi bi-trash" viewBox="0 0 16 16">
                    <path d="M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0V6z"/>
                    <path fill-rule="evenodd" d="M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1v1zM4.118 4 4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4H4.118zM2.5 3V2h11v1h-11z"/>
                  </svg>
                </button>
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
    listingId: {
      type: Number,
      required: true
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
    fullCloseDate: {
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
    },
    barcode: {
      type: String,
      default: null,
      required: false
    },
    isAdmin: {
      type: Boolean,
      default: false,
      required: true
    }
  },
  data() {
    return {
      closed: false
    }
  },
  methods: {
    getImageSrc(filename) {
      return Api.getServerURL() + "/" + filename;
    },

    /**
     * Checks if the closed date is before the current date and time
     */
    checkClosed() {
      this.closed = (new Date(Date.now()).valueOf() >= Date.parse(this.fullCloseDate).valueOf());
    },
    /**
     * Makes a call to the parent to delete the current listing
     */
    deleteListing(event, listingId) {
      const data = {
        event: event,
        listingId: listingId
      }
      this.$emit('withdrawConfirmation', data);
    }
  },
  watch: {
    closeDate: function () {
      this.checkClosed();
    }
  },
  mounted() {
    this.checkClosed();
  }
}
</script>

<style scoped>

#listing-item-image {
  object-fit: cover;
  width: 100%;
}

#carousel-item img {
  object-fit: cover !important;
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
