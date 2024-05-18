package bee.corp.utility

import bee.corp.ktasker.model.TaskData

interface TaskDeleteClickListener {
    fun onItemDeleteButtonClicked(taskView: TaskData, index: Int)
}