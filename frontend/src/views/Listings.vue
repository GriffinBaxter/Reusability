<template>
  <div>
    <main>
    <!-- Navbar -->
    <Navbar/>
    <!-- Listing Creation -->
    <create-listing @updateListings="afterCreation"></create-listing>
    <!-- Listing Container -->
    <div class="container">
      <h1 id="pageTitle">{{ businessName }}'s Listings</h1>
      <div class="card p-1">
        <!-- Order Buttons -->
        <div class="row my-3" align="center">
          <!--filter-->
          <div class="btn-group col-3 py-1" role="group">
            <button type="button" class="btn green-button dropdown-toggle col-4"
                    data-bs-toggle="dropdown" aria-expanded="false">Filter Option
            </button>

            <ul class="dropdown-menu gap-2" aria-labelledby="btnGroupDrop1">
              <!--order by quantity-->
              <button type="button" class="btn green-button-transparent col-12"
                      @click="orderListings(true, false, false, false)">
                Quantity
                <i id="quantityIcon"></i>
              </button>

              <!--order by price-->
              <button type="button" class="btn green-button-transparent col-12"
                      @click="orderListings(false, true, false, false)">
                Price
                <i id="priceIcon"></i>
              </button>

              <!--order by closing date-->
              <button type="button" class="btn green-button-transparent col-12"
                      @click="orderListings(false, false, true, false)">
                Closing Date
                <i id="closesIcon"></i>
              </button>

              <!--order by listing date-->
              <button type="button" class="btn green-button-transparent col-12"
                      @click="orderListings(false, false, false, true)">
                Listing Date
                <i id="createdIcon"></i>
              </button>
            </ul>
          </div>

          <!-- Add new Button -->
          <div class="col-md" v-if="businessAdmin">
            <button type="button" class="btn green-button w-75 my-1" data-bs-toggle="modal" data-bs-target="#listingCreationPopup">Add new</button>
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
            <button type="button" :class="`btn green-button-transparent ${isValidPageNumber(currentPage-1) ? '': 'disabled'}`" @click="updatePage($event, currentPage-1)">
              Previous
            </button>

            <!-- This is shown when there are more then 2 pages and you are at page 1-->
            <button type="button" class="btn green-button-transparent" v-if="isValidPageNumber(currentPage-2) && currentPage === totalPages-1" @click="updatePage($event, currentPage-2)">
              {{currentPage-1}}
            </button>

            <!-- Only shows when we are past at least the first page -->
            <button type="button" class="btn green-button-transparent" v-if="isValidPageNumber(currentPage-1)" @click="updatePage($event, currentPage-1)">
              {{currentPage}}
            </button>

            <!-- This converts the current page into 1 origin.-->
            <button type="button" class="btn green-button-transparent active">
              {{currentPage+1}}
            </button>

            <!-- This converts the current page into 1 origin And only shows the option if there is another page-->
            <button type="button" class="btn green-button-transparent" v-if="isValidPageNumber(currentPage+1)" @click="updatePage($event, currentPage+1)">
              {{currentPage+2}}
            </button>

            <!-- This is shown when there are more then 2 pages and you are at page 1-->
            <button type="button" class="btn green-button-transparent" v-if="isValidPageNumber(currentPage+2) && currentPage === 0" @click="updatePage($event, currentPage-2)">
              {{currentPage+3}}
            </button>

            <!-- The next button only enabled if there is another page.-->
            <button type="button" :class="`btn green-button-transparent ${isValidPageNumber(currentPage+1) ? '': 'disabled'}`" @click="updatePage($event, currentPage+1)">
              Next
            </button>
          </ul>
        </nav>

      </div>
    </div>
    <div class="card p-1" v-if="listings.length < 1">
      <p class="h2 py-5" align="center">No Listings Found</p>
    </div>
    </main>
    <!-- Footer -->
    <Footer class="footer"/>
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

      quantityAscending: false,
      priceAscending: false,
      closesAscending: false,
      createdAscending: false,

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

    /**
     * Orders the listings based on the given booleans for each column, and updates the display
     * @param quantity Boolean, whether to order by quantity
     * @param price Boolean, whether to order by price
     * @param closes Boolean, whether to order by closing date
     * @param created Boolean, whether to order by listing date
     */
    orderListings(quantity, price, closes, created) {

      if (quantity) {
        this.disableIcons();
        if (this.quantityAscending) {
          this.orderBy = "quantityASC"
          document.getElementById('quantityIcon').setAttribute('class', 'fas fa-chevron-up float-end');
        } else {
          this.orderBy = "quantityDESC"
          document.getElementById('quantityIcon').setAttribute('class', 'fas fa-chevron-down float-end');
        }

        this.quantityAscending = !this.quantityAscending;
        this.priceAscending = false;
        this.closesAscending = false;
        this.createdAscending = false;

      } else if (price) {
        this.disableIcons();
        if (this.priceAscending) {
          this.orderBy = "priceASC"
          document.getElementById('priceIcon').setAttribute('class', 'fas fa-chevron-up float-end');
        } else {
          this.orderBy = "priceDESC"
          document.getElementById('priceIcon').setAttribute('class', 'fas fa-chevron-down float-end');
        }

        this.quantityAscending = false;
        this.priceAscending = !this.priceAscending;
        this.closesAscending = false;
        this.createdAscending = false;

      } else if (closes) {
        this.disableIcons();
        if (this.closesAscending) {
          this.orderBy = "closesASC"
          document.getElementById('closesIcon').setAttribute('class', 'fas fa-chevron-up float-end');
        } else {
          this.orderBy = "closesDESC"
          document.getElementById('closesIcon').setAttribute('class', 'fas fa-chevron-down float-end');
        }

        this.quantityAscending = false;
        this.priceAscending = false;
        this.closesAscending = !this.closesAscending;
        this.createdAscending = false;

      } else if (created) {
        this.disableIcons();
        if (this.createdAscending) {
          this.orderBy = "createdASC"
          document.getElementById('createdIcon').setAttribute('class', 'fas fa-chevron-up float-end');
        } else {
          this.orderBy = "createdDESC"
          document.getElementById('createdIcon').setAttribute('class', 'fas fa-chevron-down float-end');
        }

        this.quantityAscending = false;
        this.priceAscending = false;
        this.closesAscending = false;
        this.createdAscending = !this.createdAscending;

      }

      this.$router.push({path: `/businessProfile/${this.businessId}/listings`, query: {"orderBy": this.orderBy, "page": (this.currentPage + 1).toString()}});
      this.getListings();
    },

    /**
     * Disables all ascending or descending icons in the filter buttons.
     */
    disableIcons() {
      document.getElementById('quantityIcon').setAttribute('class', '');
      document.getElementById('priceIcon').setAttribute('class', '');
      document.getElementById('closesIcon').setAttribute('class', '');
      document.getElementById('createdIcon').setAttribute('class', '');
    },

    async getListings() {
      /*
      Attempts to get listings from backend
      If successful, sends data to populatePage()
      If not, redirects to appropriate page
      */
      this.orderBy = this.$route.query["orderBy"] || "closesASC";
      this.currentPage = parseInt(this.$route.query["page"]) - 1 || 0;

      await Api.sortListings(this.businessId, this.orderBy, this.currentPage).then(response => {
        this.populatePage(response);

      }).catch((error) => {
        if (error.request && !error.response) {
          this.$router.push({path: '/timeout'});
        } else if (error.response.status === 400) {
          this.$router.push({path: '/pageDoesNotExist'});
        } else if (error.response.status === 401) {
          this.$router.push({path: '/invalidtoken'});
        } else if (error.response.status === 406) {
          this.$router.push({path: '/noBusiness'});
        } else {
          this.$router.push({path: '/timeout'});
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
            quantity: response.data[i].quantity,
            price: response.data[i].price,
            listDate: response.data[i].created,
            closeDate: response.data[i].closes,
            moreInfo: response.data[i].moreInfo,
            expires: response.data[i].inventoryItem.expires
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

    async getUserRole(id) {
      await Api.getUser(id).then(response => {
        this.role = response.data.role;
      })
    },
    /**
     * After creation success use endpoint to collect data from backend and display it.
     */
    afterCreation() {
      this.getListings();
    },
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

      this.getListings().then(
          () => {}
      ).catch(
          (e) => console.log(e)
      )
    } else {
      this.$router.push({name: 'Login'});
    }
  }
}
</script>

<style scoped>

.footer {
  margin-top: 20%;
}

#pageTitle {
  padding: 10px;
  text-align: center;
}

/* Fix Footer */
main{
  min-height: calc(100vh - 100px); /* 这个200px是header和footer的高度 */
}

Footer{
  height: 100px;
}
</style>
