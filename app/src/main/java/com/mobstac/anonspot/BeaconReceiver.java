package com.mobstac.anonspot;

import android.app.Activity;
import android.content.Context;
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

    private Activity activity;

    public BeaconReceiver (Activity activity) {
        this.activity = activity;
    }

    @Override
    public void rangedBeacons(Context context, ArrayList<MSBeacon> arrayList) {

    }

    @Override
    public void campedOnBeacon(Context context, MSBeacon msBeacon) {
        if (msBeacon.getMajor() == 50 && msBeacon.getMinor() ==70 ) {

        } else {
            TextView textView = (TextView) activity.findViewById(R.id.text_view1);
            textView.setText("Camped on: " + msBeacon.getMajor() +" " + msBeacon.getMinor());
        }
    }

    @Override
    public void exitedBeacon(Context context, MSBeacon msBeacon) {

    }

    @Override
    public void triggeredRule(Context context, String s, ArrayList<MSAction> arrayList) {

    }

    @Override
    public void enteredRegion(Context context, String s) {

    }

    @Override
    public void exitedRegion(Context context, String s) {

    }

    @Override
    public void enteredGeofence(Context context, ArrayList<MSPlace> arrayList) {

    }

    @Override
    public void exitedGeofence(Context context, ArrayList<MSPlace> arrayList) {

    }
}
