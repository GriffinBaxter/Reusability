<template>
  <div :id="'conversation_' + id">
    <div class="row">
      <button class="btn col-2 btn-light" @click="closeConversation">{{ backIcon }}</button>
      <h5 class="col m-1 align-self-center" style="text-align: center">{{ conversationData.userName }}</h5>
    </div>
    <p class="px-2 m-0" style="text-align: center">{{ conversationData.cardName }}</p>
    <hr class="m-0">
    <div v-if="loading">
      <LoadingDots/>
    </div>
    <div v-else>

      <div v-for="message in messages" v-bind:key="message.index" class="col">
        <div v-if="userId === message.senderId" class="row m-1 rounded-2 w-75 float-end" style="background-color: lightgrey">
          <p class="p-1 m-0" style="text-align: end"> {{ message.content }} </p>
        </div>

        <div v-else class="row m-1 rounded-2 w-75" style="background-color: lightblue">
          <p class="p-1 m-0"> {{ message.content }} </p>
        </div>
      </div>

      <div class="input-group m-1 py-1">
        <input type="text" class="form-control" placeholder="Enter message here" v-model="messageInput">
        <button type="button" class="input-group-text" @click="sendMessage">Send</button>
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
      userId: null,
      backIcon: "<",
      conversationData: {
        userName: "",
        cardName: "",
        userId: 0,
        marketplaceCardId: 0
      },
      messageInput: ""
    }
  },
  methods: {
    /**
     * Makes the API call and loads the conversation
     * @param conversation selected conversation
     */
    async openConversation(conversation) {
      this.loading = true
      this.conversationData = conversation
      await Api.getConversation(conversation.id).then((res) => {
        this.messages = res.data
      })
      this.loading = false
    },
    /**
     * Sends the currently typed message
     */
    sendMessage() {
      this.messageInput.trim()
      if (this.messageInput === "") {
        return
      }
      let message;
      message = {
        senderId: this.userId,
        receiverId: this.conversationData.userId,
        marketplaceCardId: this.conversationData.marketplaceCardId,
        content: this.messageInput
      }

      Api.sendReply(this.conversationData.id, message).then(() => {
        this.messages.push(message)
      }).catch((err) => {
        console.log(err)
      })
    },
    closeConversation() {
      this.userName = "";
      this.id = 0;
      this.cardName = "";
      this.$parent.closeConversation();
    }
  },
  mounted() {
    this.userId = parseInt(Cookies.get("userID"));
  }
}
</script>

<style scoped>

</style>