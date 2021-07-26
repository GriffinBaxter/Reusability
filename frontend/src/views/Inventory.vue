<template>
  <div>
    <div id="main">
      <!--nav bar-->
      <navbar @getLinkBusinessAccount="setLinkBusinessAccount" :sendData="linkBusinessAccount"/>
    <!--creation popup-->
    <inventory-item-creation @updateInventoryItem="afterCreation"
                             v-bind:currency-code="currencyCode"
                             v-bind:currency-symbol="currencySymbol"/>

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
          </div>
        </div>

        <div class="col">
          <div class="card card-body">
            <h1 style="text-align: center">Inventory</h1>

            <hr/>

            <div class="row" role="group" aria-label="Button group with nested dropdown">
              <div class="col-md-3 py-1">
                <!--creation button-->
                <button type="button" class="btn green-button w-100" data-bs-toggle="modal"
                        data-bs-target="#creationPopup">
                  Create New
                </button>
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
                    <i id="productIdIcon" aria-hidden="true"></i>
                  </button>

                  <!--order by quantity-->
                  <button type="button" class="btn green-button-transparent col-12"
                          @click="orderInventory(false, true, false, false, false, false, false, false)">
                    Quantity
                    <i id="quantityIcon" aria-hidden="true"></i>
                  </button>

                  <!--order by price per item-->
                  <button type="button" class="btn green-button-transparent col-12"
                          @click="orderInventory(false, false, true, false, false, false, false, false)">
                    Price Per Item ({{ currencySymbol }} {{ currencyCode }})
                    <i id="pricePerItemIcon" aria-hidden="true"></i>
                  </button>

                  <!--order by total price-->
                  <button type="button" class="btn green-button-transparent col-12"
                          @click="orderInventory(false, false, false, true, false, false, false, false)">
                    Total Price ({{ currencySymbol }} {{ currencyCode }})
                    <i id="totalPriceIcon" aria-hidden="true"></i>
                  </button>

                  <!--order by manufactured-->
                  <button type="button" class="btn green-button-transparent col-12"
                          @click="orderInventory(false, false, false, false, true, false, false, false)">
                    Manufactured
                    <i id="manufacturedIcon" aria-hidden="true"></i>
                  </button>

                  <!--order by sell by-->
                  <button type="button" class="btn green-button-transparent col-12"
                          @click="orderInventory(false, false, false, false, false, true, false, false)">
                    Sell By
                    <i id="sellByIcon" aria-hidden="true"></i>
                  </button>

                  <!--order by best before-->
                  <button type="button" class="btn green-button-transparent col-12"
                          @click="orderInventory(false, false, false, false, false, false, true, false)">
                    Best Before
                    <i id="bestBeforeIcon" aria-hidden="true"></i>
                  </button>

                  <!--order by expires-->
                  <button type="button" class="btn green-button-transparent col-12"
                          @click="orderInventory(false, false, false, false, false, false, false, true)">
                    Expires
                    <i id="expiresIcon" aria-hidden="true"></i>
                  </button>
                </ul>
              </div>
              <div class="col-12 col-md-6 text-secondary px-3 flex-nowrap">Filter By: {{convertToString()}}</div>
            </div>

            <!--space-->
            <br>

            <!--creation success info-->
            <div class="alert alert-success" role="alert" v-if="creationSuccess">
              <div class="row">
                <div class="col" style="text-align: center"> {{userAlertMessage}} </div>
              </div>
            </div>

            <UpdateInventoryItemModal ref="updateInventoryItemModal"
                                      :business-id="businessId"
                                      :currency-code="currencyCode"
                                      :currency-symbol="currencySymbol"
                                      v-model="currentInventoryItem"/>

            <!--inventory items-->
            <inventory-item
                v-for="inventory in inventories"
                :id="'InventoryItemCard' + inventory.index"
                v-bind:key="inventory.index"
                v-bind:images="inventory.images"
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
                v-on:click="triggerUpdateInventoryItemModal(inventory)"
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

      </div>
    </div>

    </div>
    <!--footer-->
    <Footer></Footer>

  </div>
</template>


<script>
import Footer from "../components/main/Footer";
import InventoryItem from "../components/inventory/InventoryItem";
import Navbar from "../components/main/Navbar";
import InventoryItemCreation from "../components/inventory/CreateInventoryItemModal";
import Api from "../Api";
import Cookies from "js-cookie";
import UpdateInventoryItemModal from "@/components/inventory/UpdateInventoryItemModal";
import PageButtons from "../components/PageButtons";
import CurrencyAPI from "../currencyInstance";
import {formatDate} from "../dateUtils";
import {checkAccessPermission} from "../views/helpFunction";

export default {
  components: {
    UpdateInventoryItemModal,
    InventoryItemCreation,
    Navbar,
    InventoryItem,
    Footer,
    PageButtons
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

      businessId: 0,
      creationSuccess: false,
      userAlertMessage: "",

      businessName: null,
      businessDescription: null,

      inventories: null,
      currentInventoryItem: null,

      // Currency related variables
      currencyCode: "",
      currencySymbol: "",

      // List of Business account current user account administrated
      linkBusinessAccount:[],
    }
  },
  methods: {
    /**
     * Sets the current inventory item to the one from the card you've clicked on
     * and triggers the showModal method of UpdateInventoryItemModal.
     */
    async triggerUpdateInventoryItemModal(inventory) {
      this.currentInventoryItem = await inventory;
      await this.$forceUpdate();
      this.$refs.updateInventoryItemModal.showModal();
    },

     /**
     * set link business accounts
     */
    setLinkBusinessAccount(data){
      this.linkBusinessAccount = data;
    },
    /**
     * convert orderByString to more readable for user
     */
    convertToString() {
      switch (this.orderByString) {
        case 'productIdASC':
          return "Product ID Ascending";
        case 'productIdDESC':
          return "Product ID Descending";
        case 'quantityASC':
          return "Quantity Ascending";
        case 'quantityDESC':
          return "Quantity Descending";
        case 'pricePerItemASC':
          return "Price Per Item Ascending";
        case 'pricePerItemDESC':
          return "Price Per Item Descending";
        case 'totalPriceASC':
          return "Total Price Ascending";
        case 'totalPriceDESC':
          return "Total Price Descending";
        case 'manufacturedASC':
          return "Manufactured Ascending";
        case 'manufacturedDESC':
          return "Manufactured Descending";
        case 'sellByASC':
          return "Sell By Ascending";
        case 'sellByDESC':
          return "Sell By Descending";
        case 'bestBeforeASC':
          return "Best Before Ascending";
        case 'bestBeforeDESC':
          return "Best Before Descending";
        case 'expiresASC':
          return "Expires Ascending";
        case 'expiresDESC':
          return "Expires Descending";
      }
    },
    /**
     * close creation message
     */
    closeMessage() {
      this.creationSuccess = false;
    },

    /**
     * Updates the display to show the new page when a user clicks to move to a different page.
     *
     * @param newPageNumber The new page number
     */
    updatePage(newPageNumber) {
      this.currentPage = newPageNumber;
      this.$router.push({
        path: `/businessProfile/${this.businessId}/inventory`,
        query: {"orderBy": this.orderByString, "page": (this.currentPage + 1).toString()}
      })
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

      this.$router.push({
        path: `/businessProfile/${this.businessId}/inventory`,
        query: {"orderBy": this.orderByString, "page": (this.currentPage + 1).toString()}
      });
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
        this.totalRows = parseInt(response.headers["total-rows"]);
        this.totalPages = parseInt(response.headers["total-pages"]);

        if (this.totalPages > 0 && this.currentPage > this.totalPages - 1) {
          this.$router.push({path: '/pageDoesNotExist'});
        }

        this.InventoryItemList = [...response.data];

        // No results
        if (this.InventoryItemList.length <= 0) {
          this.currentPage = 0;
          this.maxPage = 0;
          this.totalRows = 0;
          this.totalPages = 0;
          // Generate the tableData to be placed in the table & get the total number of rows.
        } else {

          this.inventories = [];

          for (let i = 0; i < this.rowsPerPage; i++) {
            if (i === this.InventoryItemList.length) {
              return
            }
            this.inventories.push({
              index: i,
              id: this.InventoryItemList[i].id,
              productName: this.InventoryItemList[i].product.name,
              productId: this.InventoryItemList[i].product.id,
              quantity: this.InventoryItemList[i].quantity,
              pricePerItem: this.InventoryItemList[i].pricePerItem,
              totalPrice: this.InventoryItemList[i].totalPrice,
              manufactured: formatDate(this.InventoryItemList[i].manufactured, false),
              manufacturedUnformatted: this.InventoryItemList[i].manufactured,
              sellBy: formatDate(this.InventoryItemList[i].sellBy, false),
              sellByUnformatted: this.InventoryItemList[i].sellBy,
              bestBefore: formatDate(this.InventoryItemList[i].bestBefore, false),
              bestBeforeUnformatted: this.InventoryItemList[i].bestBefore,
              expires: formatDate(this.InventoryItemList[i].expires, false),
              expiresUnformatted: this.InventoryItemList[i].expires,
              images: this.InventoryItemList[i].product.images
            })
          }
        }
      }).catch((error) => {
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
      this.userAlertMessage = "New Inventory Item Created";
      // The corresponding alert will close automatically after 5000ms.
      setTimeout(() => {
        this.creationSuccess = false
      }, 5000);
      this.retrieveInventoryItems();
    },
    /**
     * After edit success, show the edit info.
     */
    afterEdit() {
      this.creationSuccess = true;
      this.userAlertMessage = "Product Edited";
      // The corresponding alert will close automatically after 5000ms.
      setTimeout(() => {
        this.creationSuccess = false
      }, 5000);
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
    const actAs = Cookies.get('actAs');
    if (checkAccessPermission(this.$route.params.id, actAs)) {
      this.$router.push({path: `/businessProfile/${actAs}/inventory`});
    } else {
      /**
       * When mounted, initiate population of page.
       * If cookies are invalid or not present, redirect to login page.
       */
      const currentID = Cookies.get('userID');
      if (currentID) {
        // If the edit is successful the UpdateInventoryItemModal component will emit an 'editedInventory' event.
        // This code notices the emit and will alert the user that the edit was successful by calling the afterEdit function.
        this.$root.$on('editedInventory', this.afterEdit);

        this.businessId = this.$route.params.id;

        this.retrieveBusinessInfo();
        this.retrieveInventoryItems().catch(
            (e) => console.log(e)
        );

        await this.currencyRequest();
      }
    }
  }
}
</script>
