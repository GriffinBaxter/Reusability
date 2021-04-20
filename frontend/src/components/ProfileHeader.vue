<!--This file creates the profile header for the Profile page.-->
<!--It contains the search bar for searching for users by first name.-->

<template>
    <div class="pt-3 text-font" id=" search-bar-container">
      <div class="row text-center">
        <div class="col search-bar-positioning">
          <div class="input-group">
            <input type="text" id="search-bar" ref="searchInput" class="form-control" @keydown="enterPressed($event)" placeholder="Search all users">
            <button class="btn green-search-button" @click="searchClicked()"><font-awesome-icon icon="search" ></font-awesome-icon></button>
          </div>
        </div>
      </div>
    </div>
</template>

<script>

export default {
  name: "ProfileHeader",
  methods: {

    /**
     * When the enter key is pressed, the query is run with the search value and the user is routed to the search page.
     */
    enterPressed(event) {
      if (event.keyCode === 13) {
        // Enter pressed
        const inputQuery = this.$refs.searchInput.value;
        this.$router.push({ path: '/search', query: { searchQuery: `${inputQuery}`, orderBy: `fullNameASC`, page: "1" }})
      }
    },

    /**
     * When the search button is clicked, the query is run with the search value and the user is routed to the search page.
     */
    searchClicked() {
      const inputQuery = this.$refs.searchInput.value;
      this.$router.push({ path: '/search', query: { searchQuery: `${inputQuery}`, orderBy: `fullNameASC`, page: "1"}})
    }
  }
}
</script>

<!------------------------------------------------ Search Bar Styling ------------------------------------------------->

<style scoped>

.search-bar-positioning {
  padding-top: 40px;
}

/*TODO consider putting in style.css*/
#search-bar:focus {
  outline: none;     /* oranges! yey */
  box-shadow: 0 0 2px 2px #2eda77; /* Full freedom. (works also with border-radius) */
  border: 1px solid #1EBABC;
}

</style>
