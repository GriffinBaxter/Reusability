<template>
  <form class="input-group m-1 py-1" style="bottom:0;max-width: 270px">
    <input type="text" class="form-control" placeholder="Enter message here" v-model="messageInput">
    <button type="submit" class="input-group-text" @click="sendMessage">Send</button>
  </form>
</template>

<script>

export default {
  name: "SendMessage",
  data() {
    return {
      messageInput: ""
    }
  },
  methods: {
    /**
     * Sends the currently typed message
     */
    sendMessage(event) {
      event.preventDefault();
      this.messageInput = this.messageInput.trim()
      if (this.messageInput === "") {
        return
      }
      this.$parent.sendMessage(this.messageInput)
      this.messageInput = ""
    }
  }
}
</script>

<style scoped>

</style>