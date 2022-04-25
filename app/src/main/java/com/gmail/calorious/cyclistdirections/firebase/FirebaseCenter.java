package com.gmail.calorious.cyclistdirections.firebase;

import android.content.Context;

import com.gmail.calorious.cyclistdirections.general.CyclistRoom;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseCenter {
    private DatabaseReference users, rooms;

    // Firebase appends locally, before attempting to sync remotely with the server
    // Purpose of class is to handle firebase requests
    public void initialize(Context context) {
        FirebaseApp.initializeApp(context);
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://cyclistdirections-default-rtdb.asia-southeast1.firebasedatabase.app");
        DatabaseReference defaultRef = database.getReference();
        DatabaseReference usersRef = defaultRef.child("users").push(); // Child reference - users
        DatabaseReference roomsRef = defaultRef.child("rooms").push(); // Child reference - rooms
        this.users = usersRef;
        this.rooms = roomsRef;
    }

    // Extract room data from object
    public void newRoom(CyclistRoom room) {
        // Rooms are volatile
        DatabaseReference roomDataRef = rooms.child(room.getId()).push();
        roomDataRef.child("id").push().setValue(room.getId()).addOnSuccessListener(new FirebaseQuerySuccess()).addOnFailureListener(new FirebaseQueryFailure()).addOnCanceledListener(new FirebaseQueryCancelled());
        // TODO Account for changing active users value
        roomDataRef.child("users").push().setValue(room.getActiveUsers()).addOnSuccessListener(new FirebaseQuerySuccess()).addOnFailureListener(new FirebaseQueryFailure()).addOnCanceledListener(new FirebaseQueryCancelled());
        // TODO Account for changing started value
        roomDataRef.child("started").push().setValue(room.started()).addOnSuccessListener(new FirebaseQuerySuccess()).addOnFailureListener(new FirebaseQueryFailure()).addOnCanceledListener(new FirebaseQueryCancelled());
    }
}
