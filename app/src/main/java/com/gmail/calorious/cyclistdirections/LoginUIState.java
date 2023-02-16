package com.gmail.calorious.cyclistdirections;

public class LoginUIState {
    private final int phoneNumber;
    private final boolean otpRequested;

    public LoginUIState(int phoneNumber, boolean otpRequested) {
        this.phoneNumber = phoneNumber;
        this.otpRequested = otpRequested;
    }
    // Returns 8 digit phone number without country code.
    public int getPhoneNumber() {
        return phoneNumber;
    }

    public boolean otpRequested() {
        return otpRequested;
    }
}
