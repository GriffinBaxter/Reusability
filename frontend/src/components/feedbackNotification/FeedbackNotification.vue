<template>
  <div>

    <div class="toast-container">
      <div class="toast show" role="alert" aria-live="assertive" aria-atomic="true">
        <div class="toast-header">
          <strong class="me-auto" id="toast-heading">{{ headingText }}</strong>
          <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
        <div class="toast-body">
          {{ feedbackText }}
        </div>
      </div>


      <div class="toast show" role="alert" aria-live="assertive" aria-atomic="true">
        <div class="toast-header">
          <strong class="me-auto" id="toast-heading-1">{{ headingText }}</strong>
          <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
        <div class="toast-body">
          {{ feedbackText }}
        </div>
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
      required: true
    },
    feedbackText: {
      type: String,
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
      if (this.isErrorFeedback) {
        this.headingText = "Error";
        document.getElementById('toast-heading').classList.add('alert-danger');
        document.getElementById('toast-heading').classList.remove('alert-success');
      } else {
        this.headingText = "Success";
        document.getElementById('toast-heading').classList.add('alert-success');
        document.getElementById('toast-heading').classList.remove('alert-danger');
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