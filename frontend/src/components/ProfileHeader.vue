<template>
    <div class="pt-3 text-font" id=" search-bar-container">
      <div class="row text-center">
        <div class="col search-bar-positioning">
          <div class="input-group">
            <input type="text" id="search-bar" ref="searchInput" class="form-control" @keydown="enterPressed($event)" placeholder="Search all users">
            <button class="btn green-search-button" @click="searchClicked()"><i class="fas fa-search"></i></button>
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

.search-bar-positioning {
  padding-top: 40px;
}

#search-bar:focus {
  outline: none;     /* oranges! yey */
  box-shadow: 0 0 2px 2px #2eda77; /* Full freedom. (works also with border-radius) */
  border: 1px solid #1EBABC;
}


</style>
