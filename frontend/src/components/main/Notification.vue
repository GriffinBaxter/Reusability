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
      v-if="card.marketplaceCardPayload !== null"
      class="accordion-collapse collapse"
           :aria-labelledby="'heading_' + card.id"
           data-bs-parent="#notificationAccordion">
        <div class="accordion-body">

          <!-- marketplace card notifications -->
          <div class="row" v-if="card.keywordId === undefined">
            <div class="col" style="float: contour; text-align: center">
              <button :id="'delete_button_card_' + card.id"
                      class="btn btn-outline-danger"
                      @click="deleteCard(card.marketCardId)">
                Delete Card
              </button>
            </div>
            <div class="col">
              <button :id="'extend_button_card_' + card.id"
                      class="btn btn-outline-success"
                      @click="extendCardForDisplayPeriod(card.marketCardId)">
                Extend Card for 2 Weeks
              </button>
            </div>
          </div>

          <!-- keyword notification -->
          <div class="row" v-else>
            <div class="col" style="float: contour; text-align: center">
              <button :id="'delete_button_keyword_' + card.keywordId"
                      class="btn btn-outline-danger"
                      @click="deleteKeyword(card.keywordId)">
                Delete Keyword
              </button>
            </div>
          </div>

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
      allNoticeCards: [],
      allNewKeywords: []
    }
  },
  props: {},
  methods: {
    /**
     * catch errors.
     */
    errorCatcher(error) {
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
    },
    /**
     * populate date from backend and update allNoticeCards.
     */
    populateNotification(data) {
      let notifications = [];
      let index = 0;
      data.forEach(notification => {

        // keyword notification
        if (notification.keyword !== undefined) {
          notifications.push({
            id: index,
            keywordId: notification.keyword.id,
            description: notification.description,
            date: notification.created
          })
        } else { // marketplace notification
          notifications.push({
            id: index,
            marketCardId: notification.marketplaceCardPayload !== null ? notification.marketplaceCardPayload.id : null,
            description: notification.description,
            date: notification.created
          })
        }
        index += 1;
      })
      this.allNoticeCards = notifications;
    },
    /**
     * this function will reload all notifications for current user.
     */
    loadNotifications() {
      Api.getNotifications()
          .then(response => this.populateNotification(response.data))
          .catch((error) => this.errorCatcher(error));

    },
    /**
     * delete a market card
     * @param id marketplace card id
     */
    deleteCard(id) {
      Api.deleteACard(id)
          .then(() => this.loadNotifications())
          .catch((error) => this.errorCatcher(error));
    },
    /**
     * extend the DisplayPeriod for given card
     * @param id marketplace card id
     */
    extendCardForDisplayPeriod(id) {
      Api.extendCardDisplayPeriod(id)
          .then(() => this.loadNotifications())
          .catch((error) => this.errorCatcher(error));
    },
    /**
     * Delete a keyword, given that the logged in user is a DGGA or GAA.
     * @param id, keyword id
     */
    deleteKeyword(id) {
      Api.deleteExistingKeyword(id)
          .then(() => this.loadNotifications())
          .catch((error) => this.errorCatcher(error));
    }
  },
  beforeMount() {
    this.loadNotifications();
  }
}
</script>
