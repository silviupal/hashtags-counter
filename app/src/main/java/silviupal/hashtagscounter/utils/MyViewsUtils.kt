package silviupal.hashtagscounter.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.CompoundButton
import kotlinx.android.synthetic.main.checkbox_layout.view.*
import silviupal.hashtagscounter.R

/**
 * Created by Silviu Pal on 4/10/2019.
 */
object MyViewsUtils {
    @SuppressLint("InflateParams")
    fun getCheckbox(context: Context, listener: CompoundButton.OnCheckedChangeListener, text: String): View? {
        val checkBoxView = LayoutInflater.from(context).inflate(R.layout.checkbox_layout, null, false)
        checkBoxView.checkbox.setOnCheckedChangeListener(listener)
        checkBoxView.checkbox.text = text
        return checkBoxView
    }
}