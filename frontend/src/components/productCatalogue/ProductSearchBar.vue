<template>
  <div>
    <div class="input-group" id="search-inputs" style="alignment: center">
      <input type="text" id="product-search-bar" ref="searchInput" class="form-control" @keydown="enterPressed($event)">
      <button class="btn green-search-button" id="search-button" @click="searchClicked()">
        <i class="fas fa-search" aria-hidden="true"/>
      </button>
    </div>


  </div>

</template>

<script>
export default {
  name: "ProductSearchBar",
  data() {
    return {

    }
  },
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
      // TODO: call search api
      const searchQuery = this.$refs.searchInput.value;
      console.log(searchQuery)
    }
  }
}
</script>

<style scoped>

</style>