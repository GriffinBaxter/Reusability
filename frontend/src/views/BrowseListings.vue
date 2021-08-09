<template>
  <div>
    <div id="main">
      <!-- Navbar -->
      <Navbar/>

      <!-- TODO: make this a separate component once finished  -->

      <!--------------------------------------------  Searching, ordering, filtering   -------------------------------->
      <div id="search-filter-ordering-options-container">

        <!-- Search Bar  -->
        <div class="container mt-5">
          <div class="row" id="search-bar-container">
            <div class="input-group" id="search-inputs" style="alignment: center">
              <input type="text" id="search-bar" ref="searchInput" class="form-control">
              <button class="btn green-search-button" id="search-button"><i class="fas fa-search" aria-hidden="true"></i></button>
            </div>
          </div>

          <!------------------------------------------ searching, ordering and filtering container -------------------->
          <div class="row" id="filter-ordering-options-container">

            <!-- Match Fields -->
            <div class="col-3 mx-lg-5 my-3 d-inline-block p-2" id="match-fields">

              <div class="row">
                <label class="d-inline-block fs-5 my-2 text-center">Match Fields</label>
              </div>

              <div class="form-check d-inline-block px-3">
                <input class="form-check-input" type="radio" value="" id="radio-product-name">
                <label class="form-check-label" for="radio-product-name">
                  Product Name
                </label>
              </div>
              <div class="form-check d-inline-block px-3">
                <input class="form-check-input" type="radio" value="" id="radio-seller-location">
                <label class="form-check-label" for="radio-seller-location">
                  Seller Location
                </label>
              </div>
              <div class="form-check d-inline-block px-3">
                <input class="form-check-input" type="radio" value="" id="radio-seller-name">
                <label class="form-check-label" for="radio-seller-name">
                  Seller Name
                </label>
              </div>
            </div>

            <div class="col-2 my-4" id="order-menu">

              <!------------------------------------------ ordering by options menu ------------------------------------------->

              <div class="row">
                <label class="d-inline-block p-2 fs-5 text-center">Order By </label>

                <div class="btn-group col d-inline-block p-2" role="group">

                  <button type="button" class="btn green-button dropdown-toggle order-by-options-btn w-100"
                          data-bs-toggle="dropdown" aria-expanded="false">{{ orderByOption }}
                  </button>

                  <ul class="dropdown-menu gap-2" aria-labelledby="btnGroupDrop1">
                    <!--order by price-->
                    <button type="button" class="btn green-button-transparent col-12 order-by-options-btn"
                            @click="setOrderByOption(true, false, false, false, false, false, false, false)">
                      Price Low
                    </button>
                    <button type="button" class="btn green-button-transparent col-12 order-by-options-btn"
                            @click="setOrderByOption(false, true, false, false, false, false, false, false, false)">
                      Price High
                    </button>

                    <!--order by product name-->
                    <button type="button" class="btn green-button-transparent col-12 order-by-options-btn"
                            @click="setOrderByOption(false, false, true, false, false, false, false, false, false)">
                      Product Name
                    </button>

                    <!--order by country-->
                    <button type="button" class="btn green-button-transparent col-12 order-by-options-btn"
                            @click="setOrderByOption(false, false, false, true, false, false, false, false, false)">
                      Country
                    </button>

                    <!--order by city-->
                    <button type="button" class="btn green-button-transparent col-12 order-by-options-btn"
                            @click="setOrderByOption(false, false, false, false, true, false, false, false, false)">
                      City
                    </button>

                    <!--order by expiry date-->
                    <button type="button" class="btn green-button-transparent col-12 order-by-options-btn"
                            @click="setOrderByOption(false, false, false, false, false, true, false, false, false)">
                      Expiry Date Earliest
                    </button>
                    <button type="button" class="btn green-button-transparent col-12 order-by-options-btn"
                            @click="setOrderByOption(false, false, false, false, false, false, true, false, false)">
                      Expiry Date Latest
                    </button>

                    <!--order by seller name-->
                    <button type="button" class="btn green-button-transparent col-12 order-by-options-btn"
                            @click="setOrderByOption(false, false, false, false, false, false, false, true, false)">
                      Seller Name
                    </button>

                    <!--order by seller type-->
                    <button type="button" class="btn green-button-transparent col-12 order-by-options-btn"
                            @click="setOrderByOption(false, false, false, false, false, false, false, false, true)">
                      Seller Type
                    </button>
                  </ul>
                </div>

              </div>

              <!--------------------------------------------------------------------------------------------------------->

            </div>
            <div class="col-6 text-center my-4" id="price-and-date-filters">

              <label class="d-inline-block fs-5 my-2 text-center">Filters</label>

              <div class="row">
                <form>
                  <div class="form-group" id="price-filtering-container">
                    <label for="lowest-price-input" class="d-inline-block p-2">Price Range $ </label>
                    <input type="number" class="form-control filter-input d-inline-block" id="lowest-price-input" placeholder="0.00">

                    <label for="highest-price-input" class="d-inline-block p-2"> to $ </label>
                    <input type="number" class="form-control filter-input d-inline-block" id="highest-price-input" placeholder="0.00">
                  </div>
                </form>
              </div>

              <div class="row m-2">
                <form>
                  <div class="form-group" id="date-filtering-container">
                    <label for="start-date-input" class="d-inline-block p-2">Closing Date </label>
                    <input type="date" class="form-control filter-input d-inline-block" id="start-date-input">

                    <label for="end-date-input" class="d-inline-block p-2"> to </label>
                    <input type="date" class="form-control filter-input d-inline-block" id="end-date-input">
                  </div>
                </form>
              </div>

              <div class="text-center" id="filter-buttons-container">

                <!--------------------------------------- apply filters button -------------------------------------------->
                  <!--   TODO: add @click event               -->
                  <button type="button" class="btn btn-md btn-outline-primary green-button m-2 d-inline-block w-25">
                    Apply Filters
                  </button>

                <!--------------------------------------- clear filters button -------------------------------------------->
                  <!--   TODO: add @click event               -->
                  <button type="button" class="btn btn-md btn-outline-primary green-button m-2 d-inline-block w-25">
                    Clear Filters
                  </button>

              </div>

            </div>
          </div>
        </div>
      </div>

        <!----------------------------------------  Searching, ordering, filtering ends ------------------------------->

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
      listingList: [],

      orderByOption: "Price Low",         // default
      orderBy: this.$route.query["orderBy"] || "priceASC" // gets orderBy from URL or (if not there) sets to default

    }
  },
  computed: {
  },
  methods: {
    /**
     * Sets the order by option
     */
    setOrderByOption(priceLow, priceHigh, productName, country, city, expiryDateEarliest, expiryDateLatest, sellerName, sellerType) {
      if (priceLow) {
        this.orderByOption = "Price Low"
      } else if (priceHigh) {
        this.orderByOption = "Price High"
      } else if (productName) {
        this.orderByOption = "Product Name"
      } else if (country) {
        this.orderByOption = "Country"
      } else if (city) {
        this.orderByOption = "City"
      } else if (expiryDateEarliest) {
        this.orderByOption = "Expiry Date Earliest"
      } else if (expiryDateLatest) {
        this.orderByOption = "Expiry Date Latest"
      } else if (sellerName) {
        this.orderByOption = "Seller Name"
      } else if (sellerType) {
        this.orderByOption = "Seller Type"
      }
      this.orderCards();
    },

    //TODO: fix logic!
    /**
     * Builds the order by value that will be sent to the backend to order the cards by
     */
    createOrderByParams() {
      let direction = "ASC"
      if (this.orderByOption === "Price High" || this.orderByOption === "Expiry Date Latest") {
        direction = "DESC"
      }

      let orderByOptionString = this.orderByOption.toLocaleLowerCase();
      if (this.orderByOption === "Select Order By") {
        orderByOptionString = "created"
      }

      return `${orderByOptionString}${direction}`
    },

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

/* styling for price and date range input fields */
.form-control {
  width: 28%;
}

#search-filter-ordering-options-container {
  background-color: #f1f4f5;
  border-radius: 20px;
  margin-right: 10%;
  margin-left: 10%;
}

#search-bar-container {
  padding-top: 30px;
}

</style>
