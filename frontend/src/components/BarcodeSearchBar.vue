<template>
  <div class="row">
    <label class="d-inline-block fs-6">Barcode (EAN or UPC)</label>
    <div class="d-inline-block center">
      <input type="number" class="form-control d-inline-block" v-model="barcode" style="width: 150px" @keydown="enterPressed($event)">
      <button type="button" class="btn green-button d-inline-block" style="width: 42px"
              @click="(event) => {
                  this.$refs.barcodeScannerModal.showModel(event);
                }">
        <i class="fas fa-camera" aria-hidden="true"></i>
      </button>
      <button type="button" class="btn green-button d-inline-block" style="width: 42px" @click="searchClicked()">
        <i class="fas fa-search" aria-hidden="true"></i>
      </button>
    </div>
    <BarcodeScannerModal ref="barcodeScannerModal" @scannedBarcode="updateBarcode"/>
  </div>
</template>

<script>
import BarcodeScannerModal from "../components/BarcodeScannerModal";

export default {
  name: "BarcodeSearchBar",
  components: {
    BarcodeScannerModal
  },
  data() {
    return {
      barcode: null
    }
  },
  methods: {
    /**
     * Sets the barcode to the input one
     */
    updateBarcode(barcode) {
      this.barcode = barcode;
    },

    /**
     * When the search button is clicked, an event is emitted with the barcode
     */
    searchClicked() {
      this.$emit('barcodeSearch', this.barcode);
    },

    /**
     * When the enter key is pressed, the searchClicked method is triggered
     */
    enterPressed(event) {
      if (event.keyCode === 13) {
        // Enter pressed
        this.searchClicked()
      }
    }
  }
}
</script>

<style scoped>

</style>