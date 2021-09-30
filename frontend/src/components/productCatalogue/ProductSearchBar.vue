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

    <div class="input-group" id="search-inputs">
      <input type="text" id="product-search-bar" ref="searchInput" class="form-control" @keydown="enterPressed($event)">
      <button class="btn green-search-button" id="product-search-button" style="border-radius: 10%" @click="searchClicked()">
        <i class="fas fa-search" aria-hidden="true"/>
      </button>
      <PageSize style="margin-left: 2.25rem" :page-sizes="pageSizes" :current-page-size="pageSize" v-on:selectedPageSize="updatePageSize"></PageSize>
    </div>

    <!---------------------------------------- barcode filtering menu ----------------------------------------->

    <br>
    <div class="row">
      <label class="d-inline-block my-3 text-center col-xl-2 col-l-6 col-md-6">Barcode (EAN or UPC)</label>
      <div class="d-inline-block p-2 text-center col-xl-4 col-l-2 col-md-6">
        <input type="number" class="form-control filter-input d-inline-block" id="barcode-input" v-model="barcode" @keydown="enterPressed($event)">
        <button type="button" id="scanner-modal-btn" class="btn green-button" style="margin-top: -5px" @click="showBarcodeScannerModal($event)">
          <i class="fas fa-camera" aria-hidden="true"></i>
        </button>
      </div>

      <div class="text-center col-xl-2 col-l-2 col-md-6">
        <button type="button" id="barcode-clear-btn" class="btn btn-md btn-outline-primary green-button m-2 d-inline-block w-30"
                @click="barcode = ''">
          Clear Barcode
        </button>
      </div>
    </div>

    <BarcodeScannerModal id="barcode-scanner-modal-parent" ref="barcodeScannerModal" @scannedBarcode="updateBarcode" modal-identifier="products-business"/>

    <!--------------------------------------------------------------------------------------------------------->

  </div>

</template>

<script>

import BarcodeScannerModal from "../BarcodeScannerModal";
import PageSize from "../../components/PageSize";

export default {
  name: "ProductSearchBar",
  components: {
    PageSize,
    BarcodeScannerModal
  },
  data() {
    return {
      barcode: null,
      pageSizes: ["5", "10", "15", "25"], // list of page sizes
      pageSize: this.$route.query["pageSize"] || "5"// default page size
    }
  },
  methods: {
    /**
     * Finds the selected checkBox button
     * @return The value of the selected radio button
     */
    getSelectedCheckbox() {
      let checkboxes = document.querySelectorAll("input[name='search-type-checkbox']");
      let values = [];

      for (const checkbox of checkboxes) {
        if (checkbox.checked) {
          values.push(checkbox.value);
        }
      }

      if (values.length === 0) {
          document.getElementById('checkbox-product-name').click();
          values.push("name");
      }
      return values
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
      const searchBarcode = this.barcode;
      const searchQuery = this.$refs.searchInput.value;
      const pageSize = this.pageSize;

      if (
          searchQuery !== this.$route.query.searchQuery ||
          searchBarcode !== this.$route.query.barcode ||
          pageSize !== this.$route.query.pageSize
      ) {
        this.$emit('search', checked, searchQuery, searchBarcode, pageSize);
      }


    },
    /**
     * Updates the barcode used in the product catalogue search.
     * @param barcode The new barcode to search by.
     */
    updateBarcode(barcode) {
      this.barcode = barcode;
    },

    /**
     * Shows the barcode scanner modal
     */
    showBarcodeScannerModal(event) {
      this.$refs.barcodeScannerModal.showModel(event);
    },

    /**
     * When a user selects a page size using the PageSize component then the current page size should be
     * updated and the results should be retrieved from the backend.
     * @param selectedPageSize the newly selected page size.
     */
    updatePageSize(selectedPageSize) {
      this.pageSize = selectedPageSize;
      this.searchClicked();
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
