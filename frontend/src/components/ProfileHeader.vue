<template>
    <div class="container pt-3" id="searchContainer">
      <div class="row text-center">
        <div class="col-md-2 mb-md-0 mb-3">
          <img id="logo" src="../../public/logo_only_med.png" class="img-fluid" alt="logo">
        </div>
        <div class="col">
          <div class="input-group">
            <input type="text" id="searchBar" ref="searchInput" class="form-control" @keydown="enterPressed($event)" placeholder="Search all users">
            <button class="btn btn-primary greenButton" @click="searchClicked()"><i class="fas fa-search"></i></button>
          </div>
        </div>
        <div class="col">
          <img id="actAsPhoto" src="" alt="User/Business">
          <div class="mt-2">
            {{actAs}}
          </div>
        </div>
      </div>
    </div>
</template>

<script>

import Api from '../Api';

export default {
  name: "ProfileHeader",
  data() {
    return {
      actAs:""
    }
  },
  methods: {
    getUserData() {
      const userID = 1;
      Api.getUser(userID).then(response => (this.populatePage(response.data))).catch((error) => {

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
    setCurUser() {
      this.actAs = "curUser";
    }
  },
  mounted() {
    this.setCurUser();
  }
}
</script>

<style scoped>
#logo {
  max-height: 60px
}

.greenButton {
  background-color: #1EBA8C;
  border-color: #1EBA8C;
}

.greenButton:hover {
  background-color: transparent;
  color: #1EBA8C;
}

</style>
