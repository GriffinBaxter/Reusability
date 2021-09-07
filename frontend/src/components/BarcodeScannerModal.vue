<template>

  <!-- Modal -->
  <div class="modal fade" ref="_barcodeScannerModal" tabindex="-1" aria-labelledby="barcodeScannerModal"
       aria-hidden="true" id="barcode-scanner-modal">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="barcodeScannerModalTitle">Barcode Scanner</h5>
        </div>
        <div class="modal-body">

          <p style="color: red">
            {{ barcodeErrorMsg }}
          </p>
          
          <button id="modal-scan-by-uploading-image-button" class="btn green-button-transparent"
                  @click="onUploadClick">
            Scan by uploading image
          </button>
          <input type="file" id="imageUpload" ref="image" @change="getBarcodeStatic"
                 name="img" accept="image/png, image/gif, image/jpeg">
          <br><br>
          <button id="modal-scan-using-camera-button" v-if="liveStreamAvailable && !liveStreaming"
                  class="btn green-button-transparent" @click="getBarcodeLiveStream">
            Scan Using Camera
          </button>
          <button id="modal-stop-scanning-button" v-if="liveStreaming && !barcodeFound"
                  class="btn green-button-transparent"
                  @click="liveStreaming = false; removeCameraError();">
            Stop Scanning (Barcode Not Found)
          </button>
          <button id="modal-save-scanned-barcode-button" v-if="liveStreaming && barcodeFound"
                  class="btn green-button" @click="liveStreaming = false">
            Save Scanned Barcode
          </button>
          <br>
          <div v-if="liveStreaming"><br></div>
          <div id="scannerModalLiveStreamCamera"></div>
        </div>
        
        <div class="modal-footer">
          <button type="button" class="btn btn-outline-primary order-0 green-button-transparent"
                  data-bs-dismiss="modal">
            Close
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {Modal} from "bootstrap";
import {getBarcodeLiveStream, getBarcodeStatic} from "../barcodeUtils";

export default {
  name: "BarcodeScannerModal",
  data() {
    return {
      modal: null,

      barcode: "",
      barcodeErrorMsg: "",
      liveStreamAvailable: false,
      liveStreaming: false,
      barcodeFound: false,

      hasBeenShown: false
    }
  },
  methods: {

    /**
     * Shows the barcode scanner modal.
     */
    showModel(event) {

      // Prevent any default actions
      event.preventDefault();

      this.barcode = "";
      this.barcodeErrorMsg = "";

      // Show the modal
      this.modal.show();

      this.hasBeenShown = true;
    },

    /**
     *  Clicks the image.
     */
    onUploadClick() {
      this.$refs.image.click();
    },

    /**
     * Retrieves the barcode number (EAN or UPC) from an uploaded image.
     */
    getBarcodeStatic() {
      const outerThis = this;
      this.barcodeErrorMsg = "";
      getBarcodeStatic(this, function () {
        if (outerThis.barcodeErrorMsg === "" && outerThis.barcode !== "") {
          outerThis.$emit('scannedBarcode', outerThis.barcode);
          outerThis.modal.hide();
        }
      });
    },

    /**
     * Retrieves the barcode number (EAN or UPC) from a live camera feed, based on the most commonly occurring barcode
     * per each frame scan.
     */
    getBarcodeLiveStream() {
      const outerThis = this;
      this.barcodeErrorMsg = "";
      getBarcodeLiveStream(this, 460, "#scannerModalLiveStreamCamera", function () {
        if (outerThis.barcodeErrorMsg === "" && outerThis.barcode !== "") {
          outerThis.$emit('scannedBarcode', outerThis.barcode);
          outerThis.modal.hide();
        }
      });
    },

    /**
     * Removes the camera error message after stopping the scanning.
     */
    removeCameraError() {
      document.querySelector('#scannerModalLiveStreamCamera').innerHTML = ""
    },
  },

  mounted() {
    this.liveStreamAvailable = navigator.mediaDevices && typeof navigator.mediaDevices.getUserMedia === 'function';

    // Create a modal and attach it to the barcodeScannerModal reference.
    this.modal = new Modal(this.$refs._barcodeScannerModal);
  }
}
</script>

<style scoped>

.modal-body {
  text-align: center;
}

</style>
