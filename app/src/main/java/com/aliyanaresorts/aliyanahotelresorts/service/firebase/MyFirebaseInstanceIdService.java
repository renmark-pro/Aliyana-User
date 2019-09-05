package com.aliyanaresorts.aliyanahotelresorts.service.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseInstanceIdService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d("NEW_TOKEN",s);
    }
}
