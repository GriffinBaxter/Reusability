<template>
  <div class="card rounded shadow-sm border-0" id="card-container">
    <div class="card-body p-4">
        <h4 id="card-title" class="col">{{ title }}</h4>

      <div class="row">
        <div class="col">
          <p class="col text-secondary">Created: {{ created }} <br> Expires: {{ expires }}</p>
        </div>
      </div>

      <div id="card-img-container" class="row">
        <img id="card-img" :src="cardImage" alt="Card Image">
      </div>

      <img :src="creatorImage" id="avatar-image" alt="User Avatar">
      {{ creator.firstName }} {{ creator.lastName }}

      <div id="address-container" class="col">
        <p class="small text-muted font-italic" style="margin: 0px">{{ address }}</p>
      </div>

    </div>
  </div>
</template>

<script>
export default {
  name: "Card",
  props: {
    index: {
      type: Number,
      default: 0,
      required: true
    },
    title: {
      type: String,
      default: "",
      required: true
    },
    created: {
      type: String,
      default: "",
      required: true
    },
    expires: {
      type: String,
      default: "",
      required: true
    },
    creator: {
      type: Object,
      default: function () {return {}},
      required: true
    },
    cardImage: {
      type: String,
      default: require("../../../public/apples.jpg"),
      required: false
    },
    creatorImage: {
      type: String,
      default: require("../../../public/sample_profile_image.jpg"),
      required: false
    },
    address: {
      type: String,
      default: "",
      required: true
    }
  }
}
</script>

<style scoped>

#avatar-image {
  width: 30px;
  border-radius: 50px;
}

#card-title {
  height: 70px;
  padding-bottom: 0px;
}

#card-img-container {
  margin-bottom: 20px;
}

#card-img {
  border-radius: 35px;
}

#address-container {
  margin-top: 14px;
}

</style>
