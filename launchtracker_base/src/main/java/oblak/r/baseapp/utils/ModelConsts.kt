package oblak.r.baseapp.utils

import oblak.r.baseapp.main.DisplayableRocket
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


object ModelConsts {

    private const val inputDateFormat: String = "MMMMM d, yyyy HH:mm:ss z"
    private const val shortCountDownFormat: String = "h'h' mm'min' ss's'"

    // dummy data for when no rockets are selected
    val allRocketObj = DisplayableRocket("All")

    private val parsedLaunchFormat = SimpleDateFormat(inputDateFormat, Locale.getDefault())

    private val countDownFormat = SimpleDateFormat(shortCountDownFormat, Locale.getDefault())

    private val formattedLaunchFormat = DateFormat.getDateInstance(DateFormat.MEDIUM)

    fun parseLaunchDate(dateString: String) = parsedLaunchFormat.parse(dateString)

    fun formatLaunchDate(date: Date) = formattedLaunchFormat.format(date)

    fun formatToRemaining(date: Date): String {
        // difference in days is broken in this time format for some reason
        val diff = date.time - Date().time
        val diffInDays = diff / (3600*1000*24)
        val formatted = countDownFormat.format(Date(diff))
        return if (diffInDays < 1) {
            formatted
        } else {
            "${diffInDays}d $formatted"
        }
    }
}