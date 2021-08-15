<template>
  <div>
    <div id="main">
      <!--nav bar-->
      <Navbar></Navbar>

      <div class="container mt-4">
        <div class="card p-3">
          <h1 id="page-title">{{ businessName }} Sale History</h1>
        </div>
        <div class="card p-3">
        <table class="table table-borderless table-striped"  id="sale-table">
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
          </tr>
          </tbody>
        </table>
        </div>
      </div>

    </div>
    <Footer/>
  </div>
</template>

<script>
import Navbar from "../components/main/Navbar";
import Footer from "../components/main/Footer";
import Cookies from "js-cookie";
import {checkAccessPermission} from "@/views/helpFunction";
import Api from "@/Api";
import CurrencyAPI from "../currencyInstance";
import {formatDate} from "@/dateUtils";

export default {
  name: "SaleHistory",
  components: {
    Footer,
    Navbar
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
        {title: "Bookmarks"}
      ],
      // Currency related variables
      currencyCode: "",
      currencySymbol: "",
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
     * Calls a GET request to the backend to retrieve the sold listings for the current business.
     */
    async retrieveSoldListings() {
      await Api.getSoldListings(this.businessId).then(response => {
        this.soldListings = response.data;
        for (let i = 0; i < this.soldListings.length; i++) {
          this.soldListings[i].listingDate = formatDate(this.soldListings[i].listingDate);
          this.soldListings[i].saleDate = formatDate(this.soldListings[i].saleDate);
          this.soldListings[i].price = this.formatPrice(this.soldListings[i].price);
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
      if (error.request && !error.response) { await this.$router.push({path: '/timeout'});      }
      if (error.response.status === 401)    { await this.$router.push({path: '/invalidtoken'}); }
      if (error.response.status === 403)    { await this.$router.push({path: '/forbidden'});    }
      if (error.response.status === 406)    { await this.$router.push({path: '/noBusiness'});   }
      else { await this.$router.push({path: '/noBusiness'}); console.log(error.message); }
    },
    formatPrice(price) {
      if (this.currencySymbol !=="" && this.currencyCode !== "") { return this.currencySymbol + price +  " " + this.currencyCode; }
      return price;
    }
  },
  /**
   * When mounted, initiate population of page.
   * If cookies are invalid or not present, redirect to login page.
   */
  async mounted() {
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

</style>