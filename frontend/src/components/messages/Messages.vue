<template>
  <div id="component-wrapper" >
    <div id="messages-wrapper" >
      <LoadingDots v-if="isLoading" />
      <div id="content-wrapper" v-else>
        <MessageOption v-for="(conv) in conversations" :key="conv.id" :id="`conversation-${conv.id}`" :username="conv.userName" :image="conv.image" :new-message="conv.newMessage" :card-name="conv.cardName"></MessageOption>
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
import DefaultImage from "../../../public/profile_icon_default.png"

export default {
  name: "Messages",
  components: {LoadingDots, MessageOption},
  data() {
    return {
      conversations: [],
      errorMessage: "",
      isLoading: false,
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
    }
  },
  beforeMount() {
    this.errorMessage = "";
    this.isLoading = true;
    Api.getConversations().then(
        (res) => {
          this.conversations = res.data.map( (conversation) => {
            return {
              id: conversation.id,
              image: conversation.instigatorImage || DefaultImage,
              userName: conversation.instigatorName,
              cardName: conversation.marketplaceCardTitle,
              creationTime: conversation.created,
              newMessage: true
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
    right: 15px;
    z-index: 999;
    width: 100%;
    height: max(400px, 100%);
  }

  #messages-wrapper {
    position: absolute;
    top: 0;
    right: 15px;
    z-index: 999;

    width: 100%;
    height: max(400px, 100%);

    max-width: 274px;

    border: 1px black solid;
    overflow: auto;
    background-color: white;
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
    right: 15px;
    max-width: 274px;
    width: 100%;
    height: 24px;

    background-color: #f8080850;
  }

</style>