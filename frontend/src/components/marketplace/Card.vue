<template>
  <div class="card rounded shadow-sm border-0" id="card-container">
    <div class="card-body p-4">
      <h4 :id="'card-title_' + index"
          style="height: 70px; padding-bottom: 0;"
          class="col">
        {{ title }}
      </h4>

      <div class="row">
        <div class="col">
          <p class="col text-secondary">Created: {{ created }} <br> Expires:&nbsp; {{ expires }}</p>
        </div>
      </div>

      <hr class="mb-0"><br>

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
      default: function () {
        return {}
      },
      required: true
    },
    creatorImage: {
      type: String,
      required: true
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

#address-container {
  margin-top: 14px;
}

</style>
