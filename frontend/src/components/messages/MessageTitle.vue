<template>
  <div :id="'conversation_title_' + conversationData.id">
    <div class="row m-0 w-100">
      <button class="btn col-2 btn-light" @click="closeConversation">{{ backIcon }}</button>
        <div class="col m-1">
          <h5>
            <img :src="getThumbnailImageSrc(conversationData.images)" :alt="`user's profile image`" :class="getImageStyle(conversationData.userName)">
            {{ conversationData.userName }}
          </h5>
        </div>


    </div>
    <p class="px-2 m-0 text-center align ">{{ conversationData.cardName }}</p>
    <hr class="m-0">
  </div>
</template>

<script>
import {getThumbnailImageSrc} from "./messageHelper";

export default {
  name: "MessageTitle",
  props: {
    conversationData: {
      type: Object
    }
  },
  data() {
    return {
      backIcon: "<"
    }
  },
  methods: {

    getThumbnailImageSrc: getThumbnailImageSrc,

    closeConversation() {
      this.$parent.closeConversation();
    },
    /**
     * Returns different styling classes depending on the length of the given username.
     */
    getImageStyle(username) {
      if (username.length > 13) {
        return "user-icon rounded-circle row user-icon-large-username"
      } else {
        return "user-icon rounded-circle"
      }
    }
  }
}
</script>

<style scoped>

.user-icon {
  width: 40px;
  height: 40px;
  margin-right: 8px;
}

.user-icon-large-username {
  margin-left: 78px;
}

</style>
