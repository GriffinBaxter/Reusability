<!--This file creates the Search page.-->
<!--It current contains the navigation bar, a search results table and a footer.-->
<!--Bootstrap has been used for creating and styling the elements.-->

<template>
  <div>

    <!--nav bar-->
    <Navbar></Navbar>

    <!--search container-->
    <div id="outer-container" class="container text-font">

      <!--search bar-->
      <div class="row">
        <div class="col search-bar-positioning">
          <div class="input-group my-4">
            <input type="text" id="searchBar" class="form-control" ref="searchBar" @keydown="search($event)" tabindex="1" placeholder="Search all users">
            <button class="btn green-search-button" tabindex="2" @click="searchClicked()"><i class="fas fa-search"></i></button>
          </div>
        </div>
      </div>

      <div class="row mb-3">

        <!--order by nickname-->
        <div id="order-by-nickname-div" class="col py-2 header-col col-hover rounded-3 me-2 text-center" tabindex="3"
             @keydown="orderEnter($event)" @click="orderUsers(true, false , false, false, false)">
          <b>Nickname</b>
          <i id="nickname-icon"></i>
        </div>

        <!--order by full name-->
        <div class="col py-2 header-col col-hover rounded-3 me-2 text-center" tabindex="4"
             @keydown="orderEnter($event)" @click="orderUsers(false, true , false, false, false)">
          <b>Full name</b>
          <i id="name-icon"></i>
        </div>

        <!--order by email-->
        <div class="col py-2 header-col col-hover rounded-3 me-2 text-center" tabindex="5"
             @keydown="orderEnter($event)" @click="orderUsers(false, false , true, false, false)">
          <b>Email</b>
          <i id="email-icon"></i>
        </div>

        <!--order by address-->
        <div class="col py-2 header-col col-hover rounded-3 text-center" tabindex="6"
             @keydown="orderEnter($event)" @click="orderUsers(false, false , false, true, false)">
          <b>Address</b>
          <i id="address-icon"></i>
        </div>

      </div>

      <div class="row">
        <div class="col">

          <!--page number buttons' navigation-->
          <nav aria-label="user-table-navigation" id="pagination-nav" class="float-end" v-if="maxPage > 1">
            <ul class="pagination" id="pagination-ul">

              <li :class="toggleDisableClass('page-item', currentPage-1 <= 0)">
                <a class="page-link " href="#" @click.prevent="previousPage()">Previous</a>
              </li>

              <li class="page-item" v-if="maxPage > 2 && currentPage >= maxPage">
                <a class="page-link" href="#" @click="updatePage($event, currentPage-2)">{{currentPage-2}}</a>
              </li>

              <li class="page-item" v-if="currentPage-1 > 0">
                <a class="page-link" href="#" @click="updatePage($event, currentPage-1)">{{currentPage-1}}</a>
              </li>

              <li class="page-item active" aria-current="page">
                <a class="page-link" href="#" @click="(e) => e.preventDefault()">{{currentPage}}</a>
              </li>

              <li class="page-item" v-if="currentPage+1 <= maxPage">
                <a class="page-link" href="#" @click="updatePage($event, currentPage+1)">{{currentPage+1}}</a>
              </li>

              <li class="page-item" v-if="maxPage > 2 && currentPage <= 1">
                <a class="page-link" href="#" @click="updatePage($event, currentPage+2)">{{currentPage+2}}</a>
              </li>

              <li :class="toggleDisableClass('page-item', currentPage+1 > maxPage)" id="next-button">
                <a class="page-link" href="#" @click.prevent="nextPage()">Next</a>
              </li>
            </ul>

          </nav>


        </div>
      </div>


    </div>
  </div>
</template>

<script>
import Api from '../Api';
import Cookies from 'js-cookie';
import Navbar from "@/components/Navbar";

export default {
  name: "Search",
  components: {
    Navbar
  },
  data() {
    return {
      nickAscending: false,
      nameAscending: false,
      emailAscending: false,
      addressAscending: false,
      rowsPerPage: 5,
      currentPage: 1,
      maxPage: 1,
      userList: [],
      small: false,
      totalRows: 0,
      orderBy: "fullNameASC",
      lastQuery: "PAGEHASBEENREFRESHED" //To allow for a comparison with the previous query when there is no previous query
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
     * @param newPageNum The page to move to
     */
    updatePage(event, newPageNum) {
      event.preventDefault();
      this.currentPage = newPageNum;
      this.$router.push({
        path: "/search",
        query: {"searchQuery": this.$refs.searchBar.value, "orderBy": this.orderBy, "page": this.currentPage.toString()}
      }).catch(()=>{});
      this.requestUsers().then(() => this.buildRows())
    },

    /**
     * Emulates a click when the user presses enter on a column header.
     * @param event The keydown event
     */
    orderEnter(event) {
      if (event.keyCode === 13) {
        event.target.click();
      }
    },

    /**
     * Requests a list of users matching the given query from the back-end.
     * If successful it sets the userList variable to the response data.
     * @return {Promise}
     */
    async requestUsers() {

      const urlParams = new URLSearchParams(window.location.search);

      if (urlParams.get('searchQuery') !== null) {
        const query = urlParams.get('searchQuery').trim();

        const ordering = urlParams.get('orderBy');
        let pageNum = parseInt(urlParams.get('page'))-1;
        this.currentPage = pageNum+1;

        if (this.lastQuery !== query && this.lastQuery !== "PAGEHASBEENREFRESHED") {
          this.currentPage = 1;
          pageNum = 0;
          this.$router.push(
              {path: "/search", query: {"searchQuery": query, "orderBy": this.orderBy, "page": "1"}}
          ).catch(()=>{});
        }
        this.lastQuery = query;

        await Api.searchUsers(query, ordering, pageNum).then(response => {

          this.userList = [...response.data];
          if (this.userList.length <= 0) {
            this.currentPage = 1;
            this.maxPage = 1;
            this.totalRows = 0;
          } else {
            this.maxPage = parseInt(response.headers['total-pages']);
            this.totalRows = parseInt(response.headers['total-rows']);
          }

        }).catch((error) => {
          if (error.request && !error.response) {
            this.$router.push({path: '/timeout'});
          } else if (error.response.status === 401) {
            this.$router.push({path: '/invalidtoken'});
          } else {
            //TODO Change these to actually handle 400 responses from backend
            this.$router.push({path: '/timeout'});
          }
        })
      }
    },

    /**
     * Handles the user pressing enter with the search bar focused. Updates the search if they do.
     * @param event The keydown event
     */
    search(event) {
      if (event.keyCode === 13) {
        const inputQuery = this.$refs.searchBar.value;
        this.$router.push({
          path: "/search",
          query: {"searchQuery": inputQuery, "orderBy": this.orderBy, "page": this.currentPage.toString()}
        }).catch(()=>{});
        this.requestUsers().then(() => this.buildRows()).catch(
            (e) => console.log(e)
        );
      }
    },

    /**
     * Handles the user pressing clicking on the search button. Completes a search when they do.
     */
    searchClicked() {
      const inputQuery = this.$refs.searchBar.value;
      this.$router.push({
        path: "/search",
        query: {"searchQuery": inputQuery, "orderBy": this.orderBy, "page": this.currentPage.toString()}
      }).catch(()=>{});
      this.requestUsers().then(() => this.buildRows()).catch(
          (e) => console.log(e)
      );
    },

    /**
     * Goes to the previous page and updates the rows.
     */
    previousPage() {
      if (this.currentPage > 1) {
        this.currentPage -= 1;
        this.$router.push({
          path: "/search",
          query: {
            "searchQuery": this.$refs.searchBar.value, "orderBy": this.orderBy, "page": this.currentPage.toString()
          }
        })
        this.requestUsers().then(() => this.buildRows())
      }
    },

    /**
     * Goes to the next page and updates the rows.
     */
    nextPage() {
      if (this.currentPage < this.maxPage) {
        this.currentPage += 1;
        this.$router.push({
          path: "/search",
          query: {
            "searchQuery": this.$refs.searchBar.value, "orderBy": this.orderBy, "page": this.currentPage.toString()
          }
        })
        this.requestUsers().then(() => this.buildRows())
      }
    },

    /**
     * Orders the users based on the given booleans for each column, and updates the display
     * @param nickname Boolean, whether to order by nickname
     * @param fullName Boolean, whether to order by full name
     * @param email Boolean, whether to order by email
     * @param address Boolean, whether to order by address
     */
    orderUsers(nickname, fullName, email, address) {


      if (nickname) {
        this.disableIcons();
        if (this.nickAscending) {
          this.orderBy = "nicknameASC";
          document.getElementById('nickname-icon').setAttribute('class','fas fa-chevron-up float-end');
          this.$router.push({
            path: "/search",
            query: {
              "searchQuery": this.$refs.searchBar.value, "orderBy": "nicknameASC", "page": this.currentPage.toString()
            }
          })
        } else {
          this.orderBy = "nicknameDESC";
          document.getElementById('nickname-icon').setAttribute('class','fas fa-chevron-down float-end');
          this.$router.push({
            path: "/search",
            query: {
              "searchQuery": this.$refs.searchBar.value, "orderBy": "nicknameDESC", "page": this.currentPage.toString()
            }
          })

        }
        this.nickAscending = !this.nickAscending;
        this.nameAscending = false;
        this.emailAscending = false;
        this.addressAscending = false;
        this.requestUsers().then(() => this.buildRows());

      } else if (fullName) {
        this.disableIcons();
        if (this.nameAscending) {
          this.orderBy = "fullNameASC";
          document.getElementById('name-icon').setAttribute('class','fas fa-chevron-up float-end');
          this.$router.push({
            path: "/search",
            query: {
              "searchQuery": this.$refs.searchBar.value, "orderBy": "fullNameASC", "page": this.currentPage.toString()
            }
          })

        } else {
          this.orderBy = "fullNameDESC";
          document.getElementById('name-icon').setAttribute('class','fas fa-chevron-down float-end');
          this.$router.push({
            path: "/search",
            query: {
              "searchQuery": this.$refs.searchBar.value, "orderBy": "fullNameDESC", "page": this.currentPage.toString()
            }
          })

        }
        this.nickAscending = false;
        this.nameAscending = !this.nameAscending;
        this.emailAscending = false;
        this.addressAscending = false;
        this.requestUsers().then(() => this.buildRows());

      } else if (email) {
        this.disableIcons();
        if (this.emailAscending) {
          this.orderBy = "emailASC";
          document.getElementById('email-icon').setAttribute('class','fas fa-chevron-up float-end');
          this.$router.push({
            path: "/search",
            query: {
              "searchQuery": this.$refs.searchBar.value, "orderBy": "emailASC", "page": this.currentPage.toString()
            }
          })
        } else {
          this.orderBy = "emailDESC";
          document.getElementById('email-icon').setAttribute('class','fas fa-chevron-down float-end');
          this.$router.push({
            path: "/search",
            query: {
              "searchQuery": this.$refs.searchBar.value, "orderBy": "emailDESC", "page": this.currentPage.toString()
            }
          })

        }
        this.nickAscending = false;
        this.nameAscending = false;
        this.emailAscending = !this.emailAscending;
        this.addressAscending = false;
        this.requestUsers().then(() => this.buildRows());

      } else if (address) {
        this.disableIcons();
        if (this.addressAscending) {
          this.orderBy = "addressASC";
          document.getElementById('address-icon').setAttribute('class','fas fa-chevron-up float-end');
          this.$router.push({
            path: "/search",
            query: {
              "searchQuery": this.$refs.searchBar.value, "orderBy": "addressASC", "page": this.currentPage.toString()
            }
          })

        } else {
          this.orderBy = "addressDESC";
          document.getElementById('address-icon').setAttribute('class','fas fa-chevron-down float-end');
          this.$router.push({
            path: "/search",
            query: {
              "searchQuery": this.$refs.searchBar.value, "orderBy": "addressDESC", "page": this.currentPage.toString()
            }
          })
        }
        this.nickAscending = false;
        this.nameAscending = false;
        this.emailAscending = false;
        this.addressAscending =  !this.addressAscending;
        this.requestUsers().then(() => this.buildRows());
      }

    },

    /**
     * Disables all ascending or descending icons in the top column headers.
     */
    disableIcons() {

      // if (document.getElementById('order-by-nickname-div').childElementCount > 1) {
      //   document.getElementById('order-by-nickname-div').removeChild(document.getElementById('order-by-nickname-div').lastChild);
      // }
      document.getElementById('name-icon').setAttribute('class', '');
      document.getElementById('email-icon').setAttribute('class', '');
      document.getElementById('address-icon').setAttribute('class', '');

    },

    /**
     * Dynamically builds the rows of users from the stored userList.
     */
    buildRows() {
      const self = this;
      this.clearRows();
      let limit = this.rowsPerPage + (this.currentPage - 1) * this.rowsPerPage;
      let startIndex = 0;
      const outerContainer = document.getElementById('outer-container');
      const lastChild = outerContainer.lastChild;

      if (limit > this.userList.length) {
        limit = this.userList.length
      }

      if (this.userList.length > 0) {

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

          const userRow = document.createElement("div");
          if (i % 2 === 0) {
            userRow.setAttribute("class", "row mb-3 py-4 shadow-sm row-colour userRows");
          } else {
            userRow.setAttribute("class", "row mb-3 py-4 shadow-sm row-colour-dark userRows");
          }
          userRow.setAttribute("tabIndex", `${tabIndex}`);
          userRow.setAttribute("id", `${this.userList[i].id}`);

          const nickCol = document.createElement("div");
          nickCol.setAttribute("class", `${classInput}`);
          nickCol.setAttribute("id", `${i}-nick`);
          nickCol.innerHTML = this.userList[i].nickname;
          userRow.appendChild(nickCol);

          const nameCol = document.createElement("div");
          nameCol.setAttribute("class", `${classInput}`);
          nameCol.setAttribute("id", `${i}-name`);

          //TODO test this as not sure if we want this still -> taken from dev branch
          if (this.userList[i].middleName) {
            nameCol.innerText = this.userList[i].firstName + " " + this.userList[i].middleName + " " + this.userList[i].lastName;
          } else {
            nameCol.innerText = this.userList[i].firstName + " " + this.userList[i].lastName;
          }

          userRow.appendChild(nameCol);

          const emailCol = document.createElement("div");
          emailCol.setAttribute("class", `${classInput}`);
          emailCol.setAttribute("id", `${i}-email`);
          emailCol.innerText = this.userList[i].email;
          userRow.appendChild(emailCol);

          const addressCol = document.createElement("div");
          addressCol.setAttribute("class", `${classInput}`);
          addressCol.setAttribute("id", `${i}-address`);

          const address = this.getAddress(this.userList[i]);

          addressCol.innerText = address;
          userRow.appendChild(addressCol);

            userRow.addEventListener("click", function(event) {
              let path;

            if (event.target.id.includes('-')) {
              const row = event.target.parentNode;
              path = `/profile/${row.id}`
            } else {
              path = `/profile/${event.target.id}`
            }

            if (self.$route.path !== path) {
              self.$router.push({path});
            }

          });

          userRow.addEventListener('keydown', function (event) {
            // TODO replace all deprecated keyCode uses
            if (event.keyCode === 13) {
              event.target.click();
            }
          })

          outerContainer.insertBefore(userRow, lastChild);

          tabIndex += 1;

          }
      }

      let showingStart = this.userList.length ? (this.currentPage*this.rowsPerPage)-this.rowsPerPage+1 : 0;

      let lastEntryOfPage = limit+(this.currentPage-1)*this.rowsPerPage;

      const showingString = `Showing ${showingStart}-${lastEntryOfPage} of ${this.totalRows} results`;
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
     * Removes all rows of users from the page.
     */
    clearRows() {
      let allRows = document.getElementsByClassName("userRows");
      //TODO Not sure why i-->0 works when i >0; i-- doesn't
      for (let i = allRows.length; i-->0;) {
        allRows[i].parentNode.removeChild(allRows[i]);
      }
      if (document.contains(document.getElementById('showingRow'))) {
        document.getElementById('showingRow').remove();
      }
    },

    /*
     * Creates a string which represents a user's address.
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

    requestUsersListener() {
      this.requestUsers().then(
          () => this.buildRows()
      ).catch(
          (e) => console.log(e)
      )
    }

  },

  /**
   * When mounted, initiate population of page.
   * If cookies are invalid or not present, redirect to login page.
   */
  mounted() {
    const currentID = Cookies.get('userID');
    if (currentID) {
      this.requestUsers().then(
          () => this.buildRows()
      ).catch(
          (e) => console.log(e)
      )
    }

    document.addEventListener('page-routing', this.requestUsersListener);

    //TODO what is the purpose of this? Is it needed still?

    // let self = this;
    // this.$nextTick(function() {
    //   window.addEventListener('resize', function() {
    //     if (self.small !== document.documentElement.clientWidth <= 992) {
    //       console.log(self.small);
    //       self.buildRows();
    //       self.small = !self.small;
    //     }
    //   });
    // })
  },
  beforeDestroy() {
    document.removeEventListener('page-routing', this.requestUsersListener);
  }
}
</script>

<!--------------------------------------- Search User by Name Page Styling -------------------------------------------->

<style scoped>

#searchBar:focus {
  outline: none;
  box-shadow: 0 0 2px 2px #2eda77; /* Full freedom. (works also with border-radius) */
  border: 1px solid #1EBABC;
}

.search-bar-positioning {
  padding-top: 40px;
}

/**
 * TODO remove once footer is sticky
 * Calculates where footer should be.
 */
.all-but-footer {
  min-height: calc(100vh - 240px);
}

.page-link {
  color: #1EBA8C;
}

.page-item.active .page-link {
  background-color: #1EBA8C;
  border: 1px solid #1EBA8C;
}

.page-link:focus {
  outline: none;
  box-shadow: 0 0 2px 2px #2eda77; /* Full freedom. (works also with border-radius) */
  border: 1px solid #1EBABC;
}

</style>
