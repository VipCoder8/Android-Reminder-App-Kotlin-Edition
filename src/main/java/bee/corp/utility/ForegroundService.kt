package bee.corp.utility

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder
import bee.corp.ktasker.view.NotificationCreator

class ForegroundService : Service() {
    private val notificationCreator: NotificationCreator = NotificationCreator()
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notif: Notification = notificationCreator.createForegroundNotification(
            Constants.MainConstants.APP_NAME,
            applicationContext,
            Constants.NotificationConstants.FOREGROUND_CHANNEL_ID,
            Constants.NotificationConstants.FOREGROUND_CHANNEL_NAME,
            Constants.NotificationConstants.FOREGROUND_EXIT_ACTION_NAME,
            createExitIntent())
        startForeground(901, notif)
        return super.onStartCommand(intent, flags, startId)
    }

    private fun createExitIntent(): Intent {
        val intent = Intent(this, NotificationAppClose::class.java)
        intent.setAction(Constants.NotificationConstants.FOREGROUND_EXIT_ACTION_NAME)
        return intent
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(STOP_FOREGROUND_REMOVE)
    }

}