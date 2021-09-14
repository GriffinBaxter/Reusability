<template>
  <div class="card" style="width: 18rem;">
    <!--Bookmark-->
    <div v-if="actingBusinessId == null" class="tag-vertical discount"
         :id="'bookmarkButton_'+id"
         style="position:absolute; right: 5px"
         type="button"
         @click="changeBookmarkStatus">
      <div :id="'bookmark_'+id" v-if="isMarked">&#9829;</div>
    </div>

    <div class="everything-but-bookmark" @click="routeToSaleListing(id, inventoryItem.product.business.id)">
    <div class="row">
      <div class="col">
        <img :src="getPrimaryImageSrc(inventoryItem.product.images)" class="card-img-top" alt="default-image">
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
               v-bind:data-bs-content="addressUnpack(inventoryItem.product.business.address)"
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
          <li class="list-group-item">{{ 'Closing Date: ' + formatDateFunction(closes, false) }}</li>
          <li class="list-group-item">{{ 'Expires: ' + formatDateFunction(inventoryItem.expires, false) }}</li>
        </ul>
      </div>
    </div>
    </div>
  </div>
</template>

<script>
import {Popover} from "bootstrap";
import Api from "../../Api";
import {formatDate} from "../../dateUtils";
import {checkNullity, getFormattedAddress} from "../../views/helpFunction";

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
      default: function () {
        return {}
      },
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
    },
    actingBusinessId: {
      type: String,
      default: null
    }
  },
  data() {
    return {
      tooltipList: [],
      isMarked: this.isBookmarked,
    }
  },
  methods: {
    // change bookmark status
    changeBookmarkStatus() {
      this.isMarked = !this.isMarked
      Api.changeStatusOfAListing(this.id)
          .catch(error => console.log(error))
    },
    routeToSaleListing(index, businessId) {
      // Dispose all popover
      const lists = Array.from(document.getElementsByClassName("popover fade show bs-popover-top"))
      lists.forEach((popover) => {
        popover.remove()
      })

      // Push to listing page
      this.$router.push({
        path: `/businessProfile/${businessId}/listings/${index}`
      });
    },
    routeToSeller(businessId) {
      this.$router.push({
        path: `/businessProfile/${businessId}`
      });
    },
    formatDateFunction(date, dateAndTime) {
      return formatDate(date, dateAndTime);
    },
    getPrimaryImageSrc(images) {
      if (images.length > 0) {
        for (let image of images) {
          if (image.isPrimary) {
            return Api.getServerURL() + "/" + image.thumbnailFilename;
          }
        }
      }
      return require('../../../public/default-image.jpg')
    },
    /**
     * Unpacks the address object into an HTML string.
     * @param address Address object.
     * @returns {string} HTML address string with spacing and line breaks.
     */
    addressUnpack(address) {
      let addressString = "";

      let formattedAddress = getFormattedAddress(checkNullity(address.streetNumber), checkNullity(address.streetName),
                                                checkNullity(address.suburb),
                                                checkNullity(address.city), checkNullity(address.postcode),
                                                checkNullity(address.region), checkNullity(address.country))

      formattedAddress.forEach((fullAddress) => {
        if (fullAddress.line !== "") {
          addressString += fullAddress.line + "<br>";
        }
      })
      return addressString
    }
  },
  mounted() {
    const popoverTriggerList = Array.from(document.querySelectorAll('[data-bs-toggle="popover"]'));
    this.tooltipList = popoverTriggerList.map(function (popoverTriggerElement) {
      return new Popover(popoverTriggerElement, {sanitize: false, html: true});
    })
  }
}
</script>

<style scoped>

#seller-info-button {
  width: 82%;
}

.card {
  margin-bottom: 1em;
  margin-top: 1em;

}

.card:hover {
  cursor: pointer;
  box-shadow: 2px 2px lawngreen;
}

.green-button {
  white-space: pre-line;
}

/* vertical tag */
.tag-vertical {
  position: relative;
  padding: 5px 0;
  width: 26px;
  color: #fff;
  text-align: center;
}

.tag-vertical::after {
  position: absolute;
  content: "";
  left: 0;
  top: 100%;
  border-style: solid;
  border-width: 0 13px 13px 13px;
}

.tag-vertical.discount::after {
  border-color: #00d9a9 #00d9a9 transparent #00d9a9;
}

.tag-vertical.discount {
  background: #00d9a9;
}

</style>
