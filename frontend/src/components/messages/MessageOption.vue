<template>
  <div class="message-option">
    <div class="message-button-wrap">
      <div class="new-message-icon-wrap">
        <div class="new-message-icon" v-if="newMessage"></div>
      </div>
      <div class="user-icon-wrap">
        <img src="../../../public/profile_icon_default.png" alt="Seller image" class="user-icon">
      </div>
      <div class="user-name">
        {{limitUsernameLength(username)}}
      </div>
    </div>
    <div class="delete-icon-wrap">
      <svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" fill="currentColor" class="bi bi-trash" viewBox="0 0 16 16">
        <path d="M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0V6z"/>
        <path fill-rule="evenodd" d="M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1v1zM4.118 4 4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4H4.118zM2.5 3V2h11v1h-11z"/>
      </svg>
    </div>
  </div>
</template>

<script>
export default {
  name: "MessageOption",
  props: {
    username: {
      type: String,
      required: true
    },
    newMessage: {
      type: Boolean,
      required: true
    }
  },
  data() {
    return {
      MAX_USERNAME_LENGTH: 17
    }
  },
  methods: {
    /**
     * Takes a username and shortens it if necessary.
     *
     * @param username message title.
     * @return {string} Name of the user shortened if its longer then the max length. Otherwise the same value.
     */
    limitUsernameLength(username) {
      if (username.length >= this.MAX_USERNAME_LENGTH) {
        return username.substr(0, this.MAX_USERNAME_LENGTH) + "...";
      }
      return username;
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

    padding: 0.821em 0 ;
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
    height: 100%;

    display: flex;
    justify-content: center;
    align-items: center;
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

    display: flex;
    justify-content: center;
    align-items: center;
  }

  .user-icon {
    width: 40px;
    height: 40px;
  }

  .user-name {
    flex-grow: 100;
    max-width: 150px;
    height: 100%;

    display: flex;
    justify-content: left;
    align-items: center;

    padding: 0 0.5em;
  }

  .delete-icon-wrap {
    width: 18%;
    height: 100%;
    margin-left: auto;

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