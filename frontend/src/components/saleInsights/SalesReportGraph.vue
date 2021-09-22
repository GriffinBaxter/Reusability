<script>
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
      return this.dataList
    },
    labels() {
      return this.labelList
    },
  },
  methods: {
    renderBarChart() {
      this.renderChart({
        labels: this.labels,
        datasets: [
          {
            label: 'Bar Chart',
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
              beginAtZero: true
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
          display: true
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
</script>
