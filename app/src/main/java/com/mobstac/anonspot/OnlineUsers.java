package com.mobstac.anonspot;

import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


public class OnlineUsers extends ListFragment {

    public OnlineUsers() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        final ListView list = getListView();
        UserListAdapter adapter = new UserListAdapter(getActivity(), R.layout.user_view,
                                    AnonSpot.firebase.child(AnonSpot.spotBeaconKey).child("users"));
        list.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_online_users, container, false);
        return v;
    }
}
