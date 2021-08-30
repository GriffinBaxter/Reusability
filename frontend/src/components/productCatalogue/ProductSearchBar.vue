<template>
  <div style="margin-bottom: 20px">
    <div class="form-check form-check-inline">
      <input class="form-check-input" type="radio" name="search-type-radio" id="radio-product-name"
             value="PRODUCT_NAME" checked>
      <label class="form-check-label" for="radio-product-name">Product Name</label>
    </div>
    <div class="form-check form-check-inline">
      <input class="form-check-input" type="radio" name="search-type-radio" id="radio-product-id"
             value="PRODUCT_ID">
      <label class="form-check-label" for="radio-product-id">Product ID</label>
    </div>
    <div class="form-check form-check-inline">
      <input class="form-check-input" type="radio" name="search-type-radio" id="radio-manufacturer"
             value="MANUFACTURER">
      <label class="form-check-label" for="radio-manufacturer">Manufacturer</label>
    </div>
    <div class="form-check form-check-inline">
      <input class="form-check-input" type="radio" name="search-type-radio" id="radio-description"
             value="DESCRIPTION">
      <label class="form-check-label" for="radio-description">Description</label>
    </div>
    <div class="form-check form-check-inline">
      <input class="form-check-input" type="radio" name="search-type-radio" id="radio-all"
             value="ALL">
      <label class="form-check-label" for="radio-all">ALL</label>
    </div>

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
    return {}
  },
  methods: {
    /**
     * Finds the selected radio button
     * @return The value of the selected radio button
     */
    getSelectedRadio() {
      let radios = document.querySelectorAll("input[name='search-type-radio']");

      for (const radio of radios) {
        if (radio.checked) {
          return radio.value;
        }
      }
    },
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