package com.example.mobileappdevproj;

public class StoreBusinessDetails {
    public String businessName, businessType, phoneNumber, address, city, country, postcode, description, photo;

    //Constructor
    public StoreBusinessDetails(){};
    public StoreBusinessDetails(String bName, String bType, String bPhone,String bAddress, String bCity, String bCountry, String bPostcode, String bDescription, String bPhoto){
        this.businessName = bName;
        this.businessType = bType;
        this.phoneNumber = bPhone;
        this.address = bAddress;
        this.city = bCity;
        this.country = bCountry;
        this.postcode = bPostcode;
        this.description = bDescription;
        this.photo = bPhoto;
    }
}
