package com.gmail.calorious.cyclistdirections;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.calorious.cyclistdirections.firebase.FirebaseCentre;
import com.gmail.calorious.cyclistdirections.generic.Room;
import com.gmail.calorious.cyclistdirections.generic.RoomManager;
import com.gmail.calorious.cyclistdirections.generic.User;
import com.gmail.calorious.cyclistdirections.generic.UsersViewAdapter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class RoomLobbyActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "RoomLobbyActivity";
    private RecyclerView participants_list;
    private MapView user_location_map;
    private User user;
    private Room room;
    private GoogleMap googleMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        Toolbar top_toolbar = findViewById(R.id.top_toolbar);
        setSupportActionBar(top_toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Lobby");
        participants_list = findViewById(R.id.lobby_participants_list);
        user_location_map = findViewById(R.id.lobby_user_location);
        String user_uuid_string = bundle.getString("USER_UUID");
        String room_code = bundle.getString("ROOM_CODE");
        user = FirebaseCentre.getUser(user_uuid_string);
        if(user == null) {
            Log.wtf(TAG, "The activity could not retrieve the logged in User from the given intent data.");
            Toast.makeText(this, "The user is not logged in, returning to home!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, HomeScreenActivity.class));
            return;
        }
        room = RoomManager.getRoom(room_code);
        if(room == null) {
            Log.wtf(TAG, "The activity could not retrieve the Room object from the given intent data.");
            Toast.makeText(this, "An internal error occurred, returning to home!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, HomeScreenActivity.class));
            return;
        }
        TextView room_lobby_code = findViewById(R.id.lobby_room_code);
        String room_lobby_text = "Room Code: " + this.room.getCode();
        room_lobby_code.setText(room_lobby_text);
        // Participants list
        List<User> users = new LinkedList<>();
        users.add(user);
        participants_list.setLayoutManager(new LinearLayoutManager(this));
        participants_list.setAdapter(new UsersViewAdapter(getApplicationContext(), users));
        // Create an area to create a destination and start google maps for all when lobby is started.

        setContentView(R.layout.room_lobby);
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

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

    }
}
