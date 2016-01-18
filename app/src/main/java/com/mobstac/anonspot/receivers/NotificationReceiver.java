package com.mobstac.anonspot.receivers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.mobstac.anonspot.MainActivity;
import com.mobstac.beaconstac.core.DismissNotification;

/**
 * Created by aakash on 18/1/16.
 */
public class NotificationReceiver extends DismissNotification {
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        NotificationManager manager = (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
//        String useCase = intent.getStringExtra("case");
//        if (useCase.equals("dismiss")) {
//            manager.cancel(intent.getIntExtra("notificationID", 1));
//        } else {
//            Intent openApp = new Intent(context.getApplicationContext(), MainActivity.class);
//            openApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, openApp, 0);
//
//            context.getApplicationContext().startActivity(openApp);
//            manager.cancel(intent.getIntExtra("notificationID", 1));
//        }
//    }
}
