package com.fahrenfarther.fahrenfartherfarthernasad;

public class User {
    private String name;
    private String dateOfBirth;
    private String contactNo;
    private String licenseNo;

    public User(String name, String dateOfBirth, String contactNo, String licenseNo) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.contactNo = contactNo;
        this.licenseNo = licenseNo;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getContactNo() { return contactNo; }
    public void setContactNo(String contactNo) { this.contactNo = contactNo; }

    public String getLicenseNo() { return licenseNo; }
    public void setLicenseNo(String licenseNo) { this.licenseNo = licenseNo; }
}