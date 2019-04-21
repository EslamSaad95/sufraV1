package com.example.laptophome.sufra.helper.server;

import android.util.Log;

import com.example.laptophome.sufra.data.local.SharedPrerefrences.SharedPrerefrencesManager;
import com.example.laptophome.sufra.helper.Constant;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFireBaseInstanceId extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        // SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPrerefrencesManager.SetSharedPrerefrences(Constant.Token_FileName,"NotiToken",refreshedToken,getApplicationContext());
        //preferences.edit().putString(Constants.FIREBASE_TOKEN, refreshedToken).apply();
    }
}
