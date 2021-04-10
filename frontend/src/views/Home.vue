<!--<link rel="preconnect" href="https://fonts.gstatic.com">-->
<!--<link href="https://fonts.googleapis.com/css2?family=Oswald&display=swap" rel="stylesheet">-->
<template>

  <div>
    <Navbar :is-business-account="true"></Navbar>
    <div id="home" class="container all-but-footer">
      <div class="container-news">

        <div class="post shadow py-5 px-4">
          <div class="post-header">
            <div>
              <img id="profile-image-1" src="../../public/profile_icon_1.jpg" alt="profile image">
              <span class="account-name">Green Grocers </span>
            </div><div>
              <span>Apr 6</span>
            </div>
          </div>
          <div>
            <p class="post-description"> This is the first photo of the feed. This is a multi-lined comment for testing purposes.</p>
          </div>
          <p></p>
          <div>
            <img class="post-image" src="../../public/apples.jpg" alt="image 2">
          </div>
        </div>

        <div class="post shadow py-5 px-4">
          <div class="post-header">
            <div>
              <img id="profile-image-2" src="../../public/profile_icon_2.jpg" alt="profile image">
              <span class="account-name">Fast Frank's</span>
            </div><div>
            <span>Apr 4</span>
          </div>
          </div>
          <div>
            <p class="post-description"> This is the second photo of the feed. This is a multi-lined comment for testing purposes.</p>
          </div>
          <p></p>
          <div>
            <img class="post-image" src="../../public/cans.jpg" alt="image 2">
          </div>
        </div>

        <div class="post shadow py-5 px-4">
          <div class="post-header">
            <div>
              <img id="profile-image-3" src="../../public/profile_icon_3.jpg" alt="profile image">
              <span class="account-name">New Leaf Organics</span>
            </div><div>
            <span>Apr 3</span>
          </div>
          </div>
          <div>
            <p class="post-description"> This is the third photo of the feed. This is a multi-lined comment for testing purposes.</p>
          </div>
          <p></p>
          <div>
            <img class="post-image" src="../../public/clothes.jpg" alt="image 2">
          </div>
        </div>

      </div>
    </div>
    <Footer></Footer>
  </div>
</template>

<script>
// import Api from '../Api';
// import Cookies from 'js-cookie';
import Footer from '../components/Footer';
import Navbar from '../components/Navbar'

export default {
  name: "Home",
  components: {
    Footer,
    Navbar
  },
  methods: {

  }
}
</script>

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

/* STYLING FOR CROPPING AND REDUCING IMAGES THAT ARE LARGE THAN 600PX WIDE -> could use elsewhere in project later */
/*!**/
/* * Margins for each news item are 12px wide and news item is 600px wide.*/
/* * crop and crop img are used to crop the given image and keep the correct ratio and scale it to fit within*/
/* * the news item.*/
/* *!*/
/*.crop {*/
/*  width: 600px;*/
/*  height: 450px;*/
/*  overflow: hidden;*/
/*}*/

/*!**/
/* * Used with .crop to obtain the effect explained in .crop's comment.*/
/* *!*/
/*.crop img {*/
/*  width: 1500px;*/
/*  height: 900px;*/
/*  margin: -75px  0 -100px 0;*/
/*}*/

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
 * styling for header of the post. This will later be change to be the business icon of the owner of the post.
 */
div.post .post-header div:first-child{
  display: inline-block;
  width: 80%;
}

/*
 * styling for header of the post. This will later be change to be the business icon of the owner of the post.
 */
div.post .post-header div:first-child{
  display: inline-block;
  width: 80%;
}

/*
 * styling for date
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
 * width is 624px since news feed item is 600px and then 12px margin each side.
 */
div.container{
  width: 524px;
  margin: auto;
}

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

.all-but-footer {
  min-height: calc(100vh - 240px);
}

</style>