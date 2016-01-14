package com.mobstac.anonspot;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Button;

import com.mobstac.beaconstac.core.Beaconstac;
import com.mobstac.beaconstac.core.BeaconstacReceiver;
import com.mobstac.beaconstac.core.MSPlace;
import com.mobstac.beaconstac.models.MSAction;
import com.mobstac.beaconstac.models.MSBeacon;

import java.util.ArrayList;

/**
 * Created by mobstac on 14/1/16.
 */
public class ChattingBeaconReceiver extends BeaconstacReceiver{

    private AlertDialog dialog;
    private final Activity activity;
    private boolean inSameSpot = true;

    public ChattingBeaconReceiver(final Activity activity) {
        this.activity = activity;

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(R.string.dialog_message).setTitle(R.string.dialog_title);

        builder.setPositiveButton(R.string.dialog_leave, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                activity.finish();
            }
        });

        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void rangedBeacons(Context context, ArrayList<MSBeacon> beacons) {

    }

    @Override
    public void campedOnBeacon(Context context, MSBeacon beacon) {
        if (beacon.getBeaconKey().equals(AnonSpot.spotBeaconKey)) {
            dialog.dismiss();
        }
    }

    @Override
    public void exitedBeacon(Context context, MSBeacon beacon) {

    }

    @Override
    public void triggeredRule(Context context, String rule, ArrayList<MSAction> actions) {
        if (rule.equals("ExitAnonSpot")) {
            dialog.show();
        }
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
