<template>
  <div class="card rounded shadow-sm border-0">
    <div class="card-body p-4"><img src="" alt="" class="img-fluid d-block mx-auto mb-3">
      <h5><a class="text-dark">{{ index }} {{ title }}</a></h5>
      <p class="small text-muted font-italic">Lorem ipsum dolor sit amet, consectetur adipisicing elit.
        Lorem ipsum dolor sit amet, consectetur adipisicing elit. Lorem ipsum dolor sit amet, consectetur adipisicing
        elit.
      </p>
      <ul class="list-inline small">
      </ul>
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
    }
  }
}
</script>

<style scoped>

</style>