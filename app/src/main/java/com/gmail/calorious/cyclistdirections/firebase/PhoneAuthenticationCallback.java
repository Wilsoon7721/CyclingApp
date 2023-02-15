package com.gmail.calorious.cyclistdirections.firebase;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.gmail.calorious.cyclistdirections.LoginScreenActivity;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class PhoneAuthenticationCallback extends PhoneAuthProvider.OnVerificationStateChangedCallbacks {
    private static String verificationId;
    private static PhoneAuthProvider.ForceResendingToken resendingToken;

    public static String getVerificationId() {
        return verificationId;
    }

    public static PhoneAuthProvider.ForceResendingToken getResendingToken() {
        return resendingToken;
    }

    private Context applicationContext;
    public PhoneAuthenticationCallback(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

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
        Toast.makeText(applicationContext, "Verification code sent!", Toast.LENGTH_LONG).show();
        verificationId = verifyId;
        resendingToken = forceResendingToken;
        // TODO Create an async countdown somewhere else that counts down from timeoutseconds.
    }
}
