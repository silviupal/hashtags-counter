package silviupal.hashtagscounter.common.utils

import androidx.annotation.ColorRes
import silviupal.hashtagscounter.R

/**
 * Created by Silviu Pal on 4/6/2019.
 */
object ColorUtils {
    @ColorRes
    fun getTextColor(hashtagsCount: Int): Int {
        return if (hashtagsCount == 0) {
            R.color.textColorPrimary
        } else {
            if (hashtagsCount > 30) {
                R.color.colorError
            } else {
                R.color.textColorPrimary
            }
        }
    }
}