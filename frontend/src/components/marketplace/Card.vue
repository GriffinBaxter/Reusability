<template>
  <div class="card rounded shadow-sm border-0" id="card-container">
    <div class="card-body p-4"><img src="" alt="" class="img-fluid d-block mx-auto mb-3">
<!--      <div class="row">-->
        <h3 class="col">{{ title }}</h3>
        <p class="col text-secondary">{{ created }}</p>
<!--      </div>-->

      <div class="row">
        <img :src="cardImage" alt="Card Image">
      </div>

        <img :src="creatorImage" id="avatar-image" alt="User Avatar">
        {{ creator.firstName }} {{ creator.lastName }}
        {{ creator.homeAddress.suburb }} {{ creator.homeAddress.city }}

<!--      <h5><a class="text-dark">{{ index }} {{ title }} {{ created }}</a></h5>-->
<!--      <p class="small text-muted font-italic">{{ description }} {{ creator.firstName }}</p>-->
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
    description: {
      type: String,
      default: "",
      required: true
    },
    created: {
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
  }
}
</script>

<style scoped>

#card-container {
  width: 400px;
}

#avatar-image {
  width: 30px;
  border-radius: 50px;
}

</style>
