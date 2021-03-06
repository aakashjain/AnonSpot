package com.mobstac.anonspot;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.mobstac.anonspot.models.ChatMessage;
import com.mobstac.anonspot.utils.AnonSpotConstants;
import com.mobstac.anonspot.utils.KeyboardHider;

public class ChatMessageListFragment extends ListFragment {

    public static final String TAG = ChatMessageListFragment.class.getSimpleName();

    private Firebase chatroom;

    private EditText input;
    private ImageButton send;

    public ChatMessageListFragment() {}

    public static ChatMessageListFragment newInstance(String child) {
        ChatMessageListFragment fragment = new ChatMessageListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("child", child);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String child = getArguments().getString("child");
        chatroom = AnonSpot.firebase.child(AnonSpot.spotBeaconKey).child(child);
    }

    @Override
    public void onStart() {
        super.onStart();
        final ListView list = getListView();
        final ChatMessageListAdapter adapter = new ChatMessageListAdapter(getActivity(),
                                                R.layout.chatmessage_view, chatroom.limitToLast(100));
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
        View v = inflater.inflate(R.layout.fragment_chat, container, false);

        KeyboardHider hider = new KeyboardHider(getActivity());
        hider.setupUI(v);

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
        String name = AnonSpot.prefs.getString("name", " ");
        String gender;
        switch (AnonSpot.prefs.getString("gender", "Other")) {
            case "Male":
                gender = AnonSpotConstants.MALE_SYM;
                break;
            case "Female":
                gender = AnonSpotConstants.FEMALE_SYM;
                break;
            default:
                gender = AnonSpotConstants.OTHER_SYM;
        }
        if (!message.equals("")) {
            ChatMessage cm = new ChatMessage(gender, name, message);
            chatroom.push().setValue(cm);
            input.setText("");
        }
    }
}