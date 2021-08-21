<template>
  <div class="card border-dark" id="wrapper">

    <div v-if="hasDataLoaded">

    <div class="accordion" v-if="allNoticeCards.length === 0">
      <div class="card border-dark text-white bg-secondary"
           id="emptyMessage">
        <h4 class="card-body" style="margin: 3px; float: contour; text-align: center">
          No Notifications!
        </h4>
      </div>
    </div>

    <div v-else
         class="accordion"
         id="notificationAccordion"
         style="height:400px; overflow:auto">

        <div :id="'notification_box' + notification.id"
             class="accordion-item"
             v-for="notification in allNoticeCards"
             v-bind:key="notification.id"
             style="background-color: #ededed">

          <h2 class="accordion-header" :id="'heading_' + notification.id">
            <button class="accordion-button collapsed"
                    type="button"
                    data-bs-toggle="collapse"
                    :data-bs-target="'#collapse_' + notification.id"
                    aria-expanded="false"
                    :aria-controls="'collapse_' + notification.id">
              <h6>{{ notification.description }}</h6>
            </button>
          </h2>

        <div :id="'collapse_' + notification.id"
             class="accordion-collapse collapse"
             :aria-labelledby="'heading_' + notification.id"
             data-bs-parent="#notificationAccordion">
          <div class="row accordion-body">

            <!-- marketplace card notification options -->
            <div class="col-4" style="float: contour; text-align: center" v-if="notification.notificationType === 'MARKETPLACE' && notification.marketCardId != null">
              <button :id="'delete_button_card_' + notification.id"
                      class="btn btn-outline-danger"
                      @click="deleteCard(notification.marketCardId)">
                Delete Card
              </button>
            </div>
            <div class="col-5" v-if="notification.notificationType === 'MARKETPLACE' && notification.marketCardId != null">
              <button :id="'extend_button_card_' + notification.id"
                      class="btn btn-outline-success"
                      @click="extendCardForDisplayPeriod(notification.marketCardId)">
                Extend Card for 2 Weeks
              </button>
            </div>

            <!-- keyword notification options -->
            <div class="col" style="float: contour; text-align: center" v-else-if="notification.notificationType === 'KEYWORD'">
              <button :id="'delete_button_keyword_' + notification.id"
                      class="btn btn-outline-danger"
                      @click="deleteKeyword(notification.keywordId)">
                Delete Keyword
              </button>
            </div>

            <!-- delete notification button -->
            <div class="col" style="text-align: center">
              <button class="btn btn-outline-dark" :id="'delete_notification' + notification.id" @click="deleteNotification(notification)">
                Delete notification
              </button>
            </div>

            </div>
          </div>
        </div>
      </div>
    </div>
    <div v-else>
      <div class="accordion">
        <div class="card border-dark text-white bg-secondary"
             style="width: 300px; max-height: 67px">
          <h4 class="card-body" style="margin: 3px; float: contour; text-align: center">
            <LoadingDots style="margin: 0 !important; padding: 0 !important;"></LoadingDots>
          </h4>
        </div>
      </div>
    </div>

  </div>
</template>

<script>

import Api from "../../Api";
import Cookies from "js-cookie";
import LoadingDots from "../LoadingDots";

export default {
  name: "Notification",
  components: {LoadingDots},
  data() {
    return {
      allNoticeCards: [],
      hasDataLoaded: false
    }
  },
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
        if (notification.notificationType === 'KEYWORD') {
          notifications.push({
            id: index,
            notificationId: notification.id,
            keywordId: notification.keyword.id,
            description: notification.description,
            date: notification.created,
            notificationType: notification.notificationType
          })
        } else if (notification.notificationType === 'MARKETPLACE') { // marketplace notification
          notifications.push({
            id: index,
            notificationId: notification.id,
            marketCardId: notification.marketplaceCardPayload !== null ? notification.marketplaceCardPayload.id : null,
            description: notification.description,
            date: notification.created,
            notificationType: notification.notificationType
          })
        } else {
          notifications.push({
            id: index,
            notificationId: notification.id,
            description: notification.description,
            date: notification.created,
            notificationType: notification.notificationType
          })
        }
        index += 1;
      })
      this.allNoticeCards = notifications;
    },
    /**
     * this function will reload all notifications for current user or selected business.
     */
    loadNotifications() {
      this.hasDataLoaded = false;
      if (Cookies.get('actAs') !== undefined) {
        Api.getBusinessNotifications(Cookies.get('actAs'))
            .then(response => {
              this.populateNotification(response.data);
              this.hasDataLoaded = true;
            })
            .catch((error) => {
              this.errorCatcher(error);
              this.hasDataLoaded = true;
            });
      } else {
        Api.getNotifications()
            .then(response => {
              this.populateNotification(response.data);
              this.hasDataLoaded = true;
            })
            .catch((error) => {
              this.errorCatcher(error)
              this.hasDataLoaded = true;
            });
      }
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
    },
    /**
     * Deletes a notification for the user then re-calls loadNotifications().
     * @param notification notification to delete
     */
    deleteNotification(notification) {
      Api.deleteNotification(notification.notificationId, notification.notificationType).then(()=> {
          this.loadNotifications();
      }).catch( err => {
          console.log(err)
      })
    }
  },
  beforeMount() {
    this.loadNotifications();
  }
}
</script>

<style scoped>

  #wrapper {
    max-width: 400px;
    width: 100%;
    position: absolute;
    right: 25px;
    z-index: 999
  }

  @media screen and (max-width: 470px){
    #wrapper {
      width: 80vw;
      margin: 0 auto;
      left: 25px;
    }
  }

</style>