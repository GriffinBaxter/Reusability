<template>
  <div class="message-option">
    <div class="message-button-wrap">
      <div class="new-message-icon-wrap">
        <div class="new-message-icon" v-if="newMessage"></div>
      </div>
      <div class="user-icon-wrap">
        <img :src="getThumbnailImageSrc(images)" :alt="`seller ${userName}'s profile image`" class="user-icon rounded-circle">
      </div>
      <div class="conversation-details">
        <div class="card-name">{{limitStringLength(cardName, MAX_CARD_LENGTH)}}</div>
        <div class="user-name">{{limitStringLength(userName, MAX_USERNAME_LENGTH)}}</div>
      </div>
    </div>
    <!-- When the delete button is clicked emit an event to the parent, so that the needed methods can be called -->
    <div class="delete-icon-wrap" @click="$emit('deleteConversation', conversationId, userName)">
      <svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" fill="currentColor" class="bi bi-trash" viewBox="0 0 16 16">
        <path d="M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0V6z"/>
        <path fill-rule="evenodd" d="M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1v1zM4.118 4 4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4H4.118zM2.5 3V2h11v1h-11z"/>
      </svg>
    </div>
  </div>
</template>

<script>
import {getThumbnailImageSrc} from "./messageHelper";

export default {
  name: "MessageOption",
  props: {
    userName: {
      type: String,
      required: true
    },
    images: {
      type: Array,
      required: true
    },
    newMessage: {
      type: Boolean,
      required: true
    },
    cardName: {
      type: String,
      required: true
    },
    conversationId: {
      type: Number,
      required: true
    }
  },
  data() {
    return {
      MAX_USERNAME_LENGTH: 19,
      MAX_CARD_LENGTH: 14
    }
  },
  methods: {

    getThumbnailImageSrc: getThumbnailImageSrc,

    /**
     * Takes a string and shortens it if necessary.
     *
     * @param str {string} the string to be limited.
     * @param maxLength {number} the string limit.
     * @return {string} String shortened if its longer then the max length. Otherwise the same value.
     */
    limitStringLength(str, maxLength) {
      if (str.length >= maxLength) {
        return str.substr(0, maxLength-3) + "...";
      }
      return str;
    }
  }
}
</script>

<style scoped>

  .message-option {
    background-color: white;
    display: flex;
    cursor: pointer;
    transition: background-color 200ms ease-in-out;

    padding: 0.821em 0.5em ;
    width: 100%;

    border-bottom: 1px #88888830 solid;
  }

  .message-option:hover {
    background-color: #2eda7740;
  }

  .message-button-wrap {
    flex-grow: 100;
    height: 100%;
    display: flex;
  }

  .new-message-icon-wrap {
    width: 9%;

    display: flex;
    justify-content: center;
    align-items: center;
    margin: auto;
  }

  .new-message-icon {
    background-color: #2eda77;
    width: 10px;
    height: 10px;
    border-radius: 50%;
  }

  .user-icon-wrap {
    width: 18%;
    height: 100%;
    margin: auto 0.35em;

    display: flex;
    justify-content: center;
    align-items: center;
  }

  .user-icon {
    width: 40px;
    height: 40px;
  }

  .conversation-details {
    flex-grow: 100;
    max-width: 150px;
    height: 100%;

    display: flex;
    justify-content: left;
    align-items: center;
    flex-direction: column;

    padding: 0 0.5em;
  }

  .user-name {
    margin-right: auto;
    font-size: 0.8em;
  }

  .card-name {
    margin-right: auto;
  }

  .delete-icon-wrap {
    width: 18%;
    height: 100%;
    margin: auto;

    display: flex;
    justify-content: center;
    align-items: center;
    transition: color 200ms ease-in-out;

    cursor: pointer;
  }

  .delete-icon-wrap:hover {
    color: red;
  }

</style>
