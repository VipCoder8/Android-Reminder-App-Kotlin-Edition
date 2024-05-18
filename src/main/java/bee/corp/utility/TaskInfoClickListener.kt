package bee.corp.utility

import bee.corp.ktasker.model.TaskData

interface TaskInfoClickListener {
    fun onItemInfoButtonClicked(taskView: TaskData, position: Int)
}