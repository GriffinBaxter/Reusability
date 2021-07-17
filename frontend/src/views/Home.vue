<!--This file creates the Home page.-->
<!--It current contains the navigation bar, a news feed and a footer.-->
<!--Bootstrap has been used for creating and styling the elements.-->

<template>

  <div id="home-page-container">

    <SideNavBar></SideNavBar>

    <div id="main">
      <!--Nav bar; displays either business account or individual account nav bar-->
      <Navbar></Navbar>

      <div id="home" class="container all-but-footer">

        <!--News feed-->
        <div class="container-news text-font">

          <span style="font-size:30px;cursor:pointer" @click="openNav">&#9776; open</span>


          <!--Post 1 for news feed-->
          <div class="post shadow py-5 px-4">
            <!--Post header-->
            <div class="post-header">
              <div>
                <img id="profile-image-1" src="../../public/profile_icon_1.jpg" alt="profile image">
                <span class="account-name">Green Grocers </span>
              </div><div>
                <span>Apr 6</span>
              </div>
            </div>
            <!--Post description-->
            <div>
              <p class="post-description"> This is the first photo of the feed. This is a multi-lined comment for testing purposes.</p>
            </div>
            <p></p>
            <!--Post image-->
            <div>
              <img class="post-image" src="../../public/apples.jpg" alt="image 2">
            </div>
          </div>

          <!--Post 2 for news feed-->
          <div class="post shadow py-5 px-4">
            <!--Post header-->
            <div class="post-header">
              <div>
                <img id="profile-image-2" src="../../public/profile_icon_2.jpg" alt="profile image">
                <span class="account-name">Fast Frank's</span>
              </div><div>
              <span>Apr 4</span>
            </div>
            </div>
            <!--Post description-->
            <div>
              <p class="post-description"> This is the second photo of the feed. This is a multi-lined comment for testing purposes.</p>
            </div>
            <p></p>
            <!--Post image-->
            <div>
              <img class="post-image" src="../../public/cans.jpg" alt="image 2">
            </div>
          </div>

          <!--Post 3 for news feed-->
          <div class="post shadow py-5 px-4">
            <!--Post header-->
            <div class="post-header">
              <div>
                <img id="profile-image-3" src="../../public/profile_icon_3.jpg" alt="profile image">
                <span class="account-name">New Leaf Organics</span>
              </div><div>
              <span>Apr 3</span>
            </div>
            </div>
            <!--Post description-->
            <div>
              <p class="post-description"> This is the third photo of the feed. This is a multi-lined comment for testing purposes.</p>
            </div>
            <p></p>
            <!--Post image-->
            <div>
              <img class="post-image" src="../../public/clothes.jpg" alt="image 2">
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
import Navbar from '../components/main/Navbar'
import SideNavBar from "../components/main/SideNavBar";

export default {
  name: "Home",
  components: {
    SideNavBar,
    Footer,
    Navbar
  },
  methods: {
    /* Set the width of the side navigation to 250px and the left margin of the page content to 250px */
    openNav() {
      document.getElementById("mySidenav").style.width = "250px";
      document.getElementById("main").style.marginLeft = "250px";
    },
  },
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
header{
  background: white;
  padding: 10px
}
header h1{
  margin: 0;
}

div.post{
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
div.post .post-header div:first-child{
  display: inline-block;
  width: 80%;
}

/*
 * Styling for date in header of the post.
 */
div.post .post-header div:last-child{
  display: inline-block;
  width: 20%;
  padding:10px;
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
div.container{
  width: 424px;
  margin: auto;
}

/**
 * Styling for profile images for each post. Makes image into circle.
 * TODO make DRY
 */
#profile-image-1, #profile-image-2, #profile-image-3{
  height: 50px;
  width: auto;
  border-radius: 50px;
}

.post-image {
  border-radius: 15px;
}

.account-name {
  font-weight: bold;
  margin-left: 10px;
}

.post-description {
  margin-bottom: 30px;
}

/*Side nav styling*/

.side-nav-bar-separator {
  color: white;
}

.sidenav {
  height: 100%;
  width: 0;
  position: fixed;
  z-index: 1;
  top: 0;
  left: 0;
  background-color: #111;
  overflow-x: hidden;
  transition: 0.5s;
  padding-top: 60px;
}

.sidenav a {
  padding: 8px 8px 8px 32px;
  text-decoration: none;
  font-size: 25px;
  color: #818181;
  display: block;
  transition: 0.3s;
}

.sidenav a:hover {
  color: #f1f1f1;
}

.sidenav .closebtn {
  position: absolute;
  top: 0;
  right: 25px;
  font-size: 36px;
  margin-left: 50px;
}

@media screen and (max-height: 450px) {
  .sidenav {padding-top: 15px;}
  .sidenav a {font-size: 18px;}
}


/*-------------------------------------------- Medium break point styling -------------------------------------------*/

/*Medium break point*/
@media(min-width: 692px) {

  div.container {
    width: 524px;
    margin: auto;
  }
}


/*-------------------------------------------- Large break point styling -------------------------------------------*/

@media(min-width: 800px) {
  /*
 * Width is 524px since news feed item is 600px and then 12px margin each side.
 */
  div.container {
    width: 624px;
    margin: auto;
  }
}
</style>
