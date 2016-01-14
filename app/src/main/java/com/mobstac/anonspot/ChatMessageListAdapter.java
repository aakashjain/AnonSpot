package com.mobstac.anonspot;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;

/**
 * Created by aakash on 14/1/16.
 */
public class ChatMessageListAdapter extends FirebaseListAdapter<ChatMessage> {

    private Activity activity;

    public ChatMessageListAdapter(Activity activity, int layout, Firebase ref) {
        super(activity, ChatMessage.class, layout, ref);
        this.activity = activity;
    }

    @Override
    protected void populateView(View v, ChatMessage model, int position) {
        TextView name = (TextView) v.findViewById(R.id.name);
        TextView message = (TextView) v.findViewById(R.id.message);
        name.setText(model.getName());
        message.setText(model.getMessage());
    }
}
