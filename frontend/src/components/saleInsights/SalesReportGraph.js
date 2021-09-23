import { Bar } from 'vue-chartjs'

export default {
  name: "SalesReportGraph",
  extends: Bar,
  props: {
    labelList: {
      type: Array,
      required: true
    },
    dataList: {
      type: Array,
      required: true
    },
    sales: {
      type: Boolean,
      required: false,
      default: false
    },
    currencySymbol: {
      type: String,
      required: false,
      default: ""
    }
  },
  watch: {
    labelList() {
      this.renderBarChart();
    },
    dataList() {
      this.renderBarChart();
    },
  },
  computed: {
    data() {
      return this.dataList;
    },
    labels() {
      return this.labelList;
    },
    label() {
      if (this.sales) {
        return "Sales";
      } else {
        return "Revenue";
      }
    },
  }
  ,
  methods: {
    currencyRevenue(value) {
      return this.currencySymbol + value;
    },
    renderBarChart() {
      this.renderChart({
        labels: this.labels,
        datasets: [
          {
            label: this.label,
            data: this.data,
            fill: false,
            borderColor: '#00B88F',
            backgroundColor: '#00B88F',
            borderWidth: 1
          }
        ]
      }, {
        scales: {
          yAxes: [{
            ticks: {
              beginAtZero: true,
              callback: this.currencyRevenue
            },
            gridLines: {
              display: true
            }
          }],
          xAxes: [ {
            gridLines: {
              display: false
            }
          }]
        },
        legend: {
          display: false
        },
        responsive: true,
        maintainAspectRatio: false
      })
    }
  },
  mounted () {
    this.renderBarChart();
  }
}
