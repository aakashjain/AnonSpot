package com.mobstac.anonspot;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class PrivateChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_chat);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        Intent intent = getIntent();
//        Log.wtf("private chat", intent.getStringExtra("user2"));
//        TextView user = (TextView) findViewById(R.id.pc_user);
//        user.setText(intent.getStringExtra("user2"));

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
}
