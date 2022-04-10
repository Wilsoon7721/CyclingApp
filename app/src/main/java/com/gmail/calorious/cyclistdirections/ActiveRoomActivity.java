package com.gmail.calorious.cyclistdirections;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gmail.calorious.cyclistdirections.db.RoomDatabase;

public class ActiveRoomActivity extends AppCompatActivity {
        // Chances are its gonna waste a shit ton of data, so u can try to optimise it 
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve room
        // If Room is a new room (find that out from the database query (room id), create a new room database and pass to RoomManager handler together with the room id
        // If it's an existing room then grab the room data from the RoomManager
    }
}
