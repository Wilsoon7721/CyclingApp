package com.gmail.calorious.cyclistdirections;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.gmail.calorious.cyclistdirections.firebase.FirebaseCentre;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class LoginScreenActivity extends AppCompatActivity {
    private static final String TAG = "LoginScreenActivity";
    private static FirebaseAuth auth = FirebaseAuth.getInstance();
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
        getOTP = findViewById(R.id.login_get_otp_button);
        otpField = findViewById(R.id.login_otp_field);
        timeoutExpiry = findViewById(R.id.login_timeout_expiry);
        phoneNumberField = findViewById(R.id.login_phone_number_field);
        verifyOTP = findViewById(R.id.login_verify_otp_button);
        verificationResult = findViewById(R.id.login_verification_result);
        defaultCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                // TODO Get UUID from Firebase, set UUID to security.txt and update UI

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                if(e instanceof FirebaseAuthInvalidCredentialsException) {
                    // TODO Firebase invalid request handler
                } else if(e instanceof FirebaseTooManyRequestsException) {
                    // TODO Firebase sms quota hit handler
                }
                // TODO Display UI message

            }

            @Override
            public void onCodeSent(@NonNull String verifyId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verifyId, forceResendingToken);
                Log.d("Firebase", "Successfully dispatched a verification code for phone number verification.");
                Toast.makeText(LoginScreenActivity.this, "Verification code sent!", Toast.LENGTH_LONG).show();
                verificationId = verifyId;
                resendingToken = forceResendingToken;
                // TODO Create an async countdown somewhere else that counts down from timeoutseconds.
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
        // TODO Create saved bundle state so that android remembers if you have already requested for an OTP
    }

    public void onElementPressed(View view) {
        if (view.getVisibility() != View.VISIBLE) {
            Log.d(TAG, "Ignoring interaction with " + view.getId() + " - View is being interacted with while the view is invisible.");
            return;
        }
        if (view.getId() == R.id.login_get_otp_button) {
            phoneNumberField
            return;
        }
        if (view.getId() == R.id.login_verify_otp_button) {
            // TODO Get OTP from firebase and match with otp field, create UID OR return existing UID from Firebase and attach to security.txt file in data folder.
            return;
        }
        if(view.getId() == R.id.login_otp_field) {
            verificationResult.setVisibility(View.INVISIBLE);
            return;
        }
    }

    private void signInWithPhoneCredential(PhoneAuthCredential credential, int phoneNumber) {
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()) {
                    Log.e(TAG, "'" + otpField.getText().toString() + "' is an invalid verification code.");
                    verificationResult.setTextColor(getResources().getColor(R.color.pure_red));
                    verificationResult.setText("The verification code provided is invalid.");
                    verificationResult.setVisibility(View.VISIBLE);
                    otpField.setText("");
                    return;
                }
                Log.d(TAG, "Successfully logged in the user.");
                verificationResult.setTextColor(getResources().getColor(R.color.pure_green));
                verificationResult.setText("Logged in, please wait...");
                verificationResult.setVisibility(View.VISIBLE);
                String uuid = FirebaseCentre.getUserUUID(phoneNumber);
                if(uuid == null || uuid.trim().isEmpty()) {
                    Log.e(TAG, "Failed to write UUID to security.txt: Firebase could not resolve UUID.");
                    return;
                }
                writeSecurityFile(uuid.getBytes(StandardCharsets.UTF_8));
                // Relaunch other activity
                Intent intent = new Intent(LoginScreenActivity.this, HomeScreenActivity.class);
                startActivity(intent);
            }
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
    private void startVerification(String phoneNumber, long timeoutSeconds) {
        PhoneAuthOptions authOptions = PhoneAuthOptions.newBuilder().setPhoneNumber(phoneNumber).setTimeout(timeoutSeconds, TimeUnit.SECONDS).setCallbacks(LoginScreenActivity.getDefaultCallback()).build();
        PhoneAuthProvider.verifyPhoneNumber(authOptions);
    }

}
