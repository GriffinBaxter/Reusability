<template>
  <div class="accordion" id="notificationAccordion" style="width: 500px">
    <div class="card border-dark text-white bg-secondary mb-3" v-if="allNoticeCards.length === 0" id="emptyMessage">
      <h2 class="card-body" style="margin: 3px;float: contour; text-align: center">No notification!</h2>
    </div>
    <div :id="'notification_box' + card.id"
         class="accordion-item border-dark text-white bg-secondary"
         v-for="card in allNoticeCards"
         v-bind:key="card.id">
      <h2 class="accordion-header" :id="'heading_' + card.id">
        <button class="accordion-button collapsed"
                type="button"
                data-bs-toggle="collapse"
                :data-bs-target="'#collapse_' + card.id"
                aria-expanded="false"
                :aria-controls="'collapse_' + card.id">
          <h6>{{ card.description }}</h6>
        </button>
      </h2>
      <div :id="'collapse_' + card.id" class="accordion-collapse collapse" :aria-labelledby="'heading_' + card.id"
           data-bs-parent="#notificationAccordion">
        <div class="accordion-body">
          <strong>More info and button here</strong>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

import Api from "../../Api";

export default {
  name: "Notification",
  data() {
    return {
      allNoticeCards: []
    }
  },
  props: {},
  methods: {
    populateNotification(data) {
      data.forEach(notification => {
        this.allNoticeCards.push({id: notification.id, description: notification.description});
      })
    }
  },
  beforeMount() {
    Api.getNotifications()
        .then(response => (this.populateNotification(response.data)))
        .catch((error) => {
          if (error.status === 401) {
            // Missing or invalid token
            this.$router.push({path: '/invalidtoken'});
          }
        })
  }
}
</script>

<style scoped>

</style>