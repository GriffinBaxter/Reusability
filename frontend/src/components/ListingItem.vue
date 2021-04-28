<template>
  <div class="card border-secondary mb-3 px-2 py-2 shadow">
    <div class="row no-gutters">
      <div class="col-md-3">
        <img class="card-img" src="../../public/apples.jpg" id="listing-item-image">
      </div>
      <div class="col">
        <div class="card-body px-3 py-3" id="cardbody">
          <div class="row py-2" id="mainbody">
            <div class="col-md-3">
              <h4 class="card-title">{{quantityPerSale}}x {{ productName }}</h4>
              <p class="card-text">
                {{ productId }}
                <br>
                (Quantity: {{ quantity }})
              </p>
            </div>
            <div class="col d-flex align-items-end flex-column">
              <p align="right">{{moreInfo}}</p>
              <p>{{ description }}</p>
                <p class="card-text mt-auto" id="price">Price: ${{ price }}</p>

            </div>
          </div>
          <div class="row">
            <hr>
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
    quantityPerSale: {
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
    }
  },
  mounted() {
    document.getElementById("listing-item-image").src = this.image;
  }

}
</script>

<style>

img {
  object-fit: cover;
  width: 230px;
  height: 230px;
}

#cardbody {
  height: 224px;
}

#mainbody {
  min-height: 160px;
}

</style>