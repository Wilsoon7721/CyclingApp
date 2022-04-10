package com.gmail.calorious.cyclistdirections;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class HomeScreenActivity extends AppCompatActivity {
    private EditText codeField;
    private Button createButton, joinButton;
    /*
    ANDROID ROOM DATABASES WONT WORK - REQUIRES A SHARED STORAGE
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.home_screen);
        codeField = findViewById(R.id.insert_code_field);
        createButton = findViewById(R.id.create_button);
        joinButton = findViewById(R.id.join_code_button);
    }

    public void onButtonPressed(View view) {
        if(view.getId() == R.id.create_button) {
            // Create room?
            return;
        }
        if(view.getId() == R.id.join_code_button) {
            String baseText = codeField.getText().toString();
            if(baseText.length() < 6 || baseText.length() > 6) {
                
            }
        }
    }
}