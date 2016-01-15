package com.mobstac.anonspot;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
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
    private EditText input;
    private ImageButton send;

    public GlobalChat() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        final ListView list = getListView();
        final ChatMessageListAdapter adapter = new ChatMessageListAdapter(getActivity(), R.layout.chatmessage_view,
                AnonSpot.firebase.child(AnonSpot.spotBeaconKey).child("global").limitToLast(100));
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
        input = (EditText) v.findViewById(R.id.messageInput);
        input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    sendMessage();
                    return true;
                }
                return false;
            }
        });
        send = (ImageButton) v.findViewById(R.id.sendButton);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        return v;
    }

    private void sendMessage() {
        String message = input.getText().toString();
        String name = AnonSpot.prefs.getString("name", "-");
        String gender;
        switch (AnonSpot.prefs.getString("gender", "Other")) {
            case "Male":
                gender = (String) Html.fromHtml("&#x2642;").toString();
                break;
            case "Female":
                gender = (String) Html.fromHtml("&#x2640;").toString();
                break;
            default:
                gender = (String) Html.fromHtml("&#x25CB;").toString();
        }
        if (!message.equals("")) {
            ChatMessage cm = new ChatMessage(gender + " " + name, message);
            AnonSpot.firebase.child(AnonSpot.spotBeaconKey).child("global").push().setValue(cm);
            input.setText("");
        }
    }
}
