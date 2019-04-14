package silviupal.hashtagscounter.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Created by Silviu Pal on 4/9/2019.
 */
object DateUtils {
    val SERVER_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS"

    fun getCurrentDateAndTime(): String = SimpleDateFormat(SERVER_TIME_FORMAT, Locale.ENGLISH).format(Date())


}