package com.gmail.calorious.cyclistdirections;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;

import com.gmail.calorious.cyclistdirections.db.RoomDatabase;

import java.util.Objects;

public class HomeScreenActivity extends AppCompatActivity {

    /*
    ANDROID ROOM DATABASES WONT WORK - REQUIRES A SHARED STORAGE
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.home_screen);
    }
}