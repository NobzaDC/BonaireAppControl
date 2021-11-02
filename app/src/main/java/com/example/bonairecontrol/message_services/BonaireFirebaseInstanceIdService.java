package com.example.bonairecontrol.message_services;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;

public class BonaireFirebaseInstanceIdService extends FirebaseMessagingService {

    public static final String TAG = "NOTICIAS";

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
    }
}
