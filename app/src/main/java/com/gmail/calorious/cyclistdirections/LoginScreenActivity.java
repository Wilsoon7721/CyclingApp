package com.gmail.calorious.cyclistdirections;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

public class LoginScreenActivity extends AppCompatActivity {
    private static final String TAG = "LoginScreenActivity";
    private Button getOTP, verifyOTP;
    private EditText otpField, phoneNumberField;
    private TextView timeoutExpiry;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar top_toolbar = findViewById(R.id.top_toolbar);
        setSupportActionBar(top_toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Login");
        getOTP = findViewById(R.id.login_get_otp_button);
        otpField = findViewById(R.id.login_otp_field);
        timeoutExpiry = findViewById(R.id.login_timeout_expiry);
        phoneNumberField = findViewById(R.id.login_phone_number_field);
        verifyOTP = findViewById(R.id.login_verify_otp_button);

        // Set invisible in onCreate
        otpField.setVisibility(View.INVISIBLE);
        verifyOTP.setVisibility(View.INVISIBLE);
        timeoutExpiry.setVisibility(View.INVISIBLE);

        setContentView(R.layout.login_screen);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // TODO Create saved bundle state so that android remembers if you have already requested for an OTP
    }

    public void onElementPressed(View view) {
        if(view.getVisibility() != View.VISIBLE) {
            Log.d(TAG, "Ignoring interaction with " + view.getId() + " - View is being interacted with while the view is invisible.");
            return;
        }
        if(view.getId() == R.id.login_get_otp_button) {
            // TODO Get Number from phone number field, ask firebase to dispatch message
            return;
        }
        if(view.getId() == R.id.login_verify_otp_button) {
            // TODO Get OTP from firebase and match with otp field, create UID OR return existing UID from Firebase and attach to security.txt file in data folder.
            return;
        }
    }
}
