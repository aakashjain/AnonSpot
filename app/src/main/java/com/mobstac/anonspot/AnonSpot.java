package com.mobstac.anonspot;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.firebase.client.Firebase;
import com.mobstac.beaconstac.core.Beaconstac;

/**
 * Created by joel on 13/1/16.
 */
public class AnonSpot extends Application {

    public static Firebase firebase;
    public static SharedPreferences prefs;

    @Override
    public void onCreate() {
        super.onCreate();
        Beaconstac.getInstance(getApplicationContext())
                .setRegionParams(getString(R.string.uuid), getString(R.string.app_name));
        Firebase.setAndroidContext(getApplicationContext());
        firebase = new Firebase(AnonSpotConstants.FIREBASE_URL);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }
}
