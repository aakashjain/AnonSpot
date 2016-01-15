package com.mobstac.anonspot;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.mobstac.anonspot.receivers.ChattingBeaconReceiver;
import com.mobstac.beaconstac.core.Beaconstac;
import com.mobstac.beaconstac.core.BeaconstacReceiver;
import com.mobstac.beaconstac.utils.MSException;

public class PrivateChatActivity extends AppCompatActivity {

    private static final String TAG = PrivateChatActivity.class.getSimpleName();

    private Beaconstac beaconstac;
    private ChattingBeaconReceiver beaconReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_chat);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        beaconReceiver = new ChattingBeaconReceiver(this);
        beaconReceiver.registerBroadcast();

        beaconstac = Beaconstac.getInstance(getApplicationContext());
    }

    @Override
    protected void onStart() {
        super.onStart();

        String user1 = AnonSpot.prefs.getString("name", "");
        String user2 = getIntent().getStringExtra("user2");

        getSupportActionBar().setTitle("AnonSpot: Private with " + user2);

        String u1u2;
        if (user1.compareTo(user2) < 0) {
            u1u2 = user1 + "::" + user2;
        } else {
            u1u2 = user2 + "::" + user1;
        }

        ChatMessageFragment chatMessageFragment = ChatMessageFragment.newInstance(u1u2);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragment_container, chatMessageFragment);
        transaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        beaconReceiver.registerBroadcast();
        try {
            beaconstac.startRangingBeacons();
        } catch  (MSException e) {
            Log.e(TAG, "Couldn't start ranging");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        beaconReceiver.unregisterBroadcast();
        try {
            beaconstac.stopRangingBeacons();
        } catch  (MSException e) {
            Log.e(TAG, "Couldn't stop ranging");
        }
    }
}
