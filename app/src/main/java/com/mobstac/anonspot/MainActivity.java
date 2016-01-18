package com.mobstac.anonspot;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import com.firebase.client.MutableData;
import com.firebase.client.Transaction;
import com.firebase.client.ValueEventListener;
import com.mobstac.anonspot.models.Genders;
import com.mobstac.anonspot.receivers.SearchingBeaconReceiver;
import com.mobstac.anonspot.utils.AnonSpotConstants;
import com.mobstac.anonspot.utils.GenderSelector;
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

        beaconstac = Beaconstac.getInstance(getApplicationContext());

        beaconReceiver = new SearchingBeaconReceiver(this);
        registerBroadcast();

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
            intentFilter.addAction(MSConstants.BEACONSTAC_INTENT_ENTERED_REGION);
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
    protected void onStart() {
        super.onStart();
        if (AnonSpot.prefs.getString("gender", "-").equals("-")) {
            GenderSelector.show(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        startButton.setEnabled(false);
        try {
            beaconstac.stopRangingBeacons();
        } catch  (MSException e) {
            Log.e(TAG, "Couldn't stop ranging");
        }
        unregisterBroadcast();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AnonSpot.firebase.getAuth() != null) {
            String uid = AnonSpot.firebase.getAuth().getUid();
            AnonSpot.firebase.child(AnonSpot.spotBeaconKey).child("users").child(uid).removeValue();
            Beaconstac.getInstance(getApplicationContext()).setUserFacts("InAnonSpot", "false");
            Log.i(TAG, "CLEANING UP YAY");
        }
        registerBroadcast();
        try {
            beaconstac.startRangingBeacons();
        } catch  (MSException e) {
            Log.e(TAG, "Couldn't start ranging");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_global_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            GenderSelector.show(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            loader = ProgressDialog.show(MainActivity.this, "Just a sec",
                    "Adding you to the room", true, true);
            loader.setCanceledOnTouchOutside(false);

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
//            loader.setMessage("Sorry, the gender ratio is too skewed to let you in :(");
//            final Handler h = new Handler() {
//                @Override
//                public void handleMessage(Message msg) {
//                    loader.dismiss();
//                }
//            };
//            h.sendMessageDelayed(h.obtainMessage(), 5000);
        }
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

                final String gender = AnonSpot.prefs.getString("gender", "Other");
                Map<String, String> user = new HashMap<String, String>();
                user.put("name", response);
                user.put("gender", gender);
                AnonSpot.firebase.child(AnonSpot.spotBeaconKey).child("users").child(uids[0]).setValue(user);


                AnonSpot.firebase.child(AnonSpot.spotBeaconKey).child("genders")
                                            .runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        Genders g = mutableData.getValue(Genders.class);
                        if (g == null) {
                            g = new Genders(0, 0, 0);
                        }
                        g.increment(gender);
                        mutableData.setValue(g);
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(FirebaseError firebaseError, boolean b, DataSnapshot dataSnapshot) {
                    }
                });

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
            startActivityForResult(intent, AnonSpotConstants.USER_EXITED_SPOT);
        }
    }
}
