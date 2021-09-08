<template>
  <div :id="'conversation_title_' + conversationData.id">
    <div class="row m-0" style="width: 100%">
      <button class="btn col-2 btn-light" @click="closeConversation">{{ backIcon }}</button>
      <h5 class="col m-1 align-self-center" style="text-align: center">{{ conversationData.userName }}</h5>
    </div>
    <p class="px-2 m-0" style="text-align: center">{{ conversationData.cardName }}</p>
    <hr class="m-0">
  </div>
</template>

<script>
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
    }
  }
}
</script>

<style scoped>

</style>