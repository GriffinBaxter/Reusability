<template>
  <div>
    <div id="main">
      <!--nav bar-->
      <Navbar></Navbar>

      Period:
      <div class="btn-group col d-inline-block p-2" role="group">
        <button type="button" class="btn green-button dropdown-toggle order-by-options-btn w-100"
                data-bs-toggle="dropdown" aria-expanded="false">{{ period }}
        </button>
        <ul class="dropdown-menu gap-2" aria-labelledby="btnGroupDrop1">
          <li class="btn green-button-transparent col-12 order-by-options-btn"
              @click="period = 'Year'">
            Year
          </li>
          <li class="btn green-button-transparent col-12 order-by-options-btn"
              @click="period = 'Month'">
            Month
          </li>
          <li class="btn green-button-transparent col-12 order-by-options-btn"
              @click="period = 'Day'">
            Day
          </li>
        </ul>
      </div>

      <div v-if="period === 'Month'" class="btn-group col d-inline-block p-2" role="group">
        <button type="button" class="btn green-button dropdown-toggle order-by-options-btn w-100"
                id="sales-period-select-month"
                data-bs-toggle="dropdown" aria-expanded="false">{{ selectedMonth }}
        </button>
        <ul class="dropdown-menu gap-2" aria-labelledby="btnGroupDrop1">
          <div v-if="selectedYear === currentYear">
            <li class="btn green-button-transparent col-12 order-by-options-btn"
                v-for="month in months.slice(0, this.currentMonth + 1)" v-bind:key="month"
                @click="selectedMonth = month">
              {{ month }}
            </li>
          </div>
          <div v-else>
            <li class="btn green-button-transparent col-12 order-by-options-btn"
                v-for="month in months" v-bind:key="month"
                @click="selectedMonth = month">
              {{ month }}
            </li>
          </div>
        </ul>
      </div>

      <div v-if="period === 'Year' || period === 'Month'" class="btn-group col d-inline-block p-2" role="group">
        <button type="button" class="btn green-button dropdown-toggle order-by-options-btn w-100"
                id="sales-period-select-year"
                data-bs-toggle="dropdown" aria-expanded="false">{{ selectedYear }}
        </button>
        <ul class="dropdown-menu gap-2" aria-labelledby="btnGroupDrop1">
          <li class="btn green-button-transparent col-12 order-by-options-btn"
              v-for="validYear in validYears" v-bind:key="validYear"
              @click="selectedYear = validYear">
            {{ validYear }}
          </li>
        </ul>
      </div>

      <div v-if="period === 'Day'" class="btn-group col d-inline-block p-2" role="group">
        <input type="date" id="sales-period-select-day" v-model="selectedDay" :min="'2021-01-01'" :max="currentDay">
      </div>

      <div class="container mt-4">
        <div class="card p-3">
          <h1 id="page-title">{{ businessName }} Sale History</h1>
        </div>
        <div class="card p-3">
        <table class="table table-borderless"  id="sale-table">
          <thead>
          <tr>
            <th id="columns" v-for="column in columns" :key="column.title">{{ column.title }}</th>
          </tr>
          </thead>
          <tbody>
          <tr id="rows" v-for="listing in soldListings" :key="listing.id">
            <td>{{ listing.productId }}</td>
            <td>{{ listing.listingDate }}</td>
            <td>{{ listing.saleDate }}</td>
            <td>{{ listing.quantity }}</td>
            <td>{{ listing.price }}</td>
            <td>{{ listing.bookmarks }}</td>
            <td @click="goToCustomerProfile(listing.customer.id)" id="buyer-cell">
              {{ listing.customer.firstName }} {{ listing.customer.lastName }}
            </td>
          </tr>
          </tbody>
          <tfoot>
          <div v-if="noResults">
            No listings sold
          </div>
          <div v-else>
            Showing {{currentPage*maxSoldListings+1}}-{{numberListingsRetrieved+currentPage*maxSoldListings}} of {{totalListings}} listings sold
          </div>
          </tfoot>
        </table>
        </div>
        <!---------------------------------------------- page buttons ------------------------------------------------>
        <div class="page-buttons mt-4 mb-4" id="page-button-container">
          <PageButtons
              v-bind:totalPages="totalPages"
              v-bind:currentPage="currentPage"
              @updatePage="updatePage"/>
        </div>
      </div>
    </div>
    <Footer/>
  </div>
</template>

<script>
import Navbar from "../components/Navbar";
import Footer from "../components/main/Footer";
import Cookies from "js-cookie";
import {checkAccessPermission} from "../views/helpFunction";
import Api from "../Api";
import CurrencyAPI from "../currencyInstance";
import {formatDate} from "../dateUtils";
import PageButtons from "../components/PageButtons";
import {format} from "date-fns";

export default {
  name: "SaleHistory",
  components: {
    Footer,
    Navbar,
    PageButtons
  },
  data() {
    return {
      businessId: 0,
      businessName: "",
      businessCountry: "",
      soldListings: [],
      columns: [
        {title: "Product Id"},
        {title: "Listing Date"},
        {title: "Sale Date"},
        {title: "Quantity"},
        {title: "Price"},
        {title: "Bookmarks"},
        {title: "Buyer"}
      ],
      // Currency related variables
      currencyCode: "",
      currencySymbol: "",

      // Table
      maxSoldListings: 10,
      // Total number of pages is used to determine pagination
      totalPages: 1,
      currentPage: 0,
      noResults: false,
      numberListingsRetrieved: 0,
      totalListings: 0,

      // Sales Report Attributes

      period: "Year",

      currentYear: 0,
      selectedYear: "",
      validYears: [],

      currentMonth: 0,
      selectedMonth: "",
      months: [
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
      ],

      currentDay: "",
      selectedDay: "",
    }
  },
  methods: {
    /**
     * Calls a GET request to the backend to retrieve the information of the current business.
     */
    async retrieveBusinessInfo() {
      await Api.getBusiness(this.businessId).then(response => {
        this.businessName = response.data.name;
        this.businessCountry = response.data.address.country;
      }).catch((error) => {
        this.manageError(error);
      })
    },
    /**
     * Calls a GET request to the backend to retrieve the sold listings for the current business, and
     * then formats the retrieved data to be displayed.
     */
    async retrieveSoldListings() {
      await Api.getSoldListings(this.businessId, this.currentPage).then(response => {
        this.soldListings = response.data;
        this.numberListingsRetrieved = this.soldListings.length;
        this.totalListings = parseInt(response.headers["total-rows"]);
        this.totalPages = parseInt(response.headers["total-pages"]);
        // format the dates and prices of the listings accordingly
        for (let i = 0; i < this.soldListings.length; i++) {
          this.soldListings[i].listingDate = formatDate(this.soldListings[i].listingDate);
          this.soldListings[i].saleDate = formatDate(this.soldListings[i].saleDate);
          this.soldListings[i].price = this.formatPrice(this.soldListings[i].price);
        }
        // no results have been received so the no results message should be displayed
        if (this.soldListings.length === 0) {
          this.noResults = true;
        }
        // fill the table with empty rows so that the table remains a constant size
        if (this.soldListings.length < this.maxSoldListings) {
          for (let j = this.soldListings.length; j < this.maxSoldListings; j++) {
            this.soldListings.push({
              bookmarks: "",
              id: j,
              listingDate: "",
              price: "",
              productId: "",
              quantity: "",
              saleDate: "",
              customer: {
                firstName: "",
                lastName: ""
              }
            });
          }
        }
      }).catch((error) => {
        this.manageError(error);
      })
    },
    /**
     * Calls a GET request to the REST Countries API to retrieve the currency code and symbol for the business
     * based on its country.
     */
    async retrieveCurrencyInfo() {
      await CurrencyAPI.currencyQuery(this.businessCountry).then((response) => {
        this.currencyCode = response.data[0].currencies[0].code;
        this.currencySymbol = response.data[0].currencies[0].symbol;
      }).catch((error) => console.log(error))
    },
    /**
     * If a request in to the backend results in an error, then this method will deal with the error.
     * @param error the error received from the backend.
     */
    async manageError(error) {
      if (error.request && !error.response)      { await this.$router.push({path: '/timeout'});      }
      else if (error.response.status === 401)    { await this.$router.push({path: '/invalidtoken'}); }
      else if (error.response.status === 403)    { await this.$router.push({path: '/forbidden'});    }
      else if (error.response.status === 406)    { await this.$router.push({path: '/noBusiness'});   }
      else { await this.$router.push({path: '/noBusiness'}); console.log(error.message); }
    },
    /**
     * This method adds a currency symbol and unit to a price, if the business has a valid currency.
     * An example of the returned format is $24 USD
     */
    formatPrice(price) {
      if (this.currencySymbol !=="" && this.currencyCode !== "") { return this.currencySymbol + price +  " " + this.currencyCode; }
      return price;
    },
    /**
     * Given a new page number. The function will update the table to show the new page.
     * @param newPageNumber The 0 origin page number.
     */
    updatePage(newPageNumber) {
        this.currentPage = newPageNumber;
        this.retrieveSoldListings();
    },
    /**
     * Redirects the business administrator to the profile page of the buyer of a listing based on their id.
     */
    goToCustomerProfile(userId) {
      this.$router.push({path: `/profile/${userId}`});
    },
    /**
     * Sets the dates based on the current date (selectable years, months and days).
     */
    setDates(date) {
      // Sets validYears to a list of years from 2021 to the current year
      this.currentYear = date.getFullYear();
      this.selectedYear = this.currentYear;
      this.validYears = [];
      this.validYears.push(2021);
      while (this.validYears[this.validYears.length - 1] < this.selectedYear) {
        this.validYears.push(this.validYears[this.validYears.length - 1] + 1);
      }

      this.currentMonth = date.getMonth();
      this.selectedMonth = this.months[this.currentMonth];

      this.currentDay = format(date, 'yyyy-MM-dd');
      this.selectedDay = this.currentDay;
    }
  },
  /**
   * When mounted, initiate population of page.
   * If cookies are invalid or not present, redirect to login page.
   */
  async mounted() {
    this.setDates(new Date());

    const actAs = Cookies.get('actAs');
    if (checkAccessPermission(this.$route.params.businessId, actAs)) {
      await this.$router.push({path: `/businessProfile/${actAs}/saleHistory`});
    } else {
      const currentID = Cookies.get('userID');
      if (currentID) {
        this.businessId = parseInt(this.$route.params.businessId);
        await this.retrieveBusinessInfo();
        await this.retrieveCurrencyInfo();
        await this.retrieveSoldListings();
      }
    }
  }
}
</script>

<style scoped>

#page-title {
  padding: 10px;
  text-align: center;
}

#rows {
  min-height: 40px;
  height: 40px;
}

#buyer-cell:hover {
  color: #1EBA8C !important;
  cursor: pointer;
}

table td:hover {
  cursor: default;
}

.dropdown-menu {
  margin-top: 0;
  border: none;
  width: 200px;
}

</style>
