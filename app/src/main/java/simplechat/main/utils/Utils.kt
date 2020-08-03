package simplechat.main.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.text.SimpleDateFormat
import java.util.*


object Utils {

    @JvmStatic
    fun parseStringToDate(dateStr: String?): Date {
        val tz = TimeZone.getTimeZone("GMT")
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale("en")).apply {
            timeZone = tz
        }
        return df.parse(dateStr)
    }

    @JvmStatic
    fun dateToStringWithTimeZone(date: Date): String? {
        val tz = TimeZone.getTimeZone("GMT")
        return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).apply {
            timeZone = tz
        }.format(date)
    }

    fun showKeyboard(activity: Activity, view: View) {
        view.requestFocus()
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

}

