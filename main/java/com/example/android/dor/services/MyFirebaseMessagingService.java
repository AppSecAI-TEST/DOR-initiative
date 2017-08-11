package com.example.android.dor.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.android.dor.ManagerClass.SessionManager;
import com.example.android.dor.activity.MainActivity;
import com.example.android.dor.activity.NotificationActivity;
import com.example.android.dor.app.Config;
import com.example.android.dor.objectClass.NotificationDetails;
import com.example.android.dor.utils.NotificationUtils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by darip on 14-06-2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;
    private NotificationDetails notificationDetails;
    SessionManager session;

//    @Override
//    public void onCreate() {
//        super.onCreate();
//        session = new SessionManager(getApplicationContext());
//    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        session = new SessionManager(getApplicationContext());



        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
//            if(session.isLoggedIn()) {
                handleNotification(remoteMessage.getNotification().getBody());
//            }
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());

                    handleDataMessage(json);

            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        }else{
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");
            String title = data.getString("title");
            String message = data.getString("message");
//            boolean isBackground = data.getBoolean("is_background");
            String timestamp = data.getString("timestamp");
            JSONObject payload = data.getJSONObject("payload");
            String nid = payload.getString("nid");
            String nName = payload.getString("nName");
            String nDetail = payload.getString("nDetail");
            String nType = payload.getString("nType");
            String nTimestamp = payload.getString("nTimestamp");
            notificationDetails = new NotificationDetails(title, message, nName, nid, nDetail, nType, nTimestamp, timestamp);

            Log.e(TAG, "title: " + notificationDetails.getTitle());
            Log.e(TAG, "message: " + notificationDetails.getMsg());
            Log.e(TAG, "isBackground: " + notificationDetails.getIsBackground());
            Log.e(TAG, "timestamp: " + notificationDetails.getTimeStamp());
            Log.e(TAG, "nDetail: " + notificationDetails.getnDetail());
            Log.e(TAG, "nType: " + notificationDetails.getnType());
            Log.e(TAG, "nTimestamp: " + notificationDetails.getnTimestamp());
            Log.e(TAG, "nid: " + notificationDetails.getNid());
            Log.e(TAG, "notificationName: " + notificationDetails.getNotificationName());


            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("count", NotificationUtils.getCount());
                Log.e("Count",  NotificationUtils.getCount()+"");
                pushNotification.putExtra("title", notificationDetails.getTitle());
                pushNotification.putExtra("message", notificationDetails.getMsg());
                pushNotification.putExtra("timestamp", notificationDetails.getTimeStamp());
                pushNotification.putExtra("nDetail", notificationDetails.getnDetail());
                pushNotification.putExtra("nType", notificationDetails.getnType());
                pushNotification.putExtra("nTimestamp", notificationDetails.getnTimestamp());
                pushNotification.putExtra("nid", notificationDetails.getNid());
                pushNotification.putExtra("notificationName", notificationDetails.getNotificationName());
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            } else {
                // app is in background, show the notification in notification tray
                Log.e("LogIn", session.isLoggedIn()+"");
                if(session.isLoggedIn()) {
                    Intent resultIntent = new Intent(getApplicationContext(), NotificationActivity.class);
                    resultIntent.putExtra("message", notificationDetails.getTitle());
                    resultIntent.putExtra("countFromTray", NotificationUtils.getCount());
                    resultIntent.putExtra("title", notificationDetails.getTitle());
//                    resultIntent.putExtra("message", notificationDetails.getMsg());
                    resultIntent.putExtra("timestamp", notificationDetails.getTimeStamp());
                    resultIntent.putExtra("nDetail", notificationDetails.getnDetail());
                    resultIntent.putExtra("nType", notificationDetails.getnType());
                    resultIntent.putExtra("nTimestamp", notificationDetails.getnTimestamp());
                    resultIntent.putExtra("nid", notificationDetails.getNid());
                    resultIntent.putExtra("notificationName", notificationDetails.getNotificationName());
                    // check for image attachment
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    public NotificationDetails getDetail(){
        return notificationDetails;
    }

}


