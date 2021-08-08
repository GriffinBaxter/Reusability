<template>
  <div class="card" style="width: 18rem;">
    <div class="row">
      <div class="col">
        <img :src="require('../../../public/default-product.jpg')" class="card-img-top" alt="default-image">
      </div>
      <div class="col p-0">
          <div class="row">
            <div class="card-body" id="price-div">
              <h3>{{ '$' + price }}</h3>
            </div>
          </div>
          <div class="row">
            <div class="card-body">
              <a class="btn btn-primary green-button" id="seller-info-button"
                 data-bs-toggle="popover" data-bs-trigger="hover focus"
                 v-bind:title="inventoryItem.product.business.name"
                 data-bs-content="Address line 1 <br> address line 2 and three <br> and so many cities in this country <br> address <br>"
                 data-bs-placement="top">Seller Info</a>
            </div>
          </div>
        </div>
    </div>
    <div class="row">
      <div class="col">
        <div class="card-body pb-0 py-1">
        </div>
        <ul class="list-group list-group-flush">
          <li class="list-group-item pb-1 "><h5>{{ inventoryItem.product.name + ' x' + quantity }}</h5></li>
          <li class="list-group-item">{{ 'Closing Date: ' + closes }}</li>
          <li class="list-group-item">{{ 'Expires: ' + inventoryItem.expires }}</li>
        </ul>
      </div>
    </div>

  </div>
</template>

<script>
import {Popover} from "bootstrap";

export default {
  name: "BrowseListingCard",
  props: {
    id: {
      type: Number,
      default: 0,
      required: true
    },
    inventoryItem: {
      type: Object,
      default: function () {return {}},
      required: true
    },
    created: {
      type: String,
      default: "",
      required: true
    },
    closes: {
      type: String,
      default: "",
      required: true
    },
    isBookmarked: {
      type: Boolean,
      default: false,
      required: true
    },
    moreInfo: {
      type: String,
      default: "",
      required: true
    },
    price: {
      type: Number,
      default: 0,
      required: true
    },
    quantity: {
      type: Number,
      default: 0,
      required: true
    },
    totalBookmarks: {
      type: Number,
      default: 0,
      required: true
    }
  },
  data() {
    return {
      tooltipList: [],
    }
  },
  methods: {
    routeToSaleListing(index, businessId) {
      this.$router.push({
        path: `/businessProfile/${businessId}/listings/${index}`
      });
    },
    routeToSeller(businessId) {
      this.$router.push({
        path: `/businessProfile/${businessId}`
      });
    }
  },
  mounted() {
    const popoverTriggerList = Array.from(document.querySelectorAll('[data-bs-toggle="popover"]'));
    this.tooltipList = popoverTriggerList.map(function(popoverTriggerElement) {
      return new Popover(popoverTriggerElement, {sanitize: false, html: true});
    })
  }
}
</script>

<style scoped>

#seller-info-button {
  width: 82%;
}

/*#price-div {*/
/*  margin-left: 1.3em;*/
/*}*/

.card {
  margin: 0.5em 0.5em 0.5em 0.5em;

}

.card:hover {
  cursor: pointer;
  box-shadow: 2px 2px lawngreen;
}

.green-button {
  white-space: pre-line;
}

</style>
