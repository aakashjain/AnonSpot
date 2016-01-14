package com.mobstac.anonspot;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    private ProgressDialog loader;
    private SearchingBeaconReceiver beaconReceiver;
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

        startButton = (Button) findViewById(R.id.start_button);
        startButton.setEnabled(false);
        startButton.setOnClickListener(new MyOnClickListener());

        beaconReceiver = new SearchingBeaconReceiver(this);
        registerBroadcast();
        beaconstac = Beaconstac.getInstance(getApplicationContext());
        beaconstac.syncRules();
        try {
            beaconstac.startRangingBeacons();
        } catch  (MSException e) {
            Log.e(TAG, "Couldn't start ranging");
        }
    }

    private void registerBroadcast() {
        if (!registered) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(MSConstants.BEACONSTAC_INTENT_CAMPED_BEACON);
            intentFilter.addAction(MSConstants.BEACONSTAC_INTENT_EXITED_BEACON);
            intentFilter.addAction(MSConstants.BEACONSTAC_INTENT_RULE_TRIGGERED);
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

    @Override
    protected void onStart() {
        super.onStart();
        if (AnonSpot.prefs.getString("gender", "-").equals("-")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final CharSequence[] items = new CharSequence[] {"Male", "Female", "Other"};
            builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences.Editor editor = AnonSpot.prefs.edit();
                    editor.putString("gender", items[which].toString());
                    editor.commit();
                    dialog.cancel();
                }
            });
            builder.show();
        }
    }

    protected class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            loader = ProgressDialog.show(MainActivity.this, "Just a sec",
                                        "Adding you to the room", true, true);

            AnonSpot.firebase.child(AnonSpot.spotBeaconKey).authAnonymously(new Firebase.AuthResultHandler() {

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
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        startButton.setEnabled(false);
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
                user.put("gender", AnonSpot.prefs.getString("gender", "-"));
                AnonSpot.firebase.child(AnonSpot.spotBeaconKey).child("users").child(uids[0]).setValue(user);

                SharedPreferences.Editor editor = AnonSpot.prefs.edit();
                editor.putString("name", response);
                editor.commit();

            } catch (Exception e) {
                Log.e(TAG, e.toString());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            beaconstac.setUserFacts("InAnonSpot", "true");
            loader.dismiss();
            Intent intent  = new Intent(MainActivity.this, HolderActivity.class);
            startActivity(intent);
        }
    }
}
