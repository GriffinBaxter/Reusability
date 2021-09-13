<template>
  <div>
    <div class="main">
      <div class="options">
        <div class="row m-2">
          <form>
            <div class="form-group" id="date-filtering-container">

              <div class="row">
                <div class="col" style="max-width: 60px">
                  <label for="start-date-input" class="p-2">Date </label>
                </div>
                <div class="col-xl-3 col-md-6">
                  <input type="date" class="form-control filter-input" id="start-date-input"
                         v-model="startDate">
                </div>
                <div class="col" style="max-width: 60px">
                  <label for="end-date-input" class="p-2"> to </label>
                </div>
                <div class="col-xl-3 col-md-6">
                  <input type="date" class="form-control filter-input" id="end-date-input"
                         v-model="endDate">
                </div>
              </div>

            </div>
          </form>
        </div>
      </div>
      <div>
        Barry's stuff
      </div>
     </div>

  </div>
</template>

<script>


export default {
  name: "SalesReport",
  components: {

  },
  data() {
    return {
      startDate: null,
      endDate: null,
    }
  }
}
</script>

<style scoped>

</style>
