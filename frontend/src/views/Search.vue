<!--This file creates the Search page.-->
<!--It current contains the navigation bar, a search results table and a footer.-->
<!--Bootstrap has been used for creating and styling the elements.-->

<template>
  <div>
    <div id="main">
    <!--nav bar-->
    <Navbar></Navbar>

    <!--search container-->
    <div id="outer-container" class="container text-font">

      <!--search bar - reusing the search bar in the profile header-->
      <ProfileHeader>
        @requestUsers="requestUsers"
        @requestBusinesses="requestBusinesses"
      </ProfileHeader>

      <div class="row mb-3">

        <!-- table component which lists users or businesses -->
        <Table table-id="search-id" null-string-value="N/A" :table-tab-index="0"
               :table-headers="tableUserHeaders" :table-data="tableData"
               :max-rows-per-page="rowsPerPage" :total-rows="totalRows" :current-page-override="currentPage"
               :order-by-override="tableOrderBy" :table-data-is-page="true"
               @update-current-page="event => updatePage(event)"
               @order-by-header-index="event => orderUsers(event)"
               @row-selected="event => routeToProfile(event.index)"/>

      </div>

    </div>
    </div>
    <Footer/>
  </div>
</template>

<script>
import Api from '../Api';
import Cookies from 'js-cookie';
import Navbar from "@/components/main/Navbar";
import Footer from "@/components/main/Footer";
import ProfileHeader from "../components/ProfileHeader";
import User from "@/configs/User";
import Table from "../components/Table";

export default {
  name: "Search",
  components: {
    Footer,
    Navbar,
    ProfileHeader,
    Table
  },
  data() {
    return {
      userList: [],
      lastQuery: "PAGEHASBEENREFRESHED", //To allow for a comparison with the previous query when there is no previous query
      // A list of the user table headers
      tableUserHeaders: ["Nickname", "Full Name", "Email", "Address"],
      // A list of the ordering by headers, which is used with talking to the backend
      tableOrderByUserHeaders: ["nickname", "fullName", "email", "address"],
      // A list of all the data points (user or business) belonging to the table
      tableData: [],
      // These variables are used to control and update the table.
      rowsPerPage: 5,
      currentPage: 0,
      totalRows: 0,
      totalPages: 0,
      maxPage: 1,
      // Used to tell the table what is the current ordering (for visual purposes).
      tableOrderBy: {orderBy: null, isAscending: true},
    }
  },

  methods: {
    /**
     * Toggles the disabling of pagination buttons.
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
     * Updates the display to show the new page when a user clicks to move to a different page.
     * @param event The click event
     */
    updatePage(event) {
      this.currentPage = event.newPageNumber;
      this.$router.push({
        path: "/search",
        query: {"type": "User", "searchQuery": this.$route.query["searchQuery"], "orderBy": this.orderByString, "page": (this.currentPage + 1).toString()}
      });
      this.requestUsers(this.$route.query["searchQuery"]);
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

      // If we found a valid orderBy compare it against the allowed orderBy headers in tableOrderByUserHeaders
      if (orderBy !== null) {
        orderBy = this.tableOrderByUserHeaders.indexOf(orderBy);

        // If the orderBy is returned as -1. This means that no header was found!
        // So we say it is unordered.
        if (orderBy === -1) {
          orderBy = null;
        }
      }

      return {orderBy, isAscending};
    },

    /**
     * Requests a list of users matching the given query from the back-end.
     * If successful it sets the userList variable to the response data.
     * @return {Promise}
     */
    async requestUsers(inputQuery) {

      if (inputQuery !== null) {
        const query = inputQuery.trim();

        this.orderByString = this.$route.query["orderBy"] || "fullNameASC";
        this.currentPage = parseInt(this.$route.query["page"]) - 1 || 0;

        if (this.totalPages > 0 && this.currentPage > this.totalPages - 1) {
          this.$router.push({path: '/pageDoesNotExist'});
        }

        if (this.lastQuery !== query && this.lastQuery !== "PAGEHASBEENREFRESHED") {
          this.currentPage = 0;
          await this.$router.push(
              {path: "/search", query: {"type": "User", "searchQuery": query, "orderBy": this.orderByString, "page": "1"}}
          );
        }
        this.lastQuery = query;

        await Api.searchUsers(query, this.orderByString, this.currentPage).then(response => {

          // Parsing the orderBy string to get the orderBy and isAscending components to update the table.
          const {orderBy, isAscending} = this.parseOrderBy();
          this.tableOrderBy = {orderBy: orderBy, isAscending: isAscending};

          this.userList = response.data.map((user) => {
            return new User(user);
          });
          let newTableData = [];

          // No results
          if (this.userList.length <= 0) {
            this.currentPage = 0;
            this.maxPage = 0;
            this.totalRows = 0;
            this.totalPages = 0;
            // Generate the tableData to be placed in the table & get the total number of rows.
          } else {
            this.totalRows = parseInt(response.headers["total-rows"]);
            this.totalPages = parseInt(response.headers["total-pages"]);

            for (let i = 0; i < this.userList.length; i++) {
              const userData = this.userList[i].data;
              newTableData.push(userData.nickname);
              newTableData.push(this.getFullName(userData));
              newTableData.push(userData.email);
              newTableData.push(this.getAddress(userData));
            }
            this.tableData = newTableData;
          }
        }).catch((error) => {
          if (error.request && !error.response) {
            this.$router.push({path: '/timeout'});
          } else if (error.response.status === 400) {
            this.$router.push({path: '/pageDoesNotExist'});
          } else {
            this.$router.push({path: '/timeout'});
            console.log(error.message);
          }
        })
      }
    },

    /**
     * Creates a string which represents a user's address.
     * @param user a JSON representation of a user.
     * @return a string representing a user's address.
     */
    getAddress(user) {

      let city = "";
      if (user.homeAddress.city) {
        city = user.homeAddress.city;
      }
      let region = "";
      if (user.homeAddress.region) {
        region = user.homeAddress.region;
      }
      let country = "";
      if (user.homeAddress.country) {
        country = user.homeAddress.country;
      }

      let address = "";
      if (city !== "") {
        address = address.concat(city);
      }
      if (city !== "" && region !== "") {
        address = address.concat(", ", region);
      } else {
        address = address.concat(region);
      }

      if (region !== "" && country !== "") {
        address = address.concat(", ", country);
      } else if (city !== "" && country !== "") {
        address = address.concat(", ", country);
      } else {
        address = address.concat(country);
      }

      return address;
    },

    /**
     * Creates a string which represents a user's full name.
     * @param user a JSON representation of a user.
     * @return a string representing a user's full name.
     */
    getFullName(user) {
      if (user.middleName) {
        return user.firstName + " " + user.middleName + " " + user.lastName;
      } else {
        return user.firstName + " " + user.lastName;
      }
    },

    /**
     * Updates the URL and calls the requestUsers() to update the table.
     * @param event This contains the {orderBy, isAscending} components of the new desired ordering.
     */
    orderUsers(event) {
      this.orderByString = `${this.tableOrderByUserHeaders[event.orderBy]}${event.isAscending ? 'ASC' : 'DESC'}`
      this.$router.push({
        path: "/search",
        query: {
          "type": "User", "searchQuery": this.$route.query["searchQuery"], "orderBy": this.orderByString, "page": (this.currentPage + 1).toString()
        }
      });
      this.requestUsers(this.$route.query["searchQuery"]);
    },

    /**
     * Disables all ascending or descending icons in the top column headers.
     */
    disableIcons() {
      document.getElementById('name-icon').setAttribute('class', '');
      document.getElementById('email-icon').setAttribute('class', '');
      document.getElementById('address-icon').setAttribute('class', '');
    },

    routeToProfile(index) {
      return index;
    }

  },

  /**
   * When mounted, initiate population of page.
   * If cookies are invalid or not present, redirect to login page.
   */
  mounted() {
    const currentID = Cookies.get('userID');
    if (currentID) {
      this.requestUsers(this.$route.query["searchQuery"]);
    }
  },

}
</script>

<!--------------------------------------- Search User by Name Page Styling -------------------------------------------->

<style scoped>

/**
 * TODO remove once footer is sticky
 * Calculates where footer should be.
 */
.all-but-footer {
  min-height: calc(100vh - 240px);
}

</style>
