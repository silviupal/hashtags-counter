package silviupal.hashtagscounter.utils

import android.content.Context
import androidx.core.content.edit
import silviupal.hashtagscounter.MyApp
import silviupal.hashtagscounter.MyConstants

/**
 * Created by Silviu Pal on 4/10/2019.
 */
object SharedPrefsUtils {
    private val prefs = MyApp.instance.applicationContext.getSharedPreferences(MyConstants.SHARED_PREFS,
        Context.MODE_PRIVATE)

    fun putBool(key: String, value: Boolean) {
        prefs.edit {
            putBoolean(key, value)
        }
    }

    fun getBool(key: String, defaultValue: Boolean): Boolean = prefs.getBoolean(key, defaultValue)
}