<template>
  <div class="card border-secondary mb-3 px-2 py-2 shadow">
    <div class="row no-gutters">
      <div class="col-md-3">
        <img class="card-img" :src="require('../../public/apples.jpg')" id="inventory-item-image">
      </div>
      <div class="col-md-9">
        <div class="card-body px-3 py-3">
          <h4 class="card-title">{{ productName }}</h4>
          <p class="card-text">
            {{ productId }}
            <br>
            (Quantity: {{ quantity }})
          </p>
          <p class="card-text" align="right">
            Price Per Item: {{ pricePerItem }}
            <br>
            Total Price: {{ totalPrice }}
          </p>
          <hr>
          <div class="row">
            <div class="col-3">
              Manufactured: {{ manufactured }}
            </div>
            <div class="col-3">
              Sell By: {{ sellBy }}
            </div>
            <div class="col-3">
              Best Before: {{ bestBefore }}
            </div>
            <div class="col-3">
              Expires: {{ expires }}
            </div>
          </div>
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
      default: "../../public/apples.jpg",
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
    document.getElementById("inventory-item-image").src = this.image;
  }
}
</script>
<style>
img {
  object-fit: cover;
  width: 230px;
  height: 230px;
}

</style>