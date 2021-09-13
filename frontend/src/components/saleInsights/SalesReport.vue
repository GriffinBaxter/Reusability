<template>
  <div>
    <div class="main">
      <div class="container">
        <div class="card p-3">
          <h1 id="page-title">{{ businessName }} Sales Report</h1>

          <div class="row m-2">

            <form class="needs-validation" novalidate @submit.prevent>
              <div class="form-group" id="date-filtering-container">

                <div class="row">
                  <div class="col" style="max-width: 60px">
                    <label for="start-date-input" class="p-2">Date </label>
                  </div>
                  <div class="col-xl-3 col-md-6">
                    <input type="date" class="form-control filter-input" id="start-date-input"
                           v-model="startDate"
                           :class="toggleInvalidClass(invalidDateMsg)">
                    <div class="invalid-feedback">
                      {{invalidDateMsg}}
                    </div>
                  </div>
                  <div class="col" style="max-width: 60px">
                    <label for="end-date-input" class="p-2"> to </label>
                  </div>
                  <div class="col-xl-3 col-md-6">
                    <input type="date" class="form-control filter-input" id="end-date-input"
                           v-model="endDate">
                  </div>
                  <div class="col">
                    <button class="btn green-button" @click="applyDate($event)">
                      Apply
                    </button>
                  </div>
                </div>
              </div>

            </form>

          </div>
        </div>
        <div class="card p-3">
          Barry's stuff for task 723
        </div>
      </div>
     </div>

  </div>
</template>


<script>

import {isFuture, parseISO} from "date-fns";
import {isFirstDateBeforeSecondDate} from "../../dateUtils";

export default {
  name: "SalesReport",
  components: {
  },
  props: {
    businessName: {
      type: String,
      default: "",
      required: true
    }
  },
  data() {
    return {
      startDate: null,
      endDate: null,
      invalidDateMsg: "",
    }
  },
  methods: {

    isFirstDateBeforeSecondDate: isFirstDateBeforeSecondDate,

    /**
     * Validates the start and end dates before applying the date range to the report.
     * For the date range to be applied:
     *       1. start date must be in the past or today
     *       2. end date must be after start date
     */
    applyDate(event) {
      event.preventDefault();
      if (!isFirstDateBeforeSecondDate(this.startDate, this.endDate)) {
        this.invalidDateMsg = "Start date must be before end date"
      } else if (isFuture(parseISO(this.startDate))) {
        this.invalidDateMsg = "Start date cannot be in the future";
      } else {
        this.invalidDateMsg = "";
        // TODO: apply query method for task 723
      }
    },

    /**
     * This method toggles the appearance of the error message, where the is-invalid class is added to the messages
     * if an error message needs to be presented to the user.
     *
     * @param errorMessage, string, the error message relating to invalid input of a field.
     * @returns {[string]}, classList, a list containing the classes for an invalid message.
     */
    toggleInvalidClass(errorMessage) {
      let classList = ['form-control']
      if (errorMessage) {
        classList.push('is-invalid')
      }
      return classList
    },
  }
}
</script>

<style scoped>

#page-title {
  padding: 10px;
  text-align: center;
}

</style>
