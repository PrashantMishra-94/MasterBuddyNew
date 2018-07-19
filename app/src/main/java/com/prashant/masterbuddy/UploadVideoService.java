package com.prashant.masterbuddy;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.prashant.masterbuddy.ws.JsonServices;
import com.prashant.masterbuddy.ws.VolleyCallback;

import java.util.Random;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 */
public class UploadVideoService extends IntentService {

    private static final String USER_ID= "user_id";
    private static final String TITLE= "title";
    private static final String DESCRIPTION = "description";

    public UploadVideoService() {
        super("UploadVideoService");
    }

    public static void startServiceUpload(Context ctx, Uri path, String title, String description, int userId) {
        Intent intent = new Intent(ctx, UploadVideoService.class);
        intent.setData(path);
        intent.putExtra(TITLE, title);
        intent.putExtra(DESCRIPTION, description);
        intent.putExtra(USER_ID, userId);
        ctx.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            Uri path = intent.getData();
            String title = intent.getStringExtra(TITLE);
            String desc = intent.getStringExtra(DESCRIPTION);
            int userId = intent.getIntExtra(USER_ID, 0);
            handleActionImageUpload(path, title, desc, userId);
        }
    }

    private void handleActionImageUpload(Uri path, final String title, String desc, int userId) {
        final int notificationId = new Random().nextInt();
        final NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(Constants.NOTIFICATION_CHANNEL_ID,
                    getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);
            if (mNotificationManager != null)
                mNotificationManager.createNotificationChannel(channel);
        }
        final NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_notification);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            builder.setColor(getResources().getColor(R.color.Green));
        }
        builder.setContentTitle("Uploading " + title);
        builder.setOngoing(true);

        JsonServices services = new JsonServices(this, new VolleyCallback() {
            @Override
            public void starting() {
                if (mNotificationManager != null) {
                    mNotificationManager.notify(notificationId, builder.build());
                }
            }

            @Override
            public void onSuccess(Object objectFromJson) {
                if (mNotificationManager != null) {
                    builder.setContentTitle(title + " uploaded");
                    builder.setOngoing(false);
                    mNotificationManager.notify(notificationId, builder.build());
                }
            }

            @Override
            public void onSuccess(boolean isSuccess) {
                if (mNotificationManager != null) {
                    builder.setContentTitle(title + " uploaded");
                    builder.setOngoing(false);
                    mNotificationManager.notify(notificationId, builder.build());
                }
            }

            @Override
            public void onFailure() {
                if (mNotificationManager != null) {
                    builder.setContentTitle("Uploading failed");
                    builder.setOngoing(false);
                    mNotificationManager.notify(notificationId, builder.build());
                }
            }
        });
        services.executeVideoRequest(path, title, desc, userId);
    }
}
