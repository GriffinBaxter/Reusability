<template>

    <!--------------------------------------------  Searching, ordering, filtering   -------------------------------->
    <div id="search-filter-ordering-options-container">

      <!-- Search Bar  -->
      <div class="container mt-5">
        <div class="row" id="search-bar-container">
          <div class="input-group" id="search-inputs" style="alignment: center">
            <input type="text" id="search-bar" ref="searchInput" class="form-control" @keydown="enterPressed($event)">
            <button class="btn green-search-button" id="search-button" @click="searchClicked()"><i class="fas fa-search" aria-hidden="true"></i></button>
            <a class="btn green-button" data-bs-toggle="collapse" href="#filter-ordering-options-container" role="button"><i class="fas fa-angle-double-down" aria-hidden="true"></i></a>
          </div>


        <!------------------------------------------ searching, ordering and filtering container -------------------->
        <div class="row collapse" id="filter-ordering-options-container">

          <!-- Match Fields -->
          <div class="col-4 my-3 p-2" id="match-fields">

            <div class="row">
              <label class="d-inline-block fs-5 my-2 text-center">Match Fields</label>
            </div>

            <div class="match-radio-container py-2">
              <!--  match product name -->
              <div class="form-check radio-padding-left">
                <input class="form-check-input" type="radio" name="match-radios" value="listingName" id="radio-product-name" checked>
                <label class="form-check-label" for="radio-product-name">
                  Product Name
                </label>
              </div>

              <!--  match seller location -->
              <div class="form-check radio-padding-left">
                <input class="form-check-input " type="radio" name="match-radios" value="location" id="radio-seller-location">
                <label class="form-check-label" for="radio-seller-location">
                  Business Location
                </label>
              </div>

              <!--  match seller name -->
              <div class="form-check radio-padding-left">
                <input class="form-check-input " type="radio" name="match-radios" value="businessName" id="radio-seller-name">
                <label class="form-check-label" for="radio-seller-name">
                  Business Name
                </label>
              </div>
            </div>

            <!--  match seller type -->
            <div class="business-radio-container my-3 py-2">
              <div class="form-check radio-padding-left">
                <input class="form-check-input" type="radio" name="business-type-radios" value="ACCOMMODATION_AND_FOOD_SERVICES" id="radio-accommodation">
                <label class="form-check-label" for="radio-accommodation">
                  Accommodation and Food Services
                </label>
              </div>
              <div class="form-check radio-padding-left">
                <input class="form-check-input " type="radio" name="business-type-radios" value="RETAIL_TRADE" id="radio-retail">
                <label class="form-check-label" for="radio-retail">
                  Retail Trade
                </label>
              </div>
              <div class="form-check radio-padding-left">
                <input class="form-check-input " type="radio" name="business-type-radios" value="CHARITABLE_ORGANISATION" id="radio-charitable">
                <label class="form-check-label" for="radio-charitable">
                  Charitable Organisation
                </label>
              </div>
              <div class="form-check radio-padding-left">
                <input class="form-check-input " type="radio" name="business-type-radios" value="NON_PROFIT_ORGANISATION" id="radio-non-profit">
                <label class="form-check-label" for="radio-non-profit">
                  Non-profit Organisation
                </label>
              </div>
            </div>

            <div class="text-center" id="match-fields-clear-btn-container">

              <!--------------------------------------- clear field match button -------------------------------------------->
              <button type="button" class="btn btn-md btn-outline-primary green-button m-2 d-inline-block w-50" @click="clearRadios('business')">
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
                          @click="setOrderByOption('priceASC')">
                    Price Low
                  </button>
                  <button type="button" class="btn green-button-transparent col-12 order-by-options-btn"
                          @click="setOrderByOption('priceDESC')">
                    Price High
                  </button>

                  <!--order by product name-->
                  <button type="button" class="btn green-button-transparent col-12 order-by-options-btn"
                          @click="setOrderByOption('productNameASC')">
                    Product Name
                  </button>

                  <!--order by country-->
                  <button type="button" class="btn green-button-transparent col-12 order-by-options-btn"
                          @click="setOrderByOption('countryASC')">
                    Country
                  </button>

                  <!--order by city-->
                  <button type="button" class="btn green-button-transparent col-12 order-by-options-btn"
                          @click="setOrderByOption('cityASC')">
                    City
                  </button>

                  <!--order by expiry date-->
                  <button type="button" class="btn green-button-transparent col-12 order-by-options-btn"
                          @click="setOrderByOption('expiryDateASC')">
                    Expiry Date Earliest
                  </button>
                  <button type="button" class="btn green-button-transparent col-12 order-by-options-btn"
                          @click="setOrderByOption('expiryDateDESC')">
                    Expiry Date Latest
                  </button>

                  <!--order by seller name-->
                  <button type="button" class="btn green-button-transparent col-12 order-by-options-btn"
                          @click="setOrderByOption('sellerNameASC')">
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
                  <input type="number" min="0" class="form-control filter-input d-inline-block" id="lowest-price-input" placeholder="0.00" v-model="lowestPrice" oninput="this.value = Math.abs(this.value)">

                  <label for="highest-price-input" class="d-inline-block p-2"> to $ </label>
                  <input type="number" min="0" class="form-control filter-input d-inline-block" id="highest-price-input" placeholder="0.00" v-model="highestPrice" oninput="this.value = Math.abs(this.value)">
                </div>
              </form>
            </div>

            <div class="row m-2">
              <form>
                <div class="form-group" id="date-filtering-container">
                  <label for="start-date-input" class="d-inline-block p-2">Closing Date </label>
                  <input type="date" class="form-control filter-input d-inline-block" id="start-date-input" v-model="startDate">

                  <label for="end-date-input" class="d-inline-block p-2"> to </label>
                  <input type="date" class="form-control filter-input d-inline-block" id="end-date-input" v-model="endDate">
                </div>
              </form>
            </div>

            <div class="text-center" id="filter-buttons-container">

              <!--------------------------------------- clear filters button -------------------------------------------->
              <button type="button" class="btn btn-md btn-outline-primary green-button m-2 d-inline-block w-25" @click="clearFilters()">
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
import compareAsc from 'date-fns/compareAsc'
import {parseISO} from "date-fns";
export default {
  name: "BrowseListingsSearch",
  data() {
    return {
      orderByOption: "priceASC",         // default
      orderBy: this.$route.query["orderBy"] || "priceASC", // gets orderBy from URL or (if not there) sets to default
      orderByOptionText: "Price Low",
      businessTypeOption: null,
      businessTypeOptionText: 'Business Type',
      lowestPrice: null,
      highestPrice: null,
      startDate: null,
      endDate: null
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
     * Finds the selected radio button
     * @return The value of the selected radio button
     */
    getSelectedRadio(type) {
      let radios;
      if (type === 'match') {
        radios = document.querySelectorAll("input[name='match-radios']");
      } else if (type === 'business') {
        radios = document.querySelectorAll("input[name='business-type-radios']");
      }
      let value;

      for (const radio of radios) {
        if (radio.checked) {
          value = radio.value;
        }
      }

      if (!value) {
        return null
      }
      return value;
    },
    /**
     * Search button is clicked and query/filters for listings search are executed.
     */
    searchClicked() {

      if (!this.validateDateInput(this.startDate, this.endDate)) {
        const temp = this.startDate
        this.startDate = this.endDate
        this.endDate = temp
      }
      if (!this.validatePriceInput(this.lowestPrice, this.highestPrice)) {
        const temp = this.lowestPrice
        this.lowestPrice = this.highestPrice
        this.highestPrice = temp
      }


      const searchQuery = this.$refs.searchInput.value;
      const searchType = this.getSelectedRadio('match');
      const orderBy = this.orderByOption;
      const page = 0;
      const businessType = this.getSelectedRadio('business');
      const minimumPrice = this.lowestPrice;
      const maximumPrice = this.highestPrice;
      let fromDate = this.startDate;
      let toDate = this.endDate;
      if (fromDate != null) {
        fromDate += "T00:00";
      }
      if (toDate != null) {
        toDate += "T00:00";
      }

      if (
          searchQuery !== this.$route.query.searchQuery ||
          searchType !== this.$route.query.searchType ||
          orderBy !== this.$route.query.orderBy ||
          String(page) !== this.$route.query.page ||
          businessType !== this.$route.query.businessType ||
          minimumPrice !== this.$route.query.minimumPrice ||
          maximumPrice !== this.$route.query.maximumPrice ||
          fromDate !== this.$route.query.fromDate ||
          toDate !== this.$route.query.toDate
      ) {

        this.$router.push({
          path: '/browseListings', query: {
            searchQuery: searchQuery, searchType: searchType,
            orderBy: orderBy, page: page, businessType: businessType,
            minimumPrice: minimumPrice, maximumPrice: maximumPrice,
            fromDate: fromDate, toDate: toDate
          }
        });
      }

      this.$emit('requestListings', searchQuery);

    },
    /**
     * Clears the radio buttons used to select the match fields
     */
    clearRadios(type) {
      let radios;
      if (type === 'business') {
        radios = document.querySelectorAll("input[name='business-type-radios']");
      }
      radios.forEach(function(radio) {
        radio.checked = false
      })
    },
    /**
     * Sets the order by option
     */
    setOrderByOption(orderBy) {
      this.orderByOption = orderBy
      if (this.orderByOption === "priceASC") {
        this.orderByOptionText = "Price Low"
      } else if (this.orderByOption === "priceDESC") {
        this.orderByOptionText = "Price High"
      } else if (this.orderByOption === "productNameASC") {
        this.orderByOptionText = "Product Name"
      } else if (this.orderByOption === "countryASC") {
        this.orderByOptionText = "Country"
      } else if (this.orderByOption === "cityASC") {
        this.orderByOptionText = "City"
      } else if (this.orderByOption === "expiryDateASC") {
        this.orderByOptionText = "Expiry Date Earliest"
      } else if (this.orderByOption === "expiryDateDESC") {
        this.orderByOptionText = "Expiry Date Latest"
      } else if (this.orderByOption === "sellerNameASC") {
        this.orderByOptionText = "Business Name"
      }
    },

    /**
     * Checks that the price entered is a positive number.
     * If both prices are provided, then the first must be smaller than the second and non-negative
     * Returns true if condition met
     * @param firstPrice first price
     * @param secondPrice second price
     * @return {boolean}
     */
    validatePriceInput(firstPrice, secondPrice) {
      if (firstPrice != null && secondPrice != null) {
        return parseFloat(firstPrice) <= parseFloat(secondPrice)
      } else {
        return true
      }
    },

    /**
     * Checks that the second date is after the first date. Returns true if this is the case.
     * @param firstDate first date
     * @param secondDate second date
     * @return {boolean}
     */
    validateDateInput(firstDate, secondDate) {
      if (firstDate != null && secondDate != null) {
        const outcome = compareAsc(parseISO(firstDate), parseISO(secondDate))
        return (outcome === -1) // -1 if the first date is before the second
      } else {
        return true
      }
    },

    /**
     * Resets the filters to their default values
     */
    clearFilters() {
      this.lowestPrice = null
      this.highestPrice = null
      this.startDate = null
      this.endDate = null
    }

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

.match-radio-container, .business-radio-container {
  background-color: white;
  border-radius: 10px;
}

#search-bar-container {
  padding-top: 30px;
}

.radio-padding-left {
  padding-left: 60px;
}

</style>
