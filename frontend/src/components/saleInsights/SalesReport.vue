<template>
  <div>
    <div class="main">
      <div class="container">
        <div class="card p-3">
          <h1 id="page-title">{{ businessName }} Sales Report</h1>

          <!------------------------------------------- Period dropdown menu ------------------------------------------>

          <div class="row m-2">

            <div class="col-xl-1" style="max-width: 80px">
              <label for="period-button" class="py-3">
                Period:
              </label>
            </div>

            <div class="btn-group col-xl-2 d-inline-block p-2" role="group">
              <button type="button" class="btn green-button dropdown-toggle order-by-options-btn w-100"
                      data-bs-toggle="dropdown" aria-expanded="false" id="period-button">{{ period }}
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

            <div v-if="period === 'Month'" class="btn-group col-xl-2 d-inline-block p-2" role="group">
              <button type="button" class="btn green-button dropdown-toggle order-by-options-btn w-100"
                      id="sales-period-select-month"
                      data-bs-toggle="dropdown" aria-expanded="false">{{ selectedMonth }}
              </button>
              <ul class="dropdown-menu gap-2" aria-labelledby="btnGroupDrop1">
                <li v-if="selectedYear === currentYear">
                  <div class="btn green-button-transparent col-12 order-by-options-btn"
                      v-for="month in months.slice(0, this.currentMonth + 1)" v-bind:key="month"
                      @click="selectedMonth = month">
                    {{ month }}
                  </div>
                </li>
                <li v-else>
                  <div class="btn green-button-transparent col-12 order-by-options-btn"
                      v-for="month in months" v-bind:key="month"
                      @click="selectedMonth = month">
                    {{ month }}
                  </div>
                </li>
              </ul>
            </div>

            <div v-if="period === 'Year' || period === 'Month'" class="btn-group col-xl-2 d-inline-block p-2" role="group">
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

            <div v-if="period === 'Day'" class="btn-group col-xl-3 d-inline-block p-2" role="group">
              <input type="date" id="sales-period-select-day" class="form-control" v-model="selectedDay" :min="'2021-01-01'" :max="currentDay">
            </div>

            <div v-if="period === 'Custom'" class="btn-group col d-inline-block p-2" role="group">

              <!-------------------------------------- Custom date inputs --------------------------------------------->

                <form class="needs-validation" novalidate @submit.prevent>
                  <div class="form-group" id="date-filtering-container">

                    <div class="row">
                      <div class="col-xl-1">
                        <label for="start-date-input" class="p-2">Date </label>
                      </div>
                      <div class="col-xl-4 col-md-6">
                        <input type="date" class="form-control filter-input" id="start-date-input"
                               v-model="startDate"
                               :class="toggleInvalidClass(invalidDateMsg)">
                        <div class="invalid-feedback">
                          {{invalidDateMsg}}
                        </div>
                      </div>
                      <div class="col-xl-1">
                        <label for="end-date-input" class="p-2"> to </label>
                      </div>
                      <div class="col-xl-4 col-md-6">
                        <input type="date" class="form-control filter-input" id="end-date-input"
                               v-model="endDate">
                      </div>
                      <div class="col-xl-2 mt-lg-3 mt-md-3 mt-sm-3 mt-xl-0">
                        <button class="btn green-button" @click="applyDate($event)">
                          Apply
                        </button>
                      </div>
                    </div>
                  </div>
                </form>
          </div>

          <!---------------------------------- Granularity options menu ------------------------------------------->

          <div class="col-xl-1">
            <label for="granularity-button" class="py-3">
              Granularity:
            </label>
          </div>

          <div class="btn-group col-xl-2 d-inline-block p-2" role="group">

            <button type="button" class="btn green-button dropdown-toggle order-by-options-btn w-100"
                    data-bs-toggle="dropdown" aria-expanded="false" id="granularity-button">{{ granularity }}
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

        <!----------------------------------------- Sale history table/rows ------------------------------------------->

        <div class="card p-3">
          Barry's stuff for task 723:
          <div>
            {{ salesReportData }}
          </div>
        </div>

      </div>
     </div>

  </div>
</template>


<script>

import {isFuture, parseISO} from "date-fns";
import {isFirstDateBeforeSecondDate} from "../../dateUtils";
import {toggleInvalidClass} from "../../validationUtils";
import {formatISO, format} from "date-fns";
import Api from "../../Api";

export default {
  name: "SalesReport",
  components: {
  },
  props: {
    businessName: {
      type: String,
      default: "",
      required: true
    },
    businessCountry: {
      type: String,
      default: "",
      required: true
    },
    businessId: {
      type: Number,
      default: 0,
      required: true
    },
    currencySymbol: {
      type: String,
      default: "",
      required: true
    },
    currencyCode: {
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

      salesReportData: null
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
        this.retrieveSalesReport();
      }
    },

    /**
     * Sets the granularity option text on the button to match the selected granularity.
     * @param granularity The chosen granularity, e.g. total, yearly, monthly, daily.
     */
    setGranularityOption(granularity) {
      this.granularity = granularity;
      this.retrieveSalesReport();
    },

    /**
     * Returns the start date and end date for the given year.
     * @param year A year to give the dates of.
     */
    generateDatesFromYear(year) {
      return year
    },

    /**
     * Returns the start date and end date for the given month.
     * @param month A month to give the dates of.
     */
    generateDatesFromMonth(month) {
      return month
    },

    /**
     * Returns the start date and end date for the day.
     * @param day A year to give the dates of.
     */
    generateDatesFromDay(day) {
      return day
    },

    /**
     * Sends an API request to the backend to retrieve the sales report for the currently selected options
     */
    async retrieveSalesReport() {
      let fromDate = "";
      let toDate = "";
      if (this.period === 'Custom') {
        fromDate = formatISO(parseISO(this.startDate), { representation: 'date' }) + "T00:00";
        toDate = formatISO(parseISO(this.endDate), { representation: 'date' }) + "T23:59:59";
      } else if (this.period === 'Year') {
        this.generateDatesFromYear(this.selectedYear);
      } else if (this.period === 'Month') {
        this.generateDatesFromMonth(this.selectedMonth);
      } else if (this.period === 'Day') {
        this.generateDatesFromDay(this.selectedDay);
      }

      await Api.getSalesReport(this.businessId, fromDate, toDate, this.granularity).then(response => {
        this.salesReportData = [...response.data]
      }).catch((error) => {
        this.manageError(error);
      })
    },

    /**
     * If a request in to the backend results in an error, then this method will deal with the error.
     * @param error the error received from the backend.
     */
    async manageError(error) {
      console.log(error)
      // if (error.request && !error.response)      { await this.$router.push({path: '/timeout'});      }
      // else if (error.response.status === 401)    { await this.$router.push({path: '/invalidtoken'}); }
      // else if (error.response.status === 403)    { await this.$router.push({path: '/forbidden'});    }
      // else if (error.response.status === 406)    { await this.$router.push({path: '/noBusiness'});   }
      // else { await this.$router.push({path: '/noBusiness'}); console.log(error.message); }
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
