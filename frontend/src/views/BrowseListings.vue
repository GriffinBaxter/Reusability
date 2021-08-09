<template>
  <div>
    <div id="main">
      <!-- Navbar -->
      <Navbar/>

      <!-- TODO: make this a separate component once finished  -->

      <!--------------------------------------------  Searching, ordering, filtering   -------------------------------->

        <!-- Search Bar  -->
        <div class="container mt-5">
          <div class="row" id="search-bar-container">
            <div class="input-group" id="search-inputs" style="alignment: center">
              <input type="text" id="search-bar" ref="searchInput" class="form-control">
              <button class="btn green-search-button" id="search-button"><i class="fas fa-search" aria-hidden="true"></i></button>
            </div>
          </div>

          <!-- Match Fields -->
          <div class="row" id="search-filter-ordering-options-container">
            <div class="col-2" id="match-fields">
              <div class="form-check">
                <input class="form-check-input" type="checkbox" value="" id="checkbox-product-name">
                <label class="form-check-label" for="checkbox-product-name">
                  Product Name
                </label>
              </div>
              <div class="form-check">
                <input class="form-check-input" type="checkbox" value="" id="checkbox-seller-location">
                <label class="form-check-label" for="checkbox-seller-location">
                  Seller Location
                </label>
              </div>
              <div class="form-check">
                <input class="form-check-input" type="checkbox" value="" id="checkbox-seller-name">
                <label class="form-check-label" for="checkbox-seller-name">
                  Seller Name
                </label>
              </div>
              <div class="form-check">
                <input class="form-check-input" type="checkbox" value="" id="checkbox-seller-type">
                <label class="form-check-label" for="checkbox-seller-type">
                  Seller Type
                </label>
              </div>
            </div>

            <div class="col-2" id="order-menu">

              <!------------------------------------------ ordering by options menu ------------------------------------------->
              <div class="row">
                <p class="fw-normal">Order By</p>
              </div>

              <div class="row">
                <div class="btn-group col" role="group">

                  <button type="button" class="btn green-button dropdown-toggle order-by-options-btn"
                          data-bs-toggle="dropdown" aria-expanded="false">{{ orderByOption }}
                  </button>

                  <ul class="dropdown-menu gap-2" aria-labelledby="btnGroupDrop1">
                    <!--order by price-->
                    <button type="button" class="btn green-button-transparent col-12 order-by-options-btn"
                            @click="setOrderByOption(true, false, false, false, false, false, false)">
                      Price Low
                    </button>
                    <button type="button" class="btn green-button-transparent col-12 order-by-options-btn"
                            @click="setOrderByOption(false, true, false, false, false, false, false, false)">
                      Price High
                    </button>

                    <!--order by product name-->
                    <button type="button" class="btn green-button-transparent col-12 order-by-options-btn"
                            @click="setOrderByOption(false, false, true, false, false, false, false, false)">
                      Product Name
                    </button>

                    <!--order by country-->
                    <button type="button" class="btn green-button-transparent col-12 order-by-options-btn"
                            @click="setOrderByOption(false, false, false, true, false, false, false, false)">
                      Country
                    </button>

                    <!--order by city-->
                    <button type="button" class="btn green-button-transparent col-12 order-by-options-btn"
                            @click="setOrderByOption(false, false, false, false, true, false, false, false)">
                      City
                    </button>

                    <!--order by expiry date-->
                    <button type="button" class="btn green-button-transparent col-12 order-by-options-btn"
                            @click="setOrderByOption(false, false, false, false, false, true, false, false)">
                      Expiry Date Earliest
                    </button>
                    <button type="button" class="btn green-button-transparent col-12 order-by-options-btn"
                            @click="setOrderByOption(false, false, false, false, false, false, true, false)">
                      Expiry Date Latest
                    </button>

                    <!--order by seller name-->
                    <button type="button" class="btn green-button-transparent col-12 order-by-options-btn"
                            @click="setOrderByOption(false, false, false, false, false, false, false, true)">
                      Seller Name
                    </button>
                  </ul>
                </div>

              </div>

              <!--------------------------------------------------------------------------------------------------------->

            </div>
            <div class="col-6" id="price-and-date-filters">

              <div class="row">
                <form>
                  <div class="form-group" id="price-filtering-container">
                    <label for="lowest-price-input" class="d-inline-block">Price Range $ </label>
                    <input type="number" class="form-control filter-input d-inline-block" id="lowest-price-input" placeholder="0.00">

                    <label for="lowest-price-input" id="date-filtering-container"> to $ </label>
                    <input type="number" class="form-control filter-input d-inline-block" id="highest-price-input" placeholder="0.00">
                  </div>
                </form>
              </div>

              <div class="row">
                <form>
                  <div class="form-group">
                    <label for="lowest-price-input" class="d-inline-block">Closing Date </label>
                    <input type="date" class="form-control filter-input d-inline-block " id="start-date">

                    <label for="lowest-price-input" class="d-inline-block"> to $ </label>
                    <input type="date" class="form-control filter-input d-inline-block" id="end-date">
                  </div>
                </form>
              </div>

              <div class="row" id="filter-buttons-container">

                <!--------------------------------------- apply filters button -------------------------------------------->
                <div class="col">
                  <!--   TODO: add @click event               -->
                  <button type="button" class="btn btn-md btn-outline-primary green-button m-2">
                    Apply Filters
                  </button>
                </div>

                <!--------------------------------------- clear filters button -------------------------------------------->
                <div class="col">
                  <!--   TODO: add @click event               -->
                  <button type="button" class="btn btn-md btn-outline-primary green-button m-2">
                    Clear Filters
                  </button>
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

      orderByOption: "priceASC",         // default
      orderBy: this.$route.query["orderBy"] || "priceASC" // gets orderBy from URL or (if not there) sets to default

    }
  },
  computed: {
  },
  methods: {
    /**
     * Sets the order by option
     */
    setOrderByOption(priceLow, priceHigh, productName, country, city, expiryDateEarliest, expiryDateLatest, sellerName) {
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

#price-filtering-container, #date-filtering-container {
  margin-top: 8px;
  margin-bottom: 8px;
}

#filter-buttons-container, #price-and-date-filters {
  /*margin: auto;*/
  /*width: 50%;*/
  text-align: center;
}

/* styling for price and date range input fields */
.form-control {
  width: 26%;
}

</style>
