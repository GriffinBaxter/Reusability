<template>
  <div id="outerContainer" class="container">
    <div class="row">
      <div class="col">
        <div class="input-group my-4">
          <input type="text" id="searchBar" class="form-control" ref="searchBar" @keydown="search($event)" tabindex="1"
                 placeholder="Search all users">
          <button class="btn btn-primary greenButton" tabindex="2" @click="searchClicked()"><i
              class="fas fa-search"></i></button>
        </div>
      </div>
    </div>

    <div class="row mb-3">
      <div class="col py-2 header-col col-hover rounded-3 me-2 text-center" tabindex="3" @keydown="orderEnter($event)"
           @click="orderUsers(true, false , false, false, false)">
        <b>Nickname</b>
        <i id="nicknameIcon"></i>
      </div>
      <div class="col py-2 header-col col-hover rounded-3 me-2 text-center" tabindex="4" @keydown="orderEnter($event)"
           @click="orderUsers(false, true , false, false, false)">
        <b>Full name</b>
        <i id="nameIcon"></i>
      </div>
      <div class="col py-2 header-col col-hover rounded-3 me-2 text-center" tabindex="5" @keydown="orderEnter($event)"
           @click="orderUsers(false, false , true, false, false)">
        <b>Email</b>
        <i id="emailIcon"></i>
      </div>
      <div class="col py-2 header-col col-hover rounded-3 text-center" tabindex="6" @keydown="orderEnter($event)"
           @click="orderUsers(false, false , false, true, false)">
        <b>Address</b>
        <i id="addressIcon"></i>
      </div>
    </div>

    <div class="row">
      <div class="col">
        <!-- Avert your eyes for this... -->
        <nav aria-label="user-table-navigation" id="pagination-nav" class="float-end" v-if="maxPage > 1">
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
  name: "Search",
  data() {
    return {
      nickAscending: false,
      nameAscending: false,
      emailAscending: false,
      addressAscending: false,
      rowsPerPage: 5,
      currentPage: 1,
      maxPage: 2,
      userList: [],
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
     * Updates the display to show the new page when a user clicks to move to a different page.
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
     * Emulates a click when the user presses enter on a column header.
     *
     * @param event The keydown event
     */
    orderEnter(event) {
      if (event.keyCode === 13) {
        event.target.click();
      }
    },
    /*
     * Requests a list of users matching the given query from the back-end.
     * If successful it sets the userList variable to the response data.
     *
     * @return {Promise}
     */
    async requestUsers() {

      const urlParams = new URLSearchParams(window.location.search);
      const query = urlParams.get('searchQuery');
      await Api.searchUsers(query).then(response => {
        this.userList = [...response.data];
        // Order by nickname alphabetically by default
        this.userList.sort(function (a, b) {
          if (a.nickname < b.nickname) {
            return -1;
          }
          if (a.nickname > b.nickname) {
            return 1;
          }
          return 0;
        });
        this.maxPage = Math.ceil(this.userList.length / this.rowsPerPage)
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
    /*
     * Handles the user pressing enter with the search bar focused. Updates the search if they do.
     *
     * @param event The keydown event
     */
    search(event) {
      if (event.keyCode === 13) {
        const inputQuery = this.$refs.searchBar.value;
        history.pushState({}, null, this.$route.path + `?searchQuery=${inputQuery}`);
        this.requestUsers().then(() => this.buildRows()).catch(
            (e) => console.log(e)
        );
      }
    },
    /*
     * Handles the user pressing clicking on the search button. Completes a search when they do.
     *
     */
    searchClicked() {
      const inputQuery = this.$refs.searchBar.value;
      history.pushState({}, null, this.$route.path + `?searchQuery=${inputQuery}`);
      this.requestUsers().then(() => this.buildRows()).catch(
          (e) => console.log(e)
      );
    },
    /*
     * Goes to the previous page and updates the rows.
     *
     */
    previousPage() {
      if (this.currentPage > 1) {
        this.currentPage -= 1;
        this.buildRows()
      }
    },
    /*
     * Goes to the next page and updates the rows.
     *
     */
    nextPage() {
      if (this.currentPage < this.maxPage) {
        this.currentPage += 1;
        this.buildRows()
      }
    },
    /*
     * Orders the users based on the given booleans for each column, and updates the display
     * @param nickname Boolean, whether to order by nickname
     * @param fullName Boolean, whether to order by full name
     * @param email Boolean, whether to order by email
     * @param address Boolean, whether to order by address
     */
    orderUsers(nickname, fullName, email, address) {

      if (nickname) {
        this.disableIcons()
        if (this.nickAscending) {
          this.userList.sort(function (a, b) {
            if (a.nickname > b.nickname) {
              return -1;
            }
            if (a.nickname < b.nickname) {
              return 1;
            }
            return 0;
          })
          document.getElementById('nicknameIcon').setAttribute('class', 'fas fa-chevron-up float-end');
        } else {
          this.userList.sort(function (a, b) {
            if (a.nickname < b.nickname) {
              return -1;
            }
            if (a.nickname > b.nickname) {
              return 1;
            }
            return 0;
          })
          document.getElementById('nicknameIcon').setAttribute('class', 'fas fa-chevron-down float-end');
        }

        this.nickAscending = !this.nickAscending;
        this.nameAscending = false;
        this.emailAscending = false;
        this.addressAscending = false;
        this.joinedAscending = false;

        this.buildRows();
      } else if (fullName) {
        this.disableIcons()
        if (this.nameAscending) {
          this.userList.sort(function (a, b) {
            if (a.firstName > b.firstName) {
              return -1;
            }
            if (a.firstName < b.firstName) {
              return 1;
            }
            return 0;
          })
          document.getElementById('nameIcon').setAttribute('class', 'fas fa-chevron-up float-end');
        } else {
          this.userList.sort(function (a, b) {
            if (a.firstName < b.firstName) {
              return -1;
            }
            if (a.firstName > b.firstName) {
              return 1;
            }
            return 0;
          })
          document.getElementById('nameIcon').setAttribute('class', 'fas fa-chevron-down float-end');
        }

        this.nickAscending = false;
        this.nameAscending = !this.nameAscending;
        this.emailAscending = false;
        this.addressAscending = false;
        this.joinedAscending = false;

        this.buildRows();
      } else if (email) {
        this.disableIcons()
        if (this.emailAscending) {
          this.userList.sort(function (a, b) {
            if (a.email > b.email) {
              return -1;
            }
            if (a.email < b.email) {
              return 1;
            }
            return 0;
          })
          document.getElementById('emailIcon').setAttribute('class', 'fas fa-chevron-up float-end');
        } else {
          this.userList.sort(function (a, b) {
            if (a.email < b.email) {
              return -1;
            }
            if (a.email > b.email) {
              return 1;
            }
            return 0;
          })
          document.getElementById('emailIcon').setAttribute('class', 'fas fa-chevron-down float-end');
        }

        this.nickAscending = false;
        this.nameAscending = false;
        this.emailAscending = !this.emailAscending;
        this.addressAscending = false;
        this.joinedAscending = false;

        this.buildRows();
      } else if (address) {
        this.disableIcons()

        if (this.addressAscending) {
          this.userList.sort(function (a, b) {

            let city = "";
            if (a.homeAddress.city) {
              city = a.homeAddress.city;
            }
            let region = "";
            if (a.homeAddress.region) {
              region = a.homeAddress.region;
            }
            let country = "";
            if (a.homeAddress.country) {
              country = a.homeAddress.country;
            }

            let address1 = "";
            if (city !== "") {
              address1 = address1.concat(city);
            }
            if (city !== "" && region !== "") {
              address1 = address1.concat(", ", region);
            } else {
              address1 = address1.concat(region);
            }

            if (region !== "" && country !== "") {
              address1 = address1.concat(", ", country);
            } else if (city !== "" && country !== "") {
              address1 = address1.concat(", ", country);
            } else {
              address1 = address1.concat(country);
            }

            city = "";
            if (b.homeAddress.city) {
              city = b.homeAddress.city;
            }
            region = "";
            if (b.homeAddress.region) {
              region = b.homeAddress.region;
            }
            country = "";
            if (b.homeAddress.country) {
              country = b.homeAddress.country;
            }

            let address2 = "";
            if (city !== "") {
              address2 = address2.concat(city);
            }
            if (city !== "" && region !== "") {
              address2 = address2.concat(", ", region);
            } else {
              address2 = address2.concat(region);
            }

            if (region !== "" && country !== "") {
              address2 = address2.concat(", ", country);
            } else if (city !== "" && country !== "") {
              address2 = address2.concat(", ", country);
            } else {
              address2 = address2.concat(country);
            }

            if (address1 > address2) {
              return -1;
            }
            if (address1 < address2) {
              return 1;
            }
            return 0;
          })
          document.getElementById('addressIcon').setAttribute('class', 'fas fa-chevron-up float-end');
        } else {
          this.userList.sort(function (a, b) {

            let city = "";
            if (a.homeAddress.city) {
              city = a.homeAddress.city;
            }
            let region = "";
            if (a.homeAddress.region) {
              region = a.homeAddress.region;
            }
            let country = "";
            if (a.homeAddress.country) {
              country = a.homeAddress.country;
            }

            let address1 = "";
            if (city !== "") {
              address1 = address1.concat(city);
            }
            if (city !== "" && region !== "") {
              address1 = address1.concat(", ", region);
            } else {
              address1 = address1.concat(region);
            }

            if (region !== "" && country !== "") {
              address1 = address1.concat(", ", country);
            } else if (city !== "" && country !== "") {
              address1 = address1.concat(", ", country);
            } else {
              address1 = address1.concat(country);
            }

            city = "";
            if (b.homeAddress.city) {
              city = b.homeAddress.city;
            }
            region = "";
            if (b.homeAddress.region) {
              region = b.homeAddress.region;
            }
            country = "";
            if (b.homeAddress.country) {
              country = b.homeAddress.country;
            }

            let address2 = "";
            if (city !== "") {
              address2 = address2.concat(city);
            }
            if (city !== "" && region !== "") {
              address2 = address2.concat(", ", region);
            } else {
              address2 = address2.concat(region);
            }

            if (region !== "" && country !== "") {
              address2 = address2.concat(", ", country);
            } else if (city !== "" && country !== "") {
              address2 = address2.concat(", ", country);
            } else {
              address2 = address2.concat(country);
            }

            if (address1 < address2) {
              return -1;
            }
            if (address1 > address2) {
              return 1;
            }
            return 0;
          })
          document.getElementById('addressIcon').setAttribute('class', 'fas fa-chevron-down float-end');
        }

        this.nickAscending = false;
        this.nameAscending = false;
        this.emailAscending = false;
        this.addressAscending = !this.addressAscending;
        this.joinedAscending = false;

        this.buildRows();
      }

    },
    /*
     * Disables all ascending or descending icons in the top column headers.
     */
    disableIcons() {
      document.getElementById('nicknameIcon').setAttribute('class', '');
      document.getElementById('nameIcon').setAttribute('class', '');
      document.getElementById('emailIcon').setAttribute('class', '');
      document.getElementById('addressIcon').setAttribute('class', '');

    },
    /*
     * Dynamically builds the rows of users from the stored userList.
     */
    buildRows() {
      const self = this;
      this.clearRows();
      let limit = this.rowsPerPage + (this.currentPage - 1) * this.rowsPerPage;
      let startIndex = (this.currentPage - 1) * this.rowsPerPage;
      const outerContainer = document.getElementById('outerContainer');
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

          addressCol.innerText = address
          userRow.appendChild(addressCol);


          userRow.addEventListener("click", function (event) {
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

      let showingStart = this.userList.length ? startIndex + 1 : 0;

      const showingString = `Showing ${showingStart}-${limit} of ${this.userList.length} results`;
      const showingRow = document.createElement('div');
      showingRow.setAttribute("class", "row");
      showingRow.setAttribute("id", `showingRow`);
      const showingCol = document.createElement('div');
      showingCol.setAttribute("class", "col");
      showingCol.innerText = showingString;
      showingRow.appendChild(showingCol);

      outerContainer.insertBefore(showingRow, lastChild);

    },
    /*
     * Removes all rows of users from the page.
     */
    clearRows() {
      let allRows = document.getElementsByClassName("userRows");
      // Not sure why i-->0 works when i >0; i-- doesn't
      for (let i = allRows.length; i-- > 0;) {
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

  },
  mounted() {
    /*
    When mounted, initiate population of page.
    If cookies are invalid or not present, redirect to login page.
     */
    const currentID = Cookies.get('userID');
    if (currentID) {
      this.requestUsers().then(
          () => this.buildRows()
      ).catch(
          (e) => console.log(e)
      )
      //this.orderUsers();
    } else {
      this.$router.push({name: 'Login'});
    }

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
