<template>
  <div class="card border-secondary mb-3 px-2 py-2 shadow">
    <div class="row no-gutters">
      <div class="col-md-3">
        <img class="card-img" src="../../public/apples.jpg">
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
    productName: {
      type: String,
      default: "Watties Baked Beans - 420g can",
      required: true
    },
    productId: {
      type: String,
      default: "WATT-420-BEANS",
      required: true
    },
    quantity: {
      type: Number,
      default: null,
      required: true
    },
    pricePerItem: {
      type: Number,
      default: null,
      required: false
    },
    totalPrice: {
      type: Number,
      default: null,
      required: false
    },
    manufactured: {
      type: Date,
      default: null,
      required: false
    },
    sellBy: {
      type: Date,
      default: null,
      required: false
    },
    bestBefore: {
      type: Date,
      default: null,
      required: false
    },
    expires: {
      type: Date,
      default: null,
      required: true
    }
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