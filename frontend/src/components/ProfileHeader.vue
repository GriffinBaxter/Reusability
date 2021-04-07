<template>
    <div class="container pt-4" id="searchContainer">
      <div class="row text-center">
        <div class="col-md-3 mb-md-0 mb-3">
          <img id="logo" src="../../public/logo_only_med.png" class="img-fluid" alt="logo">
        </div>

        <div class="col">
          <div class="input-group">
            <input type="text" id="searchBar" ref="searchInput" class="form-control" @keydown="enterPressed($event)" placeholder="Search all users">
            <button class="btn btn-primary greenButton" @click="searchClicked()"><i class="fas fa-search"></i></button>
          </div>
        </div>

        <div class="col-md-3 btn-group">
          <div class="dropdown">
            <button class="btn btn-secondary dropdown-toggle"
                    type="button" id="dropdownMenu1" data-toggle="dropdown"
                    aria-haspopup="true" aria-expanded="false">
                  <img width="50px" class="rounded-circle img-thumbnail" src="../../public/sample_profile_image.jpg" alt="Acting as image"/>
                  <h6 class="text-center" id="actAsText">{{actAs}}</h6>
            </button>
            <div class="dropdown-menu" aria-labelledby="dropdownMenu1">
              <button class="dropdown-item" type="button">No businesses found</button>
            </div>
          </div>
        </div>
      </div>
    </div>
</template>


<script>

import Api from '../Api';
import Cookies from "js-cookie";

export default {
  name: "ProfileHeader",
  data() {
    return {
      actAs:"",
      businesses: []
    }
  },
  methods: {
    getUserData() {
      const currentID = Cookies.get('userID');
      Api.getUser(currentID).then(response => (this.setCurUser(response.data))).catch((error) => {

        if (error.request && !error.response) {
          this.$router.push({path: '/timeout'});
        } else if (error.response.status === 406) {
          this.$router.push({path: '/noUser'});
        } else if (error.response.status === 401) {
          this.$router.push({path: '/invalidtoken'});
        } else {
          this.$router.push({path: '/noUser'});
          console.log(error.message);
        }
      })
    },
    enterPressed(event) {
      if (event.keyCode === 13) {
        // Enter pressed
        const inputQuery = this.$refs.searchInput.value;
        this.$router.push({ path: '/search', query: { searchQuery: `${inputQuery}` }})
      }
    },
    searchClicked() {
      const inputQuery = this.$refs.searchInput.value;
      this.$router.push({ path: '/search', query: { searchQuery: `${inputQuery}` }})
    },
    setCurUser(response) {
      if (response.nickname == null) {
        this.actAs = response.firstName + " " + response.lastName;
      } else {
        this.actAs = response.nickname;
      }
      this.businesses = response.businessesAdministered;
    }
  },
  mounted() {
    this.getUserData();
  }
}
</script>

<style scoped>
#searchContainer {
  width: 80%;
}

#logo {
  max-height: 60px
}

#dropdownMenu1 {
  background-color: #1EBA8C;
  width: inherit;
}

.greenButton {
  background-color: #1EBA8C;
  border-color: #1EBA8C;
}

.greenButton:hover {
  background-color: transparent;
  color: #1EBA8C;
}

#actAsText {
  padding-left: 5px;
  display: inline;
  margin: auto;
}

</style>
