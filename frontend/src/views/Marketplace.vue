<template>

  <div>

    <!--Nav bar; displays either business account or individual account nav bar-->
    <Navbar></Navbar>

    <div id="marketplace-container">

      <OrderingOptionsMenu></OrderingOptionsMenu>

      <ul class="nav nav-tabs" id="marketplace-tabs" role="tablist">
        <li class="nav-item" role="presentation">
          <button class="nav-link active" id="for-sale-tab" data-bs-toggle="tab" data-bs-target="#for-sale" type="button" role="tab" aria-controls="for-sale" aria-selected="true">For Sale</button>
        </li>
        <li class="nav-item" role="presentation">
          <button class="nav-link" id="wanted-tab" data-bs-toggle="tab" data-bs-target="#wanted" type="button" role="tab" aria-controls="wanted" aria-selected="false">Wanted</button>
        </li>
        <li class="nav-item" role="presentation">
          <button class="nav-link" id="exchange-tab" data-bs-toggle="tab" data-bs-target="#exchange" type="button" role="tab" aria-controls="exchange" aria-selected="false">Exchange</button>
        </li>
      </ul>
      <div class="tab-content" id="marketplace-tabs-content">
        <div class="tab-pane fade show active" id="for-sale" role="tabpanel" aria-labelledby="for-sale-tab">
          <ForSale></ForSale>
        </div>
        <div class="tab-pane fade" id="wanted" role="tabpanel" aria-labelledby="wanted-tab">
          <Wanted></Wanted>
        </div>
        <div class="tab-pane fade" id="exchange" role="tabpanel" aria-labelledby="exchange-tab">
          <Exchange></Exchange>
        </div>
      </div>

    </div>

    <!--Footer contains links that are the same as those in the nav bar-->
    <Footer></Footer>

  </div>

</template>

<script>

import Footer from '../components/main/Footer';
import Navbar from '../components/main/Navbar';
import ForSale from '../components/marketplace/ForSale';
import Wanted from '../components/marketplace/Wanted';
import Exchange from '../components/marketplace/Exchange';
import OrderingOptionsMenu from "../components/marketplace/OrderingOptionsMenu";

export default {
  name: "Marketplace",
  components: {
    OrderingOptionsMenu,
    Footer,
    Navbar,
    ForSale,
    Wanted,
    Exchange
  },
  methods: {

  },

  /**
   * When mounted, if cookie is not present, redirect to login page.
   */
  mounted() {

  },
}
</script>

<style scoped>

#marketplace-container {
  margin: 4% 10% 10% 10%;
}

#for-sale-tab #wanted-tab #exchange-tab {
  font-family: 'Roboto', sans-serif;

}

/*!*TODO: FIX THIS*!*/
/*#marketplace-tabs.nav.nav-tabs:active {*/
/*  border-bottom: 8px solid mediumseagreen;*/
/*}*/

</style>

