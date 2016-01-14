package com.mobstac.anonspot;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Button;

import com.mobstac.beaconstac.core.BeaconstacReceiver;
import com.mobstac.beaconstac.core.MSPlace;
import com.mobstac.beaconstac.models.MSAction;
import com.mobstac.beaconstac.models.MSBeacon;

import java.util.ArrayList;

/**
 * Created by mobstac on 14/1/16.
 */
public class BeaconReceiver2 extends BeaconstacReceiver{

    private AlertDialog dialog;
    private final Activity activity;
    private static boolean atAnonSpot = false;

    public BeaconReceiver2(final Activity activity) {
        this.activity = activity;

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(R.string.dialog_message)
                .setTitle(R.string.dialog_title);

        builder.setPositiveButton(R.string.dialog_leave, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                activity.finish();
            }
        });

        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void rangedBeacons(Context context, ArrayList<MSBeacon> arrayList) {

    }

    @Override
    public void campedOnBeacon(Context context, MSBeacon msBeacon) {
        Log.wtf("br_2 camped on", msBeacon.getBeaconKey() + msBeacon.getMinor());
//        if(msBeaoon.getMajor()==50 && msBeacon.getMinor()==40)
//        {
//            atAnonSpot=true;
//        }
//        else
//        {
//            atAnonSpot=false;
//        }
        if (dialog.isShowing()) dialog.dismiss();
    }

    @Override
    public void exitedBeacon(Context context, MSBeacon msBeacon) {
        if (msBeacon.getMinor() == 40) {
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
