<template>
  <div class="card border-secondary mb-3 px-2 py-2 shadow" style="cursor: pointer" @click="$emit('click')">
    <div class="row no-gutters">

      <!--image-->
      <div class="col-md-2">
        <div class="ratio ratio-1x1">
          <div v-if="this.$props.images.length > 0">
            <div v-for="image of this.$props.images" v-bind:key="image.id">
              <img v-if="image.isPrimary" class="card-img" :src="getImageSrc(image.thumbnailFilename)" alt="inventory item image">
            </div>
          </div>
          <div v-else>
            <img class="card-img" :src="require('../../../public/default-image.jpg')" alt="inventory item image">
          </div>
        </div>
      </div>

      <div class="col-md-10">
        <div class="card-body px-2 py-2">

          <!--Product info-->
          <h3 class="card-title">
            {{ productName }}
            <em class="card-text fas fa-edit float-end"></em>
          </h3>
          <h6 class="card-text">
            {{ productId }}
          </h6>
          <h6 class="card-text">
            Barcode: {{ barcode }}
          </h6>
          <h6 class="card-text">
            (Quantity: {{ quantity }})
          </h6>

          <!--price-->
          <h6 class="card-text" style="text-align: right" v-if="pricePerItem != null">
            Price Per Item: {{ currencySymbol }}{{ pricePerItem }} {{ currencyCode }}
          </h6>
          <h6 class="card-text" style="text-align: right" v-else><br></h6>

          <h6 class="card-text" style="text-align: right" v-if="totalPrice != null">
            Total Price: {{ currencySymbol }}{{ totalPrice }} {{ currencyCode }}
          </h6>
          <h6 class="card-text" style="text-align: right" v-else><br></h6>

        </div>
      </div>

    </div>
    <div>
      <!--date-->
      <hr id="lineBreak">
      <div class="row">
        <div class="col-md text-center" v-if="manufactured != null">
          Manufactured: {{ manufactured }}
        </div>
        <div class="col-md text-center" v-if="sellBy != null">
          Sell By: {{ sellBy }}
        </div>
        <div class="col-md text-center" v-if="bestBefore != null">
          Best Before: {{ bestBefore }}
        </div>
        <div class="col-md text-center" v-if="expires != null">
          Expires: {{ expires }}
        </div>
      </div>
    </div>
  </div>

</template>

<script>

import Api from "../../Api";

export default {
  name: "InventoryItem",
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
    quantity: {
      type: Number,
      // eslint-disable-next-line vue/require-valid-default-prop
      default: "None",
      required: true
    },
    pricePerItem: {
      type: Number,
      // eslint-disable-next-line vue/require-valid-default-prop
      default: "None",
      required: false
    },
    totalPrice: {
      type: Number,
      // eslint-disable-next-line vue/require-valid-default-prop
      default: "None",
      required: false
    },
    manufactured: {
      type: String,
      default: "None",
      required: false
    },
    sellBy: {
      type: String,
      default: "None",
      required: false
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
      default: "N/A",
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
img {
  object-fit: cover;
  width: 100%;
}

#lineBreak {
  width: 90%;
  margin-left: 5%;
  margin-right: 5%;
}

h6 {
  margin: 0;
}

</style>
