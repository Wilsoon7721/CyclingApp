package com.gmail.calorious.cyclistdirections.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;

public class FirebaseQueryFailure implements OnFailureListener {

    @Override
    public void onFailure(@NonNull Exception e) {
        Log.i("FirebaseCloudModification", "The action could not be completed: Exception - " + e.getMessage());
    }
}
