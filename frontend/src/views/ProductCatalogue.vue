<template>
  <div id="outerContainer" class="container">

    <navbar/>

    <div id="body" class="container all-but-footer">

      <div class="row mt-3">
        <h2 align="center">Product Catalogue</h2>
      </div>

      <div class="row mb-3">
        <div class="col">
            <button class="btn btn-outline-primary float-end" tabindex="2" id="createProductButton">Create Product</button>
        </div>
      </div>

      <div id="productTable">
        <div class="row mb-3">
          <div class="col py-2 header-col col-hover rounded-3 me-2 text-center" tabindex="3" @keydown="orderEnter($event)"
               @click="orderProducts(true, false , false, false, false)">
            <b>ProductID</b>
            <i id="productIdIcon"></i>
          </div>
          <div class="col py-2 header-col col-hover rounded-3 me-2 text-center" tabindex="4" @keydown="orderEnter($event)"
               @click="orderProducts(false, true , false, false, false)">
            <b>Name</b>
            <i id="nameIcon"></i>
          </div>
          <div class="col py-2 header-col col-hover rounded-3 me-2 text-center" tabindex="5" @keydown="orderEnter($event)"
               @click="orderProducts(false, false , true, false, false)">
            <b>Manufacturer</b>
            <i id="manufacturerIcon"></i>
          </div>
          <div class="col py-2 header-col col-hover rounded-3 me-2 text-center" tabindex="6" @keydown="orderEnter($event)"
               @click="orderProducts(false, false , false, true, false)">
            <b>Recommended Retail Price</b>
            <i id="recommendedRetailPriceIcon"></i>
          </div>
          <div class="col py-2 header-col col-hover rounded-3 me-2 text-center" tabindex="7" @keydown="orderEnter($event)"
               @click="orderProducts(false, false , false, false, true)">
            <b>Created</b>
            <i id="createdIcon"></i>
          </div>
        </div>
      </div>

      <div class="row">
        <div class="col">
          <!-- Avert your eyes for this... -->
          <nav aria-label="product-table-navigation" id="pagination-nav" class="float-end" v-if="maxPage > 1">
            <ul class="pagination" id="pagination-ul">

              <li :class="toggleDisableClass('page-item', currentPage-1 <= 0)">
                <a class="page-link" href="#" @click.prevent="previousPage()">Previous</a>
              </li>

              <li class="page-item" v-if="maxPage > 2 && currentPage >= maxPage">
                <a class="page-link" href="#" @click="updatePage($event, currentPage-2)">{{ currentPage - 2 }}</a>
              </li>

              <li class="page-item" v-if="currentPage-1 > 0">
                <a class="page-link" href="#" @click="updatePage($event, currentPage-1)">{{ currentPage - 1 }}</a>
              </li>

              <li class="page-item active" aria-current="page">
                <a class="page-link" href="#" @click="(e) => e.preventDefault()">{{ currentPage }}</a>
              </li>

              <li class="page-item" v-if="currentPage+1 <= maxPage">
                <a class="page-link" href="#" @click="updatePage($event, currentPage+1)">{{ currentPage + 1 }}</a>
              </li>

              <li class="page-item" v-if="maxPage > 2 && currentPage <= 1">
                <a class="page-link" href="#" @click="updatePage($event, currentPage+2)">{{ currentPage + 2 }}</a>
              </li>

              <li :class="toggleDisableClass('page-item', currentPage+1 > maxPage)" id="next-button">
                <a class="page-link" href="#" @click.prevent="nextPage()">Next</a>
              </li>
            </ul>

          </nav>


        </div>
      </div>
    </div>

    <Footer></Footer>
  </div>
</template>

<script>

import Api from '../Api';
import Cookies from 'js-cookie';
import Navbar from "@/components/Navbar";
import Footer from "@/components/Footer";

export default {
  name: "ProductCatalogue",
  components: {
    Navbar,
    Footer
  },

  data() {
    return {
      productIdAscending: false,
      nameAscending: false,
      manufacturerAscending: false,
      recommendedRetailPriceAscending: false,
      createdAscending: false,

      businessId: 0,
      orderBy: "",
      rowsPerPage: 5,
      currentPage: 1,
      maxPage: 2,
      totalRows: 0,
      productList: [],
      small: false
    }
  },
  methods: {

    /**
     * Toggles the disabling of pagination buttons.
     *
     * @param baseClasses Base classes to add
     * @param condition Given condition for toggling
     * @returns {array} A list classes to apply
     */
    toggleDisableClass(baseClasses, condition) {
      const classList = [baseClasses]
      if (condition) {
        classList.push('disabled')
      }
      return classList
    },

    /**
     * Updates the display to show the new page when a product clicks to move to a different page.
     *
     * @param event The click event
     * @param newPageNum The page to move to
     */
    updatePage(event, newPageNum) {
      event.preventDefault();
      this.currentPage = newPageNum;
      this.$router.push({path: `/businessProfile/${this.businessId}/productCatalogue`, query: {"orderBy": this.orderBy, "page": (this.currentPage).toString()}})
      this.requestProducts();
    },

    /**
     * Emulates a click when the product presses enter on a column header.
     *
     * @param event The keydown event
     */
    orderEnter(event) {
      if (event.keyCode === 13) {
        event.target.click();
      }
    },

    /**
     * Requests a list of products matching the given business ID from the back-end.
     * If successful it sets the productList variable to the response data.
     *
     * @return {Promise}
     */
    async requestProducts() {
      this.businessId = parseInt(this.$route.params.id);

      this.orderBy = this.$route.query["orderBy"] ? this.$route.query["orderBy"] : "productIdASC";
      this.currentPage = parseInt(this.$route.query["page"]) ? parseInt(this.$route.query["page"]) : 1

      await Api.sortProducts(this.businessId, this.orderBy, this.currentPage-1).then(response => {

        this.productList = [...response.data];


        // No results
        if (this.productList.length <= 0) {
          this.currentPage = 1;
          this.maxPage = 1;
          this.totalRows = 0;
        } else {
          this.maxPage = parseInt(response.headers['total-pages']);
          this.totalRows = parseInt(response.headers["total-rows"])
        }

        this.buildRows();

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
     * Goes to the previous page and updates the rows.
     *
     */
    previousPage() {
      if (this.currentPage > 1) {
        this.currentPage -= 1;
        this.$router.push({path: `/businessProfile/${this.businessId}/productCatalogue`, query: {"orderBy": this.orderBy, "page": (this.currentPage).toString()}})
        this.requestProducts()
      }
    },

    /**
     * Goes to the next page and updates the rows.
     *
     */
    nextPage() {
      if (this.currentPage < this.maxPage) {
        this.currentPage += 1;
        this.$router.push({path: `/businessProfile/${this.businessId}/productCatalogue`, query: {"orderBy": this.orderBy, "page": (this.currentPage).toString()}})
        this.requestProducts()
      }
    },

    /**
     * Orders the products based on the given booleans for each column, and updates the display
     * @param id Boolean, whether to order by productId
     * @param name Boolean, whether to order by name
     * @param manufacturer Boolean, whether to order by manufacturer
     * @param recommendedRetailPrice Boolean, whether to order by RRP
     * @param created Boolean, whether to order by created date
     */
    orderProducts(id, name, manufacturer, recommendedRetailPrice, created) {

      if (id) {
        this.disableIcons()
        if (this.productIdAscending) {
          this.orderBy = "nameASC"
          document.getElementById('productIdIcon').setAttribute('class', 'fas fa-chevron-up float-end');
        } else {
          this.orderBy = "nameDESC"
          document.getElementById('productIdIcon').setAttribute('class', 'fas fa-chevron-down float-end');
        }

        this.productIdAscending = !this.productIdAscending;
        this.nameAscending = false;
        this.manufacturerAscending = false;
        this.recommendedRetailPriceAscending = false;
        this.createdAscending = false;

        this.buildRows();
      } else if (name) {
        this.disableIcons()
        if (this.nameAscending) {
          this.orderBy = "nameASC"
          document.getElementById('nameIcon').setAttribute('class', 'fas fa-chevron-up float-end');
        } else {
          this.orderBy = "nameDESC"
          document.getElementById('nameIcon').setAttribute('class', 'fas fa-chevron-down float-end');
        }

        this.productIdAscending = false;
        this.nameAscending = !this.nameAscending;
        this.manufacturerAscending = false;
        this.recommendedRetailPriceAscending = false;
        this.createdAscending = false;

        this.buildRows();
      } else if (manufacturer) {
        this.disableIcons()
        if (this.manufacturerAscending) {
          this.orderBy = "manufacturerASC"
          document.getElementById('manufacturerIcon').setAttribute('class', 'fas fa-chevron-up float-end');
        } else {
          this.orderBy = "manufacturerDESC"
          document.getElementById('manufacturerIcon').setAttribute('class', 'fas fa-chevron-down float-end');
        }

        this.productIdAscending = false;
        this.nameAscending = false;
        this.manufacturerAscending = !this.manufacturerAscending;
        this.recommendedRetailPriceAscending = false;
        this.createdAscending = false;

        this.buildRows();
      } else if (recommendedRetailPrice) {
        this.disableIcons()
        if (this.recommendedRetailPriceAscending) {
          this.orderBy = "recommendedRetailPriceASC"
          document.getElementById('recommendedRetailPriceIcon').setAttribute('class', 'fas fa-chevron-up float-end');
        } else {
          this.orderBy = "recommendedRetailPriceDESC"
          document.getElementById('recommendedRetailPriceIcon').setAttribute('class', 'fas fa-chevron-down float-end');
        }

        this.productIdAscending = false;
        this.nameAscending = false;
        this.manufacturerAscending = false;
        this.recommendedRetailPriceAscending = !this.recommendedRetailPriceAscending;
        this.createdAscending = false;

        this.buildRows();
      } else if (created) {
        this.disableIcons()
        if (this.createdAscending) {
          this.orderBy = "createdASC";
          document.getElementById('createdIcon').setAttribute('class', 'fas fa-chevron-up float-end');
        } else {
          this.orderBy = "createdDESC";
          document.getElementById('createdIcon').setAttribute('class', 'fas fa-chevron-down float-end');
        }
        this.productIdAscending = false;
        this.nameAscending = false;
        this.manufacturerAscending = false;
        this.recommendedRetailPriceAscending = false;
        this.createdAscending = !this.createdAscending;
      }


      this.$router.push({path: `/businessProfile/${this.businessId}/productCatalogue`, query: {"orderBy": this.orderBy, "page": (this.currentPage).toString()}});
      this.requestProducts();

    },

    /**
     * Disables all ascending or descending icons in the top column headers.
     */
    disableIcons() {
      document.getElementById('productIdIcon').setAttribute('class', '');
      document.getElementById('nameIcon').setAttribute('class', '');
      document.getElementById('manufacturerIcon').setAttribute('class', '');
      document.getElementById('recommendedRetailPriceIcon').setAttribute('class', '');
      document.getElementById('createdIcon').setAttribute('class', '');
    },

    /**
     * Dynamically builds the rows of products from the stored productList.
     */
    buildRows() {
      this.clearRows();
      let limit = this.rowsPerPage + (this.currentPage - 1) * this.rowsPerPage;
      const container = document.getElementById('body');
      const lastChild = container.lastChild;

      if (limit > this.productList.length) {
        limit = this.productList.length
      }

      if (this.productList.length > 0) {

        // 7 is the last index of the permanent items
        let tabIndex = 7;


        for (let i = 0; i < limit; i++) {
          // Check breakpoint
          // let width = window.innerWidth;

          let classInput = 'row mb-2 justify-content-center';
          let t = true;
          if (t) {
            classInput = 'col text-center';
          }

          const productRow = document.createElement("div");
          if (i % 2 === 0) {
            productRow.setAttribute("class", "row mb-3 py-4 shadow-sm row-colour productRows");
          } else {
            productRow.setAttribute("class", "row mb-3 py-4 shadow-sm row-colour-dark productRows");
          }
          productRow.setAttribute("tabIndex", `${tabIndex}`);
          productRow.setAttribute("id", `${this.productList[i].id}`);

          const productIdCol = document.createElement("div");
          productIdCol.setAttribute("class", `${classInput}`);
          productIdCol.setAttribute("id", `${i}-productId`);
          productIdCol.innerHTML = this.productList[i].id;
          productRow.appendChild(productIdCol);

          const nameCol = document.createElement("div");
          nameCol.setAttribute("class", `${classInput}`);
          nameCol.setAttribute("id", `${i}-name`);
          nameCol.innerText = this.productList[i].name;
          productRow.appendChild(nameCol);

          productRow.appendChild(nameCol);


          const manufacturerCol = document.createElement("div");
          manufacturerCol.setAttribute("class", `${classInput}`);
          manufacturerCol.setAttribute("id", `${i}-manufacturer`);
          manufacturerCol.innerText = this.productList[i].manufacturer;
          productRow.appendChild(manufacturerCol);

          const recommendedRetailPriceCol = document.createElement("div");
          recommendedRetailPriceCol.setAttribute("class", `${classInput}`);
          recommendedRetailPriceCol.setAttribute("id", `${i}-recommendedRetailPrice`);
          recommendedRetailPriceCol.innerText = this.productList[i].recommendedRetailPrice;
          productRow.appendChild(recommendedRetailPriceCol);

          const createdCol = document.createElement("div");
          createdCol.setAttribute("class", `${classInput}`);
          createdCol.setAttribute("id", `${i}-created`);
          createdCol.innerText = this.productList[i].created;
          productRow.appendChild(createdCol);

          container.insertBefore(productRow, lastChild);

          tabIndex += 1;

        }
      }

      let showingStart = this.productList.length ? (this.currentPage*this.rowsPerPage)-this.rowsPerPage+1 : 0;

      let lastEntryOfPage = limit+(this.currentPage-1)*this.rowsPerPage;

      const showingString = `Showing ${showingStart}-${lastEntryOfPage} of ${this.totalRows} results`;
      const showingRow = document.createElement('div');
      showingRow.setAttribute("class", "row");
      showingRow.setAttribute("id", `showingRow`);
      const showingCol = document.createElement('div');
      showingCol.setAttribute("class", "col");
      showingCol.innerText = showingString;
      showingRow.appendChild(showingCol);

      container.insertBefore(showingRow, lastChild);

    },

    /**
     * Removes all rows of products from the page.
     */
    clearRows() {
      let allRows = document.getElementsByClassName("productRows");
      // Not sure why i-->0 works when i >0; i-- doesn't
      for (let i = allRows.length; i-- > 0;) {
        allRows[i].parentNode.removeChild(allRows[i]);
      }
      if (document.contains(document.getElementById('showingRow'))) {
        document.getElementById('showingRow').remove();
      }
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
          () => this.buildRows()
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

.all-but-footer {
  min-height: calc(100vh - 240px);
}

</style>
