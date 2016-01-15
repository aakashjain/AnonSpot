package com.mobstac.anonspot.receivers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.util.Log;

import com.mobstac.anonspot.AnonSpot;
import com.mobstac.anonspot.R;
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
    private CountDownTimer countDownTimer;
    private final Activity activity;
    private boolean inSameSpot = true;

    public ChattingBeaconReceiver(final Activity activity) {
        this.activity = activity;

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(R.string.dialog_message).setTitle(R.string.dialog_title);

        builder.setPositiveButton(R.string.dialog_leave, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                countDownTimer.cancel();
                activity.finish();
            }
        });

        dialog = builder.create();
//        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        // count down timer
        countDownTimer = new CountDownTimer(10000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (dialog.isShowing()) {
                    dialog.setMessage( "Get back to your AnonSpot in " + millisUntilFinished/1000 + " seconds.");
                }
            }

            @Override
            public void onFinish() {
                if (dialog.isShowing()) {
                    dialog.cancel();
                    activity.finish();
                }
            }
        };
    }

    @Override
    public void rangedBeacons(Context context, ArrayList<MSBeacon> beacons) {

    }

    @Override
    public void campedOnBeacon(Context context, MSBeacon beacon) {
        Log.wtf("cb Camped on", AnonSpot.spotBeaconKey);
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
            countDownTimer.start();
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
