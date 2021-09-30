<template>
  <div class="main-feedback-container">

    <div class="toast-container" v-for="message in messages" v-bind:key="message.id">
      <div class="toast show" data-bs-animation="true" role="alert" aria-live="assertive" aria-atomic="true" v-if="message.isError">
        <div class="toast-header notification-error" :id="'toast-heading-' + message.id">
          <strong class="me-auto notification-error-font">{{ message.topic }}</strong>
        </div>
        <div class="toast-body">
          {{ message.text }}
        </div>
      </div>

      <div class="toast show" role="alert" aria-live="assertive" aria-atomic="true" v-else>
        <div class="toast-header notification-success" :id="'toast-heading-' + message.id">
          <strong class="me-auto notification-success-font">{{ message.topic }}</strong>
        </div>
        <div class="toast-body">
          {{ message.text }}
        </div>
      </div>


    </div>
  </div>

</template>

<script>
export default {
  name: "FeedbackNotification",
  props: {
    messages: {
      type: Array,
      required: true
    }
  },
  data() {
    return {}
  },
  methods: {},

}
</script>

<style scoped>

.notification-error {
  background-color: rgba(243, 216, 218, 100);
}

.notification-success {
  background-color: rgba(213, 230, 222, 100);
}

.notification-error-font {
  color: rgba(121, 40, 44, 100);
}

.notification-success-font {
  color: rgba(46, 87, 62, 100);
}


.main-feedback-container {
  position: fixed;
  right: 30px;
  bottom: 30px;
}
</style>