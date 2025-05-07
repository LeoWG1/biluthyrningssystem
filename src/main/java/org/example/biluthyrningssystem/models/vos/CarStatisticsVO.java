package org.example.biluthyrningssystem.models.vos;

import java.time.LocalDate;

/**
 * @author Frida Jakobsson
 */
public class CarStatisticsVO {
    private long totalActiveOrders;
    private long totalOrders;
    private long totalRevenue;
    private LocalDate latestOrder;

    public CarStatisticsVO() { }

    public CarStatisticsVO(long totalActiveOrders, long totalOrders, long totalRevenue, LocalDate latestOrder) {
        this.totalActiveOrders = totalActiveOrders;
        this.totalOrders = totalOrders;
        this.totalRevenue = totalRevenue;
        this.latestOrder = latestOrder;
    }

    public long getTotalActiveOrders() { return totalActiveOrders; }

    public void setTotalActiveOrders(long totalActiveOrders) { this.totalActiveOrders = totalActiveOrders; }

    public long getTotalOrders() { return totalOrders; }

    public void setTotalOrders(long totalOrders) { this.totalOrders = totalOrders; }

    public long getTotalRevenue() { return totalRevenue; }

    public void setTotalRevenue(long totalRevenue) { this.totalRevenue = totalRevenue; }

    public LocalDate getLatestOrder() { return latestOrder; }

    public void setLatestOrder(LocalDate latestOrder) { this.latestOrder = latestOrder; }
}
