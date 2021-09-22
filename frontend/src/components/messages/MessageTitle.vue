<template>
  <div :id="'conversation_title_' + conversationData.id">
    <div class="row m-0 w-100">
      <button class="btn col-2 btn-light" @click="closeConversation">{{ backIcon }}</button>
        <div class="col m-1">
          <img :src="getImageSrc()" :alt="`user's profile image`" class="user-icon rounded-circle d-inline-block">
          <h5 class="d-inline-block">
            {{ conversationData.userName }}
          </h5>
        </div>


    </div>
    <p class="px-2 m-0 text-center">{{ conversationData.cardName }}</p>
    <hr class="m-0">
  </div>
</template>

<script>
import Api from "@/Api";

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
    closeConversation() {
      this.$parent.closeConversation();
    },
    /**
     * Adds the backend server URL to the user's primary profile image URL if they have images.
     * @return String a full URL to the primary image of the user (or the default image if they don't have one)
     */
    getImageSrc() {
      if (this.conversationData.images.length === 0) {
        return require('../../../public/default-image.jpg')
      }
      const primaryImage = this.conversationData.images.filter((image) => image.isPrimary);
      return Api.getServerURL() + "/" + primaryImage[0].thumbnailFilename;
    },
  }
}
</script>

<style scoped>

.user-icon {
  width: 40px;
  height: 40px;
  margin-right: 8px;
}

</style>
