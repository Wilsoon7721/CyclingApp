package com.gmail.calorious.cyclistdirections;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gmail.calorious.cyclistdirections.generic.Room;
import com.gmail.calorious.cyclistdirections.generic.RoomManager;

import java.util.Objects;

public class RoomLobbyActivity extends AppCompatActivity {
    private static final String TAG = "RoomLobbyActivity";
    private Room room;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        setContentView(R.layout.room_lobby);
        Toolbar top_toolbar = findViewById(R.id.top_toolbar);
        setSupportActionBar(top_toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Lobby");
        String room_code = bundle.getString("ROOM_CODE");
        Room room = RoomManager.getRoom(room_code);
        if(room == null) {
            Log.wtf(TAG, "The activity could not retrieve the Room object from the given intent data.");
            return;
        }
        this.room = room;
        TextView room_lobby_code = findViewById(R.id.lobby_room_code);
        String room_lobby_text = "Room Code: " + this.room.getCode();
        room_lobby_code.setText(room_lobby_text);
        // TODO CREATION OF UI NOTE - RecyclerView can be used for Participants list, and can be enclosed within a scrollview to scroll through.
        // Create an area to create a destination and start google maps for all when lobby is started.
    }

    @Override
    protected void onResume() {
        super.onResume();
        // TODO Return saved state (if lobby started while state destroyed then throw RoomActivity)
    }

    public void onLobbyElementPressed(View view) {
        if(view.getId() == R.id.room_leave_button) {
            Intent intent = new Intent(this, HomeScreenActivity.class);
            startActivity(intent);
            // TODO ROOM LOBBY: Make sure to dump or nullify any data if needed
            finish();
            return;
        }
    }
}
