<template>
  <div>
    <div role="alert" aria-live="assertive" aria-atomic="true" class="toast" data-autohide="false">
      <div class="toast-header">
        <strong class="mr-auto">{{ this.headingText }}</strong>
      </div>
      <div class="toast-body">
        {{ feedbackText }}
      </div>
    </div>

  </div>

</template>

<script>
export default {
  name: "FeedbackNotification",
  props: {
    isErrorFeedback: {
      type: Boolean,
      default: true,
      required: true
    },
    feedbackText: {
      type: String,
      default: "",
      required: true
    },
    data() {
      return {
        headingText: "",
      }
    }
  },
  methods: {
    /**
     * Determines whether the message is an error message or a success message and sets the heading text accordingly.
     */
    setFeedbackStyling() {
      if(this.isErrorFeedback) {
        this.headingText = "Error";
        document.getElementById('toast-header').classList.add('') //TODO
      }
    }
  },
  /**
   * Sets the toast's heading upon mounting
   */
  mounted() {
    this.setFeedbackStyling();
  }

}
</script>

<style scoped>

</style>