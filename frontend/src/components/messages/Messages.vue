<template>
  <div id="messages-wrapper">
    <div id="content-wrapper">
      <MessageOption v-for="(msg, i) in messages" :key="i" :username="msg.username" :new-message="msg.newMessage"></MessageOption>
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
          newMessage: true,
        },
        {
          username: "Yhorm the Giant",
          newMessage: false,
        },
        {
          username: "Aldrich Devourer of Gods",
          newMessage: false,
        },
        {
          username: "Chapion Gundyr",
          newMessage: true,
        },
        {
          username: "Nameless King",
          newMessage: false,
        },
        {
          username: "Darkeater Midir",
          newMessage: true,
        },
        {
          username: "Lothric",
          newMessage: false,
        },
        {
          username: "Oceiros",
          newMessage: true,
        },
        {
          username: "Dancer",
          newMessage: true,
        },
        {
          username: "Wolnir",
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