<template>
  <div class="accordion" id="accordionExample" style="width: 500px">
    <div v-if="allNoticeCards.length === 0">No notification!</div>
    <div class="accordion-item"
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
           data-bs-parent="#accordionExample">
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
      console.log(data)
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