package com.mobstac.anonspot;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.firebase.client.Firebase;
import com.mobstac.anonspot.utils.AnonSpotConstants;
import com.mobstac.beaconstac.core.Beaconstac;

/**
 * Created by neelesh on 13/1/16.
 */
public class AnonSpot extends Application {

    public static Firebase firebase;
    public static SharedPreferences prefs;
    public static String spotBeaconKey;

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(getApplicationContext());
        firebase = new Firebase(AnonSpotConstants.FIREBASE_URL);
        Beaconstac.getInstance(getApplicationContext())
                .setRegionParams(getString(R.string.uuid), getString(R.string.app_name));
        Beaconstac.getInstance(getApplicationContext()).syncBeacons();
        Beaconstac.getInstance(getApplicationContext()).syncRules();
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        spotBeaconKey = "-";
    }
}
