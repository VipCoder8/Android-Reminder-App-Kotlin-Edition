package bee.corp.utility

import bee.corp.ktasker.model.TaskData

interface TaskEditClickListener {
    fun onItemEditButtonClicked(taskView: TaskData, position: Int)
}