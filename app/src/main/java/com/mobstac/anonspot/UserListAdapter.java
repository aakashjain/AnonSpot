package com.mobstac.anonspot;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;
import com.mobstac.anonspot.models.User;

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
        final TextView name = (TextView) v.findViewById(R.id.name);
        name.setText(model.getName());

        TextView gender = (TextView) v.findViewById(R.id.gender);
        switch (model.getGender()) {
            case "Male":
                gender.setText(AnonSpotConstants.MALE_SYM);
                break;
            case "Female":
                gender.setText(AnonSpotConstants.FEMALE_SYM);
                break;
            default:
                gender.setText(AnonSpotConstants.OTHER_SYM);
        }

        ImageButton button = (ImageButton) v.findViewById(R.id.goPrivate);
        button.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserListAdapter.this.activity, PrivateChat.class);
                intent.putExtra("user2", name.getText().toString());
                UserListAdapter.this.activity.startActivity(intent);
            }
        });
        if (model.getName().equals(AnonSpot.prefs.getString("name", " "))) {
            button.setEnabled(false);
            button.setVisibility(View.INVISIBLE);
            v.setBackgroundColor(AnonSpotConstants.USER_HIGHLIGHT);
        }
    }
}
