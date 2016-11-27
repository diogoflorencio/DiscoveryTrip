package com.example.diogo.discoverytrip;

/**
 * Created by diogo on 27/11/16.
 */
import android.app.Application;


import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class DiscoveryTripApp extends  Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }
}
