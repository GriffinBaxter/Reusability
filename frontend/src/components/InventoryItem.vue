<template>
  <div class="card border-secondary mb-3 px-2 py-2 shadow">
    <div class="row no-gutters">

      <!--image-->
      <div class="col-md-3">
        <div class="ratio ratio-1x1">
          <img class="card-img" :src="image" id="inventory-item-image">
        </div>
      </div>

      <div class="col-md-9">
        <div class="card-body px-3 py-3">

          <!--Product info-->
          <h3 class="card-title">{{ productName }}</h3>
          <h6 class="card-text">
            {{ productId }}
          </h6>
          <h6 class="card-text">
            (Quantity: {{ quantity }})
          </h6>

          <!--price-->
          <h6 class="card-text" align="right" v-if="pricePerItem != null">
            Price Per Item: {{ pricePerItem }}
          </h6>
          <h6 class="card-text" align="right" v-else><br></h6>

          <h6 class="card-text" align="right" v-if="totalPrice != null">
            Total Price: {{ totalPrice }}
          </h6>
          <h6 class="card-text" align="right" v-else><br></h6>

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
export default {
  name: "InventoryItem",
  props: {
    image: {
      type: String,
      default: require("../../public/apples.jpg"),
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
    }
  },
  mounted() {
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