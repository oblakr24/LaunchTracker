package oblak.r.baseapp.extensions

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by rokoblak on 10/27/16.
 */

fun String.convertDate(formats: Pair<String, String>): String {
    val date = SimpleDateFormat(formats.first, Locale.getDefault()).parse(this)
    return SimpleDateFormat(formats.second, Locale.getDefault()).format(date)
}

fun Date.getYearsBetween(otherDate: Date): Int {
    val a = Calendar.getInstance().apply {
        time = this@getYearsBetween
    }
    val b = Calendar.getInstance().apply {
        time = otherDate
    }
    var diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR)
    if (a.get(Calendar.MONTH) > b.get(Calendar.MONTH) || a.get(Calendar.MONTH) === b.get(Calendar.MONTH) && a.get(Calendar.DATE) > b.get(Calendar.DATE)) {
        diff--
    }
    return diff
}

fun Context.resolveColor(resourceId: Int): Int {
    return ContextCompat.getColor(this, resourceId)
}

fun Activity.isConnectedToNetwork(): Boolean {
    return checkConnectivity(this)
}

fun Fragment.isConnectedToNetwork(): Boolean {
    return checkConnectivity(activity)
}

fun checkConnectivity(context: Context?): Boolean {
    val cm: ConnectivityManager? = context?.applicationContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    return cm?.activeNetworkInfo?.isConnectedOrConnecting ?: false
}