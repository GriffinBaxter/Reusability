<template>
  <div style="max-width: 950px" class="mx-auto">
    <div class="sales-report-overview">Sales Report Overview</div>
    <div v-if="loading">
      <LoadingDots />
    </div>
    <div v-else-if="hideGraph">
      Not data to show...
    </div>
    <div class="box" v-else>
      <div class="wrapper">
        <div class="left-side">
          <div class="total-header">Total Sales: {{totalSales}}</div>
          <div class="total-header">Total Revenue: {{currencySymbol}}{{totalRevenue}} {{currencyCode}}</div>
        </div>
        <div class="right-side">
          <button class="btn green-button" @click="goToSales()">Full Sales Report</button>
        </div>
      </div>
      <BarChart :data-list="graphData" :label-list="weekDays"></BarChart>
    </div>

  </div>
</template>

<script>
import Cookies from "js-cookie";
import Api from "@/Api";
import BarChart from "./SalesReportGraph"
import LoadingDots from "../LoadingDots";

export default {
  name: "HomeSales",
  components: {
    LoadingDots,
    BarChart
  },
  data() {
    return {
      currencySymbol: "",
      currencyCode: "",
      graphData: [],
      loading: false,
      totalRevenue: 0,
      hideGraph: false,
      totalSales: 0,

      weekDays: ["Sun", "Mon", "Tues", "Wed", "Thu", "Fri", "Sat"]
    }
  },
  methods: {
    /**
     * Generates the range dates based off the current year.
     *
     * @return {String, String} Returns {fromDate, toDate} both are strings.
     */
    async generateDates() {
      const today = Date.now();

      const startOfWeek = today.getDate() - today.getDay();
      const endOfWeek = startOfWeek + 6;

      return {
        fromDate: new Date(today.setDate(startOfWeek)).toISOString(),
        toDate: new Date(today.setDate(endOfWeek)).toISOString()
      }
    },
    /**
     * Retrieves the business data that we need to get data for the graph.
     */
    async buildGraph() {
      this.loading = true;

      // If the data is not available then we don't try to get the data.
      if (!this.isActingAsUser()) {
        this.hideGraph = true
        this.loading = false;
        return;
      }

      // Perform the call as the isActingAsUser tests for it being a number as well.
      const businessId = parseInt(Cookies.get("actAs"));
      let revenueData = [];
      try {
        const {fromDate, toDate} = await this.generateDates();
        const businessResponse = await Api.getBusiness(businessId);
        this.currencyCode = businessResponse.data.currencyCode;
        this.currencySymbol = businessResponse.data.currencySymbol;

        const response = await Api.getSalesReport(businessId,
            fromDate,
            toDate,
            "Daily");

        response.data.forEach((record) => {
          this.totalRevenue += record.totalRevenue;
          this.totalSales += record.totalSales;
          revenueData.push(record.totalRevenue);
        });

        this.graphData = revenueData;
      } catch (error) {
        await this.handleGraphError(error);
      }

      this.loading = false;
    },
    /**
     * Handles possible errors from the get sales report API call.
     *
     * @param error The error object.
     */
    async handleGraphError(error) {
      if (error.request && !error.response)      { await this.$router.push({path: '/timeout'});      }
      else if (error.response?.status === 401)    { await this.$router.push({path: '/invalidToken'}); }
      else if (error.response?.status === 403)    { await this.$router.push({path: '/forbidden'});    }
      else {
        this.hideGraph = true
      }
    },
    /**
     * Determines if the user is acting as a business. And verifies it is a number.
     *
     * @return {boolean} Returns true if the user is acting as a business and providing a valid business
     * id type (number). Otherwise return false.
     */
    isActingAsUser() {
      return !isNaN(parseInt(Cookies.get("actAs")));
    },
    /**
     * Takes you to the sales page for your business.
     */
    async goToSales() {
      const id = parseInt(Cookies.get("actAs"));
      if (isNaN(id)) return;
      await this.$router.push({path: `businessProfile/${id}/sales`})
    }
  },
  mounted() {
    this.buildGraph();
  }
}
</script>

<style scoped>

.box {
  border: 1px black solid;
  padding: 1em;
  margin: 1em 0;
}

.sales-report-overview {
  font-family: 'Roboto', sans-serif !important;
  color: black !important;
  font-size: 1.5rem;
}

.wrapper {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 1.5em;
}

.total-header {
  font-family: 'Roboto', sans-serif !important;
  font-size: 1.25em;
  font-weight: bold;
}

@media screen and (max-width: 500px) {
  .wrapper {
    flex-direction: column;
    align-items: flex-start;
  }

  .right-side {
    margin: 1em 0;
  }
}

</style>