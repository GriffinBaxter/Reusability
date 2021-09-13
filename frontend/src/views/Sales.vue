<template>
  <div>
    <div id="main">
      <!--nav bar-->
      <Navbar></Navbar>

      <div id="sales-container">

        <ul class="nav nav-pills mb-3" id="pills-tab" role="tablist">
          <li class="nav-item" role="presentation">
            <button class="nav-link active" id="report-tab-button" data-bs-toggle="pill" data-bs-target="#report-tab" type="button" role="tab" aria-controls="pills-home" aria-selected="true" @click="showTab('report')">Sales Report</button>
          </li>
          <li class="nav-item" role="presentation">
            <button class="nav-link" id="history-tab-button" data-bs-toggle="pill" data-bs-target="#history-tab" type="button" role="tab" aria-controls="pills-profile" aria-selected="false" @click="showTab('history')">Sales History</button>
          </li>

        </ul>
        <div class="tab-content" id="pills-tabContent">
          <div class="tab-pane fade show active" id="report-tab" role="tabpanel" aria-labelledby="pills-report-tab">
            Insert Report Content
          </div>
          <div class="tab-pane fade" id="history-tab" role="tabpanel" aria-labelledby="pills-history-tab">
            <SaleHistory/>
          </div>
        </div>

      </div>

    </div>
    <Footer/>
  </div>
</template>

<script>
import Navbar from "../components/Navbar";
import SaleHistory from "../views/SaleHistory";
import {Tab} from 'bootstrap';
import Footer from "@/components/main/Footer";

export default {
  name: "Sales",
  components: {
    Footer,
    Navbar,
    SaleHistory
  },
  methods: {
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
  },
  mounted() {


    // Generate Bootstrap tabs upon mount
    const triggerTabList = [].slice.call(document.querySelectorAll('#report-tab-button, #history-tab-button'))
    triggerTabList.forEach(function (triggerEl) {
      const tabTrigger = new Tab(triggerEl)
      triggerEl.addEventListener('click', function (event) {
        event.preventDefault()
        tabTrigger.show()
      })
    })

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

</style>
