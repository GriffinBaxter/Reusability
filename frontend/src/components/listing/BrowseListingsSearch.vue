<template>

    <!--------------------------------------------  Searching, ordering, filtering   -------------------------------->
    <div id="search-filter-ordering-options-container">

      <!-- Search Bar  -->
      <div class="container mt-5">
        <div class="row" id="search-bar-container">
          <div class="input-group" id="search-inputs" style="alignment: center">
            <input type="text" id="search-bar" ref="searchInput" class="form-control" @keydown="enterPressed($event)">
            <button class="btn green-search-button" id="search-button" @click="searchClicked()"><i class="fas fa-search" aria-hidden="true"></i></button>
            <a class="btn green-button" data-bs-toggle="collapse" href="#filter-ordering-options-container" role="button"><i class="fas fa-angle-double-down"></i></a>
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
                <input class="form-check-input" type="radio" name="match-radios" value="listingName" id="radio-product-name">
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
            <div class="text-center">
              <button type="button" class="btn btn-md btn-outline-primary green-button m-2 d-inline-block w-50" @click="clearRadios('match')">
                Clear Field
              </button>
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
<!--            <div class="btn-group radio-padding-left" role="group">-->

<!--              <button type="button" class="btn green-button dropdown-toggle order-by-options-btn d-inline-block"-->
<!--                      data-bs-toggle="dropdown" aria-expanded="false">{{ businessTypeOptionText }}-->
<!--              </button>-->

<!--              <ul class="dropdown-menu" aria-labelledby="btnGroupDrop1">-->
<!--                <button type="button" class="btn green-button-transparent col-12 order-by-options-btn"-->
<!--                        @click="setBusinessTypeOption(true, false, false, false)">-->
<!--                  Accommodation and Food Services-->
<!--                </button>-->
<!--                <button type="button" class="btn green-button-transparent col-12 order-by-options-btn"-->
<!--                        @click="setBusinessTypeOption(false, true, false, false)">-->
<!--                  Retail Trade-->
<!--                </button>-->
<!--                <button type="button" class="btn green-button-transparent col-12 order-by-options-btn"-->
<!--                        @click="setBusinessTypeOption(false, false, true, false)">-->
<!--                  Charitable Organisation-->
<!--                </button>-->
<!--                <button type="button" class="btn green-button-transparent col-12 order-by-options-btn"-->
<!--                        @click="setBusinessTypeOption(false, false, false, true)">-->
<!--                  Non-profit Organisation-->
<!--                </button>-->

<!--              </ul>-->
<!--            </div>-->

            <div class="text-center" id="match-fields-clear-btn-container">

              <!--------------------------------------- clear field match button -------------------------------------------->
              <!--   TODO: add @click event               -->
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
      businessTypeOption: null,
      businessTypeOptionText: 'Business Type'
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
      const searchQuery = this.$refs.searchInput.value;
      const searchType = this.getSelectedRadio('match');
      const orderBy = this.orderByOption;
      const page = 0;
      const businessType = this.getSelectedRadio('business');
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
     * Clears the radio buttons used to select the match fields
     */
    clearRadios(type) {
      let radios;
      if (type === 'match') {
        radios = document.querySelectorAll("input[name='match-radios']");
      } else if (type === 'business') {
        radios = document.querySelectorAll("input[name='business-type-radios']");
      }
      radios.forEach(function(radio) {
        radio.checked = false
      })
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

    /**
     * Sets the business type option.
     */
    setBusinessTypeOption(acommodation, retail, charitable, nonProfit) {
      if (acommodation) {
        this.businessTypeOption = 'ACCOMMODATION_AND_FOOD_SERVICES';
        this.businessTypeOptionText = 'Accommodation and Food Services';
      } else if (retail) {
        this.businessTypeOption = 'RETAIL_TRADE';
        this.businessTypeOptionText = 'Retail Trade';
      } else if (charitable) {
        this.businessTypeOption = 'CHARITABLE_ORGANISATION';
        this.businessTypeOptionText = 'Charitable Organisation';
      } else if (nonProfit) {
        this.businessTypeOption = 'NON_PROFIT_ORGANISATION';
        this.businessTypeOptionText = 'Non-profit Organisation';
      }
      this.orderCards();
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
