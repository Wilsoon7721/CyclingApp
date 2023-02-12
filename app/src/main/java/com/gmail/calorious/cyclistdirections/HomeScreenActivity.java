package com.gmail.calorious.cyclistdirections;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class HomeScreenActivity extends AppCompatActivity {
    private Button createRoomButton, joinRoomButton;
    private EditText joinRoomField;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        Toolbar top_toolbar = findViewById(R.id.top_toolbar);
        createRoomButton = findViewById(R.id.room_create_button);
        joinRoomButton = findViewById(R.id.room_join_button);
        joinRoomField = findViewById(R.id.room_join_code_field);
        top_toolbar.setTitle("Home");
    }

    public void onElementPressed(View view) {
        if(view.getId() == R.id.room_create_button) {
            // Room create button
            return;
        }
        if(view.getId() == R.id.room_join_button) {
            // Room join button
            String specifiedCode = joinRoomField.getText().toString();
            if(specifiedCode.trim().isEmpty()) {
                showErrorAlert("The specified code cannot be empty.");
                return;
            }
            if(specifiedCode.trim().length() < 4) {
                showErrorAlert("The specified code must be 4 characters long.");
                return;
            }
            // LOGIC
            return;
        }
        if(view.getId() == R.id.room_join_code_field) {
            // Room join code field
            return;
        }
    }

    private void showErrorAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("error").setMessage(message).setPositiveButton("OK", null).setCancelable(false);
        AlertDialog alert = builder.create();
        alert.show();
    }
}
