<template>
  <div class="message-option">
    <div class="new-message-icon">
      <!-- TODO Add icon indicator-->
    </div>
    <div class="user-icon-wrap">

    </div>
    <div class="user-name">

    </div>
    <div class="delete-icon-wrap">

    </div>
  </div>
</template>

<script>
export default {
  name: "MessageOption"
}
</script>

<style scoped>

  .message-option {
    background-color: white;
    border-bottom: black solid 1px;
    display: flex;
  }

  .new-message-icon {
    width: 9%;
    height: 100%;

    background-color: orange;
  }

  .user-icon-wrap {
    width: 18%;
    height: 100%;
    background-color: #1EBABC;
  }

  .user-name {
    flex-grow: 100;
    height: 100%;
    background-color: #0074D9;
  }

  .delete-icon-wrap {
    width: 18%;
    height: 100%;
    margin-left: auto;

    background-color: red;
  }

</style>