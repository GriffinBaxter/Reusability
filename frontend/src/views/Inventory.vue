<template>
  <div>
    <!--nav bar-->
    <navbar/>

    <!--creation popup-->
    <inventory-item-creation @updateInventoryItem="afterCreation"/>

    <!--inventory container-->
    <div class="container p-5 mt-3" id="profileContainer">
      <div class="row">

        <div class="col-xl-2 mb-2">
          <div class="card text-center shadow-sm">
            <div class="card-body">

              <!--business's profile image-->
              <img class="rounded-circle img-fluid" :src="require('../../public/sample_profile_image.jpg')"
                   alt="Profile Image"/>

              <!--business's name-->
              <div class="mt-3">
                <h5>{{ businessName }}</h5>
                <div class="text-secondary">{{ businessDescription }}</div>
              </div>

            </div>
            <!--            <div class="card text-center shadow-sm-3">-->
            <!--              <div class="card-body">-->
            <!--                <button class="btn btn-lg text-secondary" id="editProfileButton">Edit Profile</button>-->
            <!--              </div>-->
            <!--            </div>-->
          </div>
        </div>

        <div class="col">
          <div class="card card-body">
            <h1 align="center">Inventory</h1>

            <hr/>

            <div class="row" role="group" aria-label="Button group with nested dropdown">
              <div class="col-md-3 py-1">
                <!--creation button-->
                <button type="button" class="btn green-button w-100" data-bs-toggle="modal"
                        data-bs-target="#creationPopup">
                  Create New
                </button>
              </div>
              <!--search bar-->
              <div class="input-group col-md py-1">
                <input type="text" class="form-control" placeholder="This is for later use."
                       aria-label="Input group example" aria-describedby="btnGroupAddon">
                <button type="button" class="btn green-button-transparent">Search</button>
              </div>

              <!--filter-->
              <div class="btn-group col-md-3 py-1" role="group">
                <button type="button" class="btn green-button dropdown-toggle"
                        data-bs-toggle="dropdown" aria-expanded="false">Filter Option
                </button>

                <ul class="dropdown-menu gap-2" aria-labelledby="btnGroupDrop1">
                  <!--order by product id-->
                  <button type="button" class="btn green-button-transparent col-12"
                          @click="orderInventory(true, false, false, false, false, false, false, false)">
                    Product ID
                    <i id="productIdIcon"></i>
                  </button>

                  <!--order by quantity-->
                  <button type="button" class="btn green-button-transparent col-12"
                          @click="orderInventory(false, true, false, false, false, false, false, false)">
                    Quantity
                    <i id="quantityIcon"></i>
                  </button>

                  <!--order by price per item-->
                  <button type="button" class="btn green-button-transparent col-12"
                          @click="orderInventory(false, false, true, false, false, false, false, false)">
                    Price Per Item ({{ currencySymbol }} {{ currencyCode }})
                    <i id="pricePerItemIcon"></i>
                  </button>

                  <!--order by total price-->
                  <button type="button" class="btn green-button-transparent col-12"
                          @click="orderInventory(false, false, false, true, false, false, false, false)">
                    Total Price ({{ currencySymbol }} {{ currencyCode }})
                    <i id="totalPriceIcon"></i>
                  </button>

                  <!--order by manufactured-->
                  <button type="button" class="btn green-button-transparent col-12"
                          @click="orderInventory(false, false, false, false, true, false, false, false)">
                    Manufactured
                    <i id="manufacturedIcon"></i>
                  </button>

                  <!--order by sell by-->
                  <button type="button" class="btn green-button-transparent col-12"
                          @click="orderInventory(false, false, false, false, false, true, false, false)">
                    Sell By
                    <i id="sellByIcon"></i>
                  </button>

                  <!--order by best before-->
                  <button type="button" class="btn green-button-transparent col-12"
                          @click="orderInventory(false, false, false, false, false, false, true, false)">
                    Best Before
                    <i id="bestBeforeIcon"></i>
                  </button>

                  <!--order by expires-->
                  <button type="button" class="btn green-button-transparent col-12"
                          @click="orderInventory(false, false, false, false, false, false, false, true)">
                    Expires
                    <i id="expiresIcon"></i>
                  </button>
                </ul>
              </div>

            </div>

            <!--space-->
            <br>

            <!--creation success info-->
            <div class="alert alert-success" role="alert" v-if="creationSuccess">
              <div class="row">
                <div class="col">New Inventory Item Create successfully!</div>
                <div class="col" align="right">
                  <button type="button" class="btn green-button px-1 py-0" @click="closeMessage">X</button>
                </div>
              </div>
            </div>

            <!--inventory items-->
            <inventory-item
                v-for="inventory in inventories"
                :id="'InventoryItemCard' + inventory.index"
                v-bind:key="inventory.index"
                v-bind:image="inventory.image"
                v-bind:product-name="inventory.productName"
                v-bind:product-id="inventory.productId"
                v-bind:quantity="inventory.quantity"
                v-bind:price-per-item="inventory.pricePerItem"
                v-bind:total-price="inventory.totalPrice"
                v-bind:manufactured="inventory.manufactured"
                v-bind:sell-by="inventory.sellBy"
                v-bind:best-before="inventory.bestBefore"
                v-bind:expires="inventory.expires"
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

      </div>
    </div>

    <!--footer-->
    <Footer></Footer>

  </div>
</template>


<script>
import Footer from "@/components/Footer";
import InventoryItem from "@/components/InventoryItem";
import Navbar from "@/components/Navbar";
import InventoryItemCreation from "@/components/CreateInventoryItemModal";
import Api from "@/Api";
import Cookies from "js-cookie";
import CurrencyAPI from "@/currencyInstance";

export default {
  components: {
    InventoryItemCreation,
    Navbar,
    InventoryItem,
    Footer
  },
  data() {
    return {
      // Table variables
      // A list of the ordering by headers, which is used with talking to the backend
      tableOrderByHeaders: ["productId", "name", "manufacturer", "recommendedRetailPrice", "created"],

      // Stores the URL string that is used by the requestProducts() to order the products
      orderByString: "",
      // A list of Product object that store the products
      InventoryItemList: [],
      // These variables are used to control and update the table.
      rowsPerPage: 5,
      currentPage: 0,
      totalPages: 0,
      totalRows: 0,

      productIdAscending: false,
      quantityAscending: false,
      pricePerItemAscending: false,
      totalPriceAscending: false,
      manufacturedAscending: false,
      sellByAscending: false,
      bestBeforeAscending: false,
      expiresAscending: false,

      businessId: null,
      creationSuccess: false,

      businessName: null,
      businessDescription: null,

      inventories: [],
      image: require("../../public/apples.jpg"),
      productName: "Watties Baked Beans - 420g can",
      productId: "WATT-420-BEANS",
      quantity: 4,
      pricePerItem: 6.5,
      totalPrice: 21.99,
      manufactured: "2021-04-23",
      sellBy: "2021-04-23",
      bestBefore: "2021-04-23",
      expires: "2021-04-23",

      // Currency related variables
      currencyCode: "",
      currencySymbol: "",
    }
  },
  methods: {
    /**
     * close creation message
     */
    closeMessage() {
      this.creationSuccess = false;
    },

    /**
     * Updates the display to show the new page when a user clicks to move to a different page.
     *
     * @param event The click event
     * @param newPageNumber The new page number
     */
    updatePage(event, newPageNumber) {
      this.currentPage = newPageNumber;
      this.$router.push({path: `/businessProfile/${this.businessId}/inventory`, query: {"orderBy": this.orderByString, "page": (this.currentPage + 1).toString()}})
      this.retrieveInventoryItems();
    },

    /**
     * Given a page number check that the page is within the acceptable range.
     * NOTE this is a 0 origin.
     * @param pageNumber The page number to be checked.
     */
    isValidPageNumber(pageNumber) {
      return 0 <= pageNumber && pageNumber < this.totalPages;
    },

    retrieveBusinessInfo() {
      Api.getBusiness(this.businessId).then(response => {
        this.businessName = response.data.name;
        this.businessDescription = response.data.description;
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

    /**
     * Orders the inventory based on the given booleans for each column, and updates the display
     * @param id Boolean, whether to order by productId
     * @param quantity Boolean, whether to order by quantity
     * @param pricePerItem Boolean, whether to order by price per item
     * @param totalPrice Boolean, whether to order by total price
     * @param manufactured Boolean, whether to order by manufacture date
     * @param sellBy Boolean, whether to order by sell by date
     * @param bestBefore Boolean, whether to order by best before date
     * @param expires Boolean, whether to order by expiration date
     */
    orderInventory(id, quantity, pricePerItem, totalPrice, manufactured, sellBy, bestBefore, expires) {

      if (id) {
        this.disableIcons();
        if (this.productIdAscending) {
          this.orderByString = "productIdASC"
          document.getElementById('productIdIcon').setAttribute('class', 'fas fa-chevron-up float-end');
        } else {
          this.orderByString = "productIdDESC"
          document.getElementById('productIdIcon').setAttribute('class', 'fas fa-chevron-down float-end');
        }

        this.productIdAscending = !this.productIdAscending;
        this.quantityAscending = false;
        this.pricePerItemAscending = false;
        this.totalPriceAscending = false;
        this.manufacturedAscending = false;
        this.sellByAscending = false;
        this.bestBeforeAscending = false;
        this.expiresAscending = false;

      } else if (quantity) {
        this.disableIcons();
        if (this.quantityAscending) {
          this.orderByString = "quantityASC"
          document.getElementById('quantityIcon').setAttribute('class', 'fas fa-chevron-up float-end');
        } else {
          this.orderByString = "quantityDESC"
          document.getElementById('quantityIcon').setAttribute('class', 'fas fa-chevron-down float-end');
        }

        this.productIdAscending = false;
        this.quantityAscending = !this.quantityAscending;
        this.pricePerItemAscending = false;
        this.totalPriceAscending = false;
        this.manufacturedAscending = false;
        this.sellByAscending = false;
        this.bestBeforeAscending = false;
        this.expiresAscending = false;

      } else if (pricePerItem) {
        this.disableIcons();
        if (this.pricePerItemAscending) {
          this.orderByString = "pricePerItemASC"
          document.getElementById('pricePerItemIcon').setAttribute('class', 'fas fa-chevron-up float-end');
        } else {
          this.orderByString = "pricePerItemDESC"
          document.getElementById('pricePerItemIcon').setAttribute('class', 'fas fa-chevron-down float-end');
        }

        this.productIdAscending = false;
        this.quantityAscending = false;
        this.pricePerItemAscending = !this.pricePerItemAscending;
        this.totalPriceAscending = false;
        this.manufacturedAscending = false;
        this.sellByAscending = false;
        this.bestBeforeAscending = false;
        this.expiresAscending = false;

      } else if (totalPrice) {
        this.disableIcons();
        if (this.totalPriceAscending) {
          this.orderByString = "totalPriceASC"
          document.getElementById('totalPriceIcon').setAttribute('class', 'fas fa-chevron-up float-end');
        } else {
          this.orderByString = "totalPriceDESC"
          document.getElementById('totalPriceIcon').setAttribute('class', 'fas fa-chevron-down float-end');
        }

        this.productIdAscending = false;
        this.quantityAscending = false;
        this.pricePerItemAscending = false;
        this.totalPriceAscending = !this.totalPriceAscending;
        this.manufacturedAscending = false;
        this.sellByAscending = false;
        this.bestBeforeAscending = false;
        this.expiresAscending = false;

      } else if (manufactured) {
        this.disableIcons();
        if (this.manufacturedAscending) {
          this.orderByString = "manufacturedASC";
          document.getElementById('manufacturedIcon').setAttribute('class', 'fas fa-chevron-up float-end');
        } else {
          this.orderByString = "manufacturedDESC";
          document.getElementById('manufacturedIcon').setAttribute('class', 'fas fa-chevron-down float-end');
        }
        this.productIdAscending = false;
        this.quantityAscending = false;
        this.pricePerItemAscending = false;
        this.totalPriceAscending = false;
        this.manufacturedAscending = !this.manufacturedAscending;
        this.sellByAscending = false;
        this.bestBeforeAscending = false;
        this.expiresAscending = false;

      } else if (sellBy) {
        this.disableIcons();
        if (this.sellByAscending) {
          this.orderByString = "sellByASC";
          document.getElementById('sellByIcon').setAttribute('class', 'fas fa-chevron-up float-end');
        } else {
          this.orderByString = "sellByDESC";
          document.getElementById('sellByIcon').setAttribute('class', 'fas fa-chevron-down float-end');
        }
        this.productIdAscending = false;
        this.quantityAscending = false;
        this.pricePerItemAscending = false;
        this.totalPriceAscending = false;
        this.manufacturedAscending = false;
        this.sellByAscending = !this.sellByAscending;
        this.bestBeforeAscending = false;
        this.expiresAscending = false;

      } else if (bestBefore) {
        this.disableIcons();
        if (this.bestBeforeAscending) {
          this.orderByString = "bestBeforeASC";
          document.getElementById('bestBeforeIcon').setAttribute('class', 'fas fa-chevron-up float-end');
        } else {
          this.orderByString = "bestBeforeDESC";
          document.getElementById('bestBeforeIcon').setAttribute('class', 'fas fa-chevron-down float-end');
        }
        this.productIdAscending = false;
        this.quantityAscending = false;
        this.pricePerItemAscending = false;
        this.totalPriceAscending = false;
        this.manufacturedAscending = false;
        this.sellByAscending = false;
        this.bestBeforeAscending = !this.bestBeforeAscending;
        this.expiresAscending = false;

      } else if (expires) {
        this.disableIcons();
        if (this.expiresAscending) {
          this.orderByString = "expiresASC";
          document.getElementById('expiresIcon').setAttribute('class', 'fas fa-chevron-up float-end');
        } else {
          this.orderByString = "expiresDESC";
          document.getElementById('expiresIcon').setAttribute('class', 'fas fa-chevron-down float-end');
        }
        this.productIdAscending = false;
        this.quantityAscending = false;
        this.pricePerItemAscending = false;
        this.totalPriceAscending = false;
        this.manufacturedAscending = false;
        this.sellByAscending = false;
        this.bestBeforeAscending = false;
        this.expiresAscending = !this.expiresAscending;

      }

      this.$router.push({path: `/businessProfile/${this.businessId}/inventory`, query: {"orderBy": this.orderByString, "page": (this.currentPage + 1).toString()}});
      this.retrieveInventoryItems();
    },

    /**
     * Disables all ascending or descending icons in the filter buttons.
     */
    disableIcons() {
      document.getElementById('productIdIcon').setAttribute('class', '');
      document.getElementById('quantityIcon').setAttribute('class', '');
      document.getElementById('pricePerItemIcon').setAttribute('class', '');
      document.getElementById('totalPriceIcon').setAttribute('class', '');
      document.getElementById('manufacturedIcon').setAttribute('class', '');
      document.getElementById('sellByIcon').setAttribute('class', '');
      document.getElementById('bestBeforeIcon').setAttribute('class', '');
      document.getElementById('expiresIcon').setAttribute('class', '');
    },

    /**
     * Requests a list of inventory item matching the given business ID from the back-end.
     * If successful it updates the Table to contain the new data points.
     * If successful it sets the productList variable to the response data.
     *
     * @return {Promise}
     */
    async retrieveInventoryItems() {

      // Getting query params from the route update.
      this.orderByString = this.$route.query["orderBy"] || "productIdASC";
      this.currentPage = parseInt(this.$route.query["page"]) - 1 || 0;

      // Perform the call to sort the products and get them back.
      await Api.sortInventoryItems(this.businessId, this.orderByString, this.currentPage).then(response => {

        this.InventoryItemList = [...response.data];

        // No results
        if (this.InventoryItemList.length <= 0) {
          this.currentPage = 0;
          this.maxPage = 0;
          this.totalRows = 0;
          this.totalPages = 0;
          // Generate the tableData to be placed in the table & get the total number of rows.
        } else {
          this.totalRows = parseInt(response.headers["total-rows"]);
          this.totalPages = parseInt(response.headers["total-pages"]);

          this.inventories = [];

          for (let i = 0; i < this.rowsPerPage; i++) {
            if (i === this.InventoryItemList.length){
              return
            }
            this.inventories.push({
              index: i,
              productName: this.InventoryItemList[i].product.name,
              productId: this.InventoryItemList[i].product.id,
              quantity: this.InventoryItemList[i].quantity,
              pricePerItem: this.InventoryItemList[i].pricePerItem,
              totalPrice: this.InventoryItemList[i].totalPrice,
              manufactured: this.InventoryItemList[i].manufactured,
              sellBy: this.InventoryItemList[i].sellBy,
              bestBefore: this.InventoryItemList[i].bestBefore,
              expires: this.InventoryItemList[i].expires
            })
          }
        }

      }).catch((error) => {
        console.log(error);
        if (error.request && !error.response) {
          this.$router.push({path: '/timeout'});
        } else if (error.response.status === 400) {
          this.$router.push({path: '/pageDoesNotExist'});
        } else if (error.response.status === 401) {
          this.$router.push({path: '/invalidtoken'});
        } else if (error.response.status === 403) {
          this.$router.push({path: '/forbidden'});
        } else if (error.response.status === 406) {
          this.$router.push({path: '/noBusiness'});
        } else {
          this.$router.push({path: '/timeout'});
          console.log(error.message);
        }
      })
    },
    /**
     * after creation success, show the success info and use endpoint to collect data from backend.
     */
    afterCreation() {
      this.creationSuccess = true;
      this.retrieveInventoryItems();
    },
    /**
     * Currency API requests.
     * An asynchronous function that calls the REST Countries API with the given country input.
     * Upon success, the filterResponse function is called with the response data.
     */
    async currencyRequest() {
      this.businessId = parseInt(this.$route.params.id);

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
    filterResponse(response) {
      this.currencyCode = response[0].currencies[0].code;
      this.currencySymbol = response[0].currencies[0].symbol;
    },
  },

  async mounted() {

    /**
     * When mounted, initiate population of page.
     * If cookies are invalid or not present, redirect to login page.
     */
    const currentID = Cookies.get('userID');
    if (currentID) {
      this.businessId = this.$route.params.id;

      await this.currencyRequest();

      this.retrieveBusinessInfo();
      this.retrieveInventoryItems().then(
          () => {}
      ).catch(
          (e) => console.log(e)
      );
    } else {
      this.$router.push({name: 'Login'});
    }

  }
}
</script>


<style scoped>

</style>
