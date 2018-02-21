package oblak.r.baseapp.utils

import oblak.r.baseapp.main.DisplayableRocket
import java.text.SimpleDateFormat
import java.util.*


object ModelConsts {

    private const val inputDateFormat: String = "MMMMM d, yyyy HH:mm:ss z"
    private const val outputDateFormat: String = "YYYY.MM.dd"
    private const val shortCountDownFormat: String = "h'h' mm'min' ss's'"

    // dummy data for when no rockets are selected
    val allRocketObj = DisplayableRocket("All")

    private val parsedLaunchFormat = SimpleDateFormat(inputDateFormat, Locale.getDefault())

    private val countDownFormat = SimpleDateFormat(shortCountDownFormat, Locale.getDefault())

    private val formattedLaunchFormat = SimpleDateFormat(outputDateFormat, Locale.getDefault())

    fun parseLaunchDate(dateString: String) = parsedLaunchFormat.parse(dateString)

    fun formatLaunchDate(date: Date) = formattedLaunchFormat.format(date)

    fun remainingDate(dateString: String) =  Date(parseLaunchDate(dateString).time - Date().time)

    fun formatToRemaining(dateString: String) = countDownFormat.format(remainingDate(dateString))

    fun formatToRemaining(date: Date): String {
        // difference in days is broken in this time format for some reason
        val diff = date.time - Date().time
        val diffInDays = diff / (3600*1000*24)
        return "${diffInDays}d ${countDownFormat.format(Date(diff))}"
    }
}