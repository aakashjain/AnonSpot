package com.mobstac.anonspot;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;

import java.util.Map;

/**
 * Created by aakash on 14/1/16.
 */
public class UserListAdapter extends FirebaseListAdapter<User> {

    private Activity activity;

    public UserListAdapter(Activity activity, int layout, Firebase ref) {
        super(activity, User.class, layout, ref);
        this.activity = activity;
    }

    @Override
    protected void populateView(View v, User model, int position) {
        TextView name = (TextView) v.findViewById(R.id.name);
        ImageView gender = (ImageView) v.findViewById(R.id.gender);
        name.setText(model.getName());
        switch (model.getGender()) {
            case "Male":
                gender.setImageResource(R.drawable.male);
                break;
            case "Female":
                gender.setImageResource(R.drawable.female);
                break;
            default:
                gender.setImageResource(R.drawable.other);
        }
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserListAdapter.this.activity, PrivateChat.class);
                UserListAdapter.this.activity.startActivity(intent);
            }
        });
    }
}
