package com.fahrenfarther.fahrenfartherfarthernasad;

public class Rental {
    private String car;
    private String startDate;
    private String endDate;
    private String totalCost;

    public Rental(String car, String startDate, String endDate, String totalCost) {
        this.car = car;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalCost = totalCost;
    }

    public String getCar() { return car; }
    public void setCar(String car) { this.car = car; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public String getTotalCost() { return totalCost; }
    public void setTotalCost(String totalCost) { this.totalCost = totalCost; }
}