<template>
  <div>
    <div role="alert" aria-live="assertive" aria-atomic="true" class="toast" data-autohide="true"
         data-bs-animation="true" data-bs-delay="5000">
      <div class="toast-header" id="toast-heading">
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

    // Initialize toast
    const toastElementList = [].slice.call(document.querySelectorAll('.toast'));
    const toastList = toastElementList.map(function (toastElement) {
      return new this.bootstrap.Toast(toastElement);
    })
  }

}
</script>

<style scoped>

</style>