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

export default {
  name: "Listings",
  components: {Footer, Navbar, BrowseListingCard, BrowseListingsSearch, PageButtons},
  data() {
    return {
      // Array that stores all retrieved listings
      listingList: [],

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
    }
  },
  computed: {
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
      this.currentPage = this.$route.query.page || 0;
      this.businessType = this.$route.query.businessType || '';
      this.minimumPrice = this.$route.query.minimumPrice || '';
      this.maximumPrice = this.$route.query.maximumPrice || '';
      this.fromDate = this.$route.query.fromDate || '';
      this.toDate = this.$route.query.toDate || '';
      
      this.currentPage = parseInt(this.currentPage)

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
          orderBy: this.orderBy, page: this.currentPage, businessType: this.businessType,
          minimumPrice: this.minimumPrice, maximumPrice: this.maximumPrice,
          fromDate: this.fromDate, toDate: this.toDate
        }
      });
      await this.requestListings();
    },
  },

  async mounted() {

    await this.requestListings();

  }
}

</script>

<style scoped>

</style>
