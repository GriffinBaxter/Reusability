<template>
  <div>
    <div v-if="creationSuccess">
      <feedback-notification :messages="messages" style="z-index:999;"/>
    </div>

    <div id="main">

      <!--nav bar-->
      <navbar @getLinkBusinessAccount="setLinkBusinessAccount" :sendData="linkBusinessAccount"/>

    <!--creation popup-->
    <inventory-item-creation @updateInventoryItem="afterCreation"
                             v-bind:currency-code="currencyCode"
                             v-bind:currency-symbol="currencySymbol"/>

    <!--inventory container-->
    <div class="container p-4 mt-3" id="profileContainer">
      <div class="row">

        <div class="col">
          <div class="card card-body">
            <h1 style="text-align: center">{{ businessName }}'s Inventory</h1>

            <hr/>

            <div class="row" role="group" aria-label="Button group with nested dropdown" style="display: flex; align-items: flex-end">
              <!--filter-->
              <div class="btn-group col-md-2 py-1 align-self-end" role="group">
                <button type="button" class="btn green-button dropdown-toggle" style="height: 38px"
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

              <div class="col-md-2 py-1 align-self-end">
                <!--creation button-->
                <button type="button" class="btn green-button w-100" data-bs-toggle="modal"
                        data-bs-target="#creationPopup" style="height: 38px">
                  Create New
                </button>
              </div>

              <div class="col-3 col-md-3 text-secondary flex-nowrap align-self-end" style="margin-bottom: 0.7em">Filter By: {{convertToString()}}</div>

              <div class="col-md-3 justify-content-md-end" style="display: flex; ">
                <BarcodeSearchBar @barcodeSearch="barcodeSearch" search-bar-identifier="inventory-business"/>
              </div>

              <div class="col justify-content-md-end" style="display: flex;">
                <PageSize :current-page-size="pageSize" :page-sizes="pageSizes" v-on:selectedPageSize="updatePageSize"></PageSize>
              </div>

            </div>

            <!--space-->
            <br>

            <UpdateInventoryItemModal ref="updateInventoryItemModal"
                                      :business-id=businessId
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
                v-bind:barcode="inventory.barcode"
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

            <div class="noInventory" v-if="noInventory">
              <div class="card p-1">
                <p class="h2 py-5" style="text-align: center">No Inventory Items Found</p>
              </div>
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
import Navbar from "../components/Navbar";
import InventoryItemCreation from "../components/inventory/CreateInventoryItemModal";
import Api from "../Api";
import Cookies from "js-cookie";
import UpdateInventoryItemModal from "../components/inventory/UpdateInventoryItemModal";
import PageButtons from "../components/PageButtons";
import {formatDate} from "../dateUtils";
import {checkAccessPermission} from "../views/helpFunction";
import PageSize from "../components/PageSize";
import BarcodeSearchBar from "../components/BarcodeSearchBar";
import FeedbackNotification from "../components/feedbackNotification/FeedbackNotification";

export default {
  components: {
    UpdateInventoryItemModal,
    InventoryItemCreation,
    Navbar,
    InventoryItem,
    Footer,
    PageSize,
    PageButtons,
    BarcodeSearchBar,
    FeedbackNotification
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

      inventories: [],
      currentInventoryItem: null,

      // currency related variables
      businessCountry: "", // used to retrieve the currency code and symbol
      currencyCode: "",
      currencySymbol: "",

      barcode: "",

      // List of Business account current user account administrated
      linkBusinessAccount:[],

      // When page is initially loaded, we don't want 'No Inventory Items Found' message to display since, inventory has not
      // been retrieved yet.
      notInitialLoad: false,

      // For toast notifications
      messages: [],
      messageIdCounter: 0,

      pageSizes: ["5", "10", "15", "25"], // a list of available page sizes
      pageSize: this.$route.query["pageSize"] || "5" // default pages size
    }
  },
  computed: {
    noInventory() {
      return (this.inventories.length < 1) && this.notInitialLoad;
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
        query: {"barcode": this.barcode, "orderBy": this.orderByString, "page": (this.currentPage + 1).toString(), "pageSize": this.pageSize}
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

    async retrieveBusinessInfo() {
      await Api.getBusiness(this.businessId).then(response => {
        this.businessName = response.data.name;
        this.businessDescription = response.data.description;
        this.businessCountry = response.data.address.country;
        this.currencySymbol = response.data.currencySymbol;
        this.currencyCode = response.data.currencyCode;
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
        query: {"barcode": this.barcode, "orderBy": this.orderByString, "page": (this.currentPage + 1).toString(), "pageSize": this.pageSize}
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
      this.pageSize = this.$route.query["pageSize"] || "5";
      this.rowsPerPage = parseInt(this.pageSize);
      this.barcode = this.$route.query["barcode"] || "";

      if (this.barcode === undefined || null) {
        this.barcode = "";
      }

      // Perform the call to sort the products and get them back.
      await Api.sortInventoryItems(this.businessId, this.orderByString, this.currentPage, this.pageSize, this.barcode).then(response => {
        this.totalRows = parseInt(response.headers["total-rows"]);
        this.totalPages = parseInt(response.headers["total-pages"]);

        if (this.totalPages > 0 && this.currentPage > this.totalPages - 1) {
          this.$router.push({path: '/pageDoesNotExist'});
        }

        this.InventoryItemList = [...response.data];

        // No results
        if (this.InventoryItemList.length <= 0) {
          this.inventories = [];
          this.currentPage = 0;
          this.totalRows = 0;
          this.totalPages = 0;
          // Generate the tableData to be placed in the table & get the total number of rows.
        } else {

          this.inventories = [];

          for (let i = 0; i < this.rowsPerPage; i++) {
            if (i === this.InventoryItemList.length) {
              return
            }
            // to use default prop
            let barcode = this.InventoryItemList[i].product.barcode;
            if (!(barcode)) { barcode = undefined; }

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
              images: this.InventoryItemList[i].product.images,
              barcode: barcode
            })
          }
        }
        this.notInitialLoad = true; // inventories has been retrieved
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
      this.messageIdCounter += 1;
      this.messages = [];
      this.messages.push(
          {
            id: this.messageIdCounter,
            isError: false,
            topic: "Success",
            text: "Inventory item successfully created."
          }
      )
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
      this.messageIdCounter += 1;
      this.messages = [];
      this.messages.push(
          {
            id: this.messageIdCounter,
            isError: false,
            topic: "Success",
            text: "Inventory item successfully edited."
          }
      )
      // The corresponding alert will close automatically after 5000ms.
      setTimeout(() => {
        this.creationSuccess = false
      }, 5000);
      this.retrieveInventoryItems();
    },

    /**
     * Routes to URL with event value as the barcode and triggers retrieveInventoryItems
     */
    barcodeSearch(event) {
      this.$router.push({
        path: `/businessProfile/${this.businessId}/inventory`,
        query: {"barcode": event, "orderBy": this.orderByString, "page": (this.currentPage + 1).toString(), "pageSize": this.pageSize}
      });
      this.retrieveInventoryItems();
    },

    /**
     * When a user selects a page size using the PageSize component then the current page size should be
     * updated and the results should be retrieved from the backend.
     * @param selectedPageSize the newly selected page size.
     */
    updatePageSize(selectedPageSize) {
      this.pageSize = selectedPageSize;
      this.currentPage = 0;
      this.$router.push({
        path: `/businessProfile/${this.businessId}/inventory`,
        query: {"barcode": this.barcode, "orderBy": this.orderByString, "page": (this.currentPage + 1).toString(), "pageSize": this.pageSize}
      })
      this.retrieveInventoryItems();
    }
  },

  async mounted() {
    const actAs = Cookies.get('actAs');
    if (checkAccessPermission(this.$route.params.id, actAs)) {
      await this.$router.push({path: `/businessProfile/${actAs}/inventory`});
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

        this.businessId = parseInt(this.$route.params.id);

        await this.retrieveBusinessInfo();
        this.retrieveInventoryItems().catch(
            (e) => console.log(e)
        );
      }
    }
  }
}
</script>
