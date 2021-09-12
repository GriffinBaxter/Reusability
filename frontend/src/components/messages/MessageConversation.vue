<template>
  <div>
    <div v-for="message in messages" v-bind:key="message.index" class="col messages">
      <div v-if="userId === message.senderId" class="row m-1 rounded-2 w-75 float-end sentMessage">
        <p class="p-1 m-0"> {{ message.content }} </p>
      </div>

      <div v-else class="row m-1 rounded-2 w-75 receivedMessage">
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
    this.userId = parseInt(Cookies.get("userID"));
  }
}
</script>

<style scoped>

.messages {
  max-width: 270px;
}

.receivedMessage {
  background-color: lightgrey;
}

.sentMessage {
  background-color: lightblue;
  text-align: end;
}

.receivedMessage, .sentMessage {
  word-wrap: break-word;
}

</style>