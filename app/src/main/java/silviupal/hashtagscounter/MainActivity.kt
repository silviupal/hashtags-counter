package silviupal.hashtagscounter

import android.os.Bundle
import android.text.Editable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_main.*
import silviupal.hashtagscounter.helpers.SimplifiedTextWatcher
import silviupal.hashtagscounter.utils.StringUtils
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    private var textChangedListener: SimplifiedTextWatcher? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("onCreate")
        setContentView(R.layout.activity_main)
        setupToolbar()
        savedInstanceState?.let {
            Timber.i("restore information from saved instance state")
            inputView.setText(it.getString(Constants.KEY_SAVE_INPUT)?.toString() ?: " ")
            inputLengthView.text = it.getString(Constants.KEY_SAVE_CHARS_NUMBER)?.toString() ?: " "
            hashtagsCounterView.text = it.getString(Constants.KEY_SAVE_HASHTAGS)?.toString() ?: " "
        }
        setupInputView()
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

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(false)
    }

    private fun setupCounterView(hashtagsCount: Int) {
        var hashtagsText = ""
        if (hashtagsCount == 0) {
            hashtagsText = getString(R.string.default_counter_hashtags_text)
            hashtagsCounterView.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
        } else {
            if (hashtagsCount > 30) {
                hashtagsCounterView.setTextColor(ContextCompat.getColor(this, R.color.error))
            } else {
                hashtagsCounterView.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
            }
            hashtagsText = String.format(getString(R.string.hashtags_counter_format),
                hashtagsCount,
                resources.getQuantityText(R.plurals.hashtags, hashtagsCount))
        }
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

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        Timber.i("onSaveInstanceState")
        outState?.let {
            it.putString(Constants.KEY_SAVE_INPUT, inputView.text?.toString() ?: " ")
            it.putString(Constants.KEY_SAVE_CHARS_NUMBER, inputLengthView.text?.toString() ?: " ")
            it.putString(Constants.KEY_SAVE_HASHTAGS, hashtagsCounterView.text?.toString() ?: " ")
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        Timber.i("onRestoreInstanceState")
        savedInstanceState?.let {
            inputView.setText(it.getString(Constants.KEY_SAVE_INPUT)?.toString() ?: " ")
            inputLengthView.text = it.getString(Constants.KEY_SAVE_CHARS_NUMBER)?.toString() ?: " "
            hashtagsCounterView.text = it.getString(Constants.KEY_SAVE_HASHTAGS)?.toString() ?: " "
        }
    }
}
