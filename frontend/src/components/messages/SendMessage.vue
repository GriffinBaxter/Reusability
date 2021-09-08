<template>
  <div class="input-group m-1 py-1" style="position:absolute; bottom:0;max-width: 270px">
    <input type="text" class="form-control" placeholder="Enter message here" v-model="messageInput">
    <button type="button" class="input-group-text" @click="sendMessage">Send</button>
  </div>
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
    sendMessage() {
      this.messageInput.trim()
      if (this.messageInput === "") {
        return
      }
      this.$parent.sendMessage(this.messageInput)
    }
  }
}
</script>

<style scoped>

</style>