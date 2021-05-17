<template>

  <div id="ordering-options-menu">

    <div id="order-by-option-menu">

      <ul class="dropdown-menu gap-2" aria-labelledby="btnGroupDrop1">
        <!--order by title-->
        <button type="button" class="btn green-button-transparent col-12"
                @click="orderListings(true, false)">
          Title
          <i id="titleIcon"></i>
        </button>

        <!--order by date-->
        <button type="button" class="btn green-button-transparent col-12"
                @click="orderListings(false, true)">
          Date
          <i id="dateIcon"></i>
        </button>
      </ul>

    </div>

    <div id="order-direction-option-menu">

      <ul class="dropdown-menu gap-2" aria-labelledby="btnGroupDrop2">
        <!--order by ascending-->
        <button type="button" class="btn green-button-transparent col-12"
                @click="orderListings(true, false)">
          Ascending
          <i id="descendingIcon"></i>
        </button>

        <!--order by date-->
        <button type="button" class="btn green-button-transparent col-12"
                @click="orderListings(false, true)">
          Descending
          <i id="descending-icon"></i>
        </button>
      </ul>

    </div>


  </div>

</template>

<script>
export default {
  name: "OrderingOptionsMenu",
  data() {
    orderBy: "",
    quantityAscending: false,
    priceAscending: false,
    closesAscending: false,
    createdAscending: false,
  }
}
</script>

<style scoped>

</style>