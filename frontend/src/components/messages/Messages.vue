<template>
  <div id="component-wrapper" >
    <div id="messages-wrapper" >
      <LoadingDots v-if="isLoading" />
      <div id="content-wrapper" v-else>
        <div v-if="!conversationIsOpen">
          <div v-for="(conv) in conversations" :key="conv.id" @click="openConversation(conv)">
            <MessageOption :id="`conversation-${conv.id}`" :userName="conv.userName" :image="conv.image"
                           :new-message="conv.newMessage" :card-name="conv.cardName"></MessageOption>
          </div>
        </div>
        <MessageConversation v-else title="currentTitle" ref="msgConversation"/>
      </div>
    </div>
    <div class="error-message" v-if="errorMessage">
      {{errorMessage}}
    </div>
  </div>
</template>

<script>
import MessageOption from "./MessageOption";
import Api from "../../Api"
import LoadingDots from "../LoadingDots";
import DefaultImage from "../../../public/profile_icon_default.png";
import Cookies from "js-cookie";
import MessageConversation from "./MessageConversation";

export default {
  name: "Messages",
  components: {MessageConversation, LoadingDots, MessageOption},
  data() {
    return {
      conversations: [],
      errorMessage: "",
      isLoading: false,
      conversationIsOpen: false
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
     * @param conversationId ID of the conversation
     */
    openConversation(conversationId) {
      this.conversationIsOpen = true;
      this.$nextTick(() => {
        this.$refs.msgConversation.openConversation(conversationId)
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
    Api.getConversations().then(
        (res) => {
          this.conversations = res.data.map( (conversation) => {
            let userImage;
            let userName;
            let userId;
            const currentId = Cookies.get("userID");
            if (conversation.instigatorId === currentId) {
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
  }

  #messages-wrapper {
    position: absolute;
    top: 0;
    right: 15px;
    z-index: 999;

    width: 100%;
    height: max(400px, 100%);
    max-width: 284px;

    border: 1px #a8a8a8 solid;
    box-shadow: -2px 10px 1rem #00000030;
    overflow: auto;
    background-color: white;


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