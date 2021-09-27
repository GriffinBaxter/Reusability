import {Bar} from 'vue-chartjs'

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
        },
        mock: {
            type: Boolean,
            required: false,
            default: false
        }
    },
    watch: {
        labelList() {
            this.renderBarChart(!this.mock);
        },
        dataList() {
            this.renderBarChart(!this.mock);
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
    },
    methods: {
        /**
         * Returns a string concatenating the currency symbol and the currency value for a given y-axis revenue value.
         *
         * @param value revenue
         * @returns {string} currency symbol and value
         */
        currencyRevenue(value) {
            return this.currencySymbol + value;
        },
        /**
         * Renders the bar chart for either sales or revenue.
         *
         * @param render Boolean determining whether the bar chart should be rendered.
         */
        renderBarChart(render) {
            if (render) {
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
                        xAxes: [{
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
        }
    },
    mounted() {
        this.renderBarChart(!this.mock);
    }
}
