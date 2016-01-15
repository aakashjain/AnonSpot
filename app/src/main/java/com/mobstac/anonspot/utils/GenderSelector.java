package com.mobstac.anonspot.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;

import com.mobstac.anonspot.AnonSpot;

/**
 * Created by aakash on 15/1/16.
 */
public class GenderSelector {
    public static void show(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final CharSequence[] items = new CharSequence[] {"Male", "Female", "Other"};
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences.Editor editor = AnonSpot.prefs.edit();
                editor.putString("gender", items[which].toString());
                editor.commit();
                dialog.cancel();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }
}
