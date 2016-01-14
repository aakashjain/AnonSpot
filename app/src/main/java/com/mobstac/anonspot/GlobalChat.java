package com.mobstac.anonspot;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class GlobalChat extends ListFragment {

    public static final String TAG = GlobalChat.class.getSimpleName();

    public GlobalChat() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        final ListView list = getListView();
        final UserListAdapter adapter = new UserListAdapter(getActivity(), R.layout.chatmessage_view,
                AnonSpot.firebase.child(AnonSpot.spotBeaconKey).child("global"));
        list.setAdapter(adapter);
        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                list.setSelection(adapter.getCount() - 1);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_global_chat, container, false);
        EditText input = (EditText) v.findViewById(R.id.messageInput);
        input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    sendMessage();
                }
                return true;
            }
        });
        v.findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        return v;
    }

    private void sendMessage() {
        EditText input = (EditText) getView().findViewById(R.id.messageInput);
        String message = input.getText().toString();
        String name = AnonSpot.prefs.getString("name", "-");
        if (!message.equals("")) {
            ChatMessage cm = new ChatMessage(name, message);
            AnonSpot.firebase.child(AnonSpot.spotBeaconKey).child("global").push().setValue(cm);
            input.setText("");
        }
    }
}
