/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dennistjahyadigotama.soaya.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.dennistjahyadigotama.soaya.QuickstartPreferences;
import com.dennistjahyadigotama.soaya.R;
import com.dennistjahyadigotama.soaya.activities.KuponActivity.KuponActivity;
import com.dennistjahyadigotama.soaya.activities.PeopleProfileActivity.PeopleProfileActivity;
import com.dennistjahyadigotama.soaya.activities.ThreadOpenActivity.ThreadOpenActivity2;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    SharedPreferences sharedPreferences;
    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Feature containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]
        sharedPreferences = getSharedPreferences("prefs",MODE_PRIVATE);
        int totalNotif = sharedPreferences.getInt("totalNotif",0);
        sharedPreferences.edit().putInt("totalNotif",totalNotif++).apply();
        boolean allowNotif = sharedPreferences.getBoolean(QuickstartPreferences.Notification,true);
        boolean couponNotif = sharedPreferences.getBoolean(QuickstartPreferences.CouponNotification,true);
        String username = sharedPreferences.getString(QuickstartPreferences.USERNAME,null);
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());
            String em=sharedPreferences.getString(QuickstartPreferences.EMAIL,null);

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0 && em!=null) {
          //  Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if(remoteMessage.getData().get("type").equals("FriendRequestNotification"))
            {
                if(allowNotif) {
                    sendNotificationFriendRequest(remoteMessage.getData().get("type"), remoteMessage.getData().get("text"), remoteMessage.getData().get("by"));
                }
                int markNotice =  sharedPreferences.getInt(QuickstartPreferences.MARKNOTICE,0);
                sharedPreferences.edit().putInt(QuickstartPreferences.MARKNOTICE,(markNotice+1)).apply();
                Intent intent = new Intent("mainactivity");
                sendLocationBroadcast(intent);

            }else if(remoteMessage.getData().get("type").equals("ThreadNotification"))
            {
                if(!remoteMessage.getData().get("by").equals(username))
                {
                    if(allowNotif)
                    {
                    sendNotificationThread(remoteMessage.getData().get("type"), remoteMessage.getData().get("text"), remoteMessage.getData().get("threadId"));
                    }
                }

                int markNotice =  sharedPreferences.getInt(QuickstartPreferences.MARKNOTICE,0);
                sharedPreferences.edit().putInt(QuickstartPreferences.MARKNOTICE,(markNotice+1)).apply();
                Intent intent = new Intent("mainactivity");
                sendLocationBroadcast(intent);

            }else if(remoteMessage.getData().get("type").equals("NewThreadNotification"))
            {
                if(!remoteMessage.getData().get("by").equals(username))
                {
                    //InsertIntoTableNotification(remoteMessage.getData().get("by"),username,remoteMessage.getData().get("threadId"));
                    if(allowNotif) {
                        sendNotificationThread(remoteMessage.getData().get("type"), remoteMessage.getData().get("text"), remoteMessage.getData().get("threadId"));
                    }
                }

            }else if(remoteMessage.getData().get("type").equals("CouponNotification"))
            {
                if(!remoteMessage.getData().get("by").equals(username))
                {
                    if(allowNotif && couponNotif) {
                        sendNotificationCoupon(remoteMessage.getData().get("type"), remoteMessage.getData().get("text"), remoteMessage.getData().get("couponId"));
                    }
                }

            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
           // Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
           // sendNotification(remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void sendLocationBroadcast(Intent intent){

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


    private void sendNotificationFriendRequest(String type, String messageBody, String username) {
        Intent intent;
        if(type.equals("FriendRequestNotification"))
        {
            intent = new Intent(this, PeopleProfileActivity.class);
            intent.putExtra("username",username);
           // intent.putExtra("notifPosition",1);
        }
        else
        {
            intent = new Intent(this, PeopleProfileActivity.class);
            intent.putExtra("username",username);
        }


        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
       // Uri defaultSoundUri= Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.isnt);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_soayanot2)
                .setContentTitle("Soaya")
                .setContentText(messageBody)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
    private void sendNotificationThread(String type, String messageBody, String id) {
        Intent intent;

            intent = new Intent(this, ThreadOpenActivity2.class);
            intent.putExtra("ThreadId",id);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, Integer.parseInt(id) /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //Uri defaultSoundUri= Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.isnt);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_soayanot2)
                .setContentTitle("Soaya")
                .setContentText(messageBody)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(Integer.parseInt(id) /* ID of notification */, notificationBuilder.build());
    }

    private void sendNotificationCoupon(String type, String messageBody, String id) {
        Intent intent;

        intent = new Intent(this, KuponActivity.class);
        intent.putExtra("id",id);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, Integer.parseInt(id) /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //Uri defaultSoundUri= Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.isnt);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_soayanot2)
                .setContentTitle("Soaya")
                .setContentText(messageBody)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(Integer.parseInt(id) /* ID of notification */, notificationBuilder.build());
    }

}
