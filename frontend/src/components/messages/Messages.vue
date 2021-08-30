<template>
  <div id="messages-wrapper">
    <MessageOption></MessageOption>
  </div>
</template>

<script>
import MessageOption from "@/components/messages/MessageOption";
export default {
  name: "Messages",
  components: {MessageOption}
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
    border: 1px black solid;

    display: grid;
    grid-template-rows: repeat(7, 1fr);
  }


</style>