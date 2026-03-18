package com.utils;

import android.app.PendingIntent;
import android.os.Build;

public class IntentAction {
    public final static String NOTIFICATION_CLICK = "com.tiktak24.notification.click";
    public final static String NOTIFICATION_CLOSE = "com.tiktak24.notification.close";
    public final static String NOTIFICATION_VIEW_ORDER = "com.tiktak24.notification.vieworder";
    public final static String NOTIFICATION_TRACK_ORDER = "com.tiktak24.notification.trackorder";

    public static int getPendingIntentFlag() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return PendingIntent.FLAG_MUTABLE;
        } else {
            return PendingIntent.FLAG_UPDATE_CURRENT;
        }
    }
}