package com.gmail.calorious.cyclistdirections;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_screen);
        Toolbar top_toolbar = findViewById(R.id.top_toolbar);
        setSupportActionBar(top_toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Settings");
    }

    public void onElementPressed(View view) {
        if(view.getId() == R.id.settings_to_home_button) {
            // TODO Save all settings

            Intent intent = new Intent(SettingsActivity.this, HomeScreenActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
