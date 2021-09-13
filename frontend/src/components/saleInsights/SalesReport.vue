<template>
  <div>
    <div class="main">
      <div class="container">
        <div class="card p-3">
          <h1 id="page-title">{{ businessName }} Sales Report</h1>

          <div class="row m-2">

            <form class="needs-validation" novalidate @submit.prevent>
              <div class="form-group" id="date-filtering-container">

                <!------------------------------------------- Date inputs --------------------------------------------->

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

              <!---------------------------------- Ordering by options menu ------------------------------------------->

              <div class="row">
                <label class="d-inline-block p-2 fs-5 text-center">Granularity: </label>

                <div class="btn-group col d-inline-block p-2" role="group">

                  <button type="button" class="btn green-button dropdown-toggle order-by-options-btn w-100"
                          data-bs-toggle="dropdown" aria-expanded="false">{{ granularityText }}
                  </button>

                  <ul class="dropdown-menu gap-2" aria-labelledby="btnGroupDrop1">
                    <!--order by price-->
                    <li class="btn green-button-transparent col-12 order-by-options-btn"
                        @click="setGranularityOption('total')">
                      Total
                    </li>

                    <!--order by product name-->
                    <li class="btn green-button-transparent col-12 order-by-options-btn"
                        @click="setGranularityOption('yearly')">
                      Yearly
                    </li>

                    <!--order by country-->
                    <li class="btn green-button-transparent col-12 order-by-options-btn"
                        @click="setGranularityOption('monthly')">
                      Monthly
                    </li>

                    <!--order by city-->
                    <li class="btn green-button-transparent col-12 order-by-options-btn"
                        @click="setGranularityOption('daily')">
                      Daily
                    </li>

                  </ul>
                </div>

              </div>

            </form>

          </div>
        </div>

        <!----------------------------------------- Sale history table/rows ------------------------------------------->

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
import {toggleInvalidClass} from "../../validationUtils";

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
      granularityText: ""
    }
  },
  methods: {

    isFirstDateBeforeSecondDate: isFirstDateBeforeSecondDate,

    toggleInvalidClass: toggleInvalidClass,

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
     * Sets the granularity option text on the button to match the selected granularity.
     * @param granularity The chosen granularity, e.g. total, yearly, monthly, daily.
     */
    setGranularityOption(granularity) {
      this.granularityText = granularity.charAt(0).toUpperCase() + granularity.slice(1);
    }
  }
}
</script>

<style scoped>

#page-title {
  padding: 10px;
  text-align: center;
}

</style>
