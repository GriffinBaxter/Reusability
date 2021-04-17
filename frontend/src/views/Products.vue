<template>
  <div id="outerContainer" class="container">

    <div class="row mb-3">
      <div class="col py-2 header-col col-hover rounded-3 me-2 text-center" tabindex="3" @keydown="orderEnter($event)"
           @click="orderProducts(true, false , false, false, false, false)">
        <b>ProductID</b>
        <i id="productIdIcon"></i>
      </div>
      <div class="col py-2 header-col col-hover rounded-3 me-2 text-center" tabindex="4" @keydown="orderEnter($event)"
           @click="orderProducts(false, true , false, false, false, false)">
        <b>Name</b>
        <i id="nameIcon"></i>
      </div>
      <div class="col py-2 header-col col-hover rounded-3 me-2 text-center" tabindex="5" @keydown="orderEnter($event)"
           @click="orderProducts(false, false , true, false, false, false)">
        <b>Description</b>
        <i id="descriptionIcon"></i>
      </div>
      <div class="col py-2 header-col col-hover rounded-3 text-center" tabindex="6" @keydown="orderEnter($event)"
           @click="orderProducts(false, false , false, true, false, false)">
        <b>Manufacturer</b>
        <i id="manufacturerIcon"></i>
      </div>
      <div class="col py-2 header-col col-hover rounded-3 text-center" tabindex="7" @keydown="orderEnter($event)"
           @click="orderProducts(false, false , false, true, false, false)">
        <b>Recommended Retail Price</b>
        <i id="recommendedRetailPriceIcon"></i>
      </div>
      <div class="col py-2 header-col col-hover rounded-3 text-center" tabindex="8" @keydown="orderEnter($event)"
           @click="orderProducts(false, false , false, true, false, false)">
        <b>Created</b>
        <i id="createdIcon"></i>
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
</template>

<script>

import Api from '../Api';
import Cookies from 'js-cookie';

export default {
  name: "Products",
  data() {
    return {
      productIdAscending: false,
      nameAscending: false,
      descriptionAscending: false,
      manufacturerAscending: false,
      recommendedRetailPriceAscending: false,
      createdAscending: false,
      rowsPerPage: 5,
      currentPage: 1,
      maxPage: 2,
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
      this.buildRows();
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

      await Api.sortProducts(3).then(response => {
        this.productList = [...response.data];
        console.log(this.productList);
        // Order by productId alphabetically by default
        this.productList.sort(function (a, b) {
          if (a.id < b.id) {
            return -1;
          }
          if (a.id > b.id) {
            return 1;
          }
          return 0;
        });
        this.maxPage = Math.ceil(this.productList.length / this.rowsPerPage)
      }).catch((error) => {
        if (error.request && !error.response) {
          this.$router.push({path: '/timeout'});
        } else if (error.response.status === 401) {
          this.$router.push({path: '/invalidtoken'});
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
        this.buildRows()
      }
    },

    /**
     * Goes to the next page and updates the rows.
     *
     */
    nextPage() {
      if (this.currentPage < this.maxPage) {
        this.currentPage += 1;
        this.buildRows()
      }
    },

    /**
     * Orders the products based on the given booleans for each column, and updates the display
     * @param id Boolean, whether to order by productId
     * @param name Boolean, whether to order by name
     * @param description Boolean, whether to order by description
     * @param manufacturer Boolean, whether to order by manufacturer
     * @param recommendedRetailPrice Boolean, whether to order by RRP
     * @param created Boolean, whether to order by created date
     */
    orderProducts(id, name, description, manufacturer, recommendedRetailPrice, created) {

      if (id) {
        this.disableIcons()
        if (this.productIdAscending) {
          this.productList.sort(function (a, b) {
            if (a.id > b.id) {
              return -1;
            }
            if (a.id < b.id) {
              return 1;
            }
            return 0;
          })
          document.getElementById('productIdIcon').setAttribute('class', 'fas fa-chevron-up float-end');
        } else {
          this.productList.sort(function (a, b) {
            if (a.id < b.id) {
              return -1;
            }
            if (a.id > b.id) {
              return 1;
            }
            return 0;
          })
          document.getElementById('productIdIcon').setAttribute('class', 'fas fa-chevron-down float-end');
        }

        this.productIdAscending = !this.productIdAscending;
        this.nameAscending = false;
        this.descriptionAscending = false;
        this.manufacturerAscending = false;
        this.recommendedRetailPriceAscending = false;
        this.createdAscending = false;

        this.buildRows();
      } else if (name) {
        this.disableIcons()
        if (this.nameAscending) {
          this.productList.sort(function (a, b) {
            if (a.name > b.name) {
              return -1;
            }
            if (a.name < b.name) {
              return 1;
            }
            return 0;
          })
          document.getElementById('nameIcon').setAttribute('class', 'fas fa-chevron-up float-end');
        } else {
          this.productList.sort(function (a, b) {
            if (a.name < b.name) {
              return -1;
            }
            if (a.name > b.name) {
              return 1;
            }
            return 0;
          })
          document.getElementById('nameIcon').setAttribute('class', 'fas fa-chevron-down float-end');
        }

        this.productIdAscending = false;
        this.nameAscending = !this.nameAscending;
        this.descriptionAscending = false;
        this.manufacturerAscending = false;
        this.recommendedRetailPriceAscending = false;
        this.createdAscending = false;

        this.buildRows();
      } else if (description) {
        this.disableIcons()
        if (this.descriptionAscending) {
          this.productList.sort(function (a, b) {
            if (a.description > b.description) {
              return -1;
            }
            if (a.description < b.description) {
              return 1;
            }
            return 0;
          })
          document.getElementById('descriptionIcon').setAttribute('class', 'fas fa-chevron-up float-end');
        } else {
          this.productList.sort(function (a, b) {
            if (a.description < b.description) {
              return -1;
            }
            if (a.description > b.description) {
              return 1;
            }
            return 0;
          })
          document.getElementById('descriptionIcon').setAttribute('class', 'fas fa-chevron-down float-end');
        }

        this.productIdAscending = false;
        this.nameAscending = false;
        this.descriptionAscending = !this.descriptionAscending;
        this.manufacturerAscending = false;
        this.recommendedRetailPriceAscending = false;
        this.createdAscending = false;

        this.buildRows();
      } else if (manufacturer) {
        this.disableIcons()
        if (this.manufacturerAscending) {
          this.productList.sort(function (a, b) {
            if (a.manufacturer > b.manufacturer) {
              return -1;
            }
            if (a.manufacturer < b.manufacturer) {
              return 1;
            }
            return 0;
          })
          document.getElementById('manufacturerIcon').setAttribute('class', 'fas fa-chevron-up float-end');
        } else {
          this.productList.sort(function (a, b) {
            if (a.manufacturer < b.manufacturer) {
              return -1;
            }
            if (a.manufacturer > b.manufacturer) {
              return 1;
            }
            return 0;
          })
          document.getElementById('manufacturerIcon').setAttribute('class', 'fas fa-chevron-down float-end');
        }

        this.productIdAscending = false;
        this.nameAscending = false;
        this.descriptionAscending = false;
        this.manufacturerAscending = !this.manufacturerAscending;
        this.recommendedRetailPriceAscending = false;
        this.createdAscending = false;

        this.buildRows();
      } else if (recommendedRetailPrice) {
        this.disableIcons()
        if (this.recommendedRetailPriceAscending) {
          this.productList.sort(function (a, b) {
            if (a.recommendedRetailPrice > b.recommendedRetailPrice) {
              return -1;
            }
            if (a.recommendedRetailPrice < b.recommendedRetailPrice) {
              return 1;
            }
            return 0;
          })
          document.getElementById('recommendedRetailPriceIcon').setAttribute('class', 'fas fa-chevron-up float-end');
        } else {
          this.productList.sort(function (a, b) {
            if (a.recommendedRetailPrice < b.recommendedRetailPrice) {
              return -1;
            }
            if (a.recommendedRetailPrice > b.recommendedRetailPrice) {
              return 1;
            }
            return 0;
          })
          document.getElementById('recommendedRetailPriceIcon').setAttribute('class', 'fas fa-chevron-down float-end');
        }

        this.productIdAscending = false;
        this.nameAscending = false;
        this.descriptionAscending = false;
        this.manufacturerAscending = false;
        this.recommendedRetailPriceAscending = !this.recommendedRetailPriceAscending;
        this.createdAscending = false;

        this.buildRows();
      } else if (created) {
        this.disableIcons()
        if (this.created) {
          this.productList.sort(function (a, b) {
            if (a.created > b.created) {
              return -1;
            }
            if (a.created < b.created) {
              return 1;
            }
            return 0;
          })
          document.getElementById('createdIcon').setAttribute('class', 'fas fa-chevron-up float-end');
        } else {
          this.productList.sort(function (a, b) {
            if (a.created < b.created) {
              return -1;
            }
            if (a.created > b.created) {
              return 1;
            }
            return 0;
          })
          document.getElementById('createdIcon').setAttribute('class', 'fas fa-chevron-down float-end');
        }

        this.productIdAscending = false;
        this.nameAscending = false;
        this.descriptionAscending = false;
        this.manufacturerAscending = false;
        this.recommendedRetailPriceAscending = false;
        this.createdAscending = !this.createdAscending;

        this.buildRows();
      }

    },

    /**
     * Disables all ascending or descending icons in the top column headers.
     */
    disableIcons() {
      document.getElementById('productIdIcon').setAttribute('class', '');
      document.getElementById('nameIcon').setAttribute('class', '');
      document.getElementById('descriptionIcon').setAttribute('class', '');
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
      let startIndex = (this.currentPage - 1) * this.rowsPerPage;
      const outerContainer = document.getElementById('outerContainer');
      const lastChild = outerContainer.lastChild;

      if (limit > this.productList.length) {
        limit = this.productList.length
      }

      if (this.productList.length > 0) {

        // 6 is the last index of the permanent items
        let tabIndex = 7;


        for (let i = startIndex; i < limit; i++) {
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

          const descriptionCol = document.createElement("div");
          descriptionCol.setAttribute("class", `${classInput}`);
          descriptionCol.setAttribute("id", `${i}-description`);
          descriptionCol.innerText = this.productList[i].description;
          productRow.appendChild(descriptionCol);

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

          outerContainer.insertBefore(productRow, lastChild);

          tabIndex += 1;

        }
      }

      let showingStart = this.productList.length ? startIndex + 1 : 0;

      const showingString = `Showing ${showingStart}-${limit} of ${this.productList.length} results`;
      const showingRow = document.createElement('div');
      showingRow.setAttribute("class", "row");
      showingRow.setAttribute("id", `showingRow`);
      const showingCol = document.createElement('div');
      showingCol.setAttribute("class", "col");
      showingCol.innerText = showingString;
      showingRow.appendChild(showingCol);

      outerContainer.insertBefore(showingRow, lastChild);

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
      //this.orderProducts();
    } else {
      this.$router.push({name: 'Login'});
    }
  }
}
</script>

<style scoped>

.greenButton {
  background-color: #1EBA8C;
  border-color: #1EBA8C;
}

.greenButton:hover {
  background-color: transparent;
  color: #1EBA8C;
}

</style>
