package com.hsappdev.ahs;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.hsappdev.ahs.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class NotifService extends FirebaseMessagingService {
    /*private static final String TAG = "NotifService";*/
    
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
        Intent intent = null;
        if(remoteMessage.getData().size() > 0) {
            /*for(Map.Entry<String, String> entry: remoteMessage.getData().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                Log.d(TAG, "key, " + key + " value " + value);
            }*/

            String articleID = remoteMessage.getData().get(getResources().getString(R.string.notif_articleID_key));
            if(articleID != null) {
                Article article = ArticleDatabase.getInstance(getApplicationContext()).getArticleById(articleID);
                if(article!=null) {

                    intent = new Intent(this, ArticleActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra(ArticleActivity.data_key, article);
                }
                else {
                    Bulletin_Article bulletin_article = BulletinDatabase.getInstance(getApplicationContext()).getArticleByID(articleID);
                    if(bulletin_article != null ){
                        intent = new Intent(this, Bulletin_Article_Activity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.putExtra(ArticleActivity.data_key, bulletin_article);
                    }
                }
            }
        }
        if(intent == null) {
            intent = new Intent(this, Notif_Activity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, getUniqueNotifID(), intent, 0);

        String CHANNEL_ID = getResources().getString(R.string.notif_channel_ID);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.downsize_feather)
                .setColor(ContextCompat.getColor(getApplicationContext(),R.color.AngryRed_9F0C0C))
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
        int ID = sharedPrefs.getInt(NOTIF_ID_KEY, 1);
        SharedPreferences.Editor editor= sharedPrefs.edit();
        editor.putInt(NOTIF_ID_KEY, ID+1).apply();
        return ID;
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }
}
