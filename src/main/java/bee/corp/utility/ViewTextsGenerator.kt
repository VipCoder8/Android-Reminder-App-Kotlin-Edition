package bee.corp.utility

import android.widget.TextView
import bee.corp.utility.TimeFormatter
import java.time.LocalDateTime
import java.util.Calendar

class ViewTextsGenerator {
    companion object {
        fun getDialogInfoMessage(title: String, dueTime: LocalDateTime, dueDate: Calendar) : String {
            return "Due Time: ${TimeFormatter.getTimeAsString(dueTime.hour, dueTime.minute)}\n" +
                    "Due Date: ${
                        TimeFormatter.getDateAsString(dueDate.get(Calendar.DAY_OF_MONTH), 
                        dueDate.get(Calendar.MONTH), 
                        dueDate.get(Calendar.YEAR))}\n" +
                    "Task Name: $title"
        }
        fun getDialogDeleteMessage(title: String) : String {
            return "Are you sure you want to delete $title task?"
        }
        fun resetTextViews(textView: TextView, defaultText: String) {
            textView.text = defaultText
        }
    }
}