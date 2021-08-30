<template>
  <div id="messages-wrapper">
    <div id="content-wrapper">
      <MessageOption v-for="(msg, i) in messages" :key="i" :username="msg.username" :new-message="msg.newMessage" :card-name="msg.cardName"></MessageOption>
    </div>
  </div>
</template>

<script>
import MessageOption from "@/components/messages/MessageOption";
export default {
  name: "Messages",
  components: {MessageOption},
  data() {
    return {
      messages: [
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
      ]
    }
  }

}
</script>

<style scoped>

  #messages-wrapper {
    position: absolute;
    top: 100%;
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


</style>