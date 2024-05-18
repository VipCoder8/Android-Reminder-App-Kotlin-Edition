package bee.corp.utility

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlin.system.exitProcess

class NotificationAppClose : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent?.action.equals(Constants.NotificationConstants.FOREGROUND_EXIT_ACTION_NAME)) {
            exitProcess(0)
        }
    }
}