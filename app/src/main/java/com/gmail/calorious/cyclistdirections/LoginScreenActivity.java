package com.gmail.calorious.cyclistdirections;

import static android.view.View.VISIBLE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.gmail.calorious.cyclistdirections.firebase.FirebaseCentre;
import com.gmail.calorious.cyclistdirections.generic.LoginModel;
import com.gmail.calorious.cyclistdirections.generic.LoginUIState;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class LoginScreenActivity extends AppCompatActivity {
    private static final String TAG = "LoginScreenActivity";
    private static FirebaseAuth auth = FirebaseAuth.getInstance();
    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private Handler handler = new Handler(Looper.getMainLooper());
    private SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    private Button getOTP, verifyOTP;
    private EditText otpField, phoneNumberField;
    private TextView timeoutExpiry, verificationResult;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks defaultCallback;
    private String verificationId;
    private PhoneAuthProvider.ForceResendingToken resendingToken;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar top_toolbar = findViewById(R.id.top_toolbar);
        setSupportActionBar(top_toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Login");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        getOTP = findViewById(R.id.login_get_otp_button);
        otpField = findViewById(R.id.login_otp_field);
        timeoutExpiry = findViewById(R.id.login_timeout_expiry);
        phoneNumberField = findViewById(R.id.login_phone_number_field);
        verifyOTP = findViewById(R.id.login_verify_otp_button);
        verificationResult = findViewById(R.id.login_verification_result);
        defaultCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                String code = phoneAuthCredential.getSmsCode();
                if(code != null) {
                    otpField.setText(code);
                    verifyCode(code);
                }
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                if(e instanceof FirebaseAuthInvalidCredentialsException) {
                    Log.e("Firebase", "An invalid request was sent to Firebase.");
                    e.printStackTrace();
                } else if(e instanceof FirebaseTooManyRequestsException) {
                    Log.e("Firebase", "The Firebase quota limit has been reached.");
                }
                String serverError = "A server error has occurred: " + e.getMessage();
                verificationResult.setText(serverError);
                verificationResult.setVisibility(VISIBLE);

            }

            @Override
            public void onCodeSent(@NonNull String verifyId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verifyId, forceResendingToken);
                Log.d("Firebase", "Successfully dispatched a verification code for phone number verification.");
                Toast.makeText(LoginScreenActivity.this, "Verification code sent!", Toast.LENGTH_LONG).show();
                verificationId = verifyId;
                resendingToken = forceResendingToken;
            }
        };
        // Set invisible in onCreate
        otpField.setVisibility(View.INVISIBLE);
        verifyOTP.setVisibility(View.INVISIBLE);
        timeoutExpiry.setVisibility(View.INVISIBLE);
        verificationResult.setVisibility(View.INVISIBLE);

        setContentView(R.layout.login_screen);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdownNow();
    }

    public void onElementPressed(View view) {
        if (view.getVisibility() != VISIBLE) {
            Log.d(TAG, "Ignoring interaction with " + view.getId() + " - View is being interacted with while the view is invisible.");
            return;
        }
        if (view.getId() == R.id.login_get_otp_button) {
            String phoneString = phoneNumberField.getText().toString();
            if(phoneString.isEmpty()) {
                showErrorAlert("Phone number cannot be empty.");
                return;
            }
            int phone = -1;
            try {
                phone = Integer.parseInt(phoneString);
            } catch(NumberFormatException ex) {
                showErrorAlert("You must enter a valid phone number.");
                return;
            }
            startVerification(phone, 60L);
            return;
        }
        if (view.getId() == R.id.login_verify_otp_button) {
            String otpString = otpField.getText().toString();
            if(otpString.isEmpty()) {
                showErrorAlert("The OTP cannot be empty.");
                return;
            }
            int otp;
            try {
                otp = Integer.parseInt(otpString);
            } catch(NumberFormatException ex) {
                showErrorAlert("The OTP must be a valid code.");
                return;
            }
            verifyCode(String.valueOf(otp));
            return;
        }
        if(view.getId() == R.id.login_otp_field) {
            verificationResult.setVisibility(View.INVISIBLE);
            return;
        }
    }

    private void showErrorAlert(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("error").setMessage(msg).setPositiveButton("OK", null).setCancelable(false);
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void signInWithPhoneCredential(PhoneAuthCredential credential, int phoneNumber) {
        auth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if(!task.isSuccessful()) {
                Log.e(TAG, "'" + otpField.getText().toString() + "' is an invalid verification code.");
                verificationResult.setTextColor(getResources().getColor(R.color.pure_red));
                verificationResult.setText("The verification code provided is invalid.");
                verificationResult.setVisibility(VISIBLE);
                otpField.setText("");
                return;
            }
            Log.d(TAG, "Successfully logged in the user.");
            verificationResult.setTextColor(getResources().getColor(R.color.pure_green));
            verificationResult.setText("Logged in, please wait...");
            verificationResult.setVisibility(VISIBLE);
            String uuid = FirebaseCentre.getUserUUID(phoneNumber);
            if(uuid == null || uuid.trim().isEmpty()) {
                Log.e(TAG, "Failed to write UUID to security.txt: Firebase could not resolve UUID.");
                return;
            }
            writeSecurityFile(uuid.getBytes(StandardCharsets.UTF_8));
            // Relaunch other activity
            Intent intent = new Intent(LoginScreenActivity.this, HomeScreenActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void writeSecurityFile(byte[] contents) {
        try(FileOutputStream fos = openFileOutput("security.txt", Context.MODE_PRIVATE)) {
            fos.write(contents);
            Log.d("IO Stream", "Successfully written contents to security.txt.");
        } catch (IOException e) {
            Log.e("IO Stream", "An I/O error occurred when writing to an internal file.");
            e.printStackTrace();
        }
    }

    // Start verification for a phone number
    // Note: Phone Number Format should follow "65XXXXXXXX"
    // Note 2: Timeout in seconds should follow a countdown inside login_screen.xml
    private void startVerification(int phoneNumber, Long timeoutSeconds) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("LOGGED_PHONE_NUMBER", phoneNumber);
        editor.apply();
        String phone = "+65" + phoneNumber;
        PhoneAuthOptions authOptions = PhoneAuthOptions.newBuilder().setPhoneNumber(phone).setTimeout(timeoutSeconds, TimeUnit.SECONDS).setCallbacks(defaultCallback).build();
        PhoneAuthProvider.verifyPhoneNumber(authOptions);
        Log.d(TAG, "Starting phone number verification of " + phone + ".");
        phoneNumberField.setEnabled(false);
        otpField.setVisibility(VISIBLE);
        timeoutExpiry.setVisibility(VISIBLE);
        verifyOTP.setVisibility(VISIBLE);
        getOTP.setEnabled(false);

        // UI SAVE POINT
        LoginModel model = new ViewModelProvider(this).get(LoginModel.class);
        model.setUIState(new LoginUIState(phoneNumber, true));
        model.getUIState().observe(this, uiState -> {
            // Update UI
            phoneNumberField.setText(uiState.getPhoneNumber());
            phoneNumberField.setEnabled(false);
            otpField.setVisibility(VISIBLE);
            timeoutExpiry.setVisibility(VISIBLE);
            verifyOTP.setVisibility(VISIBLE);
            getOTP.setEnabled(false);
        });
        AtomicInteger i = new AtomicInteger(timeoutSeconds.intValue());
        executor.scheduleWithFixedDelay(() -> {
            i.set(i.getAndDecrement());
            String text = "This request will timeout in " + i.get() + " seconds.";
            handler.post(() -> timeoutExpiry.setText(text));
        }, 0, 1, TimeUnit.SECONDS);
    }

    private void verifyCode(String code) {
        int phoneNumber = sharedPreferences.getInt("LOGGED_PHONE_NUMBER", -1);
        if(phoneNumber == -1) {
            Log.e(TAG, "Failed to verify code: SharedPreferences could not retrieve the logged in phone number.");
            return;
        }
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneCredential(credential, phoneNumber);
    }

}
