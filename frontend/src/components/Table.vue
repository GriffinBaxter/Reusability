<template>
  <div class="container" :id="tableId">
    <!-- Table data Headers-->
    <div class="row mb-3">
      <div :class="`col py-3 header-col col-hover rounded-3 text-center ${headerIndex > 0 ? 'ms-2' : ''}`"
           :key="headerIndex" v-for="(header, headerIndex) in tableHeaders"
           @click="() => handleHeaderClick(headerIndex)" @keydown="event => handleHeaderKeyDown(event, headerIndex)" style="cursor: pointer; user-select: none; -moz-user-select: none; -ms-user-select: none; -webkit-user-select: none;">
        <!-- Header name -->
        <div class="fw-bold" v-if="orderBy === headerIndex">
          <span v-html="header"></span>
          <!-- Header icon-->

          <!-- Seems to be an issue with the icons from Font-Awesome and Boostrap with the shevron so I grabbed the direct SVG definition from Boostrap. -->
          <svg :id="`${tableId}-header-${headerIndex}-icon`" v-if="isAscending"
               xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-chevron-up" viewBox="0 0 16 16">
            <path fill-rule="evenodd" d="M7.646 4.646a.5.5 0 0 1 .708 0l6 6a.5.5 0 0 1-.708.708L8 5.707l-5.646 5.647a.5.5 0 0 1-.708-.708l6-6z"/>
          </svg>

          <!-- Seems to be an issue with the icons from Font-Awesome and Boostrap with the shevron so I grabbed the direct SVG definition from Boostrap. -->
          <svg :id="`${tableId}-header-${headerIndex}-icon`" v-else
               xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-chevron-down" viewBox="0 0 16 16">
            <path fill-rule="evenodd" d="M1.646 4.646a.5.5 0 0 1 .708 0L8 10.293l5.646-5.647a.5.5 0 0 1 .708.708l-6 6a.5.5 0 0 1-.708 0l-6-6a.5.5 0 0 1 0-.708z"/>
          </svg>
        </div>
        <div v-html="header" class="fw-bold" v-else></div>
      </div>
    </div>

    <!-- Table data rows -->
    <div v-if="dataIsReady">
        <!-- The @click and @keydown are used to trigger events for the parent to be informed that the element row was pressed.-->
        <div :class="`row mb-3 py-4 shadow-sm productRows row-colour`" :tabindex="tableTabIndex+rowIndex" style="cursor: pointer"
             @click="() => handleRowClick(rowIndex)" @keydown="(event) => handleRowKeyDown(event, rowIndex)"
             v-for="(row, rowIndex) in currentPageRows" :key="`${tableId}-row-${rowIndex}`">

          <!-- Column-row data point -->
          <div class="col text-center" v-for="(dataPoint, dataPointIndex) in row" :key="`${tableId}-data-point-${rowIndex}-${dataPointIndex}`">
            {{dataPoint}}
          </div>

        </div>

    </div>

    <!-- Show a loading icon when the data is not ready -->
    <div v-else class="text-center">
      <div class="spinner-border spinner-border-sm"></div>
    </div>


    <!-- Table footer -->
    <div :id="`${tableId}-footer-row`" class="row flex-column-reverse flex-lg-row">
      <!-- Showing results out of total results section-->
      <div class="col-lg" v-if="totalRows > 0">
        Showing {{currentPage*maxRowsPerPage+1}}-{{currentPageRows.length+currentPage*maxRowsPerPage}} of {{totalRows}} results
      </div>
      <div v-else>
        No results found
      </div>
      <!---------------------------------------------- page buttons ------------------------------------------------>

      <div id="page-button-container">
        <PageButtons
            v-bind:totalPages="totalPages"
            v-bind:currentPage="currentPage"
            @updatePage="updatePage"/>
      </div>

    </div>

  </div>
</template>

<script>

import PageButtons from "./PageButtons";

export default {
  name: "Table",
  components: {
    PageButtons,
  },
  props: {
    // Table ID must be unqiue within the page it is placed!
    // This is used to identify the icons per table.
    tableId: {
      type: String,
      required: true
    },

    // Table tab index is used to define a relative tabbing index for each row.
    tableTabIndex: {
      type: Number,
      required: false,
      default() { return 0; }
    },

    // Must be a list of strings. This is used to generate the headers.
    tableHeaders: {
      type: Array,
      required: true
    },

    // Must be an array of all the data points!
    // These can be of any type.
    tableData: {
      type: Array,
      required: false,
      default() { return []; }
    },

    // The maximum number of rows per page. Default is 5.
    maxRowsPerPage: {
      type: Number,
      required: false,
      default() { return 5; }
    },

    // This defines the total number of rows belonging to the table (i.e. useful when not all the data points are provided). Default is 5.
    totalRows: {
      type: Number,
      required: false,
      default() { return 5; }
    },

    // This is the string that will appear instead of undefined or null values.
    nullStringValue: {
      type: String,
      required: false,
      default() { return "Null"; }
    },

    // This can be used to determine if null should have a higher or lower weight then its defined counter parts.
    nullWeightHigher: {
      type: Boolean,
      required: false,
      default() { return true; }
    },

    // This controls wether or not the data given is considered the page data.
    tableDataIsPage: {
      type: Boolean,
      required: false,
      default() { return false; }
    },

    // current page override. This is used to force a certain page
    // this is in 0 origin.
    currentPageOverride: {
      type: Number,
      required: false,
      default() { return null; }
    },

    // Allows the parent class to override to update the orderBy.
    orderByOverride: {
      // This is used to ensure that the orderBy object provided has the orderBy and isAscending properties and of the correct type.
      validator: orderByOverride => {
        if (orderByOverride.orderBy !== undefined && orderByOverride.isAscending !== undefined && orderByOverride.isAscending !== null) {
          return (typeof orderByOverride.orderBy === 'number' || orderByOverride.orderBy === null )&& typeof orderByOverride.isAscending === 'boolean';
        }
        return false;
      },
      required: false,
      default() { return null; }
    }


  },
  data() {
    return {
      // All rows for the table are stored here.
      rows: [],

      // Used to control when the table data is ready to be displayed.
      dataIsReady: false,

      // 0 origin of the current page. If the override has been engaged then it will be the value
      // Should only be used within the component (underscore indicated).
      currentPage: this.currentPageOverride || 0,

      // Is a list of the rows to be displayed for the current page.
      currentPageRows: [],

      // Used to keep track of the column and which direction
      orderBy: null,
      isAscending: false,

      // Total number of pages is used to determine pagination
      totalPages: 1,

      // All events must be in Kebab case, as Camel case does not work with Vue events!
      eventTypes: {
        ORDER_BY_HEADER_INDEX: 'order-by-header-index',
        ROW_SELECTED: 'row-selected',
        UPDATE_CURRENT_PAGE: 'update-current-page'
      }

    }
  },
  methods: {
    /**
     * Given a new page number. The function will either emit an update current page event. Or update the table to show the new page.
     * @param newPageNumber The 0 origin page number.
     */
    updatePage(newPageNumber) {
      // If the current page is controlled through the parent we notify the parent of the update that needs to occur.
      if (this.currentPageOverride !== null) {
        this.$emit(this.eventTypes.UPDATE_CURRENT_PAGE, {tableId: this.tableId, newPageNumber: newPageNumber});
      } else {
        // If the current page is being handled internally then it is simple update
        this.currentPage = newPageNumber;
        this.updateTable();
      }
    },
    /**
     * Update orderBy updates the current orderBy value and direction within the Table component (for visual purposes).
     * And emits a ORDER_BY_HEADER_INDEX event, which can be used by the parent to sort the tableData by this new header
     * NOTE: that then the parent must call the updateTable(true) function, as the rows must be rebuilt.
     * @param newHeaderIndex This is the header index which is selected from the options.
     */
    updateOrderBy(newHeaderIndex) {

      // Determine the new direction the table should be going in.
      let isAscending;
      if (this.orderBy !== newHeaderIndex) {
        isAscending = true;
      } else {
        isAscending = !this.isAscending;
      }

      if (this.orderByOverride !== null) {
        this.$emit(this.eventTypes.ORDER_BY_HEADER_INDEX, {tableId: this.tableId, orderBy: newHeaderIndex, isAscending: isAscending});
      } else {
        // If the new index is different then the already used ordering by, then it will update the orderBy.
        if (this.orderBy !== newHeaderIndex) {
          this.orderBy = newHeaderIndex;
        }
        // Update the direction
        this.isAscending = isAscending;

        this.sortRows();

      }
    },
    /**
     * When called the rows known to the table will be sorted in the orderBy and isAscending attributes.
     * NOTE: The priority of NULL is controlled via the nullWeightHigher prop.
     */
    sortRows() {

      if (this.orderBy === null) return;
      let nullWeight = this.nullWeightHigher ? -1 : 1

      // Update the order of the rows. NOTE this is relative to the rows we know about.
      this.rows.sort( (rowA, rowB) => {
        // If both attributes are defined. Then we can perform the ordering
        if ( rowA[this.orderBy] !== this.nullStringValue && rowB[this.orderBy] !== this.nullStringValue  ) {
          // Depending on the direction the comparison should yield different values
          if (this.isAscending) {
            return rowA[this.orderBy] > rowB[this.orderBy] ? -1 : 1;
          } else {
            return rowA[this.orderBy] > rowB[this.orderBy] ? 1 : -1;
          }
          // If attribute of rowB is null/undefined base your results soley on the direction
        } else if ( rowA[this.orderBy] !== this.nullStringValue ) {
          return this.isAscending ? -nullWeight : nullWeight;
          // If attribute of rowA is null/undefined base your results soley on the direction
        } else if ( rowB[this.orderBy] !== this.nullStringValue ) {
          return this.isAscending ?  nullWeight : -nullWeight;
        }
      })

      this.updateTable();
    },
    /**
     * Emits an event of ORDER_BY_HEADER_INDEX. This tells the parent to order by a header
     * at the given index.
     * @param headerIndex The index of the header you want to order by.
     */
    handleHeaderClick(headerIndex) {
      this.updateOrderBy(headerIndex);
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
        this.updateOrderBy(headerIndex);
      }
    },
    /**
     * Emits an event of type ROW_SELECTED, which can be used to tell the parent what row was selected.
     * The parent will need to figure out what the row means. But most likely the rows were unpacked into the
     * tableData.
     * @param relativeRowIndex The index of the row in the rows being displayed context.
     */
    handleRowClick(relativeRowIndex) {
      this.$emit(this.eventTypes.ROW_SELECTED, {tableId: this.tableId, index: relativeRowIndex + this.maxRowsPerPage*this.currentPage});
    },
    /**
     * Emits an event of type ROW_SELECTED, which can be used to tell the parent what row was selected.
     * The parent will need to figure out what the row means. But most likely the rows were unpacked into the
     * tableData. This however, only occurs if the event's keyCode is equal to the
     * ENTER keyCode.
     * @param event The keydown event object.
     * @param relativeRowIndex The index of the row in the rows being displayed context.
     */
    handleRowKeyDown(event, relativeRowIndex) {
      if (event.keyCode === 13) {
        this.$emit(this.eventTypes.ROW_SELECTED, {tableId: this.tableId, index: relativeRowIndex + this.maxRowsPerPage*this.currentPage});
      }
    },
    /**
     * An ASYNC method used to update the table values.
     * @param newData A flag used to determine if there is new data
     * @param rebuildRows A flag used to determine if the rows need to be rebuilt.
     */
    updateTable(newData = false, rebuildRows = false) {
      this.dataIsReady = false;


      // Updates the totalPages to be able to know how many pages exist
      this.totalPages = Math.ceil(this.totalRows/this.maxRowsPerPage);

      // If the current page override is specified this means that we must set the current page to it.
      if (this.currentPageOverride) {
        this.currentPage = this.currentPageOverride;
      }

      if (this.totalPages > 0 && this.currentPage > this.totalPages){
        this.$router.push({path: '/pageDoesNotExist'});
      }

      // If the override has been specified we can update these values, as it means that we want the table to represent what the override is specifying.
      if (this.orderByOverride) {
        this.orderBy = this.orderByOverride.orderBy;
        this.isAscending = this.orderByOverride.isAscending;
      }

      // Some table updates may not require the rows to be rebuilt!
      if (newData || rebuildRows) {
        this.buildRows();
      }
      this.loadCurrentPageRows();

      // This dictates the table is ready to load in new data.
      this.dataIsReady = true;
    },
    /**
     * Builds a list of rows that are added to the this.rows variable, which is used for displaying the data.
     */
    buildRows() {
      // Required initialization.
      this.rows = [];
      let row = [];
      let numberOfDataPoints = this.tableData.length + this.tableData.length % this.tableHeaders.length;

      // Prases the raw stream of tabke data and converts it into lists of rows.
      for (let i = 0; i < numberOfDataPoints; i++) {

        let dataPoint = this.nullStringValue ;

        // If the value is accessable and not null we set the data point to the value. Otherwise to the nullTableValue.
        if (i < this.tableData.length) {
          if (this.tableData[i] != null) {
            dataPoint = this.tableData[i];
          }
        }

        row.push(dataPoint);

        // Once we added a divisable amount of data points by the number of headers onto the row array
        // and this isn't the first item (this is because 0 % NUMBER == 0). Then we can add it to the rows.
        if ((i+1) % this.tableHeaders.length === 0 && i > 0) {
          this.rows.push(row);
          row = [];
        }
      }
    },
    /**
     * Loads all the rows that belong within the current page.
     */
    loadCurrentPageRows() {
      if (this.tableDataIsPage) {
        this.currentPageRows = this.rows.slice(0, this.maxRowsPerPage);
      } else {
        const startIndex = this.currentPage * this.maxRowsPerPage;
        const endIndex = this.currentPage * this.maxRowsPerPage + this.maxRowsPerPage;
        this.currentPageRows = this.rows.slice(startIndex, endIndex);
      }
    },
  },
  watch: {
    tableData: function() {
      this.updateTable(true);
    },
    currentPageOverride: function () {
      this.currentPage = this.currentPageOverride;
      this.updateTable(true);
    },
    orderByOverride: function () {
      this.orderBy = this.orderByOverride;
      this.updateTable(true);
    }
  },
  /**
   * An ASYNC function used to initialize the table.
   */
  mounted() {
    // the catch is only to prevent the console from complaining.
    this.updateTable(true);
  }
}

</script>


<style scoped>

.btn {
  color: #1EBA8C;
  background-color: #FFFFFF;
  border: 1px solid #d4d4d4;
}

.disabled {
  color: #404040;
}

.active {
  color: #FFFFFF;
  background-color: #1EBA8C;
  border: 1px solid #1EBA8C;
}

</style>