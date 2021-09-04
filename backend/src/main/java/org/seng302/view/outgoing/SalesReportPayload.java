package org.seng302.view.outgoing;

/**
 * SalesReportPayload class for sending to the frontend
 */
public class SalesReportPayload {
    private String granularityName;
    private int totalSales;
    private double totalRevenue;

    public SalesReportPayload(String granularityName, int totalSales, double totalRevenue) {
        this.granularityName = granularityName;
        this.totalSales = totalSales;
        this.totalRevenue = totalRevenue;
    }

    /**
     * Default constructor used for automated tests
     */
    public SalesReportPayload() {
    }

    public String getGranularityName() {
        return granularityName;
    }

    public void setGranularityName(String granularityName) {
        this.granularityName = granularityName;
    }

    public int getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(int totalSales) {
        this.totalSales = totalSales;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}
