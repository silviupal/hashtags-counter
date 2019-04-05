package silviupal.hashtagscounter.fragments

import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_hashtags_counter.*
import silviupal.hashtagscounter.App
import silviupal.hashtagscounter.Constants
import silviupal.hashtagscounter.Enums
import silviupal.hashtagscounter.R
import silviupal.hashtagscounter.base.BaseFragment
import silviupal.hashtagscounter.helpers.SimplifiedTextWatcher
import silviupal.hashtagscounter.utils.StringUtils
import timber.log.Timber

/**
 * Created by Silviu Pal on 05/04/2019.
 */
class HashtagsCounterFragment : BaseFragment() {
    private var textChangedListener: SimplifiedTextWatcher? = null
    private var state: Enums.HashtagsCounterFragmentStates? = null

    override fun getLayoutId(): Int = R.layout.fragment_hashtags_counter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupInputView()
    }

    private fun setupToolbar() {
        state?.let {
            when (it) {
                Enums.HashtagsCounterFragmentStates.NO_BACK_BUTTON -> {
                    listener?.setupToolbarFromFragment(getString(R.string.toolbar_title_hashtags_counter_fragment),
                        showBackButton = false)
                }
                Enums.HashtagsCounterFragmentStates.WITH_BACK_BUTTON -> {
                    listener?.setupToolbarFromFragment(getString(R.string.toolbar_title_hashtags_counter_fragment),
                        showBackButton = true)
                }
            }
        }
    }

    private fun setupInputView() {
        if (textChangedListener == null) {
            textChangedListener = object : SimplifiedTextWatcher() {
                override fun afterTextChanged(s: Editable?) {
                    Timber.i("AfterTextChanged is called")
                    s?.let { text ->
                        Timber.i("text is not null. text = $text")
                        setupCounterView(StringUtils.countHashtags(text))
                        setupCounterCharsView()
                    }
                }
            }
        }
        (inputView as TextInputEditText).removeTextChangedListener(textChangedListener)
        (inputView as TextInputEditText).addTextChangedListener(textChangedListener)
    }

    private fun setupCounterView(hashtagsCount: Int) {
        val hashtagsText: String
        val textColor: Int
        if (hashtagsCount == 0) {
            hashtagsText = getString(R.string.default_counter_hashtags_text)
            textColor = R.color.textColorPrimary
        } else {
            hashtagsText = String.format(getString(R.string.hashtags_counter_format),
                hashtagsCount,
                resources.getQuantityText(R.plurals.hashtags, hashtagsCount))
            textColor = if (hashtagsCount > 30) {
                R.color.error
            } else {
                R.color.textColorPrimary
            }
        }
        hashtagsCounterView.setTextColor(ContextCompat.getColor(App.instance.applicationContext, textColor))
        hashtagsCounterView.text = hashtagsText
    }

    private fun setupCounterCharsView() {
        val textSize = inputView.text?.length ?: 0
        inputLengthView.text = if (textSize == 0) {
            getString(R.string.default_counter_input_text)
        } else {
            String.format(getString(R.string.hashtags_counter_format),
                textSize,
                resources.getQuantityString(R.plurals.textLength, textSize))
        }
    }

    fun setState(state: Enums.HashtagsCounterFragmentStates) {
        this.state = state
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Timber.i("onSaveInstanceState")
        outState.putString(Constants.KEY_SAVE_INPUT, inputView.text?.toString() ?: " ")
        outState.putString(Constants.KEY_SAVE_CHARS_NUMBER, inputLengthView.text?.toString() ?: " ")
        outState.putString(Constants.KEY_SAVE_HASHTAGS, hashtagsCounterView.text?.toString() ?: " ")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let {
            Timber.i("restore information from saved instance state")
            inputView.setText(it.getString(Constants.KEY_SAVE_INPUT)?.toString() ?: " ")
            inputLengthView.text = it.getString(Constants.KEY_SAVE_CHARS_NUMBER)?.toString() ?: " "
            hashtagsCounterView.text = it.getString(Constants.KEY_SAVE_HASHTAGS)?.toString() ?: " "
        }
    }
}