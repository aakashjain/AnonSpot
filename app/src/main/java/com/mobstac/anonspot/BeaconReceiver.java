package com.mobstac.anonspot;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;

import android.content.DialogInterface;
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

    private Activity activity;

//    private boolean onTheSpot = false;

    public BeaconReceiver (Activity activity) {
        this.activity = activity;

    }

    @Override
    public void rangedBeacons(Context context, ArrayList<MSBeacon> beacons) {

    }

    @Override
    public void campedOnBeacon(Context context, MSBeacon msBeacon) {
        Button startButton = (Button) activity.findViewById(R.id.start_button);
        if (msBeacon.getMajor() == 50 && msBeacon.getMinor() == 40 ) {
            startButton.setEnabled(true);
        } else {
            startButton.setEnabled(false);
        }
    }

    @Override
    public void exitedBeacon(Context context, MSBeacon msBeacon) {
        if (msBeacon.getMinor() !=40 ) {

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setMessage(R.string.dialog_message)
                    .setTitle(R.string.dialog_title);

            builder.setPositiveButton(R.string.dialog_leave, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                    activity.finish();
                }
            });


            AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);

            dialog.show();
        }
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
