package bee.corp.ktasker.viewmodel
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import bee.corp.utility.Constants
import bee.corp.ktasker.view.NotificationCreator
import bee.corp.ktasker.model.TaskData
import bee.corp.utility.TaskFileManipulation
import bee.corp.utility.TaskReminder
import bee.corp.utility.TaskTimeReached
import bee.corp.utility.TimeFormatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class TaskManagement(application: Application) : AndroidViewModel(application) {
    private var tasks: ArrayList<TaskData> = ArrayList()
    private var tasksReminders: ArrayList<TaskReminder> = ArrayList()

    private var _mutableTasksInitLiveData: MutableLiveData<ArrayList<TaskData>> = MutableLiveData<ArrayList<TaskData>>().
    apply { value = tasks }
    val mutableTasksInitLiveData: LiveData<ArrayList<TaskData>> get() = _mutableTasksInitLiveData

    private var _mutableTaskAddedLiveData: MutableLiveData<Int> = MutableLiveData()
    val mutableTaskAddedLiveData: LiveData<Int> get() = _mutableTaskAddedLiveData

    private var _mutableTaskDeletedLiveData: MutableLiveData<Int> = MutableLiveData()
    val mutableTaskDeletedLiveData: LiveData<Int> get() = _mutableTaskDeletedLiveData

    private var _mutableTaskEditedLiveData: MutableLiveData<Int> = MutableLiveData()
    val mutableTaskEditedLiveData: LiveData<Int> get() = _mutableTaskEditedLiveData

    private var _mutableTaskOperationErrorLiveData: MutableLiveData<Int> = MutableLiveData()
    val mutableTaskOperationErrorLiveData: LiveData<Int> get() = _mutableTaskOperationErrorLiveData

    private var addedTasks: Int = 0
    private var loadedTasks: Boolean = false

    private var notificationCreator: NotificationCreator = NotificationCreator()
    init {
        readTasks()
    }
    fun deleteTask(task: TaskData, index: Int) {
        if(TaskFileManipulation.deleteTaskFile(task.taskPath)) {
            tasksReminders[index].cancel()
            tasksReminders.removeAt(index)
            tasks.removeAt(index)
            _mutableTaskDeletedLiveData.value = index
        } else {
            _mutableTaskOperationErrorLiveData.value =
                Constants.TaskOperationErrorConstants.TASK_DELETION_ERROR
        }
    }
    fun editTask(task: TaskData, newTitle: String, newDueTime: String, newDueDate: String) {
        task.dueTime = TimeFormatter.getLocalTimeFromString(newDueTime)
        task.dueDate = TimeFormatter.getCalendarFromString(newDueDate)
        val newTaskPath: String = TaskFileManipulation.getNewTaskFileName(
            newTitle,
            super.getApplication<Application>().applicationContext
        )
        viewModelScope.launch {
            saveTask(newTitle, newDueTime, newDueDate, true,
                task.taskPath, newTaskPath, task.isDone)
        }
        task.taskPath = newTaskPath
        _mutableTaskEditedLiveData.value = tasks.indexOf(task)
    }
    fun addTask(title: String, dueTime: String, dueDate: String, fileName: String =
        TaskFileManipulation.getNewTaskFileName(title, super.getApplication()), isDone: Boolean = false) {
        val task = TaskData(title, TimeFormatter.getCalendarFromString(dueDate),
            TimeFormatter.getLocalTimeFromString(dueTime), fileName, isDone)
        if(loadedTasks) {
            viewModelScope.launch {
                saveTask(title, dueTime, dueDate, false, "", fileName, isDone)
            }
        }
        tasks += task
        addTaskReminder(task)
        _mutableTaskAddedLiveData.value = addedTasks
        addedTasks++
    }

    private fun addTaskReminder(task: TaskData) {
        val taskReminder = TaskReminder(task)
        taskReminder.waitDueDate(object: TaskTimeReached {
            override fun onTimeReached(task: TaskData) {
                task.isDone = true
                TaskFileManipulation.updateTaskFileData(task, TimeFormatter.getDateAsString(task.dueDate),
                    TimeFormatter.getTimeAsString(task.dueTime.hour, task.dueTime.minute))
                notificationCreator.createNotification(task.title,
                    this@TaskManagement.getApplication<Application>().applicationContext,
                    Constants.NotificationConstants.TASK_REMIND_CHANNEL_ID,
                    Constants.NotificationConstants.TASK_REMIND_CHANNEL_NAME
                )
            }
        })
        tasksReminders += taskReminder
    }

    private suspend fun saveTask(title: String, dueTime: String, dueDate: String,
                                 edit: Boolean, oldTaskFileName: String = "",
                                 newTaskFileName: String =
                                     TaskFileManipulation.getNewTaskFileName(
                                         title,
                                         super.getApplication<Application>().applicationContext
                                     ), isDone: Boolean
    ) {
        withContext(Dispatchers.IO) {
            if(!edit) {
                TaskFileManipulation.createTaskFile(
                    newTaskFileName, title,
                    dueDate, dueTime, isDone
                )
            } else {
                TaskFileManipulation.editTaskFile(
                    oldTaskFileName,
                    newTaskFileName,
                    title,
                    dueDate,
                    dueTime
                )
            }
        }
    }

    private fun readTasks() {
        viewModelScope.launch {
            val files: Array<out File>? = TaskFileManipulation.readTasksFiles(
                TaskFileManipulation.getTasksFolder(
                    super.getApplication<Application>().applicationContext
                )
            )
            if (files != null) {
                for(file: File in files) {
                    if(file.absolutePath.endsWith(Constants.FileConstants.TASK_FILE_EXTENSION)) {
                        val lines = TaskFileManipulation.readFileLines(file)
                        addTask(lines[0],
                            lines[1],
                            lines[2], file.absolutePath, lines[3].toBoolean())
                    }
                }
            }
            loadedTasks = true
        }
    }
}