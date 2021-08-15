<!--This file creates the Home page.-->
<!--It current contains the navigation bar, a news feed and a footer.-->
<!--Bootstrap has been used for creating and styling the elements.-->

<template>

  <div style="position: relative;">

    <div id="main">
      <!--Nav bar; displays either business account or individual account nav bar-->
      <Navbar></Navbar>

      <div id="home" class="container all-but-footer">

        <!--News feed-->
        <div class="container-news text-font">

          <br>
          <h1 style="text-align: center">Home</h1>

          <div v-if="bookmarkMessages.length == 0 && rendered">
            <br>
            <h2 style="text-align: center">(No Bookmarked Messages)</h2>
          </div>

          <!--Post 1 for news feed-->
          <div class="post shadow py-3 px-4" v-for="message in bookmarkMessages" v-bind:key="message.id">
            <!--Post description-->
            <div>
              <p class="post-description">
                {{message.description}}</p>
            </div>
            <!--Post header-->
            <div class="">
              <p>
                Message date: {{ formatDateVar(message.created) }}
              </p>
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
      rendered: false
    }
  },
  mounted() {
    Api.getBookmarkedMessage().then(res => {
      this.bookmarkMessages = res.data.reverse();
      this.rendered = true
    })
  },
  methods: {
    formatDateVar(date) {
      return formatDate(date, true)
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

/*
 * Width is 524px since news feed item is 600px and then 12px margin each side.
 */
div.container {
  width: 424px;
  margin: auto;
}

.account-name {
  font-weight: bold;
  margin-left: 10px;
}

.post-description {
  margin-bottom: 30px;
}

/*-------------------------------------------- Medium break point styling -------------------------------------------*/

/*Medium break point*/
@media (min-width: 692px) {

  div.container {
    width: 524px;
    margin: auto;
  }
}


/*-------------------------------------------- Large break point styling -------------------------------------------*/

@media (min-width: 800px) {
  /*
 * Width is 524px since news feed item is 600px and then 12px margin each side.
 */
  div.container {
    width: 624px;
    margin: auto;
  }
}
</style>
