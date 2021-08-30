<template>
  <div id="messages-wrapper">

  </div>
</template>

<script>
export default {
  name: "Messages"
}
</script>

<style scoped>

  #messages-wrapper {
    position: absolute;
    top: 100%;
    right: 10%;
    z-index: 999;

    width: 100%;
    height: max(400px, 100%);

    max-width: 80%;


    background-color: black;
  }


</style>