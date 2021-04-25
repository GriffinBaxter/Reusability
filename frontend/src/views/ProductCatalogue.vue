<template>
  <div id="outerContainer" class="container">

    <Navbar ></Navbar>

    <div id="body" class="container all-but-footer">

      <div class="row mt-3">
        <h2 align="center">Product Catalogue</h2>
      </div>

      <div class="row mb-3">
        <div class="col">
            <button class="btn btn-outline-primary float-end" tabindex="2" id="createProductButton">Create Product</button>
        </div>
      </div>

      <Table table-id="product-catalogue-id" null-string-value="N/A" :table-tab-index="0" :table-headers="tableHeaders" :table-data="tableData"
             :max-rows-per-page="rowsPerPage" :total-rows="totalRows" :current-page-override="currentPage" :order-by-override="tableOrderBy" :table-data-is-page="true"
             @update-current-page="event => updatePage(event)" @order-by-header-index="event => orderProducts(event)" @row-selected="event => showDetails(event.index)"></Table>

    </div>

    <div v-if="showModal">
      <transition name="fade">
        <div class="modal-mask">
          <div class="modal-wrapper">
            <div class="modal-dialog modal-">
              <div class="modal-content">
                <div class="modal-body">
                  <product-modal
                      v-bind:product-id="productId"
                      v-bind:product-name="productName"
                      v-bind:description="description"
                      v-bind:manufacturer="manufacturer"
                      v-bind:recommended-retail-price="recommendedRetailPrice"
                      v-bind:created="created" />
                </div>
                <div class="modal-footer">
                  <button class="btn btn-outline-primary float-end" id="closeModalButton" @click="showModal = false">Close</button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </transition>
    </div>

    <Footer></Footer>
  </div>
</template>

<script>

import Api from '../Api';
import Cookies from 'js-cookie';
import Navbar from "@/components/Navbar";
import Footer from "@/components/Footer";
import ProductModal from "@/components/ProductModal";
import Table from "@/components/Table";

export default {
  name: "ProductCatalogue",
  components: {
    Table,
    ProductModal,
    Navbar,
    Footer
  },

  data() {
    return {
      // Table variables
      // A list of the table headers
      tableHeaders: ["ProductID", "name", "Manufacturer", "Recommended Retail Price", "Created"],
      // A list of the ordering by headers, which is used with talking to the backend
      tableOrderByHeaders: ["productId", "name", "recommendedRetailPrice", "manufacturer", "created"],
      // A list of all the data points belonging to the table
      tableData: [],
      // Used to tell the table what is the current ordering (for visual purposes).
      tableOrderBy: {orderBy: null, isAscending: true},
      // Stores the URL string that is used by the requestProducts() to order the products
      orderByString: "",
      // A list of Product object that store the products
      productList: [],
      // These variables are used to control and update the table.
      rowsPerPage: 5,
      currentPage: 0,
      totalRows: 0,


      // Product modal varaibles
      productId: null,
      productName: null,
      description: null,
      manufacturer: null,
      recommendedRetailPrice: 0,
      created: null,
      showModal: false,

      businessId: 0,
    }
  },
  methods: {
    /**
     * Shows a popup modal which contains the information about a given product.
     * @param productIndex Is the index the product to be used.
     */
    showDetails(productIndex) {
      let product = this.productList[productIndex];
      this.productId = product.id;
      this.productName = product.name;
      this.description = product.description;
      this.manufacturer = product.manufacturer;
      this.recommendedRetailPrice = product.recommendedRetailPrice;
      this.created = product.created;
      this.showModal = true;
    },

    /**
     * Updates the page number for the table and the URL to contain the new information.
     * @param event The UPDATE_CURRENT_PAGE event that is triggered by the Table.
     */
    updatePage(event) {
      this.$router.push({path: `/businessProfile/${this.businessId}/productCatalogue`, query: {"orderBy": this.orderByString, "page": (event.newPageNumber).toString()}})
      this.requestProducts();
    },
    /**
     * Parses the orderByString and returns the resulted Objects.
     * @return {{orderBy: null | String, isAscending: boolean}} This contains the {orderBy, isAscending} properties of the this.orderByString .
     */
    parseOrderBy() {
      let orderBy = null;
      let isAscending = true;

      // If the last 3 letters are ASC then we can assume the orderBy is the other component of that orderByString.
      // This also means isAscending is true.
      if (this.orderByString.slice(this.orderByString.length-3) === 'ASC') {
        orderBy = this.orderByString.slice(0, this.orderByString.length-3);

      // If the last 4 letters are DESC then we can assume the orderBy is the other component of the orderByString
      // This also means that isAscending is false.
      } else if (this.orderByString.slice(this.orderByString.length-4) === 'DESC') {
        orderBy = this.orderByString.slice(0, this.orderByString.length-4)
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
     * Requests a list of products matching the given business ID from the back-end.
     * If successful it updates the Table to contain the new data points.
     * @return {Promise}
     */
    async requestProducts() {

      // Getting all the information necssary from the route update (params and query).
      this.businessId = parseInt(this.$route.params.id);
      this.orderByString = this.$route.query["orderBy"] || "productIdASC";
      this.currentPage = parseInt(this.$route.query["page"]) || 0;

      // Perfrom the call to sort the products and get them back.
      await Api.sortProducts(this.businessId, this.orderByString, this.currentPage).then(response => {

        // Parsing the orderby string to get the orderBy and isAscending components to update the table.
        const {orderBy, isAscending} = this.parseOrderBy();
        this.tableOrderBy = {orderBy: orderBy, isAscending: isAscending};

        this.productList = [...response.data];
        let newtableData = [];

        // No results
        if (this.productList.length <= 0) {
          this.currentPage = 0;
          this.totalRows = 0;
        // Generate the tableData to be placed in the table & get the total number of rows.
        } else {
          this.totalRows = parseInt(response.headers["total-rows"]);

          for (let i = 0; i < this.productList.length; i++ ) {
            newtableData.push(this.productList[i].id);
            newtableData.push(this.productList[i].name);
            newtableData.push(this.productList[i].manufacturer);
            newtableData.push(this.productList[i].recommendedRetailPrice);
            newtableData.push(this.productList[i].created);
          }

          this.tableData = newtableData;
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
     * Updates the URL and calls the requestProducts() to update the table.
     * @param event This contains the {orderBy, isAscending} components of the new desired ordering.
     */
    orderProducts(event) {
      this.orderByString = `${this.tableOrderByHeaders[event.orderBy]}${event.isAscending ? 'ASC' : 'DESC'}`
      this.$router.push({path: `/businessProfile/${this.businessId}/productCatalogue`, query: {"orderBy": this.orderByString, "page": (this.currentPage).toString()}});
      this.requestProducts();

    },
  },

  mounted() {

    /**
     * When mounted, initiate population of page.
     * If cookies are invalid or not present, redirect to login page.
     */
    const currentID = Cookies.get('userID');
    if (currentID) {
      this.requestProducts().then(
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

.modal-content {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 99;

  width: 100%;
  max-width: 1000px;
  background-color: #FFFFFF;
  border-radius: 10px;

  padding: 10px;
}

.modal-mask {
  position: fixed;
  z-index: 9998;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, .5);
  display: table;
  transition: opacity .3s ease;
}

.all-but-footer {
  min-height: calc(100vh - 240px);
}

</style>
