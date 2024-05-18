package bee.corp.utility

import bee.corp.ktasker.model.TaskData

interface TaskTimeReached {
    fun onTimeReached(task: TaskData)
}