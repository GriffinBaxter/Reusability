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
                <li id="year-option" class="btn green-button-transparent col-12 order-by-options-btn"
                    @click="period = 'Year'; updateGranularity(); retrieveSalesReport()">
                  Year
                </li>
                <li id="month-option" class="btn green-button-transparent col-12 order-by-options-btn"
                    @click="period = 'Month'; updateGranularity(); retrieveSalesReport()">
                  Month
                </li>
                <li id="week-option" class="btn green-button-transparent col-12 order-by-options-btn"
                    @click="period = 'Week'; updateGranularity(); retrieveSalesReport()">
                  Week
                </li>
                <li id="day-option" class="btn green-button-transparent col-12 order-by-options-btn"
                    @click="period = 'Day'; updateGranularity(); retrieveSalesReport()">
                  Day
                </li>
                <li id="custom-option" class="btn green-button-transparent col-12 order-by-options-btn"
                    @click="period = 'Custom'; showTable = false">
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
                       @click="selectedMonth = month; retrieveSalesReport()">
                    {{ month }}
                  </div>
                </li>
                <li v-else>
                  <div class="btn green-button-transparent col-12 order-by-options-btn"
                       v-for="month in months" v-bind:key="month"
                       @click="selectedMonth = month; retrieveSalesReport()">
                    {{ month }}
                  </div>
                </li>
              </ul>
            </div>

            <div v-if="period === 'Year' || period === 'Month'" class="btn-group col-xl-2 d-inline-block p-2"
                 role="group">
              <button type="button" class="btn green-button dropdown-toggle order-by-options-btn w-100"
                      id="sales-period-select-year"
                      data-bs-toggle="dropdown" aria-expanded="false">{{ selectedYear }}
              </button>
              <ul class="dropdown-menu gap-2" aria-labelledby="btnGroupDrop1">
                <li class="btn green-button-transparent col-12 order-by-options-btn"
                    v-for="validYear in validYears" v-bind:key="validYear"
                    @click="selectedYear = validYear; retrieveSalesReport()">
                  {{ validYear }}
                </li>
              </ul>
            </div>

            <div v-if="period === 'Week'" class="btn-group col-xl-3 p-2" role="group">
              <input type="date" id="sales-period-select-week" class="form-control" v-model="selectedDay"
                     :min="'2021-01-01'" :max="currentDay" :class="toggleInvalidClass(invalidDayMsg)">

              <button class="btn green-button" @click="retrieveSalesReport()">
                Apply
              </button>

            </div>

            <div v-if="period === 'Day'" class="btn-group col-xl-3 p-2" role="group">
              <input type="date" id="sales-period-select-day" class="form-control" v-model="selectedDay"
                     :min="'2021-01-01'" :max="currentDay" :class="toggleInvalidClass(invalidDayMsg)">

              <button class="btn green-button" @click="retrieveSalesReport()">
                Apply
              </button>

            </div>


            <div v-if="period === 'Custom'" class="btn-group col d-inline-block p-2" role="group">

              <!-------------------------------------- Custom date inputs --------------------------------------------->

              <form class="needs-validation" novalidate @submit.prevent>
                <div class="form-group" id="date-filtering-container">

                  <div class="row">
                    <div class="col-xl-1">
                      <label for="start-date-input" class="py-2">Date </label>
                    </div>
                    <div class="col-xl-4 col-md-6">
                      <input type="date" class="form-control filter-input" id="start-date-input"
                             v-model="startDate"
                             :class="toggleInvalidClass(invalidDateMsg)"
                             :min="'2021-01-01'">
                      <div class="invalid-feedback">
                        {{ invalidDateMsg }}
                      </div>
                    </div>
                    <div class="col-xl-1">
                      <label for="end-date-input" class="p-2"> to </label>
                    </div>
                    <div class="col-xl-4 col-md-6">
                      <input type="date" class="form-control filter-input" id="end-date-input"
                             v-model="endDate"
                             :min="'2021-01-01'">
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

            <div class="col-xl-1" style="width: auto">
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
                    @click="setGranularityOption('Total', $event)">
                  Total
                </li>

                <li class="btn green-button-transparent col-12 order-by-options-btn"
                    @click="setGranularityOption('Yearly', $event)">
                  Yearly
                </li>

                <li class="btn green-button-transparent col-12 order-by-options-btn"
                    @click="setGranularityOption('Monthly', $event)">
                  Monthly
                </li>

                <li class="btn green-button-transparent col-12 order-by-options-btn"
                    @click="setGranularityOption('Weekly', $event)">
                  Weekly
                </li>

                <li class="btn green-button-transparent col-12 order-by-options-btn"
                    @click="setGranularityOption('Daily', $event)">
                  Daily
                </li>

              </ul>
            </div>
          </div>
        </div>

        <div class="card p-3" style="margin: 10px 0 0 0">
          <div class="row m-2">
            <!-------------------------------------- Visualisation dropdown menu -------------------------------------->
            <div class="col-xl-4" style="max-width: 130px">
              <label for="visualisation-button" class="py-3">
                Visualisation:
              </label>
            </div>

            <div class="btn-group col-xl-2 d-inline-block p-2 me-3" role="group">
              <button type="button" class="btn green-button dropdown-toggle order-by-options-btn w-100"
                      data-bs-toggle="dropdown" aria-expanded="false" id="visualisation-button">{{ visualisationType }}
              </button>
              <ul class="dropdown-menu gap-2" aria-labelledby="btnGroupDrop1">
                <li class="btn green-button-transparent col-12 order-by-options-btn"
                    @click="visualisationType = 'Table'">
                  Table
                </li>
                <li class="btn green-button-transparent col-12 order-by-options-btn"
                    @click="visualisationType = 'Graph'; updateGranularity(); retrieveSalesReport();">
                  Graph
                </li>
              </ul>
            </div>

            <!--------------------------------------- Graph type dropdown menu ---------------------------------------->
            <div v-if="visualisationType === 'Graph'" class="col-xl-1" style="max-width: 80px">
              <label for="graph-type-button" class="py-3">
                Data:
              </label>
            </div>

            <div v-if="visualisationType === 'Graph'" class="btn-group col-xl-2 d-inline-block p-2" role="group">
              <button type="button" class="btn green-button dropdown-toggle order-by-options-btn w-100"
                      data-bs-toggle="dropdown" aria-expanded="false" id="graph-type-button">{{ graphType }}
              </button>
              <ul class="dropdown-menu gap-2" aria-labelledby="btnGroupDrop1">
                <li class="btn green-button-transparent col-12 order-by-options-btn"
                    @click="graphType = 'Sales'">
                  Sales
                </li>
                <li class="btn green-button-transparent col-12 order-by-options-btn"
                    @click="graphType = 'Revenue'">
                  Revenue
                </li>
              </ul>
            </div>

          </div>
        </div>

        <!------------------------------------------- Sales report graphs --------------------------------------------->

        <div v-if="visualisationType === 'Graph'" class="card p-3" style="margin: 10px 0 75px 0">
          <bar-chart v-if="graphType === 'Sales'" :label-list="graphLabels" :data-list="graphDataSales"
                     :sales="true"/>
          <bar-chart v-if="graphType === 'Revenue'" :label-list="graphLabels" :data-list="graphDataRevenue"
                     :currency-symbol="currencySymbol"/>
        </div>

        <!----------------------------------------- Sales report table/rows ------------------------------------------->

        <div v-if="showTable && visualisationType === 'Table'" class="card p-3" style="margin: 10px 0 75px 0">
          <br>

          <table class="table table-hover" aria-describedby="page-title" v-if="this.granularity !== 'Total'">
            <thead>
            <tr>
              <th scope="col">Time</th>
              <th scope="col">Total Sales</th>
              <th scope="col">Total Revenue ({{ currencyCode }})</th>
            </tr>
            </thead>

            <tbody>
            <tr v-for="line in salesReportData" v-bind:key="line.granularityName">
              <td>{{ line.granularityName }}</td>
              <td>{{ line.totalSales }}</td>
              <td>{{ currencySymbol }} {{ line.totalRevenue.toFixed(2) }}</td>
            </tr>
            </tbody>
          </table>
          <div v-else>
            <h3><strong>Total Sales:</strong> {{ salesReportData[0] ? salesReportData[0].totalSales : "0" }}</h3>
            <h3><strong>Total Revenue:</strong> {{currencySymbol}}{{ salesReportData[0] ? salesReportData[0].totalRevenue.toFixed(2) : "0" }} {{currencyCode}}</h3>
          </div>
        </div>

      </div>
    </div>

  </div>
</template>


<script>

import {isFuture, parseISO, formatISO, format, lastDayOfMonth, isBefore, startOfWeek, endOfWeek} from "date-fns";
import {isFirstDateBeforeSecondDate} from "../../dateUtils";
import {toggleInvalidClass} from "../../validationUtils";
import Api from "../../Api";
import {manageError} from "../../errorHandler";
import BarChart from './SalesReportGraph.js'

export default {
  name: "SalesReport",
  components: {
    BarChart
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

      salesReportData: [],
      invalidDayMsg: "",
      showTable: false,

      graphLabels: [],
      graphDataSales: [],
      graphDataRevenue: [],
      graphType: "Sales",

      // keeps track of whether table or graph should be displayed
      visualisationType: "Table"
    }
  },
  methods: {

    isFirstDateBeforeSecondDate: isFirstDateBeforeSecondDate,

    toggleInvalidClass: toggleInvalidClass,

    manageError: manageError,

    /**
     * Validates the start and end dates before applying the date range to the report.
     * For the date range to be applied:
     *       1. start date must be in the past or today
     *       2. end date must be after start date
     * @param event The click event passed in from the Vue template button click.
     */
    applyDate(event) {
      event.preventDefault();
      if (this.period !== 'Custom') {
        this.retrieveSalesReport();
      } else if (this.startDate === null || this.endDate === null) {
        this.invalidDateMsg = "Please enter two dates"
      } else if (this.isBefore2021(this.startDate) || this.isBefore2021(this.endDate)) {
        this.invalidDateMsg = "Dates must be after 2020"
      } else if (!isFirstDateBeforeSecondDate(this.startDate, this.endDate)) {
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
     * @param event The click event passed in from the Vue template button click.
     */
    setGranularityOption(granularity, event) {
      this.granularity = granularity;
      this.applyDate(event)
    },

    /**
     * Returns the start date and end date for the given year.
     * @param year A year to give the dates of.
     */
    generateDatesFromYear(year) {
      return {
        fromDate: `${year}-01-01T00:00`,
        toDate: `${year}-12-31T23:59:59`
      }
    },

    /**
     * Returns the start date and end date for the given month.
     * @param month A month to give the dates of.
     */
    generateDatesFromMonth(month) {
      const monthNumber = this.months.indexOf(month) + 1 < 10 ? '0' + (this.months.indexOf(month) + 1).toString() : this.months.indexOf(month) + 1;
      let toDate = formatISO(lastDayOfMonth(parseISO(`${this.selectedYear}-${monthNumber}-01`)), {representation: 'date'}) + "T23:59:59";
      return {
        fromDate: `${this.selectedYear}-${monthNumber}-01T00:00`,
        toDate: toDate
      }
    },

    /**
     * Returns the start date and end date for the week.
     * @param day A week to give the dates of.
     */
    generateDatesFromWeek(day) {
      const startOfWeekDate = startOfWeek(new Date(day));
      const endOfWeekDate = endOfWeek(new Date(day));

      return {
        fromDate: formatISO(startOfWeekDate, {representation: 'date'}) + "T00:00:00",
        toDate: formatISO(endOfWeekDate, {representation: 'date'}) + "T23:59:59"
      }
    },

    /**
     * Returns the start date and end date for the day.
     * @param day A year to give the dates of.
     */
    generateDatesFromDay(day) {
      return {
        fromDate: `${day}T00:00`,
        toDate: `${day}T23:59:59`
      }
    },

    /**
     * Generates the From and To dates for the retrieve sales report API call based on the selected options
     * @return {fromDate, toDate} An object containing a fromDate and a toDate to complete a range for the API call
     */
    generateDates() {
      let dates = null;
      if (this.period === 'Custom') {
        dates = {
          fromDate: formatISO(parseISO(this.startDate), {representation: 'date'}) + "T00:00",
          toDate: formatISO(parseISO(this.endDate), {representation: 'date'}) + "T23:59:59"
        }
      } else if (this.period === 'Year') {
        dates = this.generateDatesFromYear(this.selectedYear);
      } else if (this.period === 'Month') {
        dates = this.generateDatesFromMonth(this.selectedMonth);
      } else if (this.period === 'Week') {
        dates = this.generateDatesFromWeek(this.selectedDay);
      } else if (this.period === 'Day') {
        dates = this.generateDatesFromDay(this.selectedDay);
      }
      return dates;
    },

    /**
     * Updates the granularity based on the period, only for the graph visualisation type.
     */
    updateGranularity() {
      if (this.visualisationType === "Graph") {
        if (this.period === "Year") {
          this.granularity = "Monthly"
        } else if (this.period === "Month") {
          this.granularity = "Weekly"
        } else {
          this.granularity = "Daily"
        }
      }
    },

    /**
     * Sends an API request to the backend to retrieve the sales report for the currently selected options
     */
    async retrieveSalesReport() {
      // Check that selected day is not partially empty (unlikely case)
      if (this.selectedDay === '') {
        this.invalidDayMsg = 'Please enter a date';
      } else {
        this.invalidDayMsg = "";
        const dates = this.generateDates();
        const fromDate = dates.fromDate;
        const toDate = dates.toDate;

        await Api.getSalesReport(this.businessId, fromDate, toDate, this.granularity).then(response => {
          this.salesReportData = [];
          response.data.forEach((line) => {
            this.salesReportData.push({
              granularityName: line.granularityName,
              totalSales: line.totalSales,
              totalRevenue: line.totalRevenue
            })
            this.generateGraphData();
          });
          this.showTable = true;
        }).catch((error) => {
          this.manageError(error);
        })
      }
    },

    /**
     * Generates the graph data in the required format for vue-chartjs, with the chart labels and data (for both sales
     * and revenue).
     */
    generateGraphData() {
      let labels = [];
      let salesData = [];
      let revenueData = [];
      for (const data of this.salesReportData) {
        labels.push(data.granularityName == null ? "Total" : data.granularityName);
        salesData.push(data.totalSales);
        revenueData.push(data.totalRevenue);
      }
      this.graphLabels = labels;
      this.graphDataSales = salesData;
      this.graphDataRevenue = revenueData;
    },

    /**
     * Checks if a date is before the year 2021.
     * Dates before 2021 are not accepted by the sales report back-end endpoint
     * so this must be validated here.
     * @param date A date to check
     */
    isBefore2021(date) {
      const dateToCompare = parseISO('2021-01-01');
      return isBefore(parseISO(date), dateToCompare);
    },

    /**
     * Sets the dates based on the current date (selectable years, months and days).
     * @param date A date object containing the current date.
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

  async mounted() {
    await this.setDates(new Date());
    await this.retrieveSalesReport();
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
