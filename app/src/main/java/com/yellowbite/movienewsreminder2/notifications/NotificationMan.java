package com.yellowbite.movienewsreminder2.notifications;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import com.yellowbite.movienewsreminder2.MainActivity;
import com.yellowbite.movienewsreminder2.newsService.messages.WebScraperMessage;

public final class NotificationMan
{
    public static void showNotification(Context context, WebScraperMessage message)
    {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(message.getIcon())
                .setContentTitle(message.getTitle())
                .setContentText(message.getText())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        if(message.isShowMultiLinedText())
        {
            mBuilder.setStyle(new NotificationCompat.BigTextStyle()
                .bigText(message.getText()));
        }

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(contentIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(message.getId(), mBuilder.build());
    }

    public static void showShortToast(Context context, String text)
    {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
