<template>
  <div class="accordion" id="notificationAccordion" style="width: 500px">
    <div class="card border-dark text-white bg-secondary mb-3" v-if="allNoticeCards.length === 0" id="emptyMessage">
      <h2 class="card-body" style="margin: 3px; float: contour; text-align: center"> No notification! </h2>
    </div>
    <div :id="'notification_box' + card.id"
         class="accordion-item"
         v-for="card in allNoticeCards"
         v-bind:key="card.id"
         style="background-color: #ededed">
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
      <div :id="'collapse_' + card.id"
           class="accordion-collapse collapse"
           :aria-labelledby="'heading_' + card.id"
           data-bs-parent="#notificationAccordion">
        <div class="accordion-body">
          <div class="row">
            <div class="col" style="float: contour; text-align: center">
              <button class="btn btn-outline-danger">Delete Card</button>
            </div>
            <div class="col">
              <button class="btn btn-outline-success" @click="extendCardForDisplayPeriod(card.id)">Extend Card for 2 Weeks</button>
            </div>
          </div>
          <!--          <strong>More info and button here</strong>-->
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
    },
    extendCardForDisplayPeriod(id) {
      Api.extendCardDisplayPeriod(id)
          .then()
          .catch((error) => {
            if (error.status === 401) {
              // Missing or invalid token
              this.$router.push({path: '/invalidtoken'});
            } else if (error.status === 403) {
              // No permission
              this.$router.push({path: '/invalidtoken'});
            } else if (error.status === 406) {
              // Card not exist
              this.$router.push({path: '/noCard'});
            } else {
              console.log(error)
            }
          });
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