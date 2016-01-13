package com.mobstac.anonspot;

import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.mobstac.beaconstac.utils.MSException;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private Beaconstac beaconstac;
    private Firebase room;
    private Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

//        beaconstac = Beaconstac.getInstance(getApplicationContext());
//        beaconstac.setRegionParams(getString(R.string.uuid), getString(R.string.app_name));

        Firebase.setAndroidContext(getApplicationContext());
        room = new Firebase("https://anonspot.firebaseio.com/room");
        room.authAnonymously(new Firebase.AuthResultHandler() {

            @Override
            public void onAuthenticated(AuthData authData) {
                Log.i(TAG, "Authenticated");
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                Log.i(TAG, "Error authenticating: " + firebaseError.toString());
            }
        });

        //do stuff

//        try {
//            beaconstac.startRangingBeacons();
//        } catch  (MSException e) {
//            Log.e(TAG,"Couldn't start ranging");
//        }

        // add a click listener to start button
        startButton = (Button) findViewById(R.id.start_button);
        startButton.setOnClickListener(new MyOnclickListener());
    }

    @Override
    protected void onDestroy() {
//        try {
//            beaconstac.stopRangingBeacons();
//        } catch  (MSException e) {
//            Log.e(TAG,"Couldn't stop ranging");
//        }
        super.onDestroy();
    }

    protected class MyOnclickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent  = new Intent(MainActivity.this, HolderActivity.class);
            startActivity(intent);
        }
    }
}
