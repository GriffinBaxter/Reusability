<template>
    <div class="container pt-3" id="searchContainer">
      <div class="row text-center">
        <div class="col-md-2 mb-md-0 mb-3">
          <img id="logo" src="../../public/logo_only_med.png" class="img-fluid" alt="logo">
        </div>
        <div class="col">
          <div class="input-group">
            <input type="text" id="searchBar" ref="searchInput" class="form-control" @keydown="enterPressed($event)" placeholder="Search all users">
            <button class="btn btn-primary green-search-button" @click="searchClicked()"><i class="fas fa-search"></i></button>
          </div>
        </div>
      </div>
    </div>
</template>

<script>

export default {
  name: "ProfileHeader",
  methods: {
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
    }
  }
}
</script>

<style scoped>
#logo {
  max-height: 60px
}



</style>
