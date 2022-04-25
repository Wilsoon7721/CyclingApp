package com.gmail.calorious.cyclistdirections.firebase;

import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseCenter {
    // Purpose of class is to handle firebase requests
    public void initialize(Context context) {
        FirebaseApp.initializeApp(context);
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://cyclistdirections-default-rtdb.asia-southeast1.firebasedatabase.app");
        
    }
}
