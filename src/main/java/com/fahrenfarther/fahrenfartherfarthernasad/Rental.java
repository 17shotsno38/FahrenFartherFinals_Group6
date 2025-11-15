package com.fahrenfarther.fahrenfartherfarthernasad;

public class Rental {
    private int id;
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

    // Getters
    public int getId() { return id; }
    public String getCar() { return car; }
    public String getStartDate() { return startDate; }
    public String getEndDate() { return endDate; }
    public String getTotalCost() { return totalCost; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setCar(String car) { this.car = car; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public void setTotalCost(String totalCost) { this.totalCost = totalCost; }
}
