<template>
  <!-- Modal -->
  <div class="modal fade" ref="_updateInventoryItemModal" id="updateInventoryItemModal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1"
       aria-labelledby="updateInventoryItemModalTitle" aria-hidden="false">
    <div class="modal-dialog">
      <div class="modal-content">

        <!-- Title -->
        <div class="modal-header">
          <h2 id="updateInventoryItemModalTitle">Update Inventory Item</h2>
        </div>

        <!-- Modal Body -->
        <div class="modal-body">
          <form class="row" id="inventoryItemUpdate" @submit.prevent>

            <!-- Product ID -->
            <div class="col-7 form-group py-1 px-3">
              <label for="product-id">Product ID*: </label>
              <input id="product-id" name="product-id" tabindex="1" type="text"
                     v-model="newInventoryItem.data.productId" :class="toggleInvalidClass(productIdErrorMsg)"
                     :maxlength="config.productId.maxLength" required>
              <div class="invalid-feedback">
                {{ productIdErrorMsg }}
              </div>
            </div>

            <!-- Quantity -->
            <div class="col-5 form-group py-1 px-3">
              <label for="quantity">Quantity*: </label>
              <input id="quantity" name="quantity" tabindex="2" type="number" min="0"
                     v-model="newInventoryItem.data.quantity" :class="toggleInvalidClass(quantityErrorMsg)"
                     :maxlength="config.quantity.maxLength" required>
              <div class="invalid-feedback">
                {{ quantityErrorMsg }}
              </div>
            </div>


            <!-- Price Per Item -->
            <div class="col-6 form-group py-1 px-3">
              <label for="price-per-item">Price Per Item: </label>
              <input id="price-per-item" name="price-per-item" tabindex="3" type="number" step="0.01"
                     v-model="newInventoryItem.data.pricePerItem" :class="toggleInvalidClass(pricePerItemErrorMsg)"
                     min="0" :maxlength="config.pricePerItem.maxLength">
              <div class="invalid-feedback">
                {{ pricePerItemErrorMsg }}
              </div>
            </div>

            <!-- Total Price -->
            <div class="col-6 form-group py-1 px-3">
              <label for="total-price">Total Price: </label>
              <input id="total-price" name="total-price" tabindex="4" type="number" step="0.01"
                     v-model="newInventoryItem.data.totalPrice" :class="toggleInvalidClass(totalPriceErrorMsg)"
                     :maxlength="config.totalPrice.maxLength">
              <div class="invalid-feedback">
                {{ totalPriceErrorMsg }}
              </div>
            </div>

            <!-- Manufactured -->
            <div class="col-12 form-group py-1 px-3">
              <label for="manufactured">Manufactured: </label>
              <input id="manufactured" name="manufactured" tabindex="5" type="date"
                     v-model="newInventoryItem.data.manufactured" :class="toggleInvalidClass(manufacturedErrorMsg)">
              <div class="invalid-feedback">
                {{ manufacturedErrorMsg }}
              </div>
            </div>

            <!-- Sell By -->
            <div class="col-12 form-group py-1 px-3">
              <label for="sell-by">Sell By: </label>
              <input id="sell-by" name="sell-by" tabindex="6" type="date"
                     v-model="newInventoryItem.data.sellBy" :class="toggleInvalidClass(sellByErrorMsg)">
              <div class="invalid-feedback">
                {{ sellByErrorMsg }}
              </div>
            </div>

            <!-- Best Before -->
            <div class="col-12 form-group py-1 px-3">
              <label for="best-before">Best Before: </label>
              <input id="best-before" name="best-before" tabindex="7" type="date"
                     v-model="newInventoryItem.data.bestBefore" :class="toggleInvalidClass(bestBeforeErrorMsg)">
              <div class="invalid-feedback">
                {{ bestBeforeErrorMsg }}
              </div>
            </div>

            <!-- Expires -->
            <div class="col-12 form-group py-1 px-3">
              <label for="expires">Expires*: </label>
              <input class="col-6" id="expires" name="expires" tabindex="8" type="date"
                     v-model="newInventoryItem.data.expires" :class="toggleInvalidClass(expiresErrorMsg)"
                     required>
              <div class="invalid-feedback">
                {{ expiresErrorMsg }}
              </div>
            </div>

          </form>
        </div>


        <!--footer-->
        <div class="modal-footer justify-content-between">
          <button type="button" class="btn green-button-transparent" data-bs-dismiss="modal">Cancel</button>
          <button type="button" class="btn green-button">Save changes</button>
        </div>

      </div>
    </div>
  </div>
</template>

<script>
import { Modal } from 'bootstrap'
import InventoryItem from "@/configs/InventoryItem";

export default {
  name: "UpdateInventoryItemModal",
  props: {
    value: {
      type: Object,
      required: false
    },
    businessId: {
      type: Number,
      required: true
    }
  },
  data() {
    return {
      // Used to display a modal that contains the form to edit the product
      modal: null,
      // This defines the error message regarding different responses from the request to Axios
      formErrorModalMessage: "",

      // The config for checking the inventory item parameters when updated
      config: InventoryItem.config,

      // The new inventory item
      newInventoryItem: new InventoryItem(
          {
            productId: "PROD",
            quantity: 0,
            pricePerItem: 0,
            totalPrice: 0,
            manufactured: "2021-04-05",
            sellBy: "2021-05-18",
            bestBefore: "2021-05-20",
            expires: "2021-05-22"
          }
      ),

      // Error messages
      productIdErrorMsg: "",
      quantityErrorMsg: "",
      pricePerItemErrorMsg: "",
      totalPriceErrorMsg: "",
      manufacturedErrorMsg: "",
      sellByErrorMsg: "",
      bestBeforeErrorMsg: "",
      expiresErrorMsg: "",

      // Keeps track of if there is an error in the form
      inputError: false
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
      if (errorMessage !== "") {
        classList.push('is-invalid')
      }
      return classList
    },

    /**
     * Updates the placeholder values before showing the modal.
     *
     */
    showModal() {
      console.log(this.value);
      console.log(this.businessId);

      // If the modal is already showing prevent the placeholders from being updated.
      if (!this.$refs._updateInventoryItemModal.classList.contains("show")) {
        // Update the placeholders
        this.newInventoryItem.data.productId = this.value.productId;
        this.newInventoryItem.data.quantity = this.value.quantity;
        this.newInventoryItem.data.pricePerItem = this.value.pricePerItem;
        this.newInventoryItem.data.totalPrice = this.value.totalPrice;
        this.newInventoryItem.data.manufactured = this.value.manufactured;
        this.newInventoryItem.data.sellBy = this.value.sellBy;
        this.newInventoryItem.data.bestBefore = this.value.bestBefore;
        this.newInventoryItem.data.expires = this.value.expires;

        // Reset all the error messages
        this.productIdErrorMsg = "";
        this.quantityErrorMsg = "";
        this.pricePerItemErrorMsg = "";
        this.totalPriceErrorMsg = "";
        this.manufacturedErrorMsg = "";
        this.sellByErrorMsg = "";
        this.bestBeforeErrorMsg = "";
        this.expiresErrorMsg = "";
      }

      // Show the modal
      this.modal.show();
    },
  },
  mounted() {
    // Create a modal and attach it to the updateInventoryItemModal reference.
    this.modal = new Modal(this.$refs._updateInventoryItemModal);
  }
}
</script>

<style scoped>

input:focus, textarea:focus, button:focus{
  outline: none;
  box-shadow: 0 0 2px 2px #1EBA8C; /* Full freedom. (works also with border-radius) */
  border: 1px solid #1EBABC;
}
</style>