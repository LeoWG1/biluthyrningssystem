package org.example.biluthyrningssystem.models.vos;

/**
 * @author Frida Jakobsson
 */
public class StatisticsVO {
    private long totalActiveOrders;
    private long totalOrders;
    private long totalRevenue;

    public StatisticsVO() { }

    public StatisticsVO(long totalActiveOrders, long totalOrders, long totalRevenue) {
        this.totalActiveOrders = totalActiveOrders;
        this.totalOrders = totalOrders;
        this.totalRevenue = totalRevenue;
    }

    public long getTotalActiveOrders() {
        return totalActiveOrders;
    }

    public void setTotalActiveOrders(long totalActiveOrders) {
        this.totalActiveOrders = totalActiveOrders;
    }

    public long getTotalOrders() { return totalOrders; }

    public void setTotalOrders(long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public long getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(long totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}
