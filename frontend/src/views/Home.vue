<!--This file creates the Home page.-->
<!--It current contains the navigation bar, a news feed and a footer.-->
<!--Bootstrap has been used for creating and styling the elements.-->

<template>

  <div style="position: relative;">

    <div id="main">
      <!--Nav bar; displays either business account or individual account nav bar-->
      <Navbar></Navbar>

      <div id="home" class="container all-but-footer" v-if="isActingAsUser()">
        <h1 style="text-align: center">Home</h1>

        <ul class="nav nav-tabs" id="homepage-tabs" role="tablist">
          <li class="nav-item " role="presentation">
            <button class="nav-link active" id="my-feed-tab" data-bs-toggle="tab" data-bs-target="#my-feed" type="button"
            role="tab" aria-controls="my-feed" aria-selected="true">
              My Feed
            </button>
          </li>
          <li class="nav-item " role="presentation" v-if="isActingAsUser()">
            <button class="nav-link" id="my-cards-tab" data-bs-toggle="tab" data-bs-target="#my-cards" type="button"
                    role="tab" aria-controls="my-cards" aria-selected="false">
              My Cards
            </button>
          </li>
        </ul>

        <div class="tab-content">
          <!--Home feed-->
          <div class="tab-pane fade show active" id="my-feed" role="tabpanel" aria-labelledby="my-feed-tab">
            <div id="no-bookmarks-message" class="mt-2" v-if="hasDataLoaded && bookmarkMessages.length === 0 && rendered">
              No bookmarked messages to show
            </div>

            <div class="container-news mx-md-5 text-font">

            <br>

            <!-- All Bookmarked Listings Messages-->
            <div v-if="showBookmarkMessages">

              <!-- Bookmarked listing message -->
              <div id="bookmark-messages-container" v-if="hasDataLoaded && bookmarkMessages.length !== 0 && rendered">
                <div :id="'bookmark-message-container-' + message.id"
                     class="row post shadow py-3 px-4"
                     type="button"
                     style="margin-left: 0; margin-right: 0"
                     v-for="message in bookmarkMessages" v-bind:key="message.id">

                  <div :id="'bookmark-message-link-' + message.id" class="col-11" @click="toListing(message.listingId, message.businessId)">
                    <!--Bookmarked listing message description-->
                    <div>
                      <p class="post-description">
                        {{ message.description }}</p>
                    </div>
                    <!--Listing close date-->
                    <p class="py-1">
                      <label class="bookmark-message-title">Closes:</label> {{ formatDateVar(message.closes, false) }}
                    </p>
                    <!--Date/time of message-->
                    <p class="py-1">
                      <label class="bookmark-message-title">Notification Date:</label>
                      {{ formatDateVar(message.created, true) }}
                    </p>
                  </div>

                  <!--Delete button for bookmarked listing message-->
                  <div class="col-1" id="delete-btn-container" style="position: relative;">
                    <button :id="'delete-bookmark-message-button-' + message.id"
                            type="button"
                            class="btn-close"
                            @click="deleteMessage(message.id)"/>
                  </div>
                </div>
              </div>
              <!--     Loading Dotes     -->
              <div v-if="!hasDataLoaded">
                <LoadingDots/>
              </div>

            </div>

          </div>
          </div>
          <div class="tab-pane fade" id="my-cards" role="tabpanel" aria-labelledby="my-feed-tab" v-if="isActingAsUser()">
            <div id="loading-cards-dots" v-if="loadingCards">
              <loading-dots />
            </div>
            <div id="cards-error-message" class="error-message" v-else-if="cardsError">
              {{cardsError}}
            </div>
            <div v-else>
              <UserCardsComp id="cards-container" v-if="usersCards.length > 0" :users-cards="usersCards" :show-title="false"/>
              <div id="no-cards-message" class="mt-2" v-else>No cards to show</div>
            </div>
          </div>
        </div>
      </div>

      <div class="container all-but-footer" v-else>
        <h1 style="text-align: center">Home</h1>
        <div class="sales-report-overview">Sales Report Overview</div>
        <div class="box">
        </div>
      </div>

    </div>
    <!--Footer contains links that are the same as those in the nav bar-->
    <Footer/>

  </div>
</template>

<script>
import Footer from '../components/main/Footer';
import Navbar from '../components/Navbar';
import UserCardsComp from "../components/UserCardsComp"
import Api from "../Api";
import {formatDate} from "../dateUtils";
import BarChart from '../components/saleInsights/SalesReportGraph.js'
import Cookies from "js-cookie";
import LoadingDots from "../components/LoadingDots";

export default {
  name: "Home",
  components: {
    LoadingDots,
    Footer,
    Navbar,
    BarChart,
    UserCardsComp
  },
  data() {
    return {
      bookmarkMessages: [],
      rendered: false,
      hasDataLoaded: false,
      showBookmarkMessages: false, // hide messages if acting as business
      cardsError: "",

      usersCards: [],
      userId: null,
      loadingCards: false
    }
  },
  mounted() {
    this.hasDataLoaded = false;
    this.showBookmarkMessages = true;

    const actingAs = Cookies.get('actAs');

    if (actingAs === undefined) {

      Api.getBookmarkedMessage().then(res => {
        this.bookmarkMessages = res.data.reverse();
        this.rendered = true
        this.hasDataLoaded = true;
      }).catch((err) => {
        if (err.response && err.response.status === 401) {
          this.$router.push({path: '/invalidToken'})
        } else {
          console.log(err)
        }
        this.hasDataLoaded = true;
      })
    } else {
      this.showBookmarkMessages = false;
    }

    this.userId = Cookies.get("userID");
    if (this.userId !== null && this.userId !== undefined) {
      this.retrieveUsersCards(this.userId);
    } else {
      this.$router.push({path: '/login'});
    }
  },
  methods: {
    /**
     * Formats date to String using formatDate method in dateUtils
     * @param date Date to format
     * @param tf Boolean True/False for if time should be included
     * @return {string|null} Date string
     */
    formatDateVar(date, tf) {
      return formatDate(date, tf)
    },

    /**
     * Routes the user to the listing page associated with their bookmarked message with the given business id and
     * listing id.
     * @param listingId listing id of the bookmark message
     * @param businessId business id associated with the listing id of the bookmark message
     */
    toListing(listingId, businessId) {
      this.$router.push({path: `/businessProfile/${businessId}/listings/${listingId}`})
    },

    /**
     * Deletes the bookmarked listing message
     * @param messageId the id of the bookmarked listing message to delete
     */
    deleteMessage(messageId) {
      Api.deleteBookmarkMessage(messageId)
          .then(() => {
            let newBookmarkList = [];
            this.bookmarkMessages.forEach((message) => {
              if (message.id !== messageId) {
                newBookmarkList.push(message);
              }
            })
            this.bookmarkMessages = newBookmarkList;
          })
          .catch((error) => {
            console.log(error);
          })
    },
    /**
     * Sends a get request to the backend to retrieve the cards belonging to the current user's profile you are viewing.
     * If the request was unsuccessful, the cards are not populated on the page and appropriate error messages logged.
     * @param userId the id of the user's profile you are viewing.
     */
    async retrieveUsersCards(userId) {
      this.loadingCards = true;
      this.cardsError = "";
      await Api.getUsersCards(userId).then(response => {
        this.usersCards = response.data;
        this.usersCards.sort(this.compareCards);
      }).catch((error) => this.processUserInfoError(error));
      this.loadingCards = false;
    },
    /**
     * This method is used to compare two cards' sections when sorting a user's cards by section.
     * @param card1 the user's first card used for comparison.
     * @param card2 the user's second card used for comparison.
     *
     * Preconditions:  card1 is a non-null JSON representing a MarketplaceCard.
     *                 card2 is a non-null JSON representing a MarketplaceCard.
     * Postconditions: an integer value representing the comparison outcome:
     *                 1. -1 if the section of card1 is "less" than the section of card2.
     *                 2. 1 if the section of card1 is "greater" than the section of card2.
     *                 3. 0 if the card sections are equal.
     */
    compareCards(card1, card2) {
      if (card1.section < card2.section) { return -1; }
      if (card1.section > card2.section) { return 1; }
      return 0;
    },
    /**
     * If a request is made to the backend for user info (profile information or cards) and an error occurs
     * then the appropriate error message will need to be displayed.
     * @param error an error which includes an error message.
     */
    processUserInfoError(error) {
      if (error.request && !error.response) {
        this.cardsError = "Unable to retrieve cards at this time, please try again later."
      } else if (error.response.status === 406) {
        this.cardsError = "No user id found."
      } else if (error.response.status === 401) {
        this.$router.push({path: '/invalidtoken'});
        this.cardsError = "Unauthorized to see the cards."
      } else {
        this.cardsError = "Something went wrong..."
      }
    },
    /**
     * Determines if a user is acting as a user.
     * [Note this uses the actAs cookie]
     *
     * @return {boolean} True if the user is acting as a user. Otherwise false.
     */
    isActingAsUser() {
      const value = Cookies.get("actAs")
      return value === undefined || value === null;
    }
  }
}
</script>

<!------------------------------------------------- Home Page Styling ------------------------------------------------->

<style scoped>

body {
  background: #f1f1f1;
  margin: 0;
}

.sales-report-overview {
  font-family: 'Roboto', sans-serif !important;
  color: black !important;
  font-size: 1.5rem;
}

.box {
  border: 1px black solid;
}

.nav-link {
  font-family: 'Roboto', sans-serif !important;
  color: black !important;
}

.nav-link:focus-visible {
  outline: none !important;
  box-shadow: none !important;
}

.nav-link:focus {
  outline: none !important;
  box-shadow: none !important;
}

div {
  box-sizing: border-box;
}

header {
  background: white;
  padding: 10px
}

header h1 {
  margin: 0;
}

div.post {
  background: white;
  margin-top: 20px;
  border-radius: 15px;
  margin-bottom: 5%;
}

div.post img {
  width: 100%;
}

/*
 * Styling for header of the post.
 */
div.post .post-header div:first-child {
  display: inline-block;
  width: 80%;
}

/*
 * Styling for date in header of the post.
 */
div.post .post-header div:last-child {
  display: inline-block;
  width: 20%;
  padding: 10px;
  text-align: right;
  color: gray;
}

div.post h2, div.post p {
  margin: 0;
  padding: 10px;
}

.post-description {
  margin-bottom: 30px;
}

.bookmark-message-title {
  font-weight: bold;
}

#bookmark-messages-container {
  background-color: #1EBA8C;
  padding: 20px;
  border-radius: 20px;
  margin-bottom: 80px;
}

.spinner-grow {
  height: 14px;
  width: 14px;
  margin-right: 4px;
  margin-left: 4px;
}

.error-message {
  background-color: #ff000030;
  border: 3px #ff000060 solid;
  border-radius: 0.3rem;
  padding: 1em 1em;
  margin-top: 2em;
}

#delete-btn-container {
  text-align: right;
  right: 0px;
}

/*-------------------------------------------- Medium break point styling -------------------------------------------*/

@media (min-width: 768px) {
  /*
 * Width is 524px since news feed item is 600px and then 12px margin each side.
 */
  div.container {
    width: 80%;
    margin: auto;
  }
}

/*-------------------------------------------- Large break point styling -------------------------------------------*/

@media (min-width: 992px) {
  /*
 * Width is 524px since news feed item is 600px and then 12px margin each side.
 */
  div.container {
    width: 70%;
    margin: auto;
  }
}

@media (min-width: 1200px) {
  /*
 * Width is 524px since news feed item is 600px and then 12px margin each side.
 */
  div.container {
    width: 60%;
    margin: auto;
  }
}
</style>
