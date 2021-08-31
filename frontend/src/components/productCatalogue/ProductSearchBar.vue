<template>
  <div style="margin-bottom: 20px">
    <div class="form-check form-check-inline">
      <input class="form-check-input" type="checkbox" name="search-type-checkbox" id="checkbox-product-name"
             value="PRODUCT_NAME" checked>
      <label class="form-check-label" for="checkbox-product-name">Product Name</label>
    </div>
    <div class="form-check form-check-inline">
      <input class="form-check-input" type="checkbox" name="search-type-checkbox" id="checkbox-product-id"
             value="PRODUCT_ID">
      <label class="form-check-label" for="checkbox-product-id">Product ID</label>
    </div>
    <div class="form-check form-check-inline">
      <input class="form-check-input" type="checkbox" name="search-type-checkbox" id="checkbox-manufacturer"
             value="MANUFACTURER">
      <label class="form-check-label" for="checkbox-manufacturer">Manufacturer</label>
    </div>
    <div class="form-check form-check-inline">
      <input class="form-check-input" type="checkbox" name="search-type-checkbox" id="checkbox-description"
             value="DESCRIPTION">
      <label class="form-check-label" for="checkbox-description">Description</label>
    </div>

    <div class="input-group" id="search-inputs" style="alignment: center">
      <input type="text" id="product-search-bar" ref="searchInput" class="form-control" @keydown="enterPressed($event)">
      <button class="btn green-search-button" id="product-search-button" @click="searchClicked()">
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
    getSelectedCheckbox() {
      let checkboxes = document.querySelectorAll("input[name='search-type-checkbox']");
      let value = [];

      for (const checkbox of checkboxes) {
        if (checkbox.checked) {
          value.push(checkbox.value);
        }
      }
      if (value.length === 0){
        document.getElementById("checkbox-product-name").click();
        value.push("PRODUCT_NAME");
      }
      return value
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
      this.getSelectedCheckbox()
      const searchQuery = this.$refs.searchInput.value;
      console.log(searchQuery)
    }
  }
}
</script>

<style scoped>

</style>