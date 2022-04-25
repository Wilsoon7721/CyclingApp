package com.gmail.calorious.cyclistdirections.firebase;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;

public class FirebaseQuerySuccess implements OnSuccessListener<Void> {

    @Override
    public void onSuccess(Void unused) {
        Log.i("FirebaseCloudModification", "The action was completed successfully.");

    }
}
