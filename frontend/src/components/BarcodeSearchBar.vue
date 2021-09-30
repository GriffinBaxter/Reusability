<template>
  <div class="row">
    <label class="d-inline-block fs-6">Barcode (EAN or UPC)</label>
    <div>
      <input class="form-control" v-model="barcode" style="width: 150px; float: left" @keydown="enterPressed($event)" maxlength="13">
      <button type="button" class="btn green-button"
              @click="(event) => {
                  this.$refs.barcodeScannerModal.showModel(event);
                }">
        <i class="fas fa-camera" aria-hidden="true"></i>
      </button>
      <button type="button" class="btn green-button" @click="searchClicked()">
        <i class="fas fa-search" aria-hidden="true"></i>
      </button>
    </div>
    <BarcodeScannerModal ref="barcodeScannerModal" @scannedBarcode="updateBarcode" :modal-identifier="this.searchBarIdentifier"/>
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
  props: {
    searchBarIdentifier: {
      type: String,
      required: true
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
