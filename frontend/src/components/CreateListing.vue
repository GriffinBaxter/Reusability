<template>
  <!-- Modal -->
  <div class="modal fade" id="listingCreationPopup" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1"
       aria-labelledby="creationPopupTitle" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">

        <!--title-->
        <div class="modal-header">
          <h2 class="text-center">New Listing</h2>
        </div>



        <!--modal body-->
        <div class="modal-body">
          <form id="ListingCreation" @submit.prevent>
            <!--Inventory Item Select-->
            <div class="row">
              <div class="col form-group py-1 px-3">
                <label for="inventoryId" class="form-control-label">Inventory ID*: </label>
                <select id="inventoryId" class="form-select mdb-select md-form" searchable="Search here.." tabindex="1" data-live-search="true">
                  <option value="" disabled selected>Select an Item</option>
                  <option v-for="item in inventoryItems" v-bind:key="item.id" :value="item.id">{{item.product.id}} x{{item.quantity}} (${{item.totalPrice}})</option>
                </select>
              </div>
            </div>
            <!--quantity-->
            <div class="row">
              <div class="col-sm-6 form-group py-1 px-3">
                <label for="quantity">Quantity*: </label>
                <input id="quantity" name="quantity" tabindex="2" type="number" v-model="quantity" min="0"
                       :class="toggleInvalidClass(quantityErrorMsg)" :maxlength="config.quantity.maxLength" required>
                <div class="invalid-feedback">
                  {{ quantityErrorMsg }}
                </div>
              </div>


              <!--Price-->
              <div class="col-sm-6 form-group py-1 px-3">
                <label for="price">Price: </label>
                <div class="input-group">
                  <div class="input-group-prepend">
                    <span class="input-group-text">$</span>
                  </div>
                  <input id="price" name="price" tabindex="3" type="number" step="0.01"
                         v-model="price"
                         min="0" :class="toggleInvalidClass(priceErrorMsg)"
                         :maxlength="config.price.maxLength">
                  <div class="invalid-feedback">
                    {{ priceErrorMsg }}
                  </div>
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
      const inventoryId = document.getElementById("inventoryId").value;
      console.log("Inventory Id:", inventoryId);
      if (inventoryId.length === 0) {
        console.log("Inventory Id must be selected")
      }
    },
    /**
     * Creates test data TEMP
     */
    testData() {
      const product = {id:"WATT-420-BEANS"};
      const item = {id:1, product:product, quantity:5, totalPrice:5.19};
      this.inventoryItems.push(item);
      const anotherProduct = {id:"FOOT-LETTUCE"};
      const anotherItem = {id:2, product:anotherProduct, quantity:3, totalPrice:2.59};
      this.inventoryItems.push(anotherItem);
      this.inventoryItems.push({id:7, product:anotherProduct, quantity:2});
      this.inventoryItems.push({id:234, product:product, quantity:3})
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