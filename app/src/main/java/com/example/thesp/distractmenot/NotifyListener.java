package com.example.thesp.distractmenot;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thesp on 3/19/2018.
 */

public class NotifyListener extends NotificationListenerService {

    private List<String> blockedApps;

    public void setBlockedApps(List<String> newList) {
        blockedApps = newList;
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.i("AppControl2","onNotificationPosted called");
        Notification mNotification=sbn.getNotification();
        if (mNotification!=null) {
            Bundle extras = mNotification.extras;
            ApplicationInfo ai = (ApplicationInfo) extras.get("android.appInfo");
            Log.i("AppControl2", extras.toString());
            if (ai.name != null) {
                Log.i("AppControl2", ai.name);

                // Look for notifications to clear/block
                if (blockedApps.contains(ai.name)) {
                    cancelNotification(sbn.getKey());
                    Log.i("AppControl2", "Clearing Notification for requested app.");
                }
            }
        }
    }

   /* @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }*/

    @Override
    public void onCreate() {
        super.onCreate();
        // Register for broadcast messages sent from the AppControl2 activity
        AppNotificationServiceListener listener = new AppNotificationServiceListener();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.thesp.distractmenot.Broadcasts");
        registerReceiver(listener,intentFilter);
        blockedApps = new ArrayList<>();

    }

    class AppNotificationServiceListener extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Look in the intent for the app name to block
            if (intent.hasExtra("block_app")) {
                blockedApps.add(intent.getStringExtra("block_app"));
                Log.i("AppControl2","Received broadcast from activity to block.");
            }
        }
    }
}
