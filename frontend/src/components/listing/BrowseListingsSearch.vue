<template>

    <!--------------------------------------------  Searching, ordering, filtering   -------------------------------->
    <div id="search-filter-ordering-options-container">

      <!-- Search Bar  -->
      <div class="container mt-5">
        <div class="row" id="search-bar-container">
          <div class="input-group" id="search-inputs" style="alignment: center">
            <input type="text" id="search-bar" ref="searchInput" class="form-control" @keydown="enterPressed($event)">
            <button class="btn green-search-button" id="search-button" @click="searchClicked()"><i class="fas fa-search" aria-hidden="true"></i></button>
          </div>

        <!------------------------------------------ searching, ordering and filtering container -------------------->
        <div class="row" id="filter-ordering-options-container">

          <!-- Match Fields -->
          <div class="col-4 my-3 p-2" id="match-fields">

            <div class="row">
              <label class="d-inline-block fs-5 my-2 text-center">Match Fields</label>
            </div>

            <!--  match product name -->
            <div class="form-check radio-padding-left">
              <input class="form-check-input" type="radio" value="" id="radio-product-name">
              <label class="form-check-label" for="radio-product-name">
                Product Name
              </label>
            </div>

            <!--  match seller location -->
            <div class="form-check radio-padding-left">
              <input class="form-check-input " type="radio" value="" id="radio-seller-location">
              <label class="form-check-label" for="radio-seller-location">
                Business Location
              </label>
            </div>

            <!--  match seller name -->
            <div class="form-check radio-padding-left">
              <input class="form-check-input " type="radio" value="" id="radio-seller-name">
              <label class="form-check-label" for="radio-seller-name">
                Business Name
              </label>
            </div>

            <!--  match seller type -->
            <div class="btn-group radio-padding-left" role="group">

              <button type="button" class="btn green-button dropdown-toggle order-by-options-btn d-inline-block"
                      data-bs-toggle="dropdown" aria-expanded="false">{{ matchBusinessTypeOption }}
              </button>

              <ul class="dropdown-menu" aria-labelledby="btnGroupDrop1">
                <button type="button" class="btn green-button-transparent col-12 order-by-options-btn"
                        @click="setOrderByOption(true, false, false, false)">
                  Accommodation and Food Services
                </button>
                <button type="button" class="btn green-button-transparent col-12 order-by-options-btn"
                        @click="setOrderByOption(false, true, false, false)">
                  Retail Trade
                </button>
                <button type="button" class="btn green-button-transparent col-12 order-by-options-btn"
                        @click="setOrderByOption(false, false, true, false)">
                  Charitable Organisation
                </button>
                <button type="button" class="btn green-button-transparent col-12 order-by-options-btn"
                        @click="setOrderByOption(false, false, false, true)">
                  Non-profit Organisation
                </button>

              </ul>
            </div>

            <div class="text-center" id="match-fields-clear-btn-container">

              <!--------------------------------------- clear field match button -------------------------------------------->
              <!--   TODO: add @click event               -->
              <button type="button" class="btn btn-md btn-outline-primary green-button m-2 d-inline-block w-50">
                Clear Field
              </button>

            </div>


          </div>

          <div class="col-2 my-4" id="order-menu">

            <!------------------------------------------ ordering by options menu ------------------------------------------->

            <div class="row">
              <label class="d-inline-block p-2 fs-5 text-center">Order By </label>

              <div class="btn-group col d-inline-block p-2" role="group">

                <button type="button" class="btn green-button dropdown-toggle order-by-options-btn w-100"
                        data-bs-toggle="dropdown" aria-expanded="false">{{ orderByOptionText }}
                </button>

                <ul class="dropdown-menu gap-2" aria-labelledby="btnGroupDrop1">
                  <!--order by price-->
                  <button type="button" class="btn green-button-transparent col-12 order-by-options-btn"
                          @click="setOrderByOption(true, false, false, false, false, false, false, false)">
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
          <div class="col-5 text-center my-4" id="price-and-date-filters">

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
  </div>
</template>

<script>
export default {
  name: "BrowseListingsSearch",
  data() {
    return {
      orderByOption: "priceASC",         // default
      orderBy: this.$route.query["orderBy"] || "priceASC", // gets orderBy from URL or (if not there) sets to default
      orderByOptionText: "Price Low",
      matchBusinessTypeOption: "Business Type"
    }
  },
  methods: {
    /**
     * When the enter key is pressed, the query is run with the search value.
     */
    enterPressed(event) {
      if (event.keyCode === 13) {
        // Enter pressed
        this.searchClicked()
      }
    },
    /**
     * Search button is clicked and query/filters for listings search are executed.
     */
    searchClicked() {
      const searchQuery = this.$refs.searchInput.value;
      const searchType = 'listingName';
      const orderBy = this.orderByOption;
      const page = 0;
      const businessType = null;
      const minimumPrice = null;
      const maximumPrice = null;
      const fromDate = null;
      const toDate = null;

      this.$router.push({ path: '/browseListings', query: {
        searchQuery: searchQuery, searchType: searchType,
          orderBy: orderBy, page: page, businessType: businessType,
          minimumPrice: minimumPrice, maximumPrice: maximumPrice,
          fromDate: fromDate, toDate: toDate
      }});
      this.$emit('requestListings', searchQuery);
    },

    /**
     * Sets the order by option
     */
    setOrderByOption(priceLow, priceHigh, productName, country, city, expiryDateEarliest, expiryDateLatest, sellerName) {
      if (priceLow) {
        this.orderByOption = "priceASC"
        this.orderByOptionText = "Price Low"
      } else if (priceHigh) {
        this.orderByOption = "priceDESC"
        this.orderByOptionText = "Price High"
      } else if (productName) {
        this.orderByOption = "productNameASC"
        this.orderByOptionText = "Product Name"
      } else if (country) {
        this.orderByOption = "countryASC"
        this.orderByOptionText = "Country"
      } else if (city) {
        this.orderByOption = "cityASC"
        this.orderByOptionText = "City"
      } else if (expiryDateEarliest) {
        this.orderByOption = "expiryDateASC"
        this.orderByOptionText = "Expiry Date Earliest"
      } else if (expiryDateLatest) {
        this.orderByOption = "expiryDateDESC"
        this.orderByOptionText = "Expiry Date Latest"
      } else if (sellerName) {
        this.orderByOption = "sellerNameASC"
        this.orderByOptionText = "Business Name"
      }
      this.orderCards();
    },

  }
}
</script>

<style scoped>

/* styling for price and date range input fields */
.form-control {
  width: 34%;
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

.radio-padding-left {
  padding-left: 60px;
}

</style>
