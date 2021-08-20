<template>

  <!--------------------------------------------  Searching, ordering, filtering   -------------------------------->
  <div id="search-filter-ordering-options-container">

    <!-- Search Bar  -->
    <div class="container mt-5">
      <div class="row" id="search-bar-container">
        <div class="input-group" id="search-inputs" style="alignment: center">
          <input type="text" id="search-bar" ref="searchInput" class="form-control" @keydown="enterPressed($event)">
          <button class="btn green-search-button" id="search-button" @click="searchClicked()">
            <i class="fas fa-search" aria-hidden="true"/>
          </button>
          <a class="btn green-button" data-bs-toggle="collapse" href="#filter-ordering-options-container" role="button"><i
              class="fas fa-angle-double-down" aria-hidden="true"></i></a>
        </div>


        <!------------------------------------------ searching, ordering and filtering container -------------------->
        <div class="row collapse" id="filter-ordering-options-container">

          <!-- Match Fields -->
          <div class="col-xl-4 col-md-6 mt-3 p-2" id="match-fields">

            <div class="row">
              <label class="d-inline-block fs-5 my-2 text-center">Match Fields</label>
            </div>

            <div class="match-radio-container py-2">
              <!--  match product name -->
              <div class="form-check radio-padding-left">
                <input class="form-check-input" type="radio" name="match-radios" value="listingName"
                       id="radio-product-name">
                <label class="form-check-label" for="radio-product-name">
                  Product Name
                </label>
              </div>

              <!--  match seller location -->
              <div class="form-check radio-padding-left">
                <input class="form-check-input" type="radio" name="match-radios" value="location"
                       id="radio-seller-location">
                <label class="form-check-label" for="radio-seller-location">
                  Business Location
                </label>
              </div>

              <!--  match seller name -->
              <div class="form-check radio-padding-left">
                <input class="form-check-input" type="radio" name="match-radios" value="businessName"
                       id="radio-seller-name">
                <label class="form-check-label" for="radio-seller-name">
                  Seller Name
                </label>
              </div>
            </div>

            <!--  match seller type -->
            <div class="business-radio-container my-3 py-2">
              <div class="form-check radio-padding-left">
                <input class="form-check-input" type="checkbox" name="business-type-radios"
                       value="ACCOMMODATION_AND_FOOD_SERVICES" id="radio-accommodation">
                <label class="form-check-label" for="radio-accommodation">
                  Accommodation and Food Services
                </label>
              </div>
              <div class="form-check radio-padding-left">
                <input class="form-check-input " type="checkbox" name="business-type-radios" value="RETAIL_TRADE"
                       id="radio-retail">
                <label class="form-check-label" for="radio-retail">
                  Retail Trade
                </label>
              </div>
              <div class="form-check radio-padding-left">
                <input class="form-check-input " type="checkbox" name="business-type-radios"
                       value="CHARITABLE_ORGANISATION" id="radio-charitable">
                <label class="form-check-label" for="radio-charitable">
                  Charitable Organisation
                </label>
              </div>
              <div class="form-check radio-padding-left">
                <input class="form-check-input " type="checkbox" name="business-type-radios"
                       value="NON_PROFIT_ORGANISATION" id="radio-non-profit">
                <label class="form-check-label" for="radio-non-profit">
                  Non-profit Organisation
                </label>
              </div>
            </div>

            <div class="text-center" id="match-fields-clear-btn-container">

              <!--------------------------------------- clear field match button -------------------------------------------->
              <button type="button" class="btn btn-md btn-outline-primary green-button m-2 d-inline-block w-50"
                      @click="clearRadios('business')">
                Clear Field
              </button>

            </div>


          </div>

          <div class="col-xl-4 col-md-6 mt-4" id="order-menu">

            <!------------------------------------------ ordering by options menu ------------------------------------------->

            <div class="row">
              <label class="d-inline-block p-2 fs-5 text-center">Order By </label>

              <div class="btn-group col d-inline-block p-2" role="group">

                <button type="button" class="btn green-button dropdown-toggle order-by-options-btn w-100"
                        data-bs-toggle="dropdown" aria-expanded="false">{{ orderByOptionText }}
                </button>

                <ul class="dropdown-menu gap-2" aria-labelledby="btnGroupDrop1">
                  <!--order by price-->
                  <li class="btn green-button-transparent col-12 order-by-options-btn"
                      @click="setOrderByOption('price')">
                    Price
                  </li>

                  <!--order by product name-->
                  <li class="btn green-button-transparent col-12 order-by-options-btn"
                      @click="setOrderByOption('productName')">
                    Product Name
                  </li>

                  <!--order by country-->
                  <li class="btn green-button-transparent col-12 order-by-options-btn"
                      @click="setOrderByOption('country')">
                    Country
                  </li>

                  <!--order by city-->
                  <li class="btn green-button-transparent col-12 order-by-options-btn"
                      @click="setOrderByOption('city')">
                    City
                  </li>

                  <!--order by expiry date-->
                  <li class="btn green-button-transparent col-12 order-by-options-btn"
                      @click="setOrderByOption('expiryDate')">
                    Expiry Date
                  </li>

                  <!--order by seller name-->
                  <li class="btn green-button-transparent col-12 order-by-options-btn"
                      @click="setOrderByOption('sellerName')">
                    Seller Name
                  </li>
                </ul>
              </div>

              <div class="btn-group col d-inline-block p-2" role="group">

                <button type="button" class="btn green-button dropdown-toggle order-by-options-btn w-100"
                        data-bs-toggle="dropdown" aria-expanded="false">{{ orderBySequenceText }}
                </button>

                <ul class="dropdown-menu gap-2" aria-labelledby="btnGroupDrop2">
                  <!--order by price-->
                  <li class="btn green-button-transparent col-12 order-by-options-btn"
                      @click="setOrderBySequence('ASC')">
                    {{ ascName }}
                  </li>
                  <li class="btn green-button-transparent col-12 order-by-options-btn"
                      @click="setOrderBySequence('DESC')">
                    {{ descName }}
                  </li>
                </ul>
              </div>


            </div>

            <!--------------------------------------------------------------------------------------------------------->

          </div>
          <div class="col-xl-4 text-center mt-4" id="price-and-date-filters">

            <label class="d-inline-block fs-5 my-2 text-center">Filters</label>

            <div class="row">
              <form>
                <div class="form-group" id="price-filtering-container">
                  <label for="lowest-price-input" class="d-inline-block p-2">Price Range $ </label>
                  <input type="number" min="0" class="form-control filter-input d-inline-block" id="lowest-price-input"
                         placeholder="0.00" v-model="lowestPrice">

                  <label for="highest-price-input" class="d-inline-block p-2"> to $ </label>
                  <input type="number" min="0" class="form-control filter-input d-inline-block" id="highest-price-input"
                         placeholder="0.00" v-model="highestPrice">
                </div>
              </form>
            </div>

            <div class="row m-2">
              <form>
                <div class="form-group" id="date-filtering-container">
                  <label for="start-date-input" class="d-inline-block p-2">Closing Date </label>
                  <input type="date" class="form-control filter-input d-inline-block" id="start-date-input"
                         v-model="startDate">

                  <label for="end-date-input" class="d-inline-block p-2"> to </label>
                  <input type="date" class="form-control filter-input d-inline-block" id="end-date-input"
                         v-model="endDate">
                </div>
              </form>
            </div>

            <div class="text-center" id="filter-buttons-container">

              <!--------------------------------------- clear filters button -------------------------------------------->
              <button type="button" class="btn btn-md btn-outline-primary green-button m-2 d-inline-block w-25"
                      @click="clearFilters()">
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
      orderByOption: "price",         // default
      orderBySequence: "ASC",         // default
      isASC: true,                    // default
      ascName: "From Lowest Price",      // default
      descName: "From Highest Price",  // default
      orderBy: this.$route.query["orderBy"] || "priceASC", // gets orderBy from URL or (if not there) sets to default
      orderByOptionText: "Price",
      orderBySequenceText: "From Lowest Price",
      businessTypeOption: null,
      businessTypeOptionText: 'Business Type',
      lowestPrice: null,
      highestPrice: null,
      startDate: null,
      endDate: null,
      isTypeSame: true,
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
      let value;

      if (type === 'match') {
        radios = document.querySelectorAll("input[name='match-radios']");

        for (const radio of radios) {
          if (radio.checked) {
            value = radio.value;
          }
        }
      } else if (type === 'business') {
        radios = document.querySelectorAll("input[name='business-type-radios']");
        value = [];

        for (const radio of radios) {
          if (radio.checked) {
            value.push(radio.value);
          }
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

      [this.lowestPrice, this.highestPrice] = this.validatePriceInput(this.lowestPrice, this.highestPrice)

      const searchQuery = this.$refs.searchInput.value;
      const searchType = this.getSelectedRadio('match');
      const orderBy = this.orderByOption + this.orderBySequence;
      const page = 1;
      const businessTypes = this.getSelectedRadio('business');
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

      // To check is business type same with last time
      this.isTypeSame = true;
      if (this.$route.query.businessTypes !== undefined &&
          this.$route.query.businessTypes.length === businessTypes.length) {
        this.$route.query.businessTypes.forEach((businessType) => {
          if (!businessType.contains(businessType)) {
            this.isTypeSame = false;
          }
        })
      }

      if (
          searchQuery !== this.$route.query.searchQuery ||
          searchType !== this.$route.query.searchType ||
          orderBy !== this.$route.query.orderBy ||
          String(page) !== this.$route.query.page ||
          !this.isTypeSame ||
          minimumPrice !== this.$route.query.minimumPrice ||
          maximumPrice !== this.$route.query.maximumPrice ||
          fromDate !== this.$route.query.fromDate ||
          toDate !== this.$route.query.toDate
      ) {
        this.$router.push({
          path: '/browseListings', query: {
            searchQuery: searchQuery, searchType: searchType,
            orderBy: orderBy, page: page, businessTypes: businessTypes,
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
      radios.forEach(function (radio) {
        radio.checked = false
      })
    },
    /**
     * Sets the order by option
     */
    setOrderByOption(orderBy) {
      this.orderByOption = orderBy
      if (this.orderByOption === "price") {
        this.orderByOptionText = "Price";
        this.ascName = "From Lowest Price";
        this.descName = "From Highest Price";
      } else if (this.orderByOption === "productName") {
        this.orderByOptionText = "Product Name";
        this.ascName = "From A To Z";
        this.descName = "From Z To A";
      } else if (this.orderByOption === "country") {
        this.orderByOptionText = "Country";
        this.ascName = "From A To Z";
        this.descName = "From Z To A";
      } else if (this.orderByOption === "city") {
        this.orderByOptionText = "City";
        this.ascName = "From A To Z";
        this.descName = "From Z To A";
      } else if (this.orderByOption === "expiryDate") {
        this.orderByOptionText = "Expiry Date";
        this.ascName = "From Earliest";
        this.descName = "From Latest";
      } else if (this.orderByOption === "sellerName") {
        this.orderByOptionText = "Seller Name";
        this.ascName = "From A To Z";
        this.descName = "From Z To A";
      }
      this.orderBySequenceText = this.isASC ? this.ascName : this.descName
    },

    /**
     * Sets the order by sequence
     */
    setOrderBySequence(orderBy) {
      this.orderBySequence = orderBy
      if (this.orderBySequence === "ASC") {
        this.orderBySequenceText = this.ascName;
        this.isASC = true;
      } else if (this.orderBySequence === "DESC") {
        this.orderBySequenceText = this.descName;
        this.isASC = false;
      }
    },

    /**
     * Checks that the price entered is a positive number and that the first number is smaller than the second, and
     * fixes the price input if required, and then returns the new lowest and highest prices.
     * @param lowestPrice lowest price
     * @param highestPrice highest price
     */
    validatePriceInput(lowestPrice, highestPrice) {
      // sets prices to 0 if they are negative
      if (!(lowestPrice == null || lowestPrice === "")) {
        if (parseFloat(lowestPrice) < 0) {
          lowestPrice = "0"
        }
      }
      if (!(highestPrice == null || highestPrice === "")) {
        if (parseFloat(highestPrice) < 0) {
          highestPrice = "0"
        }
      }

      // sets lowest price to 0 if there is a highest price and no lowest price
      if (
          (lowestPrice == null || lowestPrice === "") &&
          !(highestPrice == null || highestPrice === "")
      ) {
        lowestPrice = "0"
      }

      // swaps the highest and lowest prices if the lowest price is higher than the highest price
      if (
          (lowestPrice != null && lowestPrice !== "") &&
          (highestPrice != null && highestPrice !== "") &&
          (parseFloat(lowestPrice) > parseFloat(highestPrice))
      ) {
        const temp = lowestPrice
        lowestPrice = highestPrice
        highestPrice = temp
      }

      return [lowestPrice, highestPrice]
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
  padding-bottom: 30px;
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
