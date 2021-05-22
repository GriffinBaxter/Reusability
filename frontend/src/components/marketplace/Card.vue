<template>
  <div class="card rounded shadow-sm border-0">
    <div class="card-body p-4"><img src="" alt="" class="img-fluid d-block mx-auto mb-3">
      <div class="row">
        <div class="col">{{ title }}</div>
        <div class="col">{{ created }}</div>
      </div>

      <div class="row">
        <img :src="cardImage" alt="Card Image">
      </div>

        <img :src="creatorImage" id="avatar-image" alt="User Avatar">
        {{ creator.firstName }} {{ creator.lastName }}

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
      type: Date,
      default: "",
      required: true
    },
    creator: {
      type: Object,
      default: function () {return {}},
      required: true
    },
    cardImage: {
      type: Image,
      default: require("../../../public/apples.jpg"),
      required: true
    },
    creatorImage: {
      type: Image,
      default: require("../../../public/sample_profile_image.jpg"),
      required: true
    },
  }
}
</script>

<style scoped>

#avatar-image {
  width: 30px;
  border-radius: 50px;
}

</style>
