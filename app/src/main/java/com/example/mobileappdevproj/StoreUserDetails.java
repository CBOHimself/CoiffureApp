package com.example.mobileappdevproj;

public class StoreUserDetails {

    public String firstName, lastName, phoneNumber;

    //Constructor
    public StoreUserDetails(){};
    public StoreUserDetails(String userfname, String userlname, String userphone){
        this.firstName = userfname;
        this.lastName = userlname;
        this.phoneNumber = userphone;
    }
}
