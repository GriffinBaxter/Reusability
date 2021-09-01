<template>
  <div id="component-wrapper">
    <div id="messages-wrapper">
      <div id="content-wrapper">
        <MessageOption v-for="(conv) in conversations" :key="conv.id" :username="conv.username" :new-message="conv.newMessage" :card-name="conv.cardName"></MessageOption>
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

export default {
  name: "Messages",
  components: {MessageOption},
  data() {
    return {
      conversations: [
        {
          username: "Pontiff Sulyvahn",
          cardName: "Card name Example",
          newMessage: true,
        },
        {
          username: "Yhorm the Giant",
          cardName: "Card name Example",
          newMessage: false,
        },
        {
          username: "Aldrich Devourer of Gods",
          cardName: "Card name Example",
          newMessage: false,
        },
        {
          username: "Chapion Gundyr",
          cardName: "Card name Example",
          newMessage: true,
        },
        {
          username: "Nameless King",
          cardName: "Card name Example",
          newMessage: false,
        },
        {
          username: "Darkeater Midir",
          cardName: "Card name Example",
          newMessage: true,
        },
        {
          username: "Lothric",
          cardName: "Card name Example",
          newMessage: false,
        },
        {
          username: "Oceiros",
          cardName: "Card name Example",
          newMessage: true,
        },
        {
          username: "Dancer",
          cardName: "Card name Example",
          newMessage: true,
        },
        {
          username: "Wolnir",
          cardName: "Card name Example",
          newMessage: false,
        }
      ],
      errorMessage: ""
    }
  },
  methods: {
    toastErrorMessage(message) {
      this.errorMessage = message;
      setTimeout(() => {
        this.errorMessage = "";
      }, 1000)
    }
  },
  beforeMount() {
    this.errorMessage = "";
    Api.getConversations().then(
        (res) => {
          this.conversations = res.data.map( (conversation) => {
            return {
              id: conversation.id,
              image: conversation.instigatorImage && "../../../public/profile_icon_default.png",
              userName: conversation.instigatorName,
              cardName: conversation.marketplaceCardTitle,
              creation_time: conversation.created,
              newMessage: true
            };
          });

          if (this.conversations.length === 0) {
            this.errorMessage = "No messages found.";
          }
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