package oblak.r.baseapp.utils

import java.text.SimpleDateFormat
import java.util.*


object ModelConsts {
    const val inputDateFormat: String = "MMMMM d, yyyy HH:mm:ss z"
    const val outputDateFormat: String = "yyyy.MM.dd G 'at' HH:mm:ss z"
    const val countDownFormat: String = "dd'd' hh'h' mm'min' ss's'"


    val launchDateFormat = SimpleDateFormat(inputDateFormat, Locale.getDefault())

    val launchDateFormatOut = SimpleDateFormat(countDownFormat, Locale.getDefault())

    fun parseDate(dateString: String) = launchDateFormat.parse(dateString)

    fun remainingDate(dateString: String) =  Date(parseDate(dateString).time - Date().time)

    fun format(dateString: String) = launchDateFormatOut.format(remainingDate(dateString))
}