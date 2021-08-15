<template>
  <div>
    <div id="main">
      <!--nav bar-->
      <Navbar></Navbar>

      <div class="container mt-4">
        <div class="card p-3">
          <h1 id="pageTitle">{{ businessName }} Sale History</h1>
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
      currentPage: 0,
      soldListings: [],

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
      await Api.getSoldListings(this.businessId, this.currentPage).then(response => {
        this.soldListings = response.data;
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
        this.currencyCode = response[0].currencies[0].code;
        this.currencySymbol = response[0].currencies[0].symbol;
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
      // unknown error so redirect to no business page
      await this.$router.push({path: '/noBusiness'});
      console.log(error.message);
    }
  },
  /**
   * When mounted, initiate population of page.
   * If cookies are invalid or not present, redirect to login page.
   */
  async mounted() {
    const actAs = Cookies.get('actAs');
    if (checkAccessPermission(this.$route.params.id, actAs)) {
      await this.$router.push({path: `/businessProfile/${actAs}/saleHistory`});
    } else {
      const currentID = Cookies.get('userID');
      if (currentID) {
        this.businessId = this.$route.params.id;
        await this.retrieveBusinessInfo();
        await this.retrieveSoldListings();
        await this.retrieveCurrencyInfo();
      }
    }
  }
}
</script>

<style scoped>

</style>