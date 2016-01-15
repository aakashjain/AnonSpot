package com.mobstac.anonspot;

import android.app.Fragment;
import android.app.ListActivity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class PrivateChat extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_chat);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        Log.wtf("private chat", intent.getStringExtra("user2"));
        TextView user = (TextView) findViewById(R.id.pc_user);
        user.setText(intent.getStringExtra("user2"));

        String user1 = AnonSpot.prefs.getString("name", "");
        String user2 = intent.getStringExtra("user2");

        String u1u2;
        if (user1.compareTo(user2) > 0) u1u2 = user1+user2;
        else u1u2 = user2 + user1;
        ChatMessageFragment chatMessageFragment = ChatMessageFragment.newInstance(u1u2);
        getSupportFragmentManager().beginTransaction().add(R.id.chat_fragment, chatMessageFragment);
    }
}
