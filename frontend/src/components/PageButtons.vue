<!--Page Buttons-->
<!--Contains page button components, as well as methods to check if a page number is-->
<!--within a valid range and to update the page number in the parent component when the-->
<!--page is changed.-->

<template>
  <nav>
    <ul v-if="totalPages > 0" class="pagination justify-content-center">
      <!-- This is only enabled when you are past the first page -->
      <button id="firstButton" type="button" :class="`btn green-button-transparent ${isValidPageNumber(currentPage-1) ? '': 'disabled'}`" @click="updatePage(0)">
        First
      </button>

      <!-- This is only enabled when there is a previous page -->
      <button id="previousButton" type="button" :class="`btn green-button-transparent ${isValidPageNumber(currentPage-1) ? '': 'disabled'}`" @click="updatePage(currentPage-1)">
        Previous
      </button>

      <!-- This is shown when there are more than four pages and you are on the last page-->
      <button :id="`pageButton${currentPage - 3}`" type="button" class="btn green-button-transparent" v-if="isValidPageNumber(currentPage-4) && currentPage === totalPages - 1" @click="updatePage(currentPage-4)">
        {{currentPage-3}}
      </button>

      <!-- This is shown when there are more than three pages and you are past the third to last page-->
      <button :id="`pageButton${currentPage - 2}`" type="button" class="btn green-button-transparent" v-if="isValidPageNumber(currentPage-3) && currentPage > totalPages - 3" @click="updatePage(currentPage-3)">
        {{currentPage-2}}
      </button>

      <!-- This is shown when there are more than two pages and you are past the first page-->
      <button :id="`pageButton${currentPage - 1}`" type="button" class="btn green-button-transparent" v-if="isValidPageNumber(currentPage-2) && currentPage > 0" @click="updatePage(currentPage-2)">
        {{currentPage-1}}
      </button>

      <!-- Only shows when we are past at least the first page -->
      <button :id="`pageButton${currentPage}`" type="button" class="btn green-button-transparent" v-if="isValidPageNumber(currentPage-1)" @click="updatePage(currentPage-1)">
        {{currentPage}}
      </button>

      <!-- This converts the current page into 1 origin.-->
      <button :id="`pageButton${currentPage + 1}`" type="button" class="btn green-button active">
        {{currentPage+1}}
      </button>

      <!-- This converts the current page into 1 origin And only shows the option if there is another page-->
      <button :id="`pageButton${currentPage + 2}`" type="button" class="btn green-button-transparent" v-if="isValidPageNumber(currentPage+1)" @click="updatePage(currentPage+1)">
        {{currentPage+2}}
      </button>

      <!-- This is shown when there are at least three pages and you are before the second to last page-->
      <button :id="`pageButton${currentPage + 3}`" type="button" class="btn green-button-transparent" v-if="isValidPageNumber(currentPage+2) && currentPage < totalPages - 2" @click="updatePage(currentPage+2)">
        {{currentPage+3}}
      </button>

      <!-- This is shown when there are at least four pages and you are before the third page-->
      <button :id="`pageButton${currentPage + 4}`" type="button" class="btn green-button-transparent" v-if="isValidPageNumber(currentPage+3) && currentPage < 2" @click="updatePage(currentPage+3)">
        {{currentPage+4}}
      </button>

      <!-- This is shown when there are at least five pages and you are on the first page-->
      <button :id="`pageButton${currentPage + 5}`" type="button" class="btn green-button-transparent" v-if="isValidPageNumber(currentPage+4) && currentPage === 0" @click="updatePage(currentPage+4)">
        {{currentPage+5}}
      </button>

      <!-- The next button only enabled if there is another page.-->
      <button id="nextButton" type="button" :class="`btn green-button-transparent ${isValidPageNumber(currentPage+1) ? '': 'disabled'}`" @click="updatePage(currentPage+1)">
        Next
      </button>

      <!-- The last button only enabled if you are before the last page.-->
      <button id="lastButton" type="button" :class="`btn green-button-transparent ${isValidPageNumber(currentPage+1) ? '': 'disabled'}`" @click="updatePage(totalPages - 1)">
        Last
      </button>
    </ul>
  </nav>
</template>

<script>
export default {
  name: "PageMenu",
  props: {
    currentPage: {
      type: Number,
      default: 0,
      required: true
    },
    totalPages: {
      type: Number,
      default: 1,
      required: true,
    }
  },
  methods: {
    /**
     * Emits an event to the parent to call the updatePage method with the new page number.
     * @param newPageNumber The new page number
     */
    updatePage(newPageNumber) {
      this.$emit('updatePage', newPageNumber);
      this.$parent.$emit('updatePage', newPageNumber);
      window.scrollTo({
        top: 0,
        behavior: "auto"
      })
    },

    /**
     * Given a page number check that the page is within the acceptable range.
     * NOTE this is a 0 origin.
     * @param pageNumber The page number to be checked.
     */
    isValidPageNumber(pageNumber) {
      return 0 <= pageNumber && pageNumber < this.totalPages;
    },
  }
}
</script>
