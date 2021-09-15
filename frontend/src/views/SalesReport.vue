<template>
  <div>
    <div id="main">
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
    </div>
  </div>
</template>

<script>
import {format} from "date-fns";

export default {
  name: "SalesReport",
  data() {
    return {
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

.dropdown-menu {
  margin-top: 0;
  border: none;
  width: 200px;
}

</style>
