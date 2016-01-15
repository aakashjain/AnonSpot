package com.mobstac.anonspot;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class PrivateChat extends ListActivity {

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
        TextView user2 = (TextView) findViewById(R.id.pc_user);
        user2.setText(intent.getStringExtra("user2"));
    }
}
