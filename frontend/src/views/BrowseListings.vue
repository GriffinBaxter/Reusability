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
        <div class="row pb-5 mb-4">
          <div class="col-md-5 col-xl-4 mb-4 mb-lg-0 d-flex justify-content-center" v-for="listing in listingList" v-bind:key="listing.id">
            <BrowseListingCard
                v-bind:id="listing.id"
                v-bind:inventory-item="listing.inventoryItem"
                v-bind:created="listing.created"
                v-bind:closes="listing.closes"
                v-bind:is-bookmarked="listing.isBookmarked"
                v-bind:more-info="listing.moreInfo"
                v-bind:price="listing.price"
                v-bind:quantity="listing.quantity"
                v-bind:total-bookmarks="listing.totalBookmarks"/>
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
    return {
      // Array that stores all retrieved listings
      listingList: []
    }
  },
  computed: {
  },
  methods: {
  },
  async mounted() {

    await Api.searchListings('a', 'businessName', 'expiryDateASC', '0', 'RETAIL_TRADE', '0.00', '10000.00', '2021-05-15T00:00', '2022-01-01T00:00').then((response) => {
      this.listingList = [...response.data];
      console.log(this.listingList);
    }, (error) => {
      console.log(error)
    });

  }
}

</script>

<style scoped>

</style>
