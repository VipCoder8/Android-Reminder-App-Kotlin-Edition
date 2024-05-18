package bee.corp.ktasker.view

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import bee.corp.ktasker.R
import bee.corp.utility.Constants

class NotificationCreator {
    fun createForegroundNotification(title: String, c: Context,
                           channelId: String,
                           channelName: String,
                           actionName: String, actionIntent: Intent) : Notification {
        val notifBuilder: NotificationCompat.Builder = NotificationCompat.Builder(c, channelId)
        val notifManager: NotificationManager =
            c.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(
            c,
            Constants.RequestCodesConstants.GET_BROADCAST_REQUEST_CODE,
            actionIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notifChannel: NotificationChannel =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        notifManager.createNotificationChannel(notifChannel)
        notifBuilder.setSmallIcon(R.drawable.ic_launcher_foreground)
        notifBuilder.setContentTitle(title)
        notifBuilder.addAction(
            android.R.drawable.ic_menu_close_clear_cancel,
            actionName,
            pendingIntent
        )
        return notifBuilder.build()

    }
    fun createNotification(title: String, c: Context, channelId: String, channelName: String) : Notification {
        val notifBuilder: NotificationCompat.Builder = NotificationCompat.Builder(c, channelId)
        val notifManager: NotificationManager =
            c.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notifChannel: NotificationChannel =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        notifManager.createNotificationChannel(notifChannel)
        notifBuilder.setSmallIcon(R.drawable.ic_launcher_foreground)
        notifBuilder.setContentTitle(title)
        val notification = notifBuilder.build()
        notifManager.notify(9, notification)
        return notification
    }
}