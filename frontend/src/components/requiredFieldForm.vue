<template>
  <div class="validation-message-wrap text-font" v-if="customMessage">
    <div class="validation-message">
      {{ customMessage }}
    </div>
    <slot></slot>
  </div>
  <div v-else>
    <slot></slot>
  </div>
</template>

<script>
export default {
  name: "required-field-form",
  props: {
    customMessage: String
  }
}
</script>

<style scoped>
.validation-message {
  color: red;
  border: 2px red solid;
}





</style>