package bee.corp.ktasker.view

import android.Manifest
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import bee.corp.utility.Constants
import bee.corp.utility.ForegroundService
import bee.corp.ktasker.R
import bee.corp.ktasker.model.TaskData
import bee.corp.utility.TaskDeleteClickListener
import bee.corp.utility.TaskEditClickListener
import bee.corp.utility.TaskInfoClickListener
import bee.corp.utility.TimeFormatter
import bee.corp.utility.ViewTextsGenerator
import bee.corp.ktasker.viewmodel.TaskManagement
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    private var isEditingTask: Boolean = false

    //Main Activity
    private lateinit var tasksView: RecyclerView
    private lateinit var tasksAdapter: TasksAdapter
    private lateinit var taskManager: TaskManagement
    private lateinit var addButton: ImageButton
    private lateinit var dialogCreator: DialogCreator

    //Add Task Dialog
    private lateinit var addTaskFormLayout: ConstraintLayout
    private lateinit var taskNameTextView: EditText
    private lateinit var taskDueDateTextView: TextView
    private lateinit var taskDueTimeTextView: TextView
    private lateinit var addTaskDialog: AlertDialog
    private lateinit var timePickerDialog: TimePickerDialog
    private lateinit var datePickerDialog: DatePickerDialog

    //Delete Task Dialog
    private lateinit var deleteTaskDialog: AlertDialog

    //Edit Task Dialog
    private lateinit var editTaskFormLayout: ConstraintLayout
    private lateinit var newTaskNameTextView: EditText
    private lateinit var newTaskDueDateTextView: TextView
    private lateinit var newTaskDueTimeTextView: TextView
    private lateinit var editTaskDialog: AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestPermissions()
        startAllServices()
        initViews()
        initViewsListeners()
        initViewModels()
        observeViewModels()
    }

    private fun requestPermissions() {
        if(android.os.Build.VERSION.SDK_INT == android.os.Build.VERSION_CODES.TIRAMISU) {
            if(this.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)!=PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    Constants.RequestCodesConstants.REQUEST_PERMISSIONS_REQUEST_CODE
                )
            }
        }
        if(this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                Constants.RequestCodesConstants.REQUEST_PERMISSIONS_REQUEST_CODE
            )
        }
        if(this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                Constants.RequestCodesConstants.REQUEST_PERMISSIONS_REQUEST_CODE
            )
        }
    }
    private fun startAllServices() {
        val intent = Intent(this, ForegroundService::class.java)
        startService(intent)
    }

    private fun initViews() {
        initAddTaskDialogViews()
        initEditTaskDialogViews()

        initPickerDialogs()

        dialogCreator = DialogCreator()
        initAddTaskDialog()

        tasksView = findViewById(R.id.tasks_view)
        tasksView.addItemDecoration(TaskViewDivider(15))
        addButton = findViewById(R.id.add_task_button)
    }

    private fun initAddTaskDialogViews() {
        addTaskFormLayout = LayoutInflater.from(applicationContext).inflate(R.layout.dialog_add_task_layout, null) as ConstraintLayout
        taskNameTextView = addTaskFormLayout.findViewById(R.id.task_name_text)
        taskDueDateTextView = addTaskFormLayout.findViewById(R.id.date_picker_text)
        taskDueTimeTextView = addTaskFormLayout.findViewById(R.id.time_picker_text)
    }

    private fun initEditTaskDialogViews() {
        editTaskFormLayout = LayoutInflater.from(applicationContext).inflate(R.layout.dialog_edit_task_layout, null) as ConstraintLayout
        newTaskNameTextView = editTaskFormLayout.findViewById(R.id.new_task_name_text)
        newTaskDueDateTextView = editTaskFormLayout.findViewById(R.id.new_date_picker_text)
        newTaskDueTimeTextView = editTaskFormLayout.findViewById(R.id.new_time_picker_text)
    }

    private fun initViewsListeners() {
        taskDueDateTextView.setOnClickListener {
            isEditingTask = false
            datePickerDialog.show() }
        newTaskDueDateTextView.setOnClickListener {
            isEditingTask = true
            datePickerDialog.show()
        }
        taskDueTimeTextView.setOnClickListener {
            isEditingTask = false
            timePickerDialog.show() }
        newTaskDueTimeTextView.setOnClickListener {
            isEditingTask = true
            timePickerDialog.show() }
        addButton.setOnClickListener { addTaskDialog.show() }
    }

    private fun initPickerDialogs() {
        datePickerDialog = DatePickerDialog(this@MainActivity)
        datePickerDialog.setOnDateSetListener { _, year, month, dayOfMonth ->
            if(!isEditingTask) {
                taskDueDateTextView.text = TimeFormatter.getDateAsString(dayOfMonth, month, year)
            } else {
                newTaskDueDateTextView.text = TimeFormatter.getDateAsString(dayOfMonth, month, year)
            }
        }
        timePickerDialog = TimePickerDialog(this@MainActivity,
            { _, hourOfDay, minute ->
                if(!isEditingTask) {
                    taskDueTimeTextView.text = TimeFormatter.getTimeAsString(hourOfDay, minute)
                } else {
                    newTaskDueTimeTextView.text = TimeFormatter.getTimeAsString(hourOfDay, minute)
                }
            },
            Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE),
            true)
    }

    private fun initAddTaskDialog() {
        addTaskDialog = dialogCreator.createViewedDialog(addTaskFormLayout,
            this@MainActivity,
            Constants.DialogTitleConstants.ADD_TASK_DIALOG_TITLE
        ) { dialog, _ ->
            val taskName = taskNameTextView.text.toString()
            val taskDueTime = taskDueTimeTextView.text.toString()
            val taskDueDate = taskDueDateTextView.text.toString()

            if (taskName.isNotBlank() &&
                (taskDueTime.isNotBlank() && taskDueTime != Constants.TextViewsConstants.DUE_TIME_DEFAULT_TEXT) &&
                (taskDueDate.isNotBlank() && taskDueDate != Constants.TextViewsConstants.DUE_DATE_DEFAULT_TEXT)) {
                taskManager.addTask(taskName, taskDueTime, taskDueDate)
                dialog.dismiss()
            } else {
                if (taskName.isBlank()) {
                    Toast.makeText(applicationContext,
                        Constants.ToastMessagesConstants.TASK_NAME_EMPTY_ERROR, Toast.LENGTH_LONG).show()
                }
                if (taskDueTime.isBlank() || taskDueTime == Constants.TextViewsConstants.DUE_TIME_DEFAULT_TEXT) {
                    Toast.makeText(applicationContext,
                        Constants.ToastMessagesConstants.TASK_DUE_TIME_EMPTY_ERROR, Toast.LENGTH_LONG).show()
                }
                if (taskDueDate.isBlank() || taskDueDate == Constants.TextViewsConstants.DUE_DATE_DEFAULT_TEXT) {
                    Toast.makeText(applicationContext,
                        Constants.ToastMessagesConstants.TASK_DUE_DATE_EMPTY_ERROR, Toast.LENGTH_LONG).show()
                }
            }
            taskNameTextView.text.clear()
        }
    }

    private fun showAndEditTaskDialog(task: TaskData) {
        if (editTaskFormLayout.parent != null) {
            (editTaskFormLayout.parent as ViewGroup).removeView(editTaskFormLayout)
        }

        newTaskNameTextView.setText(task.title)
        newTaskDueDateTextView.text = TimeFormatter.getDateAsString(task.dueDate)
        newTaskDueTimeTextView.text =
            TimeFormatter.getTimeAsString(task.dueTime.hour, task.dueTime.minute)

        editTaskDialog = dialogCreator.createViewedDialog(editTaskFormLayout,
            this@MainActivity,
            Constants.DialogTitleConstants.EDIT_TASK_DIALOG_TITLE
        ) { dialog, _ ->
            if(newTaskNameTextView.text.toString().isNotBlank()) {
                taskManager.editTask(task, newTaskNameTextView.text.toString(), newTaskDueTimeTextView.text.toString(), newTaskDueDateTextView.text.toString())
                task.title = newTaskNameTextView.text.toString()
                newTaskNameTextView.text.clear()
                ViewTextsGenerator.resetTextViews(
                    newTaskDueDateTextView,
                    Constants.TextViewsConstants.DUE_DATE_DEFAULT_TEXT
                )
                ViewTextsGenerator.resetTextViews(
                    newTaskDueTimeTextView,
                    Constants.TextViewsConstants.DUE_TIME_DEFAULT_TEXT
                )
                dialog.dismiss()
            }
        }
        editTaskDialog.show()
    }

    private fun showAndDeleteTaskDialog(taskName: String, task: TaskData, index: Int) {
        deleteTaskDialog = dialogCreator.createSimpleDialog(
            Constants.DialogTitleConstants.DELETE_TASK_DIALOG_TITLE,
            this@MainActivity,
            ViewTextsGenerator.getDialogDeleteMessage(taskName)
        ) { dialog, _ -> taskManager.deleteTask(task, index); dialog.dismiss()}
        deleteTaskDialog.show()
    }

    private fun showInfoDialog(task: TaskData) {
        dialogCreator.createSimpleDialog(
            Constants.DialogTitleConstants.INFO_TASK_DIALOG_TITLE,
            this@MainActivity,
            ViewTextsGenerator.getDialogInfoMessage(task.title, task.dueTime, task.dueDate)
        ) { dialog, _ ->
            dialog.dismiss()
        }.show()
    }

    private fun initTasksButtonsClickListeners() {
        tasksAdapter.addTaskDeleteClickListener(object: TaskDeleteClickListener {
            override fun onItemDeleteButtonClicked(taskView: TaskData, index: Int) {
                showAndDeleteTaskDialog(taskView.title, taskView, index)
            }
        })
        tasksAdapter.addTaskEditClickListener(object: TaskEditClickListener {
            override fun onItemEditButtonClicked(taskView: TaskData, position: Int) {
                showAndEditTaskDialog(taskView)
            }
        })
        tasksAdapter.addTaskInfoClickListener(object: TaskInfoClickListener {
            override fun onItemInfoButtonClicked(taskView: TaskData, position: Int) {
                showInfoDialog(taskView)
            }
        })
    }

    private fun initViewModels() {
        taskManager = ViewModelProvider(this)[TaskManagement::class.java]
    }

    private fun observeViewModels() {
        taskManager.mutableTasksInitLiveData.observe(this) {
            if(it.isEmpty()) {
                findViewById<TextView>(R.id.no_tasks_placeholder).visibility = View.VISIBLE
            }
            tasksAdapter = TasksAdapter(it)
            tasksView.adapter = tasksAdapter
            initTasksButtonsClickListeners()
        }
        taskManager.mutableTaskAddedLiveData.observe(this) {
            tasksAdapter.notifyItemInserted(it)
            manageNoTasksIndicatorPlaceholder()
        }
        taskManager.mutableTaskDeletedLiveData.observe(this) {
            tasksAdapter.notifyItemRemoved(it)
            Toast.makeText(this@MainActivity,
                Constants.ToastMessagesConstants.TASK_DELETION_SUCCESS, Toast.LENGTH_SHORT).show()
            manageNoTasksIndicatorPlaceholder()
        }
        taskManager.mutableTaskEditedLiveData.observe(this) {
            tasksAdapter.notifyItemChanged(it)
        }
        taskManager.mutableTaskOperationErrorLiveData.observe(this) {
            if(it == Constants.TaskOperationErrorConstants.TASK_CREATION_ERROR) {
                Toast.makeText(this@MainActivity,
                    Constants.ToastMessagesConstants.TASK_CREATION_ERROR, Toast.LENGTH_SHORT).show()
            }
            if(it == Constants.TaskOperationErrorConstants.TASK_EDITING_ERROR) {
                Toast.makeText(this@MainActivity,
                    Constants.ToastMessagesConstants.TASK_EDITING_ERROR, Toast.LENGTH_SHORT).show()
            }
            if(it == Constants.TaskOperationErrorConstants.TASK_DELETION_ERROR) {
                Toast.makeText(this@MainActivity,
                    Constants.ToastMessagesConstants.TASK_DELETION_ERROR, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun manageNoTasksIndicatorPlaceholder() {
        if(tasksAdapter.itemCount==0) {
            findViewById<TextView>(R.id.no_tasks_placeholder).visibility = View.VISIBLE
        } else {
            findViewById<TextView>(R.id.no_tasks_placeholder).visibility = View.GONE
        }
    }

}