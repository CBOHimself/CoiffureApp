package com.example.mobileappdevproj;

public class ReadWriteUserDetails {

    public String firstName, lastName, phoneNumber;

    //Constructor
    public ReadWriteUserDetails(){};
    public ReadWriteUserDetails(String userfname, String userlname, String userphone){
        this.firstName = userfname;
        this.lastName = userlname;
        this.phoneNumber = userphone;
    }
}
