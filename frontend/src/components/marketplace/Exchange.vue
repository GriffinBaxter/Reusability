<template>
  <div>
    <div class="container py-5">

      <h2 class="font-weight-bold mb-2">For Sale Items</h2>
      <p class="font-italic text-muted mb-4">Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt.</p>

      <!---------------------------------------------- ordering menu ------------------------------------------------>

      <div id="ordering-options-menu-container">
        <OrderingOptionsMenu></OrderingOptionsMenu>
      </div>

      <!------------------------------------------------ cards ------------------------------------------------------>

      <!-- First Row [Exchange Items]-->
      <div class="row pb-5 mb-4">
        <div class="col-lg-3 col-md-6 mb-4 mb-lg-0">
          <!-- Card-->
          <ExchangeCard></ExchangeCard>
        </div>

        <div class="col-lg-3 col-md-6 mb-4 mb-lg-0">
          <!-- Card-->
          <ExchangeCard></ExchangeCard>
        </div>

        <div class="col-lg-3 col-md-6 mb-4 mb-lg-0">
          <!-- Card-->
          <ExchangeCard></ExchangeCard>
        </div>

        <div class="col-lg-3 col-md-6 mb-4 mb-lg-0">
          <!-- Card-->
          <ExchangeCard></ExchangeCard>
        </div>
      </div>


    </div>
  </div>

</template>

<script>
import ExchangeCard from './ExchangeCard'
import OrderingOptionsMenu from "./OrderingOptionsMenu";

export default {
  name: "Exchange",
  components: {
    OrderingOptionsMenu,
    ExchangeCard,
  },
  methods: {

  }
}
</script>

<style scoped>

#ordering-options-menu-container {
  margin-top: 4%;
  margin-bottom: 4%;
}

</style>
