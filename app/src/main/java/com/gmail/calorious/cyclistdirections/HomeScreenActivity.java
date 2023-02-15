package com.gmail.calorious.cyclistdirections;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class HomeScreenActivity extends AppCompatActivity {
    private Button createRoomButton, joinRoomButton;
    private EditText joinRoomField;
    private TextView loggedInMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        Toolbar top_toolbar = findViewById(R.id.top_toolbar);
        setSupportActionBar(top_toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Home");
        createRoomButton = findViewById(R.id.room_create_button);
        joinRoomButton = findViewById(R.id.room_join_button);
        joinRoomField = findViewById(R.id.room_join_code_field);
        loggedInMessage = findViewById(R.id.logged_in_message);
        File securityFile = findInternalFile("security.txt");
        if(securityFile == null) {
            Log.d("Login Handler", "Could not find security.txt in internal data directory, redirecting user to login screen.");
            Intent loginIntent = new Intent(HomeScreenActivity.this, LoginScreenActivity.class);
            startActivity(loginIntent);
            return;
        }
        String uid;
        try(Scanner scanner = new Scanner(securityFile)) {
            uid = scanner.nextLine();
        } catch(IOException ex) {
            Log.e("IO Stream", "An I/O error occurred when reading the security file.");
            ex.printStackTrace();
            return;
        }
        // TODO dig through firebase and login based on existing UID
        // Update message using loggedInMessage variable.
    }

    public void onElementPressed(View view) {
        if(view.getId() == R.id.room_create_button) {
            // TODO Room create button
            return;
        }
        if(view.getId() == R.id.room_join_button) {
            String specifiedCode = joinRoomField.getText().toString();
            if(specifiedCode.trim().isEmpty()) {
                showErrorAlert("The specified code cannot be empty.");
                return;
            }
            if(specifiedCode.trim().length() < 4) {
                showErrorAlert("The specified code must be 4 characters long.");
                return;
            }
            // TODO ROOM JOIN BUTTON LOGIC
            return;
        }
        if(view.getId() == R.id.room_join_code_field) {
            // TODO Room join code field
            return;
        }
    }

    private void showErrorAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("error").setMessage(message).setPositiveButton("OK", null).setCancelable(false);
        AlertDialog alert = builder.create();
        alert.show();
    }

    private File findInternalFile(String fileName) {
        File file = new File(getFilesDir(), fileName);
        if(!file.exists())
            return null;
        return file;
    }

    private void writeInternalFile(String fileName, byte[] contents) {
        try(FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE)) {
            fos.write(contents);
        } catch (IOException e) {
            Log.e("IO Stream", "An I/O error occurred when writing to an internal file.");
            e.printStackTrace();
        }
    }
}
