package bee.corp.utility

import android.content.Context
import bee.corp.ktasker.model.TaskData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Calendar

class TaskFileManipulation {
    companion object {
        fun deleteTaskFile(path: String) : Boolean {
            val file = File(path)
            return file.delete()
        }
        fun getNewTaskFileName(taskTitle: String, c: Context) : String {
            val calendar = Calendar.getInstance()
            return c.getExternalFilesDir(null)!!.absolutePath + "/" +
                    "${taskTitle}_${calendar.get(Calendar.HOUR_OF_DAY)}_" +
                    "${calendar.get(Calendar.MINUTE)}_" +
                    "${calendar.get(Calendar.SECOND)}_${calendar.get(Calendar.MILLISECOND)}${Constants.FileConstants.TASK_FILE_EXTENSION}"
        }
        fun getTaskFileName(taskTitle: String, dueDate: Calendar, c: Context) : String {
            return c.getExternalFilesDir(null)!!.absolutePath + "/" +
                    "${taskTitle}_${dueDate.get(Calendar.HOUR_OF_DAY)}_" +
                    "${dueDate.get(Calendar.MINUTE)}_" +
                    "${dueDate.get(Calendar.SECOND)}_${dueDate.timeInMillis}${Constants.FileConstants.TASK_FILE_EXTENSION}"
        }
        fun createTaskFile(taskTitle: String, dueDate: String, dueTime: String, c: Context) {
            val file = File(getNewTaskFileName(taskTitle, c))
            println("second path: ${file.absolutePath}")
            if(!file.exists()) {
                file.createNewFile()
                file.writeText(taskTitle + "\n" + dueTime + "\n" + dueDate)
            }
        }
        fun createTaskFile(path: String, taskTitle: String, dueDate: String, dueTime: String, isDone: Boolean) : Boolean {
            val file = File(path)
            if(!file.exists()) {
                val created = file.createNewFile()
                file.writeText(taskTitle + "\n" + dueTime + "\n" + dueDate + "\n" + isDone)
                return created
            }
            return false
        }
        fun updateTaskFileData(task: TaskData, dueDate: String, dueTime: String) {
            val file = File(task.taskPath)
            file.writeText(task.title + "\n" + dueTime + "\n" + dueDate + "\n" + task.isDone)
        }
        fun editTaskFile(oldTaskFileName: String, newTaskFileName: String,
                         taskTitle: String, dueDate: String, dueTime: String) : Boolean {
            val file = File(oldTaskFileName)
            file.writeText(taskTitle + "\n" + dueTime + "\n" + dueDate)
            return file.renameTo(File(newTaskFileName))
        }
        suspend fun getTasksFolder(c: Context) : File {
            return withContext(Dispatchers.IO){File(c.getExternalFilesDir(null)!!.absolutePath + "/")}
        }

        suspend fun readTasksFiles(tasksFolder: File) : Array<out File>? {
            return withContext(Dispatchers.IO) {tasksFolder.listFiles()}
        }
        suspend fun readFileLines(file: File) : List<String> {
            return withContext(Dispatchers.IO) {file.bufferedReader().readLines()}
        }
    }
}