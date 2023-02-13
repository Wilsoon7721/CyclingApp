package com.gmail.calorious.cyclistdirections.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseCentre {
    private FirebaseDatabase database;
    private DatabaseReference usersRef, roomsRef;
    public FirebaseCentre() {
        database = FirebaseDatabase.getInstance("https://cyclistdirections-default-rtdb.asia-southeast1.firebasedatabase.app");
        usersRef = database.getReference("users");
        roomsRef = database.getReference("rooms");
    }
}
