<template>
  <div class="card border-secondary mb-3 px-2 py-2 shadow">
    <div class="row no-gutters">
      <div class="col-md-3">
        <img class="card-img" src="../../public/profile_icon_default.png" id="listing-item-image">
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
            Price: {{ price }}
          </p>
          <hr>
          <div class="row">
            <div class="col-3">
              Listing Date: {{ listDate }}
            </div>
            <div class="col-3">
              Closing Date: {{ closeDate }}
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
  name: "ListingItem",
  props: {
    image: {
      type: String,
      default: "../../public/apples.jpg",
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
    }
  },
  mounted() {
    document.getElementById("listing-item-image").src = this.image;
  }

}
</script>

<style scoped>

</style>