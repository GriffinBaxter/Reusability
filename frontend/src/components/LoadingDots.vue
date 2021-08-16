<template>
  <div class="d-flex justify-content-center py-md-4 my-md-4">
    <div class="spinner-grow" role="status">
      <span class="sr-only">Loading...</span>
    </div>
    <div class="spinner-grow" role="status">
      <span class="sr-only">Loading...</span>
    </div>
    <div class="spinner-grow" role="status">
      <span class="sr-only">Loading...</span>
    </div>
  </div>
</template>

<script>
export default {
  name: "loadingDots"
}
</script>

<style scoped>

.spinner-grow {
  height: 14px;
  width: 14px;
  margin-right: 4px;
  margin-left: 4px;
}

</style>