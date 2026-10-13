package com.nobuhle.smartpantrymanager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ExpiryNotificationHelper {

    private static final String CHANNEL_ID =
            "expiry_notifications";

    private static final String CHANNEL_NAME =
            "Expiry Notifications";

    private static final String CHANNEL_DESCRIPTION =
            "Notifications about pantry ingredients approaching their expiry date.";

    public static void createNotificationChannel(
            Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel =
                    new NotificationChannel(
                            CHANNEL_ID,
                            CHANNEL_NAME,
                            NotificationManager.IMPORTANCE_HIGH
                    );

            channel.setDescription(
                    CHANNEL_DESCRIPTION
            );

            NotificationManager notificationManager =
                    context.getSystemService(
                            NotificationManager.class
                    );

            if (notificationManager != null) {

                notificationManager.createNotificationChannel(
                        channel
                );
            }
        }
    }

    public static void showExpiryNotification(
            Context context,
            String ingredientName,
            long daysRemaining) {

        String message;

        if (daysRemaining < 0) {

            message =
                    ingredientName +
                            " has already expired.";

        } else if (daysRemaining == 0) {

            message =
                    ingredientName +
                            " expires today.";

        } else if (daysRemaining == 1) {

            message =
                    ingredientName +
                            " expires tomorrow.";

        } else {

            message =
                    ingredientName +
                            " expires in " +
                            daysRemaining +
                            " days.";
        }

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(
                        context,
                        CHANNEL_ID
                )
                        .setSmallIcon(
                                R.mipmap.ic_launcher
                        )
                        .setContentTitle(
                                "Pantry Expiry Alert"
                        )
                        .setContentText(
                                message
                        )
                        .setStyle(
                                new NotificationCompat.BigTextStyle()
                                        .bigText(message)
                        )
                        .setPriority(
                                NotificationCompat.PRIORITY_HIGH
                        )
                        .setDefaults(
                                NotificationCompat.DEFAULT_ALL
                        )
                        .setAutoCancel(true);

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(
                        context
                );

        try {
            notificationManager.notify(
                    ingredientName.hashCode(),
                    builder.build()
            );
        } catch (SecurityException ignored) {
            // Permission missing or revoked
        }
    }
}
