<template>
  <div>
    <div id="main">
      <!-- Navbar -->
      <Navbar/>
      <div class="container mt-5">
        <div class="row" id="search-bar-container">
          <div class="input-group" id="search-inputs" style="alignment: center">
            <input type="text" id="search-bar" ref="searchInput" class="form-control">
            <button class="btn green-search-button" id="search-button"><i class="fas fa-search" aria-hidden="true"></i></button>
          </div>
        </div>
        <br>
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
import Api from "../Api"

export default {
  name: "Listings",
  components: {Footer, Navbar, BrowseListingCard},
  data() {
    return {}
  },
  computed: {
  },
  methods: {
  },
  async mounted() {

    await Api.searchListings('Waste', 'businessName', 'expiryDateASC', '0', 'RETAIL_TRADE', '10.00', '90.00', '2021-05-15T00:00', '2022-01-01T00:00').then((response) => {
      console.log(response);
    }, (error) => {
      console.log(error)
    });

  }
}

</script>

<style scoped>

</style>
