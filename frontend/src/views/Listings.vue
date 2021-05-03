<template>
  <div>
    <!-- Navbar -->
    <Navbar/>
    <!-- Listing Creation -->
    <create-listing v-bind:business-id="businessId"></create-listing>
    <!-- Listing Container -->
    <div class="container">
      <h1 id="pageTitle">{{ businessName }}'s Listings</h1>
      <div class="card p-1">
        <!-- Order Buttons -->
        <div class="row my-3" align="center">
          <div class="col-md" v-if="listings.length > 0">
            <button type="button" class="btn btn-outline-success w-75 my-1">New Listings</button>
          </div>
          <div class="col-md" v-if="listings.length > 0">
            <button type="button" class="btn btn-outline-success w-75 my-1">Closing Soon</button>
          </div>
          <div class="col-md" v-if="listings.length > 0">
            <button type="button" class="btn btn-outline-success w-75 my-1">Name</button>
          </div>
          <!-- Add new Button -->
          <div class="col-md" v-if="businessAdmin">
            <button type="button" class="btn btn-success w-75 my-1" data-bs-toggle="modal" data-bs-target="#listingCreationPopup">Add new</button>
          </div>
        </div>
        <!-- Listings -->
        <ListingItem
            v-for="item in listings"
            v-bind:key="item.index"
            v-bind:product-name="item.productName"
            v-bind:description="item.description"
            v-bind:product-id="item.productId"
            v-bind:quantity="item.quantity"
            v-bind:quantityPerSale="item.quantityPerSale"
            v-bind:price="item.price"
            v-bind:listDate="item.listDate"
            v-bind:close-date="item.closeDate"
            v-bind:best-before="item.bestBefore"
            v-bind:expires="item.expires"
            v-bind:moreInfo="item.moreInfo"
            v-bind:currency-code="currencyCode"
            v-bind:currency-symbol="currencySymbol"
        />

        <!--space-->
        <br>

        <!--pagination-->
        <nav>
          <ul v-if="totalPages > 0" class="pagination justify-content-center">
            <!-- This is only enabled when there is a previous page -->
            <button type="button" :class="`btn btn-outline-primary ${isValidPageNumber(currentPage-1) ? '': 'disabled'}`" @click="updatePage($event, currentPage-1)">
              Previous
            </button>

            <!-- This is shown when there are more then 2 pages and you are at page 1-->
            <button type="button" class="btn btn-outline-primary" v-if="isValidPageNumber(currentPage-2) && currentPage === totalPages-1" @click="updatePage($event, currentPage-2)">
              {{currentPage-1}}
            </button>

            <!-- Only shows when we are past at least the first page -->
            <button type="button" class="btn btn-outline-primary" v-if="isValidPageNumber(currentPage-1)" @click="updatePage($event, currentPage-1)">
              {{currentPage}}
            </button>

            <!-- This converts the current page into 1 origin.-->
            <button type="button" class="btn btn-outline-primary active">
              {{currentPage+1}}
            </button>

            <!-- This converts the current page into 1 origin And only shows the option if there is another page-->
            <button type="button" class="btn btn-outline-primary" v-if="isValidPageNumber(currentPage+1)" @click="updatePage($event, currentPage+1)">
              {{currentPage+2}}
            </button>

            <!-- This is shown when there are more then 2 pages and you are at page 1-->
            <button type="button" class="btn btn-outline-primary" v-if="isValidPageNumber(currentPage+2) && currentPage === 0" @click="updatePage($event, currentPage-2)">
              {{currentPage+3}}
            </button>

            <!-- The next button only enabled if there is another page.-->
            <button type="button" :class="`btn btn-outline-primary ${isValidPageNumber(currentPage+1) ? '': 'disabled'}`" @click="updatePage($event, currentPage+1)">
              Next
            </button>
          </ul>
        </nav>

      </div>
    </div>
    <div class="card p-1" v-if="listings.length < 1">
      <p class="h2 py-5" align="center">No Listings Found</p>
    </div>
    <!-- Footer -->
    <Footer/>
  </div>
</template>

<script>
import Navbar from "@/components/Navbar";
import ListingItem from "@/components/ListingItem";
import Api from "@/Api";
import Cookies from "js-cookie";
import CreateListing from "@/components/CreateListing";
import Footer from "@/components/Footer";
import CurrencyAPI from "@/currencyInstance";

export default {
name: "Listings",
  components: {Footer, CreateListing, ListingItem, Navbar},
  data() {
    return {
      allListings: [],
      listings: [],
      businessName: "",
      businessAdmin: false,
      businessId: -1,
      role: "",
      orderBy: "",
      rowsPerPage: 5,
      currentPage: 0,
      totalPages: 0,
      totalRows: 0,

      currencyCode: "",
      currencySymbol: ""
    }
  },
  methods: {
    /**
     * Updates the display to show the new page when a user clicks to move to a different page.
     *
     * @param event The click event
     * @param newPageNumber The new page number
     */
    updatePage(event, newPageNumber) {
      this.currentPage = newPageNumber;
      this.$router.push({path: `/businessProfile/${this.businessId}/listings`, query: {"orderBy": this.orderBy, "page": (this.currentPage + 1).toString()}})
      this.getListings();
    },

    /**
     * Given a page number check that the page is within the acceptable range.
     * NOTE this is a 0 origin.
     * @param pageNumber The page number to be checked.
     */
    isValidPageNumber(pageNumber) {
      return 0 <= pageNumber && pageNumber < this.totalPages;
    },

    async getListings() {
      /*
      Attempts to get listings from backend
      If successful, sends data to populatePage()
      If not, redirects to appropriate page
      */
      this.orderBy = this.$route.query["orderBy"] || "idASC";
      this.currentPage = parseInt(this.$route.query["page"]) - 1 || 0;

      await Api.sortListings(this.businessId, this.orderBy, this.currentPage).then(response => {
        this.populatePage(response);

      }).catch((error) => {
        if (error.request && !error.response) {
          this.$router.push({path: '/timeout'});
        } else if (error.response.status === 401) {
          this.$router.push({path: '/invalidtoken'});
        } else if (error.response.status === 406) {
          this.$router.push({path: '/noBusiness'});
        } else {
          this.$router.push({path: '/noBusiness'});
          console.log(error.message);
        }
      })
    },
    getBusiness(id) {
      Api.getBusiness(id).then(response => (this.getBusinessData(response.data))).catch((error) => {
        if (error.request && !error.response) {
          this.$router.push({path: '/timeout'});
        } else if (error.response.status === 401) {
          this.$router.push({path: '/invalidtoken'});
        } else if (error.response.status === 406) {
          this.$router.push({path: '/noBusiness'});
        } else {
          this.$router.push({path: '/noBusiness'});
          console.log(error.message);
        }
      })
    },
    getBusinessData(data) {
      this.businessName = data.name;
      // Checks if user is acting as business
      const actAs = Cookies.get('actAs');
      this.businessAdmin = actAs === String(data.id);
      // Checks if user is a global admin
      if (this.businessAdmin === false) {
        if (this.role === "DEFAULTGLOBALAPPLICATIONADMIN" || this.role === "GLOBALAPPLICATIONADMIN") {
          this.businessAdmin = true;
        }
      }
    },
    populatePage(response) {
      if (response.data.length <= 0) {
        this.currentPage = 0;
        this.maxPage = 0;
        this.totalRows = 0;
        this.totalPages = 0;
        // Generate the tableData to be placed in the table & get the total number of rows.
      } else {
        this.totalRows = parseInt(response.headers["total-rows"]);
        this.totalPages = parseInt(response.headers["total-pages"]);

        this.listings = [];

        for (let i = 0; i < this.rowsPerPage; i++) {
          if (i === response.data.length) {
            return
          }
          this.listings.push({
            productName: response.data[i].inventoryItem.product.name,
            description: response.data[i].inventoryItem.product.description,
            productId: response.data[i].inventoryItem.product.id,
            quantityPerSale: response.data[i].inventoryItem.quantity,
            quantity: response.data[i].quantity,
            price: response.data[i].price,
            listDate: response.data[i].created,
            closeDate: response.data[i].closes,
            moreInfo: response.data[i].moreInfo
          })
        }
      }
    },

    /**
     * Currency API requests.
     * An asynchronous function that calls the REST Countries API with the given country input.
     * Upon success, the filterResponse function is called with the response data.
     */
    async currencyRequest() {
      /*
        Request business from backend. If received assign the country of the business
        to a variable.
        */
      let country = "";
      await Api.getBusiness(this.businessId).then((response) => {
        country = response.data.address.country;
      })
          .catch((error) => console.log(error))

      await CurrencyAPI.currencyQuery(country).then((response) => {
        this.filterResponse(response.data);
      })
          .catch((error) => console.log(error))
    },

    /**
     * Retrieves the currency code and symbol that we want from the API response.
     * @param response The response from the REST countries API
     */
    filterResponse(response) {
      this.currencyCode = response[0].currencies[0].code;
      this.currencySymbol = response[0].currencies[0].symbol;
    },

    fakeListings() {
      this.listings.push({
        productName: 'Beans',
        productId: 'WATT-420-BEANS',
        description: 'Watties baked beanz is natures super food. 99% fat free, high in protein, source of iron and a great source of dietary fibre. Watties baked beans are low gi, giving you long-lasting energy to keep you going for longer. Proudly made in nz.',
        quantityPerSale: 4,
        quantity: 3,
        price: 17.99,
        listDate: '28/4/2021',
        closeDate: '1/5/2021',
        expires: '2/5/2021',
        moreInfo: 'Seller may be willing to consider near offers'
      })
      this.listings.push({
        productName: 'Apples',
        productId: 'APPLES',
        quantityPerSale: 2,
        quantity: 5,
        price: 5,
        listDate: '28/4/2021',
        closeDate: '1/5/2021',
        expires: '2/5/2021'
      })
      this.listings.push({
        productName: 'XXX',
        productId: 'XXX',
        description: 'Watties baked beanz is natures super food. 99% fat free, high in protein, source of iron and a great source of dietary fibre. Watties baked beans are low gi, giving you long-lasting energy to keep you going for longer. Proudly made in nz.',
        quantityPerSale: 4,
        quantity: 3,
        price: 17.99,
        listDate: '28/4/2021',
        closeDate: '10/5/2021',
        expires: '24/5/2021',
        moreInfo: ''
      })
      this.listings.push({
        productName: 'YYY',
        productId: 'UHHHH',
        description: "",
        quantityPerSale: 4,
        quantity: 3,
        price: 17.99,
        listDate: '28/4/2021',
        closeDate: '1/5/2021',
        expires: '2/5/2021',
        moreInfo: 'Seller may be willing to consider near offers'
      })
    },
    async getUserRole(id) {
      await Api.getUser(id).then(response => {
        this.role = response.data.role;
      })
    }
  },
  async mounted() {
    /**
     * When mounted, initiate population of page.
     * If cookies are invalid or not present, redirect to login page.
     */
    const currentID = Cookies.get('userID');
    if (currentID) {
      await this.getUserRole(currentID);
      this.businessId = await parseInt(this.$route.params.id);
      await this.getBusiness(this.businessId);

      await this.currencyRequest();

      this.getListings(this.businessId);

      // this.fakeListings();
      // this.populatePage(this.listings);
    } else {
      this.$router.push({name: 'Login'});
    }
  }
}
</script>

<style scoped>

#pageTitle {
  padding: 10px;
  text-align: center;
}

</style>
