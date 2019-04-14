package silviupal.hashtagscounter.utils

import android.content.Context
import android.content.DialogInterface
import android.view.View
import androidx.appcompat.app.AlertDialog
import silviupal.hashtagscounter.R

/**
 * Created by Silviu Pal on 4/10/2019.
 */
object DialogUtils {
    fun buildAlertDialogWithCheckbox(context: Context,
        title: String,
        message: String,
        checkBoxView: View?,
        positiveClick: DialogInterface.OnClickListener): AlertDialog.Builder {
        val normalAlertDialog = buildNormalAlertDialog(context, title, message, positiveClick)
        checkBoxView?.let(normalAlertDialog::setView)
        return normalAlertDialog
    }

    fun buildNormalAlertDialog(context: Context,
        title: String,
        message: String,
        positiveClick: DialogInterface.OnClickListener): AlertDialog.Builder {
        return AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setCancelable(true)
            .setPositiveButton(context.getString(R.string.yes), positiveClick)
            .setNegativeButton(context.getString(R.string.cancel), null)
    }

    fun buildNoMessageNoTitleAlertDialog(context: Context): AlertDialog.Builder {
        return AlertDialog.Builder(context)
            .setCancelable(true)
            .setPositiveButton(context.getString(R.string.yes), null)
            .setNegativeButton(context.getString(R.string.cancel), null)
    }
}