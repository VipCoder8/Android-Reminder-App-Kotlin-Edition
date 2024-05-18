package bee.corp.utility

import bee.corp.ktasker.model.TaskData
import java.time.LocalDateTime
import java.util.Calendar

class TaskReminder(private val task: TaskData) {
    private var canceled: Boolean = false
    fun waitDueDate(timeReached: TaskTimeReached) {
        var currentCalendar = Calendar.getInstance()
        var currentTime = LocalDateTime.now()
        Thread {
            while(!canceled && !task.isDone) {
                currentCalendar = Calendar.getInstance()
                currentTime = LocalDateTime.now()
                if((task.dueDate.get(Calendar.DAY_OF_MONTH) <= currentCalendar.get(Calendar.DAY_OF_MONTH) &&
                            task.dueDate.get(Calendar.YEAR) <= currentCalendar.get(Calendar.YEAR))
                    && (task.dueTime.hour <= currentTime.hour && task.dueTime.minute <= currentTime.minute)) {
                    timeReached.onTimeReached(task)
                    cancel()
                }
                Thread.sleep(100)
            }
        }.start()
    }
    fun cancel() {
        this.canceled = true
    }
}