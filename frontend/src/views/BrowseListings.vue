<template>
  <div>
    <div id="main">
      <!-- Navbar -->
      <Navbar/>

      <BrowseListingsSearch  @requestListings="requestListings"/>
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
      <PageButtons></PageButtons>
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
import BrowseListingsSearch from '../components/listing/BrowseListingsSearch';
import PageButtons from "../components/PageButtons";

export default {
  name: "Listings",
  components: {Footer, Navbar, BrowseListingCard, BrowseListingsSearch, PageButtons},
  data() {
    return {
      // Array that stores all retrieved listings
      listingList: [],
    }
  },
  computed: {
  },
  methods: {

    /**
     * Order the cards
     */
    orderCards() {
      const order = this.createOrderByParams()

      // Checks the orderBy has changed to prevent NavigationDuplicated Errors
      if (order !== this.orderBy) {
        this.orderBy = order;
        this.$parent.$emit("orderedCards", this.orderBy)
      }

      // now can use this.orderBy to request cards from backend

    },

    /**
     * Takes the search query params from the current URL and executes an API call to filter listings
     * @return {Promise<void>}
     */
    async requestListings() {

      const searchQuery = this.$route.query.searchQuery || '';
      const searchType = this.$route.query.searchType || '';
      const orderBy = this.$route.query.orderBy || '';
      const page = this.$route.query.page || '0';
      const businessType = this.$route.query.businessType || '';
      const minimumPrice = this.$route.query.minimumPrice || '';
      const maximumPrice = this.$route.query.maximumPrice || '';
      const fromDate = this.$route.query.fromDate || '';
      const toDate = this.$route.query.toDate || '';

      await Api.searchListings(
          searchQuery, searchType, orderBy, page, businessType, minimumPrice, maximumPrice, fromDate, toDate
      ).then((response) => {
        this.listingList = [...response.data];
      }, (error) => {
        console.log(error)
      });
    },
  },

  async mounted() {

    await this.requestListings();

  }
}

</script>

<style scoped>

</style>
