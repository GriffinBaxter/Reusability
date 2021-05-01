<template>
  <div>
    <!--nav bar-->
    <navbar/>

    <!--creation popup-->
    <inventory-item-creation/>

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

            <div class="btn-group" role="group" aria-label="Button group with nested dropdown">
              <div class="btn-group col-2 py-1">
                <!--creation button-->
                <button type="button" class="btn btn-success col-2 py-1" data-bs-toggle="modal"
                        data-bs-target="#creationPopup">
                  Creat New
                </button>
              </div>
              <!--search bar-->
              <div class="input-group py-1 px-1">
                <input type="text" class="form-control" placeholder="This is for later use."
                       aria-label="Input group example" aria-describedby="btnGroupAddon">
                <button type="button" class="btn btn-outline-primary">Search</button>
              </div>

              <!--filter-->
              <div class="btn-group col-3 py-1" role="group">
                <button type="button" class="btn btn-primary dropdown-toggle col-4"
                        data-bs-toggle="dropdown" aria-expanded="false">Filter
                </button>

                <ul class="dropdown-menu gap-2" aria-labelledby="btnGroupDrop1">
                  <!--order by product name-->
                  <button type="button" class="btn btn-outline-primary col-12">Product Name</button>

                  <!--order by product id-->
                  <button type="button" class="btn btn-outline-primary col-12">Product ID</button>

                  <!--order by quantity-->
                  <button type="button" class="btn btn-outline-primary col-12">Quantity</button>

                  <!--order by price per item-->
                  <button type="button" class="btn btn-outline-primary col-12">Price Per Item</button>

                  <!--order by total price-->
                  <button type="button" class="btn btn-outline-primary col-12">Total Price</button>

                  <!--order by manufactured-->
                  <button type="button" class="btn btn-outline-primary col-12">Manufactured</button>

                  <!--order by sell by-->
                  <button type="button" class="btn btn-outline-primary col-12">Sell By</button>

                  <!--order by best before-->
                  <button type="button" class="btn btn-outline-primary col-12">Best Before</button>

                  <!--order by expires-->
                  <button type="button" class="btn btn-outline-primary col-12">Expires</button>
                </ul>
              </div>

            </div>

            <!--inventory items-->
            <inventory-item
                v-for="inventory in inventories"
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
                v-bind:expires="inventory.expires"/>

            <!--pagination-->
            <nav>
              <ul class="pagination justify-content-center">
                <li class="page-item">
                  <a class="page-link" href="#" aria-label="Previous">
                    <span aria-hidden="true">&laquo;</span>
                  </a>
                </li>
                <li class="page-item"><a class="page-link" href="#">1</a></li>
                <li class="page-item"><a class="page-link" href="#">2</a></li>
                <li class="page-item"><a class="page-link" href="#">3</a></li>
                <li class="page-item">
                  <a class="page-link" href="#" aria-label="Next">
                    <span aria-hidden="true">&raquo;</span>
                  </a>
                </li>
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
import InventoryItemCreation from "@/components/CreateNewInventoryItem";
import Api from "@/Api";

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
      // Used to tell the table what is the current ordering (for visual purposes).
      tableOrderBy: {orderBy: null, isAscending: true},
      // Stores the URL string that is used by the requestProducts() to order the products
      orderByString: "",
      // A list of Product object that store the products
      InventoryItemList: [],
      // These variables are used to control and update the table.
      rowsPerPage: 5,
      currentPage: 0,
      totalRows: 0,


      businessId: null,

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
      expires: "2021-04-23"
    }
  },
  methods: {
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
     * Parses the orderByString and returns the resulted Objects.
     * @return {{orderBy: null | String, isAscending: boolean}} This contains the {orderBy, isAscending} properties of the this.orderByString .
     * Emulates a click when the product presses enter on a column header.
     *
     * @param event The keydown event
     */
    parseOrderBy() {
      let orderBy = null;
      let isAscending = true;

      // If the last 3 letters are ASC then we can assume the orderBy is the other component of that orderByString.
      // This also means isAscending is true.
      if (this.orderByString.slice(this.orderByString.length - 3) === 'ASC') {
        orderBy = this.orderByString.slice(0, this.orderByString.length - 3);

        // If the last 4 letters are DESC then we can assume the orderBy is the other component of the orderByString
        // This also means that isAscending is false.
      } else if (this.orderByString.slice(this.orderByString.length - 4) === 'DESC') {
        orderBy = this.orderByString.slice(0, this.orderByString.length - 4)
        isAscending = false;
      }

      // If we found a valid orderBy compare it against he allowed orderBy headers in tableOrderByHeaders
      if (orderBy !== null) {
        orderBy = this.tableOrderByHeaders.indexOf(orderBy);

        // If the orderBy is returned as -1. This means that no header was found!
        // So we say it is unordered.
        if (orderBy === -1) {
          orderBy = null;
        }
      }

      return {orderBy, isAscending};
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
      this.currentPage = parseInt(this.$route.query["page"]) || 0;

      // Perform the call to sort the products and get them back.
      await Api.sortInventoryItems(this.businessId, this.orderByString, this.currentPage).then(response => {


        // Parsing the orderBy string to get the orderBy and isAscending components to update the table.
        const {orderBy, isAscending} = this.parseOrderBy();
        this.tableOrderBy = {orderBy: orderBy, isAscending: isAscending};

        this.InventoryItemList = [...response.data];

        let newTableData = [];

        // No results
        if (this.InventoryItemList.length <= 0) {
          this.currentPage = 1;
          this.maxPage = 1;
          this.totalRows = 0;
          // Generate the tableData to be placed in the table & get the total number of rows.
        } else {
          this.totalRows = parseInt(response.headers["total-rows"]);

          for (let i = 0; i < this.InventoryItemList.length; i++) {

            newTableData.push({
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
          this.inventories = newTableData;
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
  },
  mounted() {
    this.businessId = this.$route.params.id;
    this.retrieveBusinessInfo()
    this.retrieveInventoryItems()

    //example

  }
}
</script>


<style scoped>

</style>
