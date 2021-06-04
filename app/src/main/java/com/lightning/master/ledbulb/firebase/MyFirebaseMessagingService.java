package com.lightning.master.ledbulb.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.lightning.master.ledbulb.R;
import com.lightning.master.ledbulb.Splash;

import org.json.JSONException;
import org.json.JSONObject;

import static android.app.NotificationManager.IMPORTANCE_DEFAULT;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;


/**
 * Created by Kunal on 22-Sep-17.
 */


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;
    public static String notification_action="";
    String  click_action="";
    String booking_id="",scroll_bottom="";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.i("test6", "test5: ");

        Log.i("notif", "From: " + remoteMessage.getFrom());

        Log.i("msg", "onMessageReceived: " + remoteMessage.getData().get("message"));

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
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

        click_action=remoteMessage.getNotification().getClickAction();
        Intent intent =new Intent(click_action);
        intent.putExtra("booking_id",booking_id);
        intent.putExtra("scroll_bottom",scroll_bottom);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        String channelId = "Default";
        NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
//                .setSound(defaultSoundUri)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody()).setAutoCancel(true).setContentIntent(pendingIntent);;
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }

        manager.notify(0, builder.build());

        super.onMessageReceived(remoteMessage);
    }

    private void handleNotification(String message) {
        Log.i(TAG, "test5: ");
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound)
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        }else{

            Log.i("test5", "test5: ");

            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.i("push", "" + json);

        try {
            JSONObject data = json;
            if (data.has("booking_id")){
                if (!data.isNull("booking_id")){

                    booking_id = data.getString("booking_id");
                }else{
                    booking_id="";
                }
            }else{
                booking_id="";
            }
            if (data.has("scroll_bottom")){
                if (!data.isNull("scroll_bottom")){

                    scroll_bottom = data.getString("scroll_bottom");
                }else{
                    scroll_bottom="";
                }
            }else{
                scroll_bottom="";
            }
            String title = "";
            String message = "";
            boolean isBackground = false;
            String imageUrl = "";
            String timestamp = "";
//            JSONObject payload = data.getJSONObject("payload");

            Log.e(TAG, "data 112211: " + data);
            Log.e(TAG, "notification_action:  112211" + notification_action);
//            Log.e(TAG, "NAME:  112211" + NAME);
            Log.e(TAG, "title:  112211" + title);
            Log.e(TAG, "message:  112211" + message);
            Log.e(TAG, "isBackground:  112211" + isBackground);
//            Log.e(TAG, "payload:  112211" + payload.toString());
            Log.e(TAG, "imageUrl:  112211" + imageUrl);
            Log.e(TAG, "timestamp: 112211 " + timestamp);


            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            } else {

                // app is in background, show the notification in notification tray
                Intent resultIntent = new Intent(getApplicationContext(), Splash.class);
                resultIntent.putExtra("message", message);

                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }
}