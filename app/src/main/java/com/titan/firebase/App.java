package com.titan.firebase;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

public class App extends Application {

    public static final String FIREBASE_CHANNEL_ID = "firebasechannel";

    @Override
    public void onCreate() {
        super.onCreate();

        //if (prefs.termsAndConditionsAccepted) {

            Fabric fabric = new Fabric.Builder(this)
                    .kits(new Crashlytics())

                    .debuggable(BuildConfig.DEBUG) // Enables Crashlytics debugger
                    .build();
           Fabric.with(fabric);
        //}

        Timber.plant(new Timber.DebugTree());

        /*
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new ReleaseTree());
        }
        */

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(FIREBASE_CHANNEL_ID, "Firebase", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("This is Channel 1");


            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

    }
}