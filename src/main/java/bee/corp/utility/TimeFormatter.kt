package bee.corp.utility

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

class TimeFormatter {
    companion object {
        private val currentCalendar: Calendar = Calendar.getInstance()
        fun getCalendarFromString(date: String) : Calendar {
            val cal = Calendar.getInstance()
            val dateArray: List<String> = date.split("/")
            cal.set(dateArray[2].toInt(),dateArray[1].toInt(),dateArray[0].toInt())
            cal.set(Calendar.MILLISECOND, currentCalendar.get(Calendar.MILLISECOND))
            return cal
        }
        fun getLocalTimeFromString(time: String) : LocalDateTime {
            val timeArray: List<String> = time.split(":")
            return LocalDateTime.now()
                .withHour(timeArray[0].toInt())
                .withMinute(timeArray[1].toInt())
        }
        fun getTimeAsString(hour: Int, minute: Int) : String {
            return "${String.format("%02d", hour)}:${String.format("%02d", minute)}"
        }
        fun getDateAsString(day: Int, month: Int, year: Int) : String {
            val localDate = LocalDate.of(year, month + 1, day)
            val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            return localDate.format(dateFormatter)
        }
        fun getDateAsString(cal: Calendar) : String {
            val localDate = LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
            val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            return localDate.format(dateFormatter)
        }
    }
}