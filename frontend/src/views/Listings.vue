<template>
  <div>
    <div id="main">
    <!-- Navbar -->
    <Navbar/>
    <!-- Listing Creation -->
    <create-listing @updateListings="afterCreation"
                    v-bind:currency-code="currencyCode"
                    v-bind:currency-symbol="currencySymbol"/>
    <!-- Listing Container -->
    <div class="container mt-4">
      <div class="card p-3">
        <h1 id="pageTitle">{{ businessName }}'s Listings</h1>
        <hr>
        <div class="row" role="group" aria-label="Button group with nested dropdown">
          <!--filter-->
          <div class="btn-group col-md-3 py-1 align-self-center" role="group">
            <button type="button" class="btn green-button dropdown-toggle" style="height: 38px"
                    data-bs-toggle="dropdown" aria-expanded="false">Filter Option
            </button>

            <ul class="dropdown-menu gap-2" aria-labelledby="btnGroupDrop1">
              <!--order by quantity-->
              <button type="button" class="btn green-button-transparent col-12"
                      @click="orderListings(true, false, false, false)">
                Quantity
                <i id="quantityIcon" aria-hidden="true"></i>
              </button>

              <!--order by price-->
              <button type="button" class="btn green-button-transparent col-12"
                      @click="orderListings(false, true, false, false)">
                Price
                <i id="priceIcon" aria-hidden="true"></i>
              </button>

              <!--order by closing date-->
              <button type="button" class="btn green-button-transparent col-12"
                      @click="orderListings(false, false, true, false)">
                Closing Date
                <i id="closesIcon" aria-hidden="true"></i>
              </button>

              <!--order by listing date-->
              <button type="button" class="btn green-button-transparent col-12"
                      @click="orderListings(false, false, false, true)">
                Listing Date
                <i id="createdIcon" aria-hidden="true"></i>
              </button>
            </ul>
          </div>

          <!-- Create New Button -->
          <div class="col-md-2 py-1 align-self-center" v-if="businessAdmin">
            <button type="button" class="btn green-button w-100" data-bs-toggle="modal" data-bs-target="#listingCreationPopup" style="height: 38px">Create New</button>
          </div>

          <div class="col-3 col-md-4 text-secondary flex-nowrap align-self-center">Filter By: {{convertToString()}}</div>

          <div class="col-md-3 py-1">
            <BarcodeSearchBar @barcodeSearch="barcodeSearch"/>
          </div>

        </div>

        <!--space-->
        <br>

        <!--creation success info-->
        <div class="alert alert-success" role="alert" v-if="creationSuccess">
          <div class="row">
            <div class="col" style="text-align: center">New Listing Created</div>
          </div>
        </div>

        <!-- Listings -->
        <ListingItem
            v-for="item in listings"
            v-bind:key="item.index"
            v-bind:listing-id="item.id"
            v-bind:product-name="item.productName"
            v-bind:description="item.description"
            v-bind:product-id="item.productId"
            v-bind:quantity="item.quantity"
            v-bind:price="item.price"
            v-bind:listDate="item.listDate"
            v-bind:close-date="item.closeDate"
            v-bind:full-close-date="item.fullCloseDate"
            v-bind:best-before="item.bestBefore"
            v-bind:expires="item.expires"
            v-bind:moreInfo="item.moreInfo"
            v-bind:currency-code="currencyCode"
            v-bind:currency-symbol="currencySymbol"
            v-bind:images="item.images"
            v-bind:barcode="item.barcode"
            v-bind:isAdmin="businessAdmin"
        />

        <!--space-->
        <br>

        <!---------------------------------------------- page buttons ------------------------------------------------>

        <div id="page-button-container">
          <PageButtons
              v-bind:totalPages="totalPages"
              v-bind:currentPage="currentPage"
              @updatePage="updatePage"/>
        </div>

      </div>
    </div>
      <div class="noListings" v-if="noListings">
        <div class="card p-1">
          <p class="h2 py-5" style="text-align: center">No Listings Found</p>
        </div>
      </div>
    </div>
    <!-- Footer -->
    <Footer class="footer"/>
  </div>
</template>

<script>
import Navbar from "../components/Navbar";
import ListingItem from "../components/listing/ListingItem";
import Api from "../Api";
import Cookies from "js-cookie";
import CreateListing from "../components/listing/CreateListingModal";
import Footer from "../components/main/Footer";
import PageButtons from "../components/PageButtons";
import {formatDate} from "../dateUtils";
import BarcodeSearchBar from "../components/BarcodeSearchBar";

export default {
name: "Listings",
  components: {Footer, CreateListing, ListingItem, Navbar, PageButtons, BarcodeSearchBar},
  data() {
    return {
      allListings: [],
      listings: [],
      // When page is initially loaded, we don't want 'No Listings Found' message to display since, listings have not
      // been retrieved yet.
      notInitialLoad: false,
      businessName: "",
      businessAdmin: false,
      businessId: -1,
      role: "",
      currentUser: -1,

      orderBy: "",
      rowsPerPage: 5,
      currentPage: 0,
      totalPages: 0,
      totalRows: 0,

      quantityAscending: false,
      priceAscending: false,
      closesAscending: false,
      createdAscending: false,

      // currency related variables
      businessCountry: "", // used to retrieve the currency code and symbol
      currencyCode: "",
      currencySymbol: "",

      barcode: "",

      creationSuccess: false
    }
  },
  computed: {
  noListings() {
    return (this.listings.length < 1) && this.notInitialLoad;
  }
  },
  methods: {
    /**
     * Delete a listing at ID
     * @param id ID of listing to be deleted
     */
    async deleteListing(id) {
      await Api.deleteListing(id).then(() => {
        this.getListings()
      }).catch((err) => {
        if (err.response) {
          if (err.response.status === 406) {
            this.getListings()
          } else if (err.response.status === 401) {
            this.$router.push({name: "InvalidToken"})
          } else if (err.response.status === 403) {
            this.businessAdmin = false
          } else {
            console.log(err)
          }
        } else {
          console.log(err)
        }
      })
    },
    /**
     * convert orderByString to more readable for user
     */
    convertToString() {
      switch (this.orderBy) {
        case 'quantityASC': return "Quantity Ascending";
        case 'quantityDESC': return "Quantity Descending";
        case 'priceASC': return "Price Ascending";
        case 'priceDESC': return "Price Descending";
        case 'closesASC': return "Closes Ascending";
        case 'closesDESC': return "Closes Descending";
        case 'createdASC': return "Created Ascending";
        case 'createdDESC': return "Created Descending";
      }
    },
    /**
     * Updates the display to show the new page when a user clicks to move to a different page.
     *
     * @param newPageNumber The new page number
     */
    updatePage(newPageNumber) {
      this.currentPage = newPageNumber;
      this.$router.push({path: `/businessProfile/${this.businessId}/listings`, query: {"barcode": this.barcode, "orderBy": this.orderBy, "page": (this.currentPage + 1).toString()}})
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

      this.$router.push({path: `/businessProfile/${this.businessId}/listings`, query: {"barcode": this.barcode, "orderBy": this.orderBy, "page": (this.currentPage + 1).toString()}});
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
      this.barcode = this.$route.query["barcode"] || "";

      if (this.barcode === undefined || this.barcode === null) {
        this.barcode = "";
      }

      await Api.sortListings(this.businessId, this.orderBy, this.currentPage, this.barcode).then(response => {
        this.totalRows = parseInt(response.headers["total-rows"]);
        this.totalPages = parseInt(response.headers["total-pages"]);

        if (this.totalPages > 0 && this.currentPage > this.totalPages - 1) {
          this.$router.push({path: '/pageDoesNotExist'});
        }

        this.populatePage(response);
        this.notInitialLoad = true;

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
    async getBusiness(id) {
      await Api.getBusiness(id).then(response => (this.getBusinessData(response.data))).catch((error) => {
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
      this.businessCountry = data.address.country;
      this.currencySymbol = data.currencySymbol;
      this.currencyCode = data.currencyCode;
      // Checks if user is acting as business
      const actAs = Cookies.get('actAs');
      this.businessAdmin = actAs === String(data.id);
      // Checks if user is a global admin
      if (actAs === undefined && this.businessAdmin === false) {
        data.administrators.forEach(user => {
          if (this.currentUser === user.id.toString()) {
            this.businessAdmin = true;
          }
        });
        if (this.role === "DEFAULTGLOBALAPPLICATIONADMIN" || this.role === "GLOBALAPPLICATIONADMIN") {
          this.businessAdmin = true;
        }
      }
    },
    populatePage(response) {
      if (response.data.length <= 0) {
        this.listings = [];
        this.currentPage = 0;
        this.maxPage = 0;
        this.totalRows = 0;
        this.totalPages = 0;
        // Generate the tableData to be placed in the table & get the total number of rows.
      } else {
        this.listings = [];

        for (let i = 0; i < this.rowsPerPage; i++) {
          if (i === response.data.length) {
            return
          }
          this.listings.push({
            id: response.data[i].id,
            productName: response.data[i].inventoryItem.product.name,
            description: response.data[i].inventoryItem.product.description,
            productId: response.data[i].inventoryItem.product.id,
            quantity: response.data[i].quantity,
            price: response.data[i].price,
            listDate: formatDate(response.data[i].created, false),
            closeDate: formatDate(response.data[i].closes, false),
            fullCloseDate: response.data[i].closes,
            moreInfo: response.data[i].moreInfo,
            expires: formatDate(response.data[i].inventoryItem.expires, false),
            images: response.data[i].inventoryItem.product.images,
            barcode: response.data[i].inventoryItem.product.barcode
          })
        }
      }
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
      this.creationSuccess = true;
      // The corresponding alert will close automatically after 5000ms.
      setTimeout(() => {
        this.creationSuccess = false
      }, 5000);
      this.getListings();
    },

    /**
     * Routes to URL with event value as the barcode and triggers getListings
     */
    barcodeSearch(event) {
      this.$router.push({path: `/businessProfile/${this.businessId}/listings`,
        query: {"barcode": event, "orderBy": this.orderBy, "page": (this.currentPage + 1).toString()}})
      this.getListings();
    }
  },
  async mounted() {
    /**
     * When mounted, initiate population of page.
     * If cookies are invalid or not present, redirect to login page.
     */
    this.currentUser = Cookies.get('userID');
    if (this.currentUser) {
      await this.getUserRole(this.currentUser);
      this.businessId = await parseInt(this.$route.params.id);
      await this.getBusiness(this.businessId);

      this.getListings().catch(
          (e) => console.log(e)
      );
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
