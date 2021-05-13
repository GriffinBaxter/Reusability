<template>
  <div>
      <div class="container py-5">

        <!-- First Row [For Sale Items]-->
        <h2 class="font-weight-bold mb-2">For Sale Items</h2>
        <p class="font-italic text-muted mb-4">Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt.</p>

        <div class="row pb-5 mb-4">
          <div class="col-lg-3 col-md-6 mb-4 mb-lg-0">
            <!-- Card-->
            <ForSaleCard></ForSaleCard>
          </div>

          <div class="col-lg-3 col-md-6 mb-4 mb-lg-0">
            <!-- Card-->
            <ForSaleCard></ForSaleCard>
          </div>

          <div class="col-lg-3 col-md-6 mb-4 mb-lg-0">
            <!-- Card-->
            <ForSaleCard></ForSaleCard>
          </div>

          <div class="col-lg-3 col-md-6 mb-4 mb-lg-0">
            <!-- Card-->
            <ForSaleCard></ForSaleCard>
          </div>
        </div>

        <!-- Second Row [For Sale Items]-->

        <div class="row pb-5 mb-4">
          <div class="col-lg-3 col-md-6 mb-4 mb-lg-0">
            <!-- Card-->
            <ForSaleCard></ForSaleCard>

          </div>

          <div class="col-lg-3 col-md-6 mb-4 mb-lg-0">
            <!-- Card-->
            <ForSaleCard></ForSaleCard>
          </div>

          <div class="col-lg-3 col-md-6 mb-4 mb-lg-0">
            <!-- Card-->
            <ForSaleCard></ForSaleCard>
          </div>

          <div class="col-lg-3 col-md-6 mb-4 mb-lg-0">
            <!-- Card-->
            <ForSaleCard></ForSaleCard>
          </div>
        </div>


      </div>
    </div>
</template>

<script>
import ForSaleCard from './ForSaleCard'

export default {
  name: "ForSale",
  components: {
    ForSaleCard,
  },
  methods: {

  }
}
</script>

<style scoped>

</style>
