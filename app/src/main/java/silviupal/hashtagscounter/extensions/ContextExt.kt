package silviupal.hashtagscounter.extensions

import android.content.ClipData
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_hashtags_counter.*
import silviupal.hashtagscounter.MyConstants

/**
 * Created by Silviu Pal on 4/8/2019.
 */
fun Context.showToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun Context.copyToClipboard(textToCopy: String) {
    val clipboard = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(MyConstants.COPY_TO_CLIPBOARD_TITLE, textToCopy)
    clipboard.primaryClip = clip
}

fun Context.pasteFromClipboard(): String? {
    val clipboard = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboard.primaryClipDescription?.let { clipDescription ->
        if (clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
            val item = clipboard.primaryClip?.getItemAt(0)
            item?.let {
                return it.text.toString()
            }
        }
    }
    return null
}