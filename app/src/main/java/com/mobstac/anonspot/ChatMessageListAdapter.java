package com.mobstac.anonspot;

import android.app.Activity;
import android.text.Html;
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
    private static final Html female = Html.fromHtml("&#x2640;");
    private static final Html male = Html.fromHtml("&#x2642;");
    private static final Html other = Html.fromHtml("&#x25CB;");


    public ChatMessageListAdapter(Activity activity, int layout, Firebase ref) {
        super(activity, ChatMessage.class, layout, ref);
        this.activity = activity;
    }

    @Override
    protected void populateView(View v, ChatMessage model, int position) {
        TextView name = (TextView) v.findViewById(R.id.name);
        TextView message = (TextView) v.findViewById(R.id.message);
        name.setText(Html.fromHtml("&#x2640;") + " " + model.getName() + " : ");
        message.setText(model.getMessage());
    }
}
