package silviupal.hashtagscounter.common.utils

import android.content.Context
import silviupal.hashtagscounter.R
import java.util.regex.Pattern

/**
 * Created by Silviu Pal on 31/03/2019.
 */
object HashtagsUtils {
    private const val hashtagsRegex = "#[\\p{L}_0-9]+"

    fun getNumberOfHashtagsFromText(textToCheck: String): Int = getHashtagsList(
        textToCheck).size

    fun getHashtagsList(textToCheck: String): List<String> {
        if (textToCheck.isEmpty()) {
            return emptyList()
        }
        val matcher = Pattern.compile(hashtagsRegex).matcher(textToCheck)
        val hashtagsList = arrayListOf<String>()
        while (matcher.find()) {
            hashtagsList += matcher.group()
        }
        return hashtagsList
    }

    fun getHashtagsCounterText(hashtagsCount: Int, context: Context?): String {
        context?.let {
            return if (hashtagsCount == 0) {
                it.getString(R.string.default_counter_hashtags_text)
            } else {
                String.format(it.getString(R.string.hashtags_counter_format),
                    hashtagsCount,
                    it.resources.getQuantityText(R.plurals.hashtags, hashtagsCount))
            }
        }
        return ""
    }

    fun getCharsCountText(charsCount: Int, context: Context?): String {
        context?.let {
            return if (charsCount == 0) {
                it.getString(R.string.default_counter_input_text)
            } else {
                String.format(it.getString(R.string.hashtags_counter_format),
                    charsCount,
                    it.resources.getQuantityString(R.plurals.textLength, charsCount))
            }
        }
        return ""
    }

    fun isNotAHashtag(hashtagName: String): Boolean = !hashtagName.matches(Regex(hashtagsRegex))
}