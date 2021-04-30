<template>
  <!-- Modal -->
  <div class="modal fade" id="listingCreationPopup" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1"
       aria-labelledby="creationPopupTitle" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">

        <!--title-->
        <div class="modal-header">
          <h2 align="central" id="creationPopupTitle">New Listing Item</h2>
        </div>



        <!--modal body-->
        <div class="modal-body">
          <form id="ListingCreation" @submit.prevent>
            <!--Inventory Item Select-->
            <div class="row">
              <div class="col form-group py-1 px-3">
                <label for="" class="form-control-label">Inventory ID*: </label>
                <select class="form-control selectpicker" id="selectInventoryItem" placeholder="" data-live-search="true">
                  <option v-for="item in inventoryItems" v-bind:key="item.id" >{{item.product.id}} x{{item.quantity}}</option>
                </select>
              </div>
            </div>
            <!--quantity-->
            <div class="row">
              <div class="col-6 form-group py-1 px-3">
                <label for="quantity">Quantity*: </label>
                <input id="quantity" name="quantity" tabindex="2" type="number" v-model="quantity" min="0"
                       :class="toggleInvalidClass(quantityErrorMsg)" :maxlength="config.quantity.maxLength" required>
                <div class="invalid-feedback">
                  {{ quantityErrorMsg }}
                </div>
              </div>


              <!--Price-->
              <div class="col-6 form-group py-1 px-3">
                <label for="price">Price: </label>
                <input id="price" name="price" tabindex="3" type="number" step="0.01"
                       v-model="price"
                       min="0" :class="toggleInvalidClass(priceErrorMsg)"
                       :maxlength="config.price.maxLength">
                <div class="invalid-feedback">
                  {{ priceErrorMsg }}
                </div>
              </div>
            </div>

            <!--More-Info-->
            <div class="row form-group py-1 px-3">
              <label for="more-info">More info: </label>
              <input id="more-info" name="more-info" tabindex="4" type="text" step="0.01" v-model="moreInfo"
                     min="0" :class="toggleInvalidClass(moreInfoErrorMsg)" :maxlength="config.moreInfo.maxLength">
              <div class="invalid-feedback">
                {{ moreInfoErrorMsg }}
              </div>
            </div>

            <!--Close Date-->
            <div class="row form-group py-1 px-3">
              <label for="closes">Close Date: </label>
              <input id="closes" name="closes" tabindex="5" type="date" v-model="closes"
                     :class="toggleInvalidClass(closesErrorMsg)">
              <div class="invalid-feedback">
                {{ closesErrorMsg }}
              </div>
            </div>


          </form>
        </div>


        <!--footer-->
        <div class="modal-footer">
          <button type="button" class="btn btn-light" @click="dataReset()" data-bs-dismiss="modal">Cancel</button>
          <button type="button" class="btn btn-success" @click="createNewInventoryItem()">Confirm</button>
        </div>

      </div>
    </div>
  </div>

</template>

<script>
import {Listing} from "@/Api";

export default {
  name: "CreateListing",
  props: {
    businessId: {
      type: Number,
      required: true
    }
  },
  data() {
    return {
      config: Listing.config,
      inventoryItems: [],

      // Inventory Id related variables
      inventoryId: "",
      inventoryIdErrorMsg: "",
      // Quantity related variables
      quantity: "",
      quantityErrorMsg: "",
      // Price related variables
      price: "",
      priceErrorMsg: "",
      // MoreInfo related variables
      moreInfo: "",
      moreInfoErrorMsg: "",
      // Closes related variables
      closes: "",
      closesErrorMsg: "",
    }
  },
  methods: {
    /**
     * This method toggles the appearance of the error message, where the is-invalid class is added to the messages
     * if an error message needs to be presented to the user.
     *
     * @param errorMessage, string, the error message relating to invalid input of a field.
     * @returns {[string]}, classList, a list containing the classes for an invalid message.
     */
    toggleInvalidClass(errorMessage) {
      let classList = ['form-control']
      if (errorMessage) {
        classList.push('is-invalid')
      }
      return classList
    },
    /**
     * Resets data on the page
     */
    dataReset() {
      this.inventoryId = null;
      this.inventoryIdErrorMsg = null;
      this.quantity = null;
      this.quantityErrorMsg = null;
      this.price = null;
      this.priceErrorMsg = null;
      this.moreInfo = null;
      this.moreInfoErrorMsg = null;
      this.closes = null;
      this.closesErrorMsg = null;
    },
    /**
     * Creates the new Inventory Item
     */
    createNewInventoryItem() { // TODO

    },
    /**
     * Creates test data TEMP
     */
    testData() {
      const product = {id:"WATT-420-BEANS"};
      const item = {id:1, product:product, quantity:5};
      this.inventoryItems.push(item);
      const anotherProduct = {id:"FOOT-LETTUCE"};
      const anotherItem = {id:2, product:anotherProduct, quantity:3};
      this.inventoryItems.push(anotherItem);
    }
  },
  mounted() {
    // Adds test data
    this.testData();
  }
}
</script>

<style scoped>

</style>