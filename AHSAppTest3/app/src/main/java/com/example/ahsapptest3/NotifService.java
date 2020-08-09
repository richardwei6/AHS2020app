package com.example.ahsapptest3;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class NotifService extends FirebaseMessagingService {
    private static final String TAG = "NotifService";
    
    public NotifService() {
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        /*
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (*//* Check if data needs to be processed by long running job *//* true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }*/

        // Also if you intend on generating your own notifications as a result of a received FCM
        Intent intent = new Intent(this, Notif_Activity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        String CHANNEL_ID = getResources().getString(R.string.notif_channel_ID);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.bookmarked_icon_inactive)
                .setColor(ContextCompat.getColor(getApplicationContext(),R.color.AngryRed_GoldenYellow_Midpoint))
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setPriority(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(getUniqueNotifID(), builder.build());
    }

    private static final String NOTIF_ID_FILE_KEY = "NOTIF_ID_FILE_KEY";
    private static final String NOTIF_ID_KEY = "NOTIF_ID_KEY";
    private int getUniqueNotifID() {
        SharedPreferences sharedPrefs = getApplicationContext().getSharedPreferences(NOTIF_ID_FILE_KEY, MODE_PRIVATE);
        int ID = sharedPrefs.getInt(NOTIF_ID_KEY, 0);
        SharedPreferences.Editor editor= sharedPrefs.edit();
        editor.putInt(NOTIF_ID_KEY, ID+1).apply();
        return ID;
    }

    private void handleNow() {
    }

    private void scheduleJob() {
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }
}
