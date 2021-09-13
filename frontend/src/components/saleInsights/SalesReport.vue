<template>
  <div>
    <div class="main">
      <div class="container">
        <div class="card p-3">
          <h1 id="page-title">{{ businessName }} Sales Report</h1>

          <!------------------------------------------- Period dropdown menu ------------------------------------------>

          <div class="row m-2">
            Period:
            <div class="btn-group col d-inline-block p-2" role="group">
              <button type="button" class="btn green-button dropdown-toggle order-by-options-btn w-100"
                      data-bs-toggle="dropdown" aria-expanded="false">{{ period }}
              </button>
              <ul class="dropdown-menu gap-2" aria-labelledby="btnGroupDrop1">
                <li class="btn green-button-transparent col-12 order-by-options-btn"
                    @click="period = 'Year'">
                  Year
                </li>
                <li class="btn green-button-transparent col-12 order-by-options-btn"
                    @click="period = 'Month'">
                  Month
                </li>
                <li class="btn green-button-transparent col-12 order-by-options-btn"
                    @click="period = 'Day'">
                  Day
                </li>
                <li class="btn green-button-transparent col-12 order-by-options-btn"
                    @click="period = 'Custom'">
                  Custom
                </li>
              </ul>
            </div>

            <div v-if="period === 'Month'" class="btn-group col d-inline-block p-2" role="group">
              <button type="button" class="btn green-button dropdown-toggle order-by-options-btn w-100"
                      id="sales-period-select-month"
                      data-bs-toggle="dropdown" aria-expanded="false">{{ selectedMonth }}
              </button>
              <ul class="dropdown-menu gap-2" aria-labelledby="btnGroupDrop1">
                <div v-if="selectedYear === currentYear">
                  <li class="btn green-button-transparent col-12 order-by-options-btn"
                      v-for="month in months.slice(0, this.currentMonth + 1)" v-bind:key="month"
                      @click="selectedMonth = month">
                    {{ month }}
                  </li>
                </div>
                <div v-else>
                  <li class="btn green-button-transparent col-12 order-by-options-btn"
                      v-for="month in months" v-bind:key="month"
                      @click="selectedMonth = month">
                    {{ month }}
                  </li>
                </div>
              </ul>
            </div>

            <div v-if="period === 'Year' || period === 'Month'" class="btn-group col d-inline-block p-2" role="group">
              <button type="button" class="btn green-button dropdown-toggle order-by-options-btn w-100"
                      id="sales-period-select-year"
                      data-bs-toggle="dropdown" aria-expanded="false">{{ selectedYear }}
              </button>
              <ul class="dropdown-menu gap-2" aria-labelledby="btnGroupDrop1">
                <li class="btn green-button-transparent col-12 order-by-options-btn"
                    v-for="validYear in validYears" v-bind:key="validYear"
                    @click="selectedYear = validYear">
                  {{ validYear }}
                </li>
              </ul>
            </div>

            <div v-if="period === 'Day'" class="btn-group col d-inline-block p-2" role="group">
              <input type="date" id="sales-period-select-day" v-model="selectedDay" :min="'2021-01-01'" :max="currentDay">
            </div>

            <div v-if="period === 'Custom'" class="btn-group col d-inline-block p-2" role="group">

              <!-------------------------------------- Custom date inputs --------------------------------------------->

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

          <!---------------------------------- Ordering by options menu ------------------------------------------->

          <div class="row">
            <label class="d-inline-block p-2 fs-5 text-center">Granularity: </label>

            <div class="btn-group col d-inline-block p-2" role="group">

              <button type="button" class="btn green-button dropdown-toggle order-by-options-btn w-100"
                      data-bs-toggle="dropdown" aria-expanded="false">{{ granularity }}
              </button>

              <ul class="dropdown-menu gap-2" aria-labelledby="btnGroupDrop1">
                <li class="btn green-button-transparent col-12 order-by-options-btn"
                    @click="setGranularityOption('Total')">
                  Total
                </li>

                <li class="btn green-button-transparent col-12 order-by-options-btn"
                    @click="setGranularityOption('Yearly')">
                  Yearly
                </li>

                <li class="btn green-button-transparent col-12 order-by-options-btn"
                    @click="setGranularityOption('Monthly')">
                  Monthly
                </li>

                <li class="btn green-button-transparent col-12 order-by-options-btn"
                    @click="setGranularityOption('Daily')">
                  Daily
                </li>

              </ul>
            </div>

          </div>

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
import {format} from "date-fns";

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

      // Sales Report Date Attributes

      startDate: null,
      endDate: null,
      invalidDateMsg: "",
      granularity: "Total",

      // Sales Report Period Attributes

      period: "Year",

      currentYear: 0,
      selectedYear: "",
      validYears: [],

      currentMonth: 0,
      selectedMonth: "",
      months: [
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
      ],

      currentDay: "",
      selectedDay: "",
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
      this.granularity = granularity;
    },

    /**
     * Sets the dates based on the current date (selectable years, months and days).
     */
    setDates(date) {
      // Sets validYears to a list of years from 2021 to the current year
      this.currentYear = date.getFullYear();
      this.selectedYear = this.currentYear;
      this.validYears = [];
      this.validYears.push(2021);
      while (this.validYears[this.validYears.length - 1] < this.selectedYear) {
        this.validYears.push(this.validYears[this.validYears.length - 1] + 1);
      }

      this.currentMonth = date.getMonth();
      this.selectedMonth = this.months[this.currentMonth];

      this.currentDay = format(date, 'yyyy-MM-dd');
      this.selectedDay = this.currentDay;
    }
  },

  mounted() {
    this.setDates(new Date());
  }
}
</script>

<style scoped>

#page-title {
  padding: 10px;
  text-align: center;
}

.dropdown-menu {
  margin-top: 0;
  border: none;
  width: 200px;
}

</style>
