package com.example.hello.coachingapp;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagingServices extends FirebaseMessagingService{
    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String refereshedtoken = instanceIdResult.getToken();
                Log.d("Hello", "initFCM: token: " + refereshedtoken);
                sendRegistrationToServer(refereshedtoken);
            }
        });
    }

    private void sendRegistrationToServer(String token) {
        Log.d("Hello", "sendRegistrationToServer: sending token to server: " + token);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("messaging_token")
                .setValue(token);
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
        String notificationData = "";
        try{
            notificationData = remoteMessage.getData().toString();
            notificationTitle = remoteMessage.getNotification().getTitle();
            notificationBody = remoteMessage.getNotification().getBody();
        }catch (NullPointerException e){
            Log.e("Hello", "onMessageReceived: NullPointerException: " + e.getMessage() );
        }
        Log.d("Hello", "onMessageReceived: data: " + notificationData);
        Log.d("Hello", "onMessageReceived: notification body: " + notificationBody);
        Log.d("Hello", "onMessageReceived: notification title: " + notificationTitle);


        String dataType = remoteMessage.getData().get("data_type");
        if(dataType.equals("direct_message")){
            Log.d("Hello", "onMessageReceived: new incoming message.");
            String title = remoteMessage.getData().get("data_title");
            String message = remoteMessage.getData().get("data_message");
            String messageId = remoteMessage.getData().get("data_message_id");
            sendMessageNotification(title, message, messageId);
        }
    }

    @SuppressLint("ResourceAsColor")
    private void sendMessageNotification(String title, String message, String messageId){
        Log.d("Hello", "sendChatmessageNotification: building a chatmessage notification");

        //get the notification id
        int notificationId = buildNotificationId(messageId);

        // Instantiate a Builder object.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,
                "default_notification_channel_id");
        // Creates an Intent for the Activity
        Intent pendingIntent = new Intent(this, MainActivity.class);
        // Sets the Activity to start in a new, empty task
        pendingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // Creates the PendingIntent
        PendingIntent notifyPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        pendingIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        //add properties to the builder
        builder.setSmallIcon(R.drawable.ic_access_time_black_24dp)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                        R.drawable.ic_access_time_black_24dp))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentTitle(title)
                .setColor(R.color.colorPrimary)
                .setAutoCancel(true)
                //.setSubText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setOnlyAlertOnce(true);

        builder.setContentIntent(notifyPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(notificationId, builder.build());

    }

    private int buildNotificationId(String id){
        Log.d("Hello", "buildNotificationId: building a notification id.");

        int notificationId = 0;
        for(int i = 0; i < 9; i++){
            notificationId = notificationId + id.charAt(0);
        }
        Log.d("Hello", "buildNotificationId: id: " + id);
        Log.d("Hello", "buildNotificationId: notification id:" + notificationId);
        return notificationId;
    }

}
