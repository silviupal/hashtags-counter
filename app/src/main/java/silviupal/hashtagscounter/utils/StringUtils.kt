package silviupal.hashtagscounter.utils

import java.util.regex.Pattern

/**
 * Created by Silviu Pal on 31/03/2019.
 */
object StringUtils {
    //private const val hashtagsRegex = "#[0-9a-zA-Z_]+"
    private const val hashtagsRegex = "#[\\p{L}_]+"

    fun countHashtags(textToCheck: CharSequence): Int {
        if (textToCheck.isEmpty()) {
            return 0
        }

        val matcher = Pattern.compile(hashtagsRegex).matcher(textToCheck)

        val hashtagsList = arrayListOf<String>()
        var hashtagsCounter = 0

        while (matcher.find()) {
            hashtagsList += matcher.group()
            hashtagsCounter++
        }

        return hashtagsCounter
    }
}