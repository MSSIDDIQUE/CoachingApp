package com.saquib.hello.coachingapp;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class FirebaseMessagingServices extends FirebaseMessagingService{

    @Override
    public void onNewToken(final String token) {
        super.onNewToken(token);
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String refereshedtoken = instanceIdResult.getToken();
                Log.d("Hello", "initFCM: token: " + refereshedtoken);
                SharedHelper.setDefaults("tokenId",token,getApplicationContext());
            }
        });
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String notificationBody = "";
        String notificationTitle = "";
        if (remoteMessage.getNotification() != null) {
            Map<String,String> data = remoteMessage.getData();
            displayNotification(getApplicationContext(),data.get("title"),data.get("body"));
        }
    }

    public static void displayNotification(Context context, String title, String body) {

            Intent intent = new Intent(context, MainActivity.class);

            PendingIntent pendingIntent = PendingIntent.getActivity(
                    context,
                    100,
                    intent,
                    PendingIntent.FLAG_CANCEL_CURRENT
            );

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context, "Study Solutions")
                            .setSmallIcon(R.drawable.appicon)
                            .setContentTitle(title)
                            .setContentText(body)
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true)
                            .setPriority(NotificationCompat.PRIORITY_HIGH);

            NotificationManagerCompat mNotificationMgr = NotificationManagerCompat.from(context);
            mNotificationMgr.notify(0, mBuilder.build());

        }

}
