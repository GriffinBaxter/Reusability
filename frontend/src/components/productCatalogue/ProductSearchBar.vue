<template>
  <div id="search-container" style="margin-bottom: 20px">
    <div class="form-check form-check-inline">
      <input class="form-check-input" type="checkbox" name="search-type-checkbox" id="checkbox-product-name"
             value="name" checked>
      <label class="form-check-label" for="checkbox-product-name">Product Name</label>
    </div>
    <div class="form-check form-check-inline">
      <input class="form-check-input" type="checkbox" name="search-type-checkbox" id="checkbox-product-id"
             value="id">
      <label class="form-check-label" for="checkbox-product-id">Product ID</label>
    </div>
    <div class="form-check form-check-inline">
      <input class="form-check-input" type="checkbox" name="search-type-checkbox" id="checkbox-manufacturer"
             value="manufacturer">
      <label class="form-check-label" for="checkbox-manufacturer">Manufacturer</label>
    </div>
    <div class="form-check form-check-inline">
      <input class="form-check-input" type="checkbox" name="search-type-checkbox" id="checkbox-description"
             value="description">
      <label class="form-check-label" for="checkbox-description">Description</label>
    </div>

    <div class="input-group" id="search-inputs" style="alignment: center">
      <input type="text" id="product-search-bar" ref="searchInput" class="form-control" @keydown="enterPressed($event)">
      <button class="btn green-search-button" id="product-search-button" @click="searchClicked()">
        <i class="fas fa-search" aria-hidden="true"/>
      </button>
    </div>

    <!---------------------------------------- barcode filtering menu ----------------------------------------->

    <br>
    <div class="row">
      <label class="d-inline-block my-3 text-center col-xl-2 col-l-6 col-md-6">Barcode (EAN or UPC)</label>
      <div class="d-inline-block p-2 text-center col-xl-4 col-l-2 col-md-6">
        <input type="number" class="form-control filter-input d-inline-block" id="barcode-input" v-model="barcode">
        <button type="button" class="btn green-button" style="margin-top: -5px" @click="(event) => {
                  this.$refs.barcodeScannerModal.showModel(event);
                }">
          <i class="fas fa-camera" aria-hidden="true"></i>
        </button>
      </div>

      <div class="text-center col-xl-2 col-l-2 col-md-6">
        <button type="button" class="btn btn-md btn-outline-primary green-button m-2 d-inline-block w-30"
                @click="barcode = ''">
          Clear Barcode
        </button>
      </div>
    </div>

    <!--------------------------------------------------------------------------------------------------------->

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
     * Finds the selected checkBox button
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
        value.push("name");
      }
      return value
    },
    /**
     * When the enter key is pressed, the query is run with the search value.
     */
    enterPressed(event) {
      if (event.keyCode === 13) {
        // Enter pressed
        this.searchClicked();
      }
    },
    /**
     * Search button is clicked and query/filters for product search are executed.
     */
    searchClicked() {
      const checked = this.getSelectedCheckbox();
      const searchQuery = this.$refs.searchInput.value;
      this.$emit('search', checked, searchQuery);
    }
  }
}
</script>

<style scoped>

#search-container {
  background-color: #f1f4f5;
  border-radius: 20px;
  padding: 30px 6%;
  margin-top: 20px;
}

#barcode-input {
  max-width: 260px;
}
</style>