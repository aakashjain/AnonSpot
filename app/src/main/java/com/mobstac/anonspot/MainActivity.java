package com.mobstac.anonspot;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import com.mobstac.beaconstac.core.Beaconstac;
import com.mobstac.beaconstac.core.MSConstants;
import com.mobstac.beaconstac.utils.MSException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private Beaconstac beaconstac;
    
    private Button startButton;
    private Firebase ref;
    private SharedPreferences prefs;
    private BeaconReceiver beaconReceiver;
    private boolean registered = false;
    private boolean appInForeground = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

        beaconstac = Beaconstac.getInstance(getApplicationContext());
        beaconstac.setRegionParams(getString(R.string.uuid), getString(R.string.app_name));

        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        Firebase.setAndroidContext(getApplicationContext());
        ref = new Firebase(AnonSpotConstants.FIREBASE_URL);
        ref.authAnonymously(new Firebase.AuthResultHandler() {

            @Override
            public void onAuthenticated(AuthData authData) {
                Log.i(TAG, "Authenticated");
                new RandomNameGetter().execute(authData.getUid());
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                Log.i(TAG, "Error authenticating: " + firebaseError.toString());
            }
        });

        //do stuff

        try {
            beaconstac.startRangingBeacons();
        } catch  (MSException e) {
            Log.e(TAG,"Couldn't start ranging");
        }
        // add a click listener to start button
        startButton = (Button) findViewById(R.id.start_button);
        startButton.setOnClickListener(new MyOnClickListener());

        beaconReceiver = new BeaconReceiver(this);
        registerBroadcast();
    }

    private void registerBroadcast() {
        if (!registered) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(MSConstants.BEACONSTAC_INTENT_RANGED_BEACON);
            intentFilter.addAction(MSConstants.BEACONSTAC_INTENT_CAMPED_BEACON);
            intentFilter.addAction(MSConstants.BEACONSTAC_INTENT_EXITED_BEACON);
            intentFilter.addAction(MSConstants.BEACONSTAC_INTENT_RULE_TRIGGERED);
            intentFilter.addAction(MSConstants.BEACONSTAC_INTENT_ENTERED_REGION);
            intentFilter.addAction(MSConstants.BEACONSTAC_INTENT_EXITED_REGION);
            registerReceiver(beaconReceiver, intentFilter);
            registered = true;
        }
    }

    private void unregisterBroadcast() {
        if (registered) {
            unregisterReceiver(beaconReceiver);
            registered = false;
        }
    }

    @Override
    protected void onDestroy() {
        try {
            beaconstac.stopRangingBeacons();
        } catch  (MSException e) {
            Log.e(TAG,"Couldn't stop ranging");
        }
        super.onDestroy();
    }

    protected class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent  = new Intent(MainActivity.this, HolderActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterBroadcast();

        appInForeground = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerBroadcast();
        appInForeground = true;
    }

    private class RandomNameGetter extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... uids) {
            try {
                URL url = new URL(AnonSpotConstants.NAME_GEN_URL);
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                int responseCode = connection.getResponseCode();
                if (responseCode != HttpsURLConnection.HTTP_OK) {
                    Log.i(TAG, "Request not OK: " + String.valueOf(responseCode));
                }

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String response = reader.readLine();
                connection.disconnect();
                Log.i(TAG, "GET request successful");

                Map<String, String> user = new HashMap<String, String>();
                user.put("name", response);
                user.put("gender", prefs.getString("gender", ""));
                ref.child("users").child(uids[0]).setValue(user);

            } catch (Exception e) {
                Log.e(TAG, e.toString());
                e.printStackTrace();
            }
            return null;
        }
    }


}
