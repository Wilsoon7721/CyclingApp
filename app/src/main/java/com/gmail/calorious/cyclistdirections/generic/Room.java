package com.gmail.calorious.cyclistdirections.generic;

import android.util.Log;

import androidx.annotation.NonNull;

import com.gmail.calorious.cyclistdirections.firebase.FirebaseCentre;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
 * Notes:
 *  - The database will NEVER have a complete copy of Room as it does not have any data relating to users leaving/joining the room.
 *  - As such, the application must preserve a copy of whichever room the user joins, in the form of the Room    object.
 *  - An updateDatabase() method is available in order to upload the current data to the database.
 *  - However, the data in the database is NEVER current.
 */
public class Room {
    private String roomCode;
    private boolean started;
    private List<User> users;

    public Room() {
        this.roomCode = new Random().ints(97, 123).limit(4).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString().toUpperCase();
        this.started = false;
        this.users = new ArrayList<>();
    }

    public String getCode() {
        return roomCode;
    }

    public boolean sessionStarted() {
        return started;
    }

    public List<User> getUsers() {
        return users;
    }

    public void updateDatabase() {
        DatabaseReference currentRoomRef = FirebaseCentre.getRoomsReference().child(roomCode);
        currentRoomRef.child("roomCode").setValue(roomCode);
        currentRoomRef.child("started").setValue(started);
        currentRoomRef.child("users").setValue(users);
    }
}
