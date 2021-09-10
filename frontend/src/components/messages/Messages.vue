<template>
  <div id="component-wrapper" >
    <MessageTitle id="message-title" v-if="conversationIsOpen" v-bind:conversation-data="conversationData" />
    <div id="messages-wrapper" >
      <LoadingDots v-if="isLoading" />
      <div id="content-wrapper" v-else>
        <div v-if="!conversationIsOpen">
          <div v-for="(conv) in conversations" :key="conv.id" @click="openConversation(conv)">
            <MessageOption :id="`conversation-${conv.id}`" :userName="conv.userName" :image="conv.image"
                           :new-message="conv.newMessage" :card-name="conv.cardName"></MessageOption>
          </div>
        </div>
        <div class="overflow-auto" v-else>
          <MessageConversation id="message-conversation" ref="msgConversation" v-bind:messages="messages"/>
        </div>
      </div>
    </div>
    <div class="error-message" v-if="errorMessage">
      {{errorMessage}}
    </div>
    <SendMessage id="send-message" v-if="conversationIsOpen"/>
  </div>
</template>

<script>
import MessageOption from "./MessageOption";
import Api from "../../Api"
import LoadingDots from "../LoadingDots";
import DefaultImage from "../../../public/profile_icon_default.png";
import Cookies from "js-cookie";
import MessageConversation from "./MessageConversation";
import MessageTitle from "./MessageTitle";
import SendMessage from "./SendMessage";

export default {
  name: "Messages",
  components: {MessageConversation, LoadingDots, MessageOption, MessageTitle, SendMessage},
  data() {
    return {
      conversations: [],
      errorMessage: "",
      isLoading: false,
      conversationIsOpen: false,
      conversationData: {},
      messages: [],
      currentId: 0
    }
  },
  methods: {
    /**
     * Toasts a message for 1 second to give an update about error messages.
     *
     * @param message the message to show
     */
    toastErrorMessage(message) {
      this.errorMessage = message;
      setTimeout(() => {
        this.errorMessage = "";
      }, 1000)
    },
    /**
     * Opens and loads the selected conversation
     * @param conversation Conversation data
     */
    async openConversation(conversation) {
      this.isLoading = true
      this.conversationData = conversation;
      await Api.getConversation(conversation.id).then((res) => {
        this.messages = res.data.reverse()
        this.conversationIsOpen = true;
      }).catch(() => {
        this.errorMessage = "Something went wrong"
      })
      this.isLoading = false
    },
    /**
     * Sends the currently typed message
     */
    sendMessage(messageInput) {
      let message = {
        senderId: this.currentId,
        receiverId: this.conversationData.userId,
        marketplaceCardId: this.conversationData.marketplaceCardId,
        content: messageInput
      }

      Api.sendReply(this.conversationData.id, message).then(() => {
        this.messages.push(message)
      }).catch((err) => {
        if (err.response) {
          if (err.response.status === 401) {
            this.$router.push({name: "InvalidToken"})
          } else {
            this.errorMessage = `${err.response.status} - ${err.response.message}`
          }
        } else if (err.request) {
            this.errorMessage = "Timeout"
        } else {
          this.errorMessage = "Something went wrong"
        }
      })
    },
    /**
     * Closes the open conversation
     */
    closeConversation() {
      this.conversationIsOpen = false;
    }
  },
  beforeMount() {
    this.errorMessage = "";
    this.isLoading = true;
    this.currentId = parseInt(Cookies.get("userID"));
    Api.getConversations().then(
        (res) => {
          this.conversations = res.data.map( (conversation) => {
            let userImage;
            let userName;
            let userId;
            if (conversation.instigatorId === this.currentId) {
              userImage = conversation.receiverImage;
              userName = conversation.receiverName;
              userId = conversation.receiverId;
            } else {
              userImage = conversation.instigatorImage;
              userName = conversation.instigatorName;
              userId = conversation.instigatorId;
            }
            return {
              id: conversation.id,
              image: userImage || DefaultImage,
              userName: userName,
              userId: userId,
              cardName: conversation.marketplaceCardTitle,
              creationTime: conversation.created,
              newMessage: true,
              marketplaceCardId: conversation.marketplaceCardId
            };
          });
          if (this.conversations.length === 0) {
            this.errorMessage = "No messages found.";
          }
          this.isLoading = false;
        }
  ).catch((err) => {
      if (err.response) {
        if (err.response.status === 401) {
          this.$router.push({path: '/invalidtoken'});
        } else  {
          this.toastErrorMessage("Something went wrong sorry.");
        }
      } else if (err.request) {
          this.toastErrorMessage("Timed out getting conversations.")
      } else {
        this.toastErrorMessage("Something went wrong sorry.");
      }
      this.isLoading = false;
    });
  }

}
</script>

<style scoped>

  #component-wrapper {
    position: absolute;
    top: 100%;
    bottom: 0;
    right: 2px;
    z-index: 999;
    height: 83vh;
    width: 300px;

    border: 1px #a8a8a8 solid;
    box-shadow: -2px 10px 1rem #00000030;
    background-color: white;

    display: flex;
    flex-direction: column;

  }

  #messages-wrapper {
    right: 15px;
    z-index: 999;

    width: 100%;
    height: max(400px, 100%);
    max-width: 284px;

    overflow: auto;
  }

  #messages-wrapper::-webkit-scrollbar {
    width: 20px;
  }

  #messages-wrapper::-webkit-scrollbar-thumb {
    height: 56px;
    border-radius: 10px;
    border: 4px solid transparent;
    background-clip: content-box;
    background-color: #aaa;
  }


  #content-wrapper {
    max-width: 270px;
    height: fit-content;

    display: grid;
    grid-template-columns: 1fr;
  }

  .error-message {
    position: absolute;
    z-index: 1000;
    top: 0;
    text-align: center;
    width: 100%;
    height: 24px;

    background-color: #f8080850;
  }

</style>