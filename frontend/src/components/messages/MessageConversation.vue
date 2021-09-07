<template>
  <div :id="'conversation_' + id">
    <div v-if="loading">
      <LoadingDots/>
    </div>
    <div v-else>

      <div v-for="message in messages" v-bind:key="message.index">
        <div v-if="userId === message.senderId" class="m-1 rounded-2 w-75 float-end" style="background-color: lightgrey">
          <p class="p-1" style="text-align: end"> {{ message.content }} </p>
        </div>

        <div v-else class="m-1 rounded-2 w-75" style="background-color: lightblue">
          <p class="p-1"> {{ message.content }} </p>
        </div>
      </div>

      <div class="input-group">
        <input type="text" class="form-control" placeholder="Enter message here">
        <button class="input-group-text" @click="sendMessage">Send</button>
      </div>
    </div>
  </div>
</template>

<script>
import Api from "../../Api"
import LoadingDots from "@/components/LoadingDots";
import Cookies from "js-cookie";

export default {
  name: "MessageConversation",
  components: {LoadingDots},
  data() {
    return {
      id: 0,
      loading: true,
      messages: [],
      userId: null
    }
  },
  methods: {
    /**
     * Makes the API call and loads the conversation
     * @param conversationId ID of the conversation
     */
    async openConversation(conversationId) {
      this.loading = true
      this.id = conversationId
      await Api.getConversation(conversationId).then((res) => {
        this.messages = res.data
      })
      this.loading = false
    },
    /**
     * Sends the currently typed message
     */
    sendMessage() {

    }
  },
  mounted() {
    this.userId = parseInt(Cookies.get("userID"));
  }
}
</script>

<style scoped>

</style>