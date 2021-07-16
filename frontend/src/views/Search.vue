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
      <ProfileHeader
          @requestUsers="requestUsers"
          @requestBusinesses="requestBusinesses"/>

      <div class="row mb-3 mt-3">

        <!-- table component which lists users or businesses -->
        <Table table-id="search-id" null-string-value="N/A" :table-tab-index="0"
               :table-headers="tableHeaders" :table-data="tableData"
               :max-rows-per-page="rowsPerPage" :total-rows="totalRows" :current-page-override="currentPage"
               :order-by-override="tableOrderBy" :table-data-is-page="true"
               @update-current-page="event => updatePage(event)"
               @order-by-header-index="event => orderData(event)"
               @row-selected="event => routeToProfile(event.index)"/>

      </div>

    </div>
    </div>
    <Footer/>
  </div>
</template>

<script>
import Api from '../Api';
import Navbar from "@/components/main/Navbar";
import Footer from "@/components/main/Footer";
import ProfileHeader from "../components/ProfileHeader";
import User from "@/configs/User";
import Table from "../components/Table";
import Business from "@/configs/Business";

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
      dataList: [],
      // A list of the user table headers
      userHeaders: ["Nickname", "Full Name", "Email", "Address"],
      // A list of the order by headers for users, which is used for talking to the backend
      orderByUserHeaders: ["nickname", "fullName", "email", "address"],
      // A list of the business table headers
      businessHeaders: ["Name", "Address", "Business Type"],
      // A list of the order by headers for businesses, which is used for talking to the backend
      orderByBusinessHeaders: ["name", "address"],
      // Generic headers which get switched between business and user depending on the selected search type
      tableHeaders: [],
      tableOrderByHeaders: [],
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
      orderByString: "",
      selectedBusinessType: "Any",
      query: "",
      searchType: "",
    }
  },

  methods: {

    /**
     * Updates the display to show the new page when a user clicks to move to a different page.
     * @param event The click event
     */
    updatePage(event) {
      this.currentPage = event.newPageNumber;

      if (this.searchType === 'User') {
        this.$router.push({
          path: "/search",
          query: {"type": "User", "searchQuery": this.query, "orderBy": this.orderByString, "page": (this.currentPage + 1).toString()}
        });
        this.requestUsers(this.query);
      } else if (this.searchType === 'Business') {
        this.$router.push({
          path: "/search",
          query: {"type": "Business", "searchQuery": this.query, "businessType": this.selectedBusinessType, "orderBy": this.orderByString, "page": (this.currentPage + 1).toString()}
        });
        this.requestBusinesses(this.query, this.selectedBusinessType);
      }
    },

    /**
     * Parses the orderByString and returns the resulted Objects.
     * @return {{orderBy: null | String, isAscending: boolean}} This contains the {orderBy, isAscending} properties of the this.orderByString .
     * Emulates a click when the product presses enter on a column header.
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
     * Requests a list of businesses matching the given query from the back-end.
     * If successful it sets the dataList variable to the response data.
     * @return {Promise}
     *
     * @param {inputQuery} The query received from the search bar
     * @param {businessType} The type of business the user would like to search for
     */
    async requestBusinesses(inputQuery, businessType) {
      this.tableData = [];

      if (inputQuery === undefined) {
        inputQuery = this.$route.query["searchQuery"];
      }

      if (businessType === undefined) {
        businessType = "Any";
      }

      this.searchType = 'Business';
      this.tableHeaders = this.businessHeaders;
      this.tableOrderByHeaders = this.orderByBusinessHeaders;

      if (inputQuery !== null) {
        this.query = inputQuery.trim();
        this.selectedBusinessType = businessType;

        this.orderByString = this.$route.query["orderBy"] || "nameASC";
        this.currentPage = parseInt(this.$route.query["page"]) - 1 || 0;

        if (this.totalPages > 0 && this.currentPage > this.totalPages - 1) {
          await this.$router.push({path: '/pageDoesNotExist'});
        }

        await Api.searchBusinesses(this.query, this.convertBusinessType(this.selectedBusinessType), this.orderByString, this.currentPage).then(response => {

          // Parsing the orderBy string to get the orderBy and isAscending components to update the table.
          const {orderBy, isAscending} = this.parseOrderBy();
          this.tableOrderBy = {orderBy: orderBy, isAscending: isAscending};

          this.dataList = response.data.map((business) => {
            return new Business(business);
          });
          let newTableData = [];

          // No results
          if (this.dataList.length <= 0) {
            this.currentPage = 0;
            this.maxPage = 0;
            this.totalRows = 0;
            this.totalPages = 1;
            // Generate the tableData to be placed in the table & get the total number of rows.
          } else {
            this.totalRows = parseInt(response.headers["total-rows"]);
            this.totalPages = parseInt(response.headers["total-pages"]);

            for (let i = 0; i < this.dataList.length; i++) {
              const businessData = this.dataList[i].data;
              newTableData.push(businessData.name);
              newTableData.push(this.getAddress(businessData));
              newTableData.push(this.convertBusinessType(businessData.businessType));
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
     * Converts a provided string representing a business type to a backend-friendly
     * string representation and vice versa.
     *
     * @param {businessType} The business type to be converted into a backend-friendly format and vice versa
     * @return a backend-friendly or frontend-friendly string conversion of the provided business type
     */
    convertBusinessType(businessType) {
      switch (businessType) {
        case ("Any"):
          return "";
        case ("Accommodation and Food Services"):
          return "ACCOMMODATION_AND_FOOD_SERVICES";
        case ("Retail Trade"):
          return "RETAIL_TRADE";
        case ("Charitable Organisation"):
          return "CHARITABLE_ORGANISATION";
        case ("Non Profit Organisation"):
          return "NON_PROFIT_ORGANISATION";
        case ("ACCOMMODATION_AND_FOOD_SERVICES"):
          return "Accommodation and Food Services";
        case ("RETAIL_TRADE"):
          return "Retail Trade";
        case ("CHARITABLE_ORGANISATION"):
          return "Charitable Organisation";
        case ("NON_PROFIT_ORGANISATION"):
          return "Non Profit Organisation";
      }
    },

    /**
     * Requests a list of users matching the given query from the back-end.
     * If successful it sets the dataList variable to the response data.
     * @return {Promise}
     *
     * @param {inputQuery} The query received from the search bar
     */
    async requestUsers(inputQuery) {
      this.tableData = [];

      if (inputQuery === undefined) {
        inputQuery = this.$route.query["searchQuery"];
      }

      this.searchType = 'User';
      this.tableHeaders = this.userHeaders;
      this.tableOrderByHeaders = this.orderByUserHeaders;

      if (inputQuery !== null) {
        this.query = inputQuery.trim();

        this.orderByString = this.$route.query["orderBy"] || "fullNameASC";
        this.currentPage = parseInt(this.$route.query["page"]) - 1 || 0;

        if (this.totalPages > 0 && this.currentPage > this.totalPages - 1) {
          await this.$router.push({path: '/pageDoesNotExist'});
        }

        await Api.searchUsers(this.query, this.orderByString, this.currentPage).then(response => {

          // Parsing the orderBy string to get the orderBy and isAscending components to update the table.
          const {orderBy, isAscending} = this.parseOrderBy();
          this.tableOrderBy = {orderBy: orderBy, isAscending: isAscending};

          this.dataList = response.data.map((user) => {
            return new User(user);
          });
          let newTableData = [];

          // No results
          if (this.dataList.length <= 0) {
            this.currentPage = 0;
            this.maxPage = 0;
            this.totalRows = 0;
            this.totalPages = 1;
            // Generate the tableData to be placed in the table & get the total number of rows.
          } else {
            this.totalRows = parseInt(response.headers["total-rows"]);
            this.totalPages = parseInt(response.headers["total-pages"]);

            for (let i = 0; i < this.dataList.length; i++) {
              const userData = this.dataList[i].data;
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
     * Creates a string which represents an entity's address.
     * @param entity a JSON representation of a user or business.
     * @return a string representing an entity's address.
     */
    getAddress(entity) {

      let city = "";
      let region = "";
      let country = "";

      if (entity.homeAddress !== undefined) {
        if (entity.homeAddress.city) {
          city = entity.homeAddress.city;
        }
        if (entity.homeAddress.region) {
          region = entity.homeAddress.region;
        }
        if (entity.homeAddress.country) {
          country = entity.homeAddress.country;
        }
      } else {
        if (entity.address.city) {
          city = entity.address.city;
        }
        if (entity.address.region) {
          region = entity.address.region;
        }
        if (entity.address.country) {
          country = entity.address.country;
        }
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
     * Updates the URL and calls the requestUsers() or requestBusinesses() methods to update the table depending on the current search type.
     * @param event This contains the {orderBy, isAscending} components of the new desired ordering.
     */
    orderData(event) {
      this.orderByString = `${this.tableOrderByHeaders[event.orderBy]}${event.isAscending ? 'ASC' : 'DESC'}`

      if (this.searchType === 'User') {
        this.$router.push({
          path: "/search",
          query: {
            "type": "User", "searchQuery": this.query, "orderBy": this.orderByString, "page": (this.currentPage + 1).toString()
          }
        });
        this.requestUsers(this.query);
      } else if (this.searchType === 'Business') {
        this.$router.push({
          path: "/search",
          query: {
            "type": "Business", "searchQuery": this.query, "businessType": this.selectedBusinessType, "orderBy": this.orderByString, "page": (this.currentPage + 1).toString()
          }
        });
        this.requestBusinesses(this.query, this.selectedBusinessType);
      }
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
    this.searchType = this.$route.query["type"];
    this.query = this.$route.query["searchQuery"];

    if (this.searchType === 'User') {
      this.requestUsers(this.query);
    } else if (this.searchType === 'Business') {
      this.selectedBusinessType = this.$route.query["businessType"];
      this.requestBusinesses(this.query, this.selectedBusinessType);
    }
  },

}
</script>

<!--------------------------------------- Search User by Name Page Styling -------------------------------------------->

<style scoped>

</style>
