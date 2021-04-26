<template>

  <div class="container p-5 mt-3">
    <div class="card text-center shadow-sm">

      <div class="col my-2">
        <h2 class="py-3">New Inventory Item</h2>

        <!--product id-->
        <div class="row py-2" align="left">
          <div class="col-1"/>
          <label class="col-4 align-baseline" for="product-id">Product ID: </label>
          <input class="col-6" id="product-id" name="product-id" tabindex="1" type="text" v-model="productId"
                 :class="xx" required>
          <div class="col-1"/>

          <div class="invalid-feedback">
          </div>
        </div>

        <!--quantity-->
        <div class="row py-2" align="left">
          <div class="col-1"/>
          <label class="col-4 align-baseline" for="quantity">Quantity: </label>
          <input class="col-6" id="quantity" name="quantity" tabindex="2" type="number" v-model="quantity"
                 :class="xx" required>
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
                 v-model="pricePerItem"
                 :class="xx" required>
          <div class="col-1"/>

          <div class="invalid-feedback">
            {{ pricePerItemErrorMsg }}
          </div>
        </div>

        <!--total price-->
        <div class="row py-2" align="left">
          <div class="col-1"/>
          <label class="col-4 align-baseline" for="total-price">Total Price: </label>
          <input class="col-6" id="total-price" name="total-price" tabindex="4" type="number" v-model="totalPrice"
                 :class="xx" required>
          <div class="col-1"/>

          <div class="invalid-feedback">
            {{ totalPriceErrorMsg }}
          </div>
        </div>

        <!--manufactured-->
        <div class="row py-2" align="left">
          <div class="col-1"/>
          <label class="col-4 align-baseline" for="manufactured">Manufactured: </label>
          <input class="col-6" id="manufactured" name="manufactured" tabindex="5" type="date" v-model="manufactured"
                 :class="xx" required>
          <div class="col-1"/>

          <div class="invalid-feedback">
            {{ manufacturedErrorMsg }}
          </div>
        </div>

        <!--sell by-->
        <div class="row py-2" align="left">
          <div class="col-1"/>
          <label class="col-4 align-baseline" for="sell-by">Sell By: </label>
          <input class="col-6" id="sell-by" name="sell-by" tabindex="6" type="date" v-model="sellBy"
                 :class="xx" required>
          <div class="col-1"/>

          <div class="invalid-feedback">
            {{ sellNyErrorMsg }}
          </div>
        </div>

        <!--best before-->
        <div class="row py-2" align="left">
          <div class="col-1"/>
          <label class="col-4 align-baseline" for="best-before">Best Before: </label>
          <input class="col-6" id="best-before" name="best-before" tabindex="7" type="date" v-model="bestBefore"
                 :class="xx" required>
          <div class="col-1"/>

          <div class="invalid-feedback">
            {{ bestBeforeErrorMsg }}
          </div>
        </div>

        <!--expires-->
        <div class="row py-2" align="left">
          <div class="col-1"/>
          <label class="col-4 align-baseline" for="expires">Expires: </label>
          <input class="col-6" id="expires" name="expires" tabindex="8" type="date" v-model="expires"
                 :class="xx" required>
          <div class="col-1"/>

          <div class="invalid-feedback">
            {{ expiresErrorMsg }}
          </div>
        </div>

        <!--buttons-->
        <div class="row py-5">
          <div class="col-6">
            <button type="button" class="btn btn-light">Cancel</button>
          </div>
          <div class="col-6">
            <button  type="button" class="btn btn-success">Confirm</button>
          </div>

        </div>


      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Modal',
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