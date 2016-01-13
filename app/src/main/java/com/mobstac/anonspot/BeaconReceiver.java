package com.mobstac.anonspot;

import android.content.Context;

import com.mobstac.beaconstac.core.BeaconstacReceiver;
import com.mobstac.beaconstac.core.MSPlace;
import com.mobstac.beaconstac.models.MSAction;
import com.mobstac.beaconstac.models.MSBeacon;

import java.util.ArrayList;

/**
 * Created by aakash on 13/1/16.
 */
public class BeaconReceiver extends BeaconstacReceiver {

    @Override
    public void rangedBeacons(Context context, ArrayList<MSBeacon> beacons) {

    }

    @Override
    public void campedOnBeacon(Context context, MSBeacon beacon) {

    }

    @Override
    public void exitedBeacon(Context context, MSBeacon beacon) {

    }

    @Override
    public void triggeredRule(Context context, String rule, ArrayList<MSAction> actions) {

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
