<template>
  <div id="home" class="container">
    <div class="container-news">
      <div class="post">
        <div class="post-header">
          <div>
            <h2>First Photo</h2>
          </div><div>
          <span>12/14</span>
        </div>
        </div>
        <div class="crop">
          <img src="https://i.pinimg.com/originals/89/27/dc/8927dcf60dfd605fce74cc3e2aea015f.jpg" alt="image 1">
        </div>
        <div>
          <p> This is my first photo of the feed. This is a multilined comment for testing purposes. I am learning to code with #BreatheCode</p>
        </div>
      </div>
      <div class="post">
        <div class="post-header">
          <div>
            <h2>First Photo</h2>
          </div><div>
          <span>12/14</span>
        </div>
        </div>
        <div>
          <img src="https://images.unsplash.com/photo-1494548162494-384bba4ab999?ixid=MXwxMjA3fDB8MHxzZWFyY2h8MXx8ZGF3bnxlbnwwfHwwfA%3D%3D&ixlib=rb-1.2.1&w=1000&q=80" height=400 width=200 alt="image 2">
        </div>
        <div>
          <p> This is my first photo of the feed. This is a multilined comment for testing purposes. I am learning to code with #BreatheCode</p>
        </div>
      </div>
      <div class="post">
        <div class="post-header">
          <div>
            <h2>First Photo</h2>
          </div><div>
          <span>12/14</span>
        </div>
        </div>
        <div>
          <img src="https://images.unsplash.com/photo-1494548162494-384bba4ab999?ixid=MXwxMjA3fDB8MHxzZWFyY2h8MXx8ZGF3bnxlbnwwfHwwfA%3D%3D&ixlib=rb-1.2.1&w=1000&q=80" height=400 width=200 alt="image 3">
        </div>
        <div>
          <p> This is my first photo of the feed. This is a multilined comment for testing purposes. I am learning to code with #BreatheCode</p>
        </div>
      </div>
    </div>
    <Footer />
  </div>
</template>

<script>
// import Api from '../Api';
// import Cookies from 'js-cookie';
import Footer from '../components/Footer';

export default {
  name: "Home",
  components: {
    Footer
  },
  methods: {

  }
}
</script>

<style scoped>

body {
  background: #BDBDBD;
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

/*
 * Margins for each news item are 12px wide and news item is 600px wide.
 * crop and crop img are used to crop the given image and keep the correct ratio and scale it to fit within
 * the news item.
 */
.crop {
  width: 600px;
  height: 450px;
  overflow: hidden;
}

/*
 * Used with .crop to obtain the effect explained in .crop's comment.
 */
.crop img {
  width: 1500px;
  height: 900px;
  margin: -75px  0 -100px 0;
}

div.post{
  background: white;
  margin-top: 10px;
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
  padding: 20px;
}

/*
 * width is 624px since news feed item is 600px and then 12px margin each side.
 */
div.container{
  width: 624px;
  margin: auto;
}
</style>