package com.mobstac.anonspot.receivers;

import android.app.Activity;
import android.content.Context;

import android.content.IntentFilter;
import android.util.Log;
import android.widget.Button;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.mobstac.anonspot.AnonSpot;
import com.mobstac.anonspot.R;
import com.mobstac.anonspot.models.Genders;
import com.mobstac.beaconstac.core.Beaconstac;
import com.mobstac.beaconstac.core.BeaconstacReceiver;
import com.mobstac.beaconstac.core.MSConstants;
import com.mobstac.beaconstac.core.MSPlace;
import com.mobstac.beaconstac.models.MSAction;
import com.mobstac.beaconstac.models.MSBeacon;

import java.util.ArrayList;

/**
 * Created by aakash on 13/1/16.
 */
public class SearchingBeaconReceiver extends BeaconstacReceiver {

    private Activity activity;
    private Button startButton;
    private boolean registered;

    public SearchingBeaconReceiver(final Activity activity) {
        startButton = (Button) activity.findViewById(R.id.start_button);
        registered = false;
        this.activity = activity;
    }

    @Override
    public void rangedBeacons(Context context, ArrayList<MSBeacon> beacons) {

    }

    @Override
    public void campedOnBeacon(Context context, MSBeacon beacon) {
        AnonSpot.spotBeaconKey = beacon.getBeaconKey();
        Log.wtf("sb Camped on", AnonSpot.spotBeaconKey);
    }

    @Override
    public void exitedBeacon(Context context, MSBeacon beacon) {
        startButton.setEnabled(false);
    }

    @Override
    public void triggeredRule(Context context, String rule, ArrayList<MSAction> actions) {
        switch (rule) {
            case "EnterAnonSpotStep1":
                setGenderFact(context);
                break;
            case "EnterAnonSpotStep2":
                startButton.setEnabled(true);
                break;
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

    private void setGenderFact(final Context context) {
        final String gender = AnonSpot.prefs.getString("gender", "Other");
        Firebase genderStore = AnonSpot.firebase.child(AnonSpot.spotBeaconKey).child("genders");
        genderStore.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Genders current = dataSnapshot.getValue(Genders.class);
                float ratio;
                if (current != null) {
                    ratio = current.getRatio(gender);
                } else {
                    ratio = 0;
                }
                Beaconstac.getInstance(context.getApplicationContext()).setUserFacts("ratio", ratio);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("FIREBASE", "Error fetching genders");
            }
        });
    }

    public void registerBroadcast() {
        if (!registered) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(MSConstants.BEACONSTAC_INTENT_CAMPED_BEACON);
            intentFilter.addAction(MSConstants.BEACONSTAC_INTENT_EXITED_BEACON);
            intentFilter.addAction(MSConstants.BEACONSTAC_INTENT_RULE_TRIGGERED);
            activity.registerReceiver(this, intentFilter);
            registered = true;
        }
    }

    public void unregisterBroadcast() {
        if (registered) {
            activity.unregisterReceiver(this);
            registered = false;
        }
    }
}
