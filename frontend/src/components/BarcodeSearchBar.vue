<template>
  <div class="row">
    <label class="d-inline-block fs-6">Barcode (EAN or UPC)</label>
    <div class="d-inline-block">
      <input class="form-control d-inline-block" v-model="barcode" style="width: 150px; float: left;" @keydown="enterPressed($event)" maxlength="13">
      <button type="button" class="btn green-button "
              @click="(event) => {
                  this.$refs.barcodeScannerModal.showModel(event);
                }">
        <i class="fas fa-camera" aria-hidden="true"></i>
      </button>
      <button type="button" class="btn green-button " @click="searchClicked()">
        <i class="fas fa-search" aria-hidden="true"></i>
      </button>
      <PageSize></PageSize>
    </div>
    <BarcodeScannerModal ref="barcodeScannerModal" @scannedBarcode="updateBarcode"/>
  </div>
</template>

<script>
import BarcodeScannerModal from "../components/BarcodeScannerModal";
import PageSize  from "@/components/PageSize";

export default {
  name: "BarcodeSearchBar",
  components: {
    BarcodeScannerModal,
    PageSize
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
