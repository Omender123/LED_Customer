package com.lightning.master.ledbulb.firebase;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import androidx.legacy.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.lightning.master.ledbulb.R;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by
 */

public class FirebaseDataReceiver extends WakefulBroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("BroadcastReceiver::", "BroadcastReceiver");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(intent.getExtras().getString("title"))
                .setContentText(intent.getExtras().getString("message"));
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
}