package bee.corp.utility

class Constants {
    object MainConstants {
        const val APP_NAME = "Tasker"
    }
    object NotificationConstants {
        const val FOREGROUND_CHANNEL_ID = "tasker_foreground"
        const val FOREGROUND_CHANNEL_NAME = MainConstants.APP_NAME + "Foreground"
        const val FOREGROUND_EXIT_ACTION_NAME = "Exit"
        const val TASK_REMIND_CHANNEL_ID = "task_notification"
        const val TASK_REMIND_CHANNEL_NAME = "Task Notification"
    }
    object FileConstants {
        const val TASK_FILE_EXTENSION = ".tsk"
    }
    object RequestCodesConstants {
        const val GET_BROADCAST_REQUEST_CODE = 1
        const val REQUEST_PERMISSIONS_REQUEST_CODE = 2
    }
    object ToastMessagesConstants {
        const val TASK_DELETION_SUCCESS = "Task deleted successfully!"
        const val TASK_NAME_EMPTY_ERROR = "Task name can't be empty!"
        const val TASK_DUE_TIME_EMPTY_ERROR = "Due time can't be empty!"
        const val TASK_DUE_DATE_EMPTY_ERROR = "Due date can't be empty!"
        const val TASK_CREATION_ERROR = "Couldn't create the task!(try relaunching the app)"
        const val TASK_DELETION_ERROR = "Couldn't delete the task!(try relaunching the app)"
        const val TASK_EDITING_ERROR = "Couldn't edit the task!(try relaunching the app)"
    }
    object DialogTitleConstants {
        const val ADD_TASK_DIALOG_TITLE = "Add Task"
        const val DELETE_TASK_DIALOG_TITLE = "Task Deletion"
        const val EDIT_TASK_DIALOG_TITLE = "Edit Task"
        const val INFO_TASK_DIALOG_TITLE = "Task Info"
    }
    object TextViewsConstants {
        const val DUE_DATE_DEFAULT_TEXT = "Date"
        const val DUE_TIME_DEFAULT_TEXT = "Time"
    }
    object TaskOperationErrorConstants {
        const val TASK_CREATION_ERROR = 0
        const val TASK_DELETION_ERROR = 1
        const val TASK_EDITING_ERROR = 2
    }
}