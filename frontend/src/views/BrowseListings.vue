<template>
  <div>
    <div id="main">
      <!-- Navbar -->
      <Navbar/>
      <div class="container mt-5">
<!--        <input class="form-control" type="text">-->
        <div class="row pb-5 mb-4">
          <div class="col-md-5 col-xl-4 mb-4 mb-lg-0 d-flex justify-content-center">
            <BrowseListingCard></BrowseListingCard>
          </div>
          <div class="col-md-5 col-xl-4 mb-4 mb-lg-0 d-flex justify-content-center">
            <BrowseListingCard></BrowseListingCard>
          </div>
          <div class="col-md-5 col-xl-4 mb-4 mb-lg-0 d-flex justify-content-center">
            <BrowseListingCard></BrowseListingCard>
          </div>
          <div class="col-md-5 col-xl-4 mb-4 mb-lg-0 d-flex justify-content-center">
            <BrowseListingCard></BrowseListingCard>
          </div>
          <div class="col-md-5 col-xl-4 mb-4 mb-lg-0 d-flex justify-content-center">
            <BrowseListingCard></BrowseListingCard>
          </div>
          <div class="col-md-5 col-xl-4 mb-4 mb-lg-0 d-flex justify-content-center">
            <BrowseListingCard></BrowseListingCard>
          </div>
          <div class="col-md-5 col-xl-4 mb-4 mb-lg-0 d-flex justify-content-center">
            <BrowseListingCard></BrowseListingCard>
          </div>
          <div class="col-md-5 col-xl-4 mb-4 mb-lg-0 d-flex justify-content-center">
            <BrowseListingCard></BrowseListingCard>
          </div>
        </div>
      </div>

    </div>
    <!-- Footer -->
    <Footer class="footer"/>
  </div>
</template>

<script>
import Navbar from "../components/main/Navbar";
import Footer from "../components/main/Footer";
import BrowseListingCard from "../components/listing/BrowseListingCard";

export default {
  name: "Listings",
  components: {Footer, Navbar, BrowseListingCard},
  data() {
    return {}
  },
  computed: {
  },
  methods: {
  }
}

</script>

<style scoped>

</style>
