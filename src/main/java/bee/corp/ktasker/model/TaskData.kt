package bee.corp.ktasker.model

import java.time.LocalDateTime
import java.util.Calendar

data class TaskData(var title: String, var dueDate: Calendar, var dueTime: LocalDateTime, var taskPath: String, var isDone: Boolean = false)
