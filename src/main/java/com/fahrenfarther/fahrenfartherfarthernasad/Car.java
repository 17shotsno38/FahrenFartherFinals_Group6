package com.fahrenfarther.fahrenfartherfarthernasad;

public class Car {
    private String model;
    private int year;
    private String licensePlate;
    private String dailyRate;

    public Car(String model, int year, String licensePlate, String dailyRate) {
        this.model = model;
        this.year = year;
        this.licensePlate = licensePlate;
        this.dailyRate = dailyRate;
    }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }

    public String getDailyRate() { return dailyRate; }
    public void setDailyRate(String dailyRate) { this.dailyRate = dailyRate; }
}