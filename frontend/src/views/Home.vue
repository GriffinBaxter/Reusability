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

          <div v-if="bookmarkMessages.length === 0 && rendered">
            <br>
            <h2 style="text-align: center">(No Bookmarked Messages)</h2>
          </div>

          <!-- All Bookmarked Listings Messages-->
          <div id="bookmark-messages-container" v-if="hasDataLoaded">
            <div :id="'bookmark-message-container-' + message.id"
                 class="post shadow py-3 px-4"
                 type="button"
                 v-for="message in bookmarkMessages" v-bind:key="message.id"
                 @click="toListing(message.listingId, message.businessId)">
              <!--Post description-->
              <div>
                <p class="post-description">
                  {{message.description}}</p>
              </div>
              <!--Listing close date-->
              <p class="py-1">
                <label class="bookmark-message-title">Closes:</label> {{ formatDateVar(message.closes, false) }}
              </p>
              <!--Date/time of message-->
              <p class="py-1">
                <label class="bookmark-message-title">Notification Date:</label> {{ formatDateVar(message.created, true) }}
              </p>
            </div>
          </div>
          <!--     Loading Dotes     -->
          <div v-else class="d-flex justify-content-center py-md-4 my-md-4">
            <div class="spinner-grow" role="status">
              <span class="sr-only">Loading...</span>
            </div>
            <div class="spinner-grow" role="status">
              <span class="sr-only">Loading...</span>
            </div>
            <div class="spinner-grow" role="status">
              <span class="sr-only">Loading...</span>
            </div>
          </div>

        </div>
      </div>

    </div>
    <!--Footer contains links that are the same as those in the nav bar-->
    <Footer></Footer>

  </div>
</template>

<script>
import Footer from '../components/main/Footer';
import Navbar from '../components/main/Navbar';
import Api from "../Api";
import {formatDate} from "../dateUtils";

export default {
  name: "Home",
  components: {
    Footer,
    Navbar
  },
  data() {
    return {
      bookmarkMessages: [],
      rendered: false,
      hasDataLoaded: false
    }
  },
  mounted() {
    this.hasDataLoaded = false;
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
