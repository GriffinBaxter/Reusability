<template>
  <div class="container" :id="tableId">
    <!-- Headers -->
    <div class="row mb-3">
      <div class="col py-3 header-col col-hover rounded-3 me-2 text-center"
           :key="index"
           v-for="(header, index) in tableHeaders"
           @click="() => handleHeaderClick(index)"
           @keydown="event => handleHeaderKeyDown(event, index)">
        <!-- Header name -->
        <b>{{header}}</b>
        <!-- Header icon-->
        <i :id="`${tableId}-header-${header}-icon`"></i>
      </div>
    </div>

    <!-- Data rows -->
    <div>

    </div>

    <!-- Pagination navigation-->

  </div>
</template>

<script>
export default {
  name: "Table",
  props: {
    // Table ID must be unqiue within the page it is placed!
    // This is used to identify the icons per table.
    tableId: {
      type: String,
      required: true
    },

    // Must be a list of strings. This is used to generate the headers.
    tableHeaders: {
      type: Array,
      required: true,
      default() {return [];}
    },

    // Must be an array of all the data points!
    // These can be of any type.
    tableData: {
      type: Array,
      required: false,
      default() {return [];}
    }
  },
  data() {
    return {
      rows: [],
      dataIsReady: false
    }
  },
  methods: {
    /**
     * Emits an event of ORDER_BY_HEADER_INDEX. This tells the parent to order by a header
     * at the given index.
     * @param headerIndex The index of the header you want to order by.
     */
    handleHeaderClick(headerIndex) {
      this.$emit(this.eventTypes.ORDER_BY_HEADER_INDEX, headerIndex)
    },
    /**
     * Emits an event of ORDER_BY_HEADER_INDEX. This tells the parent to order by a header
     * at the given index. This however, only occurs if the event's keyCode is equal to the
     * ENTER keyCode.
     * @param event The key down event.
     * @param headerIndex The index of the header you want to order by.
     */
    handleHeaderKeyDown(event, headerIndex) {
      if (event.keyCode === 13) {
        this.$emit(this.eventTypes.ORDER_BY_HEADER_INDEX, headerIndex)
      }
    },
    /**
     *
     */
    buildRows() {
      // Required initialization.
      this.dataIsReady = false;
      this.rows = [];
      let row = [];

      // Prases the raw stream of tabke data and converts it into lists of rows.
      for (let i = 0; i < this.tableData; i++) {
        row.push(this.tableData[i]);

        // Once we added a divisable amount of data points by the number of headers onto the row array
        // and this isn't the first item (this is because 0 % NUMBER == 0). Then we can add it to the rows.
        if (i % this.tableHeaders.length === 0 && i > 0) {
          this.rows.push(row);
          row = [];
        }

        // This dictates the table is ready to load in new data.
        this.dataIsReady = false;

      }
    }
  },
  // All events must be in Kebab case, as Camel case does not work with Vue events!
  eventTypes: {
    ORDER_BY_HEADER_INDEX: 'order-by-header-index'
  }
}


</script>

<style scoped>

</style>