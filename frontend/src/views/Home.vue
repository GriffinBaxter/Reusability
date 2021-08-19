<!--This file creates the Home page.-->
<!--It current contains the navigation bar, a news feed and a footer.-->
<!--Bootstrap has been used for creating and styling the elements.-->

<template>

  <div style="position: relative;">

    <div id="main">
      <!--Nav bar; displays either business account or individual account nav bar-->
      <Navbar></Navbar>

      <div id="home" class="container all-but-footer">

        <!--Home feed-->
        <div class="container-news mx-md-5 text-font">

          <br>
          <h1 style="text-align: center">Home</h1>

          <!-- All Bookmarked Listings Messages-->
          <div v-if="showBookmarkMessages">

            <!-- Bookmarked listing message -->
            <div id="bookmark-messages-container" v-if="hasDataLoaded">
              <div v-if="bookmarkMessages.length === 0 && rendered">
                <h2 id="no-bookmark-message" style="text-align: center">(No Bookmarked Messages)</h2>
              </div>

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
            <div v-else>
              <LoadingDots/>
            </div>

          </div>

        </div>
      </div>

    </div>
    <!--Footer contains links that are the same as those in the nav bar-->
    <Footer/>

  </div>
</template>

<script>
import Footer from '../components/main/Footer';
import Navbar from '../components/main/Navbar';
import Api from "../Api";
import {formatDate} from "../dateUtils";
import Cookies from "js-cookie";
import LoadingDots from "../components/LoadingDots";

export default {
  name: "Home",
  components: {
    LoadingDots,
    Footer,
    Navbar
  },
  data() {
    return {
      bookmarkMessages: [],
      rendered: false,
      hasDataLoaded: false,
      showBookmarkMessages: false, // hide messages if acting as business
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
