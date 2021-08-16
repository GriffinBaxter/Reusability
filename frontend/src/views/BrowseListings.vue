<template>
  <div>
    <div id="main">
      <!-- Navbar -->
      <Navbar/>

      <h1 style="text-align: center" class="mt-5 mb-0">Browse Listings</h1>

      <div class="noListings" v-if="noListings">
        <div class="card p-1">
          <p class="h2 py-5" style="text-align: center">No Listings Found</p>
        </div>
      </div>
      
      <BrowseListingsSearch  @requestListings="requestListings"/>
      <br>
      <div id="all-listings-cards-container" class="row pb-5 mb-4">
        <div class="col-md-6 col-xxl-3 col-xl-4 mb-4 mb-lg-0 d-flex justify-content-center" v-for="listing in listingList" v-bind:key="listing.id">
          <BrowseListingCard
              v-bind:id="listing.id"
              v-bind:inventory-item="listing.inventoryItem"
              v-bind:created="listing.created"
              v-bind:closes="listing.closes"
              v-bind:is-bookmarked="listing.isBookmarked"
              v-bind:more-info="listing.moreInfo"
              v-bind:price="listing.price"
              v-bind:quantity="listing.quantity"
              v-bind:total-bookmarks="listing.totalBookmarks"
              v-bind:actingBusinessId="actingBusinessId"/>
        </div>
      </div>
      <PageButtons
          v-bind:totalPages="totalPages"
          v-bind:currentPage="currentPage"
          @updatePage="updatePage"/>
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
import Cookies from "js-cookie";

export default {
  name: "Listings",
  components: {Footer, Navbar, BrowseListingCard, BrowseListingsSearch, PageButtons},
  data() {
    return {
      // Array that stores all retrieved listings
      listingList: [],
      notInitialLoad: false,

      searchQuery: "",
      searchType: "",
      orderBy: "",
      businessType: "",
      minimumPrice: "",
      maximumPrice: "",
      fromDate: "",
      toDate: "",

      currentPage: 0,
      totalPages: 0,
      totalRows: 0,
      
      actingBusinessId: null,
    }
  },
  computed: {
    noListings() {
      return (this.listingList.length < 1) && this.notInitialLoad;
    }
  },
  methods: {

    /**
     * Takes the search query params from the current URL and executes an API call to filter listings
     * @return {Promise<void>}
     */
    async requestListings() {

      this.searchQuery = this.$route.query.searchQuery || '';
      this.searchType = this.$route.query.searchType || '';
      this.orderBy = this.$route.query.orderBy || '';
      this.currentPage = parseInt(this.$route.query.page) - 1 || 0;
      this.businessType = this.$route.query.businessType || '';
      this.minimumPrice = this.$route.query.minimumPrice || '';
      this.maximumPrice = this.$route.query.maximumPrice || '';
      this.fromDate = this.$route.query.fromDate || '';
      this.toDate = this.$route.query.toDate || '';

      if (this.currentPage < 0) {
        this.currentPage = 0
      }

      await Api.searchListings(
          this.searchQuery, this.searchType,
          this.orderBy, this.currentPage, this.businessType,
          this.minimumPrice, this.maximumPrice,
          this.fromDate, this.toDate
      ).then((response) => {
        this.totalPages = parseInt(response.headers["total-pages"]);

        if (this.totalPages > 0 && this.currentPage > this.totalPages - 1) {
          this.$router.push({path: '/pageDoesNotExist'});
        }

        this.notInitialLoad = true;

        this.listingList = [...response.data];

      }, (error) => {
        console.log(error)
      });
    },
    
    /**
     * Updates the display to show the new page when a user clicks to move to a different page.
     *
     * @param newPageNumber The new page number
     */
    async updatePage(newPageNumber) {
      this.currentPage = newPageNumber;
      await this.$router.push({
        path: '/browseListings', query: {
          searchQuery: this.searchQuery, searchType: this.searchType,
          orderBy: this.orderBy, page: (this.currentPage + 1).toString(), businessType: this.businessType,
          minimumPrice: this.minimumPrice, maximumPrice: this.maximumPrice,
          fromDate: this.fromDate, toDate: this.toDate
        }
      });
      await this.requestListings();
    },
  },

  async mounted() {

    this.actingBusinessId = Cookies.get("actAs");
    await this.requestListings();

  }
}

</script>

<style scoped>



@media (min-width: 720px) {
  #all-listings-cards-container {
    margin-left: 120px;
    margin-right: 120px;
  }
}

</style>
