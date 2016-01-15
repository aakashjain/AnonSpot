package com.mobstac.anonspot.receivers;

import android.app.Activity;
import android.content.Context;

import android.util.Log;
import android.widget.Button;

import com.mobstac.anonspot.AnonSpot;
import com.mobstac.anonspot.R;
import com.mobstac.beaconstac.core.BeaconstacReceiver;
import com.mobstac.beaconstac.core.MSPlace;
import com.mobstac.beaconstac.models.MSAction;
import com.mobstac.beaconstac.models.MSBeacon;

import java.util.ArrayList;

/**
 * Created by aakash on 13/1/16.
 */
public class SearchingBeaconReceiver extends BeaconstacReceiver {

    private final Activity activity;
    private Button startButton;

    public SearchingBeaconReceiver(Activity activity) {
        this.activity = activity;
        startButton = (Button) activity.findViewById(R.id.start_button);
    }

    @Override
    public void rangedBeacons(Context context, ArrayList<MSBeacon> beacons) {

    }

    @Override
    public void campedOnBeacon(Context context, MSBeacon beacon) {
        AnonSpot.spotBeaconKey = beacon.getBeaconKey();
        Log.i("Camped on", AnonSpot.spotBeaconKey);
    }

    @Override
    public void exitedBeacon(Context context, MSBeacon beacon) {
        startButton.setEnabled(false);
    }

    @Override
    public void triggeredRule(Context context, String rule, ArrayList<MSAction> actions) {
        if (rule.equals("EnterAnonSpot")) {
            startButton.setEnabled(true);
        }
    }

    @Override
    public void enteredRegion(Context context, String region) {
        Log.i("Entered region", region);
    }

    @Override
    public void exitedRegion(Context context, String region) {

    }

    @Override
    public void enteredGeofence(Context context, ArrayList<MSPlace> places) {

    }

    @Override
    public void exitedGeofence(Context context, ArrayList<MSPlace> places) {

    }
}
