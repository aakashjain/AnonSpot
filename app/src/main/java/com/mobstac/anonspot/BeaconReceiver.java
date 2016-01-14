package com.mobstac.anonspot;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.mobstac.beaconstac.core.BeaconstacReceiver;
import com.mobstac.beaconstac.core.MSPlace;
import com.mobstac.beaconstac.models.MSAction;
import com.mobstac.beaconstac.models.MSBeacon;

import java.util.ArrayList;

/**
 * Created by aakash on 13/1/16.
 */
public class BeaconReceiver extends BeaconstacReceiver {

    private final Activity activity;

    private Button startButton;

    private static boolean atAnonSpot = false;

    public BeaconReceiver(Activity activity) {
        this.activity = activity;
        startButton = (Button) activity.findViewById(R.id.start_button);
    }

    @Override
    public void rangedBeacons(Context context, ArrayList<MSBeacon> beacons) {

    }

    @Override
    public void campedOnBeacon(Context context, MSBeacon beacon) {
//        TextView label = (TextView) activity.findViewById(R.id.text_view1);
//        label.setText(beacon.getBeaconKey());
        Log.wtf("br_1 camped on", beacon.getBeaconKey());
    }

    @Override
    public void exitedBeacon(Context context, MSBeacon beacon) {

    }

    @Override
    public void triggeredRule(Context context, String rule, ArrayList<MSAction> actions) {
        Log.wtf("triggered", rule);
        if (rule.equals("EnterAnonSpot")) {
            startButton.setEnabled(true);
            atAnonSpot = true;
        }
    }

    @Override
    public void enteredRegion(Context context, String region) {

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
