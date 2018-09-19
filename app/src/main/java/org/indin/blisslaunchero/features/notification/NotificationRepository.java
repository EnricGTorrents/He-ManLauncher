package org.indin.blisslaunchero.features.notification;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.jakewharton.rxrelay2.BehaviorRelay;

import android.app.Notification;
import android.service.notification.StatusBarNotification;
import android.util.Log;

/**
 * Created by Amit Kumar
 * Email : mr.doc10jl96@gmail.com
 */

public class NotificationRepository {

    private BehaviorRelay<Set<String>> notificationRelay;
    private static final String TAG = "NotificationRepository";

    private static NotificationRepository sInstance;

    private NotificationRepository() {
        notificationRelay = BehaviorRelay.createDefault(Collections.emptySet());
    }

    public static NotificationRepository getNotificationRepository() {
        if (sInstance == null) {
            sInstance = new NotificationRepository();
        }
        return sInstance;
    }

    public void updateNotification(List<StatusBarNotification> list) {
        Log.d(TAG, "updateNotification() called with: list = [" + list.size() + "]");
        Set<String> notificationSet = new HashSet<>();
        for (StatusBarNotification statusBarNotification : list) {
            Notification notification = statusBarNotification.getNotification();
            if ((notification.flags & Notification.FLAG_ONGOING_EVENT)
                    == Notification.FLAG_ONGOING_EVENT
                    || (notification.flags & Notification.FLAG_FOREGROUND_SERVICE)
                    == Notification.FLAG_FOREGROUND_SERVICE) {
                continue;
            }
            notificationSet.add(statusBarNotification.getPackageName());
        }
        this.notificationRelay.accept(notificationSet);
    }

    public BehaviorRelay<Set<String>> getNotifications() {
        return this.notificationRelay;
    }
}
