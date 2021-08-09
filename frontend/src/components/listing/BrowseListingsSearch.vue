<template>
  <div class="row" id="search-bar-container">
    <div class="input-group" id="search-inputs" style="alignment: center">
      <input type="text" id="search-bar" ref="searchInput" class="form-control" @keydown="enterPressed($event)">
      <button class="btn green-search-button" id="search-button" @click="searchClicked()"><i class="fas fa-search" aria-hidden="true"></i></button>
    </div>
  </div>
</template>

<script>
export default {
  name: "BrowseListingsSearch",
  methods: {
    /**
     * When the enter key is pressed, the query is run with the search value.
     */
    enterPressed(event) {
      if (event.keyCode === 13) {
        // Enter pressed
        this.searchClicked()
      }
    },
    /**
     * Search button is clicked and query/filters for listings search are executed.
     */
    searchClicked() {
      const searchQuery = this.$refs.searchInput.value;
      const searchType = 'listingName';
      const orderBy = 'productNameASC';
      const page = 0;
      const businessType = null;
      const minimumPrice = null;
      const maximumPrice = null;
      const fromDate = null;
      const toDate = null;

      this.$router.push({ path: '/browseListings', query: {
        searchQuery: searchQuery, searchType: searchType,
          orderBy: orderBy, page: page, businessType: businessType,
          minimumPrice: minimumPrice, maximumPrice: maximumPrice,
          fromDate: fromDate, toDate: toDate
      }});
      this.$emit('requestListings', searchQuery);
    }
  }
}
</script>

<style scoped>

</style>
