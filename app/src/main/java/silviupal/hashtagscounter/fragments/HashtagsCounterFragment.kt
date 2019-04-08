package silviupal.hashtagscounter.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_hashtags_counter.*
import silviupal.hashtagscounter.App
import silviupal.hashtagscounter.MyConstants
import silviupal.hashtagscounter.R
import silviupal.hashtagscounter.activities.MainActivity
import silviupal.hashtagscounter.base.BaseFragment
import silviupal.hashtagscounter.extensions.copyToClipboard
import silviupal.hashtagscounter.extensions.pasteFromClipboard
import silviupal.hashtagscounter.extensions.showToast
import silviupal.hashtagscounter.helpers.SimplifiedTextWatcher
import silviupal.hashtagscounter.utils.ColorUtils
import silviupal.hashtagscounter.utils.StringUtils
import timber.log.Timber

/**
 * Created by Silviu Pal on 05/04/2019.
 */
class HashtagsCounterFragment : BaseFragment() {
    private var textChangedListener: SimplifiedTextWatcher? = null

    override fun getLayoutId(): Int = R.layout.fragment_hashtags_counter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActionsOnTools()
        setupInputView()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        context?.let { contextObject ->
            this.listener = contextObject as MainActivity
        }
    }

    override fun setupToolbar() {
        listener?.setToolbarTitle(getString(R.string.toolbar_title_hashtags_counter_fragment))
    }

    private fun setupActionsOnTools() {
        copyView.setOnClickListener {
            if (!getInputText().isEmpty()) {
                context?.let { contextObject ->
                    contextObject.copyToClipboard(getInputText())
                    contextObject.showToast(getString(R.string.toast_copy_text))
                }
            }
        }

        pasteView.setOnClickListener {
            context?.let { contextObject ->
                if (getInputText().length == 5000) {
                    contextObject.showToast(getString(R.string.toast_input_view_limit_achieved))
                } else {
                    val textToPaste = contextObject.pasteFromClipboard()
                    textToPaste?.let { pasteThisText ->
                        val textAfterPaste = getInputText() + pasteThisText
                        inputView.setText(textAfterPaste)
                        inputView.requestFocus()
                        inputView.setSelection(inputView.length())
                    } ?: contextObject.showToast(getString(R.string.toast_nothing_to_paste))
                }
            }
        }

        clearView.setOnClickListener {
            if (!getInputText().isEmpty()) {
                inputView.setText("")
            }
        }
    }

    private fun setupInputView() {
        if (textChangedListener == null) {
            textChangedListener = object : SimplifiedTextWatcher() {
                override fun afterTextChanged(s: Editable?) {
                    s?.let { text ->
                        setupCounterView(StringUtils.countHashtagsFromText(text.toString()))
                        setupCounterCharsView()
                    }
                }
            }
        }
        removeTextChangedListener()
        addTextChangedListener()
    }

    private fun setupCounterView(hashtagsCount: Int) {
        hashtagsCounterView.text = StringUtils.getHashtagsCounterText(hashtagsCount, context)
        hashtagsCounterView.setTextColor(ContextCompat.getColor(App.instance.applicationContext,
            ColorUtils.getTextColor(hashtagsCount)))
    }

    private fun setupCounterCharsView() {
        inputLengthView.text = StringUtils.getCharsCountText(inputView.text?.length ?: 0, context)
    }

    private fun getInputText(): String = inputView.text?.toString() ?: ""

    private fun addTextChangedListener() {
        (inputView as TextInputEditText).addTextChangedListener(textChangedListener)
    }

    private fun removeTextChangedListener() {
        (inputView as TextInputEditText).removeTextChangedListener(textChangedListener)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Timber.i("onSaveInstanceState")
        outState.putString(MyConstants.KEY_SAVE_INPUT, getInputText())
        outState.putString(MyConstants.KEY_SAVE_CHARS_NUMBER, inputLengthView.text?.toString() ?: "")
        outState.putString(MyConstants.KEY_SAVE_HASHTAGS, hashtagsCounterView.text?.toString() ?: "")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let {
            Timber.i("restore information from saved instance state")
            inputView.setText(it.getString(MyConstants.KEY_SAVE_INPUT)?.toString() ?: "")
            inputLengthView.text = it.getString(MyConstants.KEY_SAVE_CHARS_NUMBER)?.toString() ?: ""
            hashtagsCounterView.text = it.getString(MyConstants.KEY_SAVE_HASHTAGS)?.toString() ?: ""
        }
    }
}