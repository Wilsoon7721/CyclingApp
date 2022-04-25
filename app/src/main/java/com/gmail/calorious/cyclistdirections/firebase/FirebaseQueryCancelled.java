package com.gmail.calorious.cyclistdirections.firebase;

import android.util.Log;

import com.google.android.gms.tasks.OnCanceledListener;

public class FirebaseQueryCancelled implements OnCanceledListener {
    @Override
    public void onCanceled() {
        Log.i("FirebaseCloudModification", "Action cancelled.");
    }
}
