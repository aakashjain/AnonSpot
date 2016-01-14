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

    private final Activity activity;
    private AlertDialog dialog;
    private Button startButton;

    private static boolean atAnonSpot = false;

    public BeaconReceiver(Activity activity) {
        this.activity = activity;

        startButton = (Button) activity.findViewById(R.id.start_button);

//        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//        builder.setMessage(R.string.dialog_message)
//                .setTitle(R.string.dialog_title);
//
//        builder.setPositiveButton(R.string.dialog_leave, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                activity.finish();
//            }
//        });
//
//        dialog = builder.create();
//        dialog.setCanceledOnTouchOutside(false);
        //TODO: put this in the chat receiver ^
    }

    @Override
    public void rangedBeacons(Context context, ArrayList<MSBeacon> beacons) {

    }

    @Override
    public void campedOnBeacon(Context context, MSBeacon beacon) {
        TextView label = (TextView) activity.findViewById(R.id.text_view1);
        label.setText(beacon.getBeaconKey());
    }

    @Override
    public void exitedBeacon(Context context, MSBeacon beacon) {
        if (atAnonSpot) {
//            dialog.show(); TODO: put this in the other receiver
            startButton.setEnabled(false);
        }
    }

    @Override
    public void triggeredRule(Context context, String rule, ArrayList<MSAction> actions) {
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
