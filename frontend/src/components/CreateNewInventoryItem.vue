<template>
  <!-- Modal -->
  <div class="modal fade" id="creationPopup" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1"
       aria-labelledby="creationPopupTitle" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">

        <!--title-->
        <div class="modal-header">
          <h2 align="central" id="creationPopupTitle">New Inventory Item</h2>
        </div>

        <!--modal body-->
        <div class="modal-body">
          <!--product id-->
          <div class="row py-2" align="left">
            <div class="col-1"/>
            <label class="col-4 align-baseline" for="product-id">Product ID: </label>
            <input class="col-6" id="product-id" name="product-id" tabindex="1" type="text" v-model="productId"
                   required>
            <div class="col-1"/>

            <div class="invalid-feedback">
            </div>
          </div>

          <!--quantity-->
          <div class="row py-2" align="left">
            <div class="col-1"/>
            <label class="col-4 align-baseline" for="quantity">Quantity: </label>
            <input class="col-6" id="quantity" name="quantity" tabindex="2" type="number" v-model="quantity"
                   required>
            <div class="col-1"/>

            <div class="invalid-feedback">
              {{ quantityErrorMsg }}
            </div>
          </div>

          <!--price per item-->
          <div class="row py-2" align="left">
            <div class="col-1"/>
            <label class="col-4 align-baseline" for="price-per-item">Price Per Item: </label>
            <input class="col-6" id="price-per-item" name="price-per-item" tabindex="3" type="number"
                   v-model="pricePerItem">
            <div class="col-1"/>

            <div class="invalid-feedback">
              {{ pricePerItemErrorMsg }}
            </div>
          </div>

          <!--total price-->
          <div class="row py-2" align="left">
            <div class="col-1"/>
            <label class="col-4 align-baseline" for="total-price">Total Price: </label>
            <input class="col-6" id="total-price" name="total-price" tabindex="4" type="number"
                   v-model="totalPrice">
            <div class="col-1"/>

            <div class="invalid-feedback">
              {{ totalPriceErrorMsg }}
            </div>
          </div>

          <!--manufactured-->
          <div class="row py-2" align="left">
            <div class="col-1"/>
            <label class="col-4 align-baseline" for="manufactured">Manufactured: </label>
            <input class="col-6" id="manufactured" name="manufactured" tabindex="5" type="date"
                   v-model="manufactured">
            <div class="col-1"/>

            <div class="invalid-feedback">
              {{ manufacturedErrorMsg }}
            </div>
          </div>

          <!--sell by-->
          <div class="row py-2" align="left">
            <div class="col-1"/>
            <label class="col-4 align-baseline" for="sell-by">Sell By: </label>
            <input class="col-6" id="sell-by" name="sell-by" tabindex="6" type="date" v-model="sellBy">
            <div class="col-1"/>

            <div class="invalid-feedback">
              {{ sellNyErrorMsg }}
            </div>
          </div>

          <!--best before-->
          <div class="row py-2" align="left">
            <div class="col-1"/>
            <label class="col-4 align-baseline" for="best-before">Best Before: </label>
            <input class="col-6" id="best-before" name="best-before" tabindex="7" type="date" v-model="bestBefore">
            <div class="col-1"/>

            <div class="invalid-feedback">
              {{ bestBeforeErrorMsg }}
            </div>
          </div>

          <!--expires-->
          <div class="row py-2" align="left">
            <div class="col-1"/>
            <label class="col-4 align-baseline" for="expires">Expires: </label>
            <input class="col-6" id="expires" name="expires" tabindex="8" type="date" v-model="expires" required>
            <div class="col-1"/>

            <div class="invalid-feedback">
              {{ expiresErrorMsg }}
            </div>
          </div>

        </div>

        <!--footer-->
        <div class="modal-footer">
          <button type="button" class="btn btn-light" data-bs-dismiss="modal">Cancel</button>
          <button type="button" class="btn btn-success">Confirm</button>
        </div>

      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'InventoryItemCreation',
  data() {
    return {
      // product Id related variables
      productId: "",
      productIdErrorMsg: "",

      // quantity related variables
      quantity: "",
      quantityErrorMsg: "",

      // price per item related variables
      pricePerItem: "",
      pricePerItemErrorMsg: "",

      // total price related variables
      totalPrice: "",
      totalPriceErrorMsg: "",

      // manufactured related variables
      manufactured: "",
      manufacturedErrorMsg: "",

      // sell by related variables
      sellBy: "",
      sellNyErrorMsg: "",

      // best Before Id related variables
      bestBefore: "",
      bestBeforeErrorMsg: "",

      // expires related variables
      expires: "",
      expiresErrorMsg: ""
    }
  },
  methods: {
    close() {
      this.$emit('close');
    },
  },
};
</script>

<style>
</style>