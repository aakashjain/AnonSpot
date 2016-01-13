package com.mobstac.anonspot;

import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import com.mobstac.beaconstac.core.Beaconstac;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private Beaconstac beaconstac;
    private Firebase room;

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
    }
}
