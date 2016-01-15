package com.mobstac.anonspot;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.mobstac.anonspot.receivers.ChattingBeaconReceiver;
import com.mobstac.anonspot.utils.GenderSelector;
import com.mobstac.beaconstac.core.Beaconstac;
import com.mobstac.beaconstac.utils.MSException;

public class HolderActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ChattingBeaconReceiver beaconReceiver;
    private Beaconstac beaconstac;
    private ViewPager mViewPager;

    private static final String TAG = HolderActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holder);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        beaconReceiver = new ChattingBeaconReceiver(this);
        beaconstac = Beaconstac.getInstance(getApplicationContext());

        Toast.makeText(this, "Your name is: " + AnonSpot.prefs.getString("name", ""), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
        super.onBackPressed();
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

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0)
                return ChatMessageFragment.newInstance("global");
            else
                return new UserListFragment();
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Global Chat";
                case 1:
                    return "Online Users";
            }
            return null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AnonSpotConstants.USER_EXITED_SPOT && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }
}
