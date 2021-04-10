<template>
    <div class="container pt-4" id="header">
      <div class="row text-center">
        <div class="col-md-3 mb-md-0 mb-3">
          <img id="logo" src="../../public/logo_only_med.png" class="img-fluid" alt="logo">
        </div>

        <div class="col">
          <div class="input-group" id="searchContainer">
            <input type="text" id="searchBar" ref="searchInput" class="form-control" @keydown="enterPressed($event)" placeholder="Search all users">
            <button class="btn btn-primary greenButton" @click="searchClicked()"><i class="fas fa-search"></i></button>
          </div>
        </div>

        <div class="col-md-3 btn-group">
          <div class="dropdown" id="dropDown">
            <button id="dropDownButton" class="btn btn-success" type="button" @click="showDropMenu">
              <img src="../../public/sample_profile_image.jpg" width="45px" class="rounded-circle img-thumbnail" alt="Acting as image" id="actAsImg"/>
              <div id="ActAsTxt">{{actAs}}</div>
            </button>
            <ul v-if="showMenu" class="dropdown-menu" aria-labelledby="dropdownMenu1">
              <div id="InteractText"> Interact as: </div>
              <li id="dropListItems" class="list-group-item" v-for="(act, index) in list" :key = "act.id" @click="itemClicked(index)"> {{act.name}} </li>
            </ul>
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
      currentUser:"",
      actAs: "",
      actAsId: null,
      list: [],
      businesses: [],
      showMenu: false,
      imgSrc: null
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
      this.currentUser = response;
      if (Cookies.get('actAs')) {
        this.actAsId = Cookies.get('actAs');
      } else {
        if (response.nickname == null) {
          this.actAs = response.firstName;
        } else {
          this.actAs = response.nickname;
        }
      }
      this.businesses = response.businessesAdministered;
      this.refreshDropdown();
    },
    showDropMenu() {
      this.showMenu = !this.showMenu;
    },
    refreshDropdown() {
      if (this.currentUser.nickname == null)
      {
        this.list = [ {id:this.currentUser.id, name:this.currentUser.firstName} ];
      } else {
        this.list = [ {id:this.currentUser.id, name:this.currentUser.nickname} ];
      }
      for (let i=0; i<this.businesses.length; i++) {
        this.list.push(this.businesses[i]);
      }
    },
    itemClicked(index) {
      this.showDropMenu();
      if (index === 0)
      {
        // Delete Cookie
        Cookies.remove('actAs');
        //
        this.actAsId = null;
        if (this.currentUser.nickname) {
          this.actAs = this.currentUser.nickname;
        } else {
          this.actAs = this.currentUser.firstName;
        }
      } else {
        this.thumbnail = null;
        Cookies.set('actAs', this.list[index].id);
        this.actAsId = this.list[index].id;
        this.actAs = this.list[index].name;
      }
    }
  },
  mounted() {
    this.getUserData();
    console.log();
  }
}
</script>

<style scoped>
#header {
  width: 80%;
}

#searchContainer {
  vertical-align: middle;
  padding: 15px;
  width: 100%;
}

#logo {
  max-height: 60px;
}

#dropDownButton {
  background-color: #1EBA8C;
  height: 60px;
  width: 100%;
}

#dropDown {
  padding-left: 10%;
  width: 90%;
  max-height:60px;
  margin-top: 5px;
}

#InteractText {
  margin: 0;
  padding: 1px;
  color: white;
  text-align: center;
}

#ActAsTxt {
  margin: 0;
  text-align: center;
  line-height: 45px;
  display: inline-block;
}

ul {
  display: block;
  width: inherit;
  background-color: #1EBA8C;
  padding: 0;
  margin-top: 1px;
  border: 1px solid #17946f;
}

#dropListItems {
  text-align: center;
  color: white;
  background-color: inherit;
  padding: 4px;
  width: 100%;
  cursor: pointer;
}

.greenButton {
  background-color: #1EBA8C;
  border-color: #1EBA8C;
}

.greenButton:hover {
  background-color: transparent;
  color: #1EBA8C;
}

#actAsImg {
  float: left;
  display: inline-block;
}
</style>
