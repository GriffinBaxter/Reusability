<template>
  <div>
    <div id="main" class="container">
      <!--nav bar-->
      <Navbar></Navbar>

      <div id="sales-container container">

        <ul class="nav nav-pills mb-3 mx-3" id="pills-tab" role="tablist">
          <li class="nav-item" role="presentation">
            <button class="nav-link active" id="report-tab-button" data-bs-toggle="pill" data-bs-target="#report-tab" type="button" role="tab" aria-controls="pills-home" aria-selected="true" @click="showTab('report')">Sales Report</button>
          </li>
          <li class="nav-item" role="presentation">
            <button class="nav-link" id="history-tab-button" data-bs-toggle="pill" data-bs-target="#history-tab" type="button" role="tab" aria-controls="pills-profile" aria-selected="false" @click="showTab('history')">Sales History</button>
          </li>

        </ul>
        <div class="tab-content" id="pills-tabContent">
          <div class="tab-pane fade show active" id="report-tab" role="tabpanel" aria-labelledby="pills-report-tab">
            <SalesReport
                :business-id="businessId"
                :business-country="businessCountry"
                :business-name="businessName"
                :currency-symbol="currencySymbol"
                :currency-code="currencyCode"/>
          </div>
          <div class="tab-pane fade" id="history-tab" role="tabpanel" aria-labelledby="pills-history-tab">
            <SaleHistory
                :business-id="businessId"
                :business-country="businessCountry"
                :business-name="businessName"
                :currency-symbol="currencySymbol"
                :currency-code="currencyCode"
                ref="saleHistory"/>
          </div>
        </div>

      </div>

    </div>
    <Footer/>
  </div>
</template>

<script>
import Navbar from "../components/Navbar";
import SaleHistory from "../components/saleInsights/SaleHistory";
import {Tab} from 'bootstrap';
import Footer from "../components/main/Footer";
import SalesReport from "../components/saleInsights/SalesReport";
import Cookies from "js-cookie";
import {checkAccessPermission} from "@/views/helpFunction";
import Api from "../Api";
import {manageError} from "../errorHandler";

export default {
  name: "Sales",
  components: {
    Footer,
    Navbar,
    SalesReport,
    SaleHistory
  },
  data() {
    return {
      businessId: 0,
      businessName: "",
      businessCountry: "",
      currencySymbol: "",
      currencyCode: "",
    }
  },
  methods: {
    manageError: manageError,
    /**
     * Opens a Bootstrap tab upon click via the passed in name.
     * @param tabName A string containing the name of the tab to switch to
     */
    showTab(tabName) {
      let query;
      if (tabName === 'history') {
        query = '#history-tab-button';
      } else {
        query = '#report-tab-button';
      }
      const triggerEl = document.querySelector(query)
      Tab.getInstance(triggerEl).show()
    },
    /**
     * Calls a GET request to the backend to retrieve the information of the current business.
     */
    async retrieveBusinessInfo() {
      await Api.getBusiness(this.businessId).then(response => {
        this.businessName = response.data.name;
        this.businessCountry = response.data.address.country;
        this.currencySymbol = response.data.currencySymbol;
        this.currencyCode = response.data.currencyCode;
      }).catch((error) => {
        this.manageError(error);
      })
    },
  },
  /**
   * Runs after the component is mounted
   * @return {Promise<void>}
   */
  async mounted() {
    // Generate Bootstrap tabs upon mount
    const triggerTabList = [].slice.call(document.querySelectorAll('#report-tab-button, #history-tab-button'))
    triggerTabList.forEach(function (triggerEl) {
      const tabTrigger = new Tab(triggerEl)
      triggerEl.addEventListener('click', function (event) {
        event.preventDefault()
        tabTrigger.show()
      })
    })

    const actAs = Cookies.get('actAs');
    if (checkAccessPermission(this.$route.params.id, actAs)) {
      await this.$router.push({path: `/businessProfile/${actAs}/sales`});
    } else {
      const currentID = Cookies.get('userID');
      if (currentID) {
        this.businessId = parseInt(this.$route.params.id);
        await this.retrieveBusinessInfo();
        await this.$refs.saleHistory.retrieveSoldListings();
      }
    }
  }
}
</script>

<style scoped>

#sales-container {
  margin-top: 4%;
  margin-right: 10%;
  margin-left: 10%;
}

#history-tab, #report-tab {
  font-family: 'Roboto', sans-serif;
  color: black;
}

.nav-link.active {
  background-color: #1EBA8C;
}

.nav-link {
  color: darkgreen;
}

</style>
