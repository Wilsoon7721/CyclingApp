package com.gmail.calorious.cyclistdirections.generic;

import android.util.Log;

import com.gmail.calorious.cyclistdirections.firebase.FirebaseCentre;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/*
 * Notes:
 *  - All data in this class is generated within the application before being passed to Database.
 *  - updateDatabase() method passes this entire User object to the database.
 *  - As such, changes to phone no. and name can be made within the object itself prior to running updateDatabase().
 */
// Phone Number in Integer is XXXXXXX [No Country Code]
public class User {
    private UUID userUUID;
    private int phoneNumber;
    private String name;
    public User() {}

    public User(UUID userUUID, Integer phoneNumber, String name) {
        this.phoneNumber = phoneNumber;
        this.name = name;
        if(userUUID != null) {
            this.userUUID = userUUID;
            return;
        }
        this.userUUID = UUID.randomUUID(); // assign random uuid if instantiating with null uuid
    }

    public UUID getUUID() {
        return userUUID;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean updateDatabase() {
        AtomicBoolean success = new AtomicBoolean();
        FirebaseCentre.getUsersReference().child(userUUID.toString()).setValue(this).addOnCompleteListener(task -> {
            success.set(task.isSuccessful());
           if(task.isSuccessful()) {
               Log.d(FirebaseCentre.TAG, "User [" + userUUID.toString() + "]: Successfully updated the database with data from the User object.");
               return;
           }
           Log.e(FirebaseCentre.TAG, "User [" + userUUID.toString() + "]: An error occurred while updating the database with data from the User object.", task.getException());
        });
        return success.get();
    }
}
