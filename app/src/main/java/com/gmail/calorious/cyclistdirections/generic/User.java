package com.gmail.calorious.cyclistdirections.generic;

public class User {
    private int phoneNumber;
    private String uid;
    public User() {}

    public User(Integer phoneNumber, String uid) {
        this.phoneNumber = phoneNumber;
        this.uid = uid;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public String getUID() {
        return uid;
    }
}
