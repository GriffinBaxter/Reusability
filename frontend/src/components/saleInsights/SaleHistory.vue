<template>
  <div>
    <div id="main">
      <div class="container mt-4">
        <div class="card p-3">
          <h1 id="page-title">{{ businessName }} Sale History</h1>
        </div>
        <div class="card p-3">
        <table class="table table-borderless"  id="sale-table">
          <thead>
          <tr>
            <th id="columns" v-for="column in columns" :key="column.title">{{ column.title }}</th>
          </tr>
          </thead>
          <tbody>
          <tr id="rows" v-for="listing in soldListings" :key="listing.id">
            <td>{{ listing.productId }}</td>
            <td>{{ listing.listingDate }}</td>
            <td>{{ listing.saleDate }}</td>
            <td>{{ listing.quantity }}</td>
            <td>{{ listing.price }}</td>
            <td>{{ listing.bookmarks }}</td>
            <td @click="goToCustomerProfile(listing.customer.id)" id="buyer-cell">
              {{ listing.customer.firstName }} {{ listing.customer.lastName }}
            </td>
          </tr>
          </tbody>
          <tfoot>
          <div v-if="noResults">
            No listings sold
          </div>
          <div v-else>
            Showing {{currentPage*maxSoldListings+1}}-{{numberListingsRetrieved+currentPage*maxSoldListings}} of {{totalListings}} listings sold
          </div>
          </tfoot>
        </table>
        </div>
        <!---------------------------------------------- page buttons ------------------------------------------------>
        <div class="page-buttons mt-4 mb-4" id="page-button-container">
          <PageButtons
              v-bind:totalPages="totalPages"
              v-bind:currentPage="currentPage"
              @updatePage="updatePage"/>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Api from "../../Api";
import {formatDate} from "../../dateUtils";
import PageButtons from "../PageButtons";

export default {
  name: "SaleHistory",
  components: {
    PageButtons
  },
  props: {
    businessName: {
      type: String,
      default: "",
      required: true
    },
    businessCountry: {
      type: String,
      default: "",
      required: true
    },
    businessId: {
      type: Number,
      default: 0,
      required: true
    },
    currencySymbol: {
      type: String,
      default: "",
      required: true
    },
    currencyCode: {
      type: String,
      default: "",
      required: true
    }
  },
  data() {
    return {
      soldListings: [],
      columns: [
        {title: "Product Id"},
        {title: "Listing Date"},
        {title: "Sale Date"},
        {title: "Quantity"},
        {title: "Price"},
        {title: "Bookmarks"},
        {title: "Buyer"}
      ],

      // Table
      maxSoldListings: 10,
      // Total number of pages is used to determine pagination
      totalPages: 1,
      currentPage: 0,
      noResults: false,
      numberListingsRetrieved: 0,
      totalListings: 0
    }
  },

  methods: {
    /**
     * Calls a GET request to the backend to retrieve the sold listings for the current business, and
     * then formats the retrieved data to be displayed.
     */
    async retrieveSoldListings() {
      await Api.getSoldListings(this.businessId, this.currentPage).then(response => {
        this.soldListings = response.data;
        this.numberListingsRetrieved = this.soldListings.length;
        this.totalListings = parseInt(response.headers["total-rows"]);
        this.totalPages = parseInt(response.headers["total-pages"]);
        // format the dates and prices of the listings accordingly
        for (let i = 0; i < this.soldListings.length; i++) {
          this.soldListings[i].listingDate = formatDate(this.soldListings[i].listingDate);
          this.soldListings[i].saleDate = formatDate(this.soldListings[i].saleDate);
          this.soldListings[i].price = this.formatPrice(this.soldListings[i].price);
        }
        // no results have been received so the no results message should be displayed
        if (this.soldListings.length === 0) {
          this.noResults = true;
        }
        // fill the table with empty rows so that the table remains a constant size
        if (this.soldListings.length < this.maxSoldListings) {
          for (let j = this.soldListings.length; j < this.maxSoldListings; j++) {
            this.soldListings.push({
              bookmarks: "",
              id: j,
              listingDate: "",
              price: "",
              productId: "",
              quantity: "",
              saleDate: "",
              customer: {
                firstName: "",
                lastName: ""
              }
            });
          }
        }
      }).catch((error) => {
        this.manageError(error);
      })
    },
    /**
     * If a request in to the backend results in an error, then this method will deal with the error.
     * @param error the error received from the backend.
     */
    async manageError(error) {
      if (error.request && !error.response)      { await this.$router.push({path: '/timeout'});      }
      else if (error.response.status === 401)    { await this.$router.push({path: '/invalidtoken'}); }
      else if (error.response.status === 403)    { await this.$router.push({path: '/forbidden'});    }
      else if (error.response.status === 406)    { await this.$router.push({path: '/noBusiness'});   }
      else { await this.$router.push({path: '/noBusiness'}); console.log(error.message); }
    },
    /**
     * This method adds a currency symbol and unit to a price, if the business has a valid currency.
     * An example of the returned format is $24 USD
     */
    formatPrice(price) {
      if ((this.currencySymbol !== "" && this.currencySymbol !== null) && (this.currencyCode !== "" && this.currencyCode !== null)) {
        return this.currencySymbol + price +  " " + this.currencyCode;
      } else if (this.currencySymbol !== "" && this.currencySymbol !== null) {
        return this.currencySymbol + price;
      } else if (this.currencyCode !== "" && this.currencyCode !== null) {
        return price + " " + this.currencyCode;
      }
      return price;
    },
    /**
     * Given a new page number. The function will update the table to show the new page.
     * @param newPageNumber The 0 origin page number.
     */
    updatePage(newPageNumber) {
        this.currentPage = newPageNumber;
        this.retrieveSoldListings();
    },
    /**
     * Redirects the business administrator to the profile page of the buyer of a listing based on their id.
     */
    goToCustomerProfile(userId) {
      this.$router.push({path: `/profile/${userId}`});
    }
  }
}
</script>

<style scoped>

#page-title {
  padding: 10px;
  text-align: center;
}

#rows {
  min-height: 40px;
  height: 40px;
}

#buyer-cell:hover {
  color: #1EBA8C !important;
  cursor: pointer;
}

table td:hover {
  cursor: default;
}

</style>
