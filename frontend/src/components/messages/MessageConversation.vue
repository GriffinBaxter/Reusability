<template>
  <div>
    <div v-for="message in messages" v-bind:key="message.index" class="col" style="max-width: 270px">
      <div v-if="userId === message.senderId" class="row m-1 rounded-2 w-75 float-end" style="background-color: lightgrey; word-wrap: break-word">
        <p class="p-1 m-0" style="text-align: end"> {{ message.content }} </p>
      </div>

      <div v-else class="row m-1 rounded-2 w-75 float-end" style="background-color: lightblue; word-wrap: break-word">
        <p class="p-1 m-0"> {{ message.content }} </p>
      </div>
    </div>
  </div>
</template>

<script>
import Cookies from "js-cookie";

export default {
  name: "MessageConversation",
  props: {
    messages: {
      type: Array,
      required: true
    }
  },
  data() {
    return {
      userId: 0,
    }
  },
  mounted() {
    this.userId = Cookies.get("userID");
  }
}
</script>

<style scoped>

</style>