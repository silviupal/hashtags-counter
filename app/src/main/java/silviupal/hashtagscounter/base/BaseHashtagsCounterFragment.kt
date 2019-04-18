package silviupal.hashtagscounter.base

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import kotlinx.android.synthetic.main.merge_hashtags_counter_layout.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import silviupal.hashtagscounter.MyApp
import silviupal.hashtagscounter.MyConstants
import silviupal.hashtagscounter.R
import silviupal.hashtagscounter.database.MyDatabase
import silviupal.hashtagscounter.database.entities.HashtagEntity
import silviupal.hashtagscounter.extensions.copyToClipboard
import silviupal.hashtagscounter.extensions.pasteFromClipboard
import silviupal.hashtagscounter.extensions.showToast
import silviupal.hashtagscounter.helpers.SimplifiedTextWatcher
import silviupal.hashtagscounter.items.HashtagCheckboxItem
import silviupal.hashtagscounter.models.HashtagCheckboxModel
import silviupal.hashtagscounter.utils.ColorUtils
import silviupal.hashtagscounter.utils.DialogUtils
import silviupal.hashtagscounter.utils.HashtagsUtils
import timber.log.Timber
import java.util.regex.Pattern
import android.widget.TextView
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.text.SpannableString
import android.graphics.Color
import kotlinx.android.synthetic.main.fragment_edit_item.*
import kotlinx.android.synthetic.main.hashtag_list_dialog_layout.view.*
import silviupal.hashtagscounter.utils.KeyboardUtils

/**
 * Created by Silviu Pal on 4/9/2019.
 */
abstract class BaseHashtagsCounterFragment : BaseFragment() {
    val job = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + job)

    var textHasDuplicates: Boolean = false

    private var textChangedListener: SimplifiedTextWatcher? = null

    abstract fun setActionButton()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupInputView()
        setupActionsOnTools()
    }

    private fun setupActionsOnTools() {
        tvCopyView.setOnClickListener { view ->
            if (!getInputText().isEmpty()) {
                view.context.apply {
                    copyToClipboard(getInputText())
                    showToast(getString(R.string.toast_copy_text))
                }
            } else {
                view.context.showToast(getString(R.string.toast_nothing_to_copy))
            }
        }

        tvPasteView.setOnClickListener { view ->
            if (getInputText().length == MyConstants.MAX_INPUT_LENGTH) {
                view.context.showToast(getString(R.string.toast_input_view_limit_achieved))
            } else {
                val textToPaste = view.context.pasteFromClipboard()
                textToPaste?.let { pasteThisText ->
                    val textAfterPaste = getInputText() + pasteThisText
                    etInput.setText(textAfterPaste)
                    etInput.requestFocus()
                    etInput.setSelection(etInput.length())
                } ?: view.context.showToast(getString(R.string.toast_nothing_to_paste))
            }
        }

        tvClearView.setOnClickListener { view ->
            if (!getInputText().isEmpty()) {
                showClearDialog(view.context)
            } else {
                view.context.showToast(getString(R.string.toast_nothing_to_clear))
            }
        }

        tvInsertHashtag.setOnClickListener { view ->
            if (getInputText().length == MyConstants.MAX_INPUT_LENGTH) {
                view.context.showToast(getString(R.string.toast_input_view_limit_achieved))
            } else {
                showHashtagsDialog(view.context)
            }
        }
    }

    private fun showClearDialog(context: Context) {
        DialogUtils.buildNormalAlertDialog(context,
            getString(R.string.dialog_warning_title),
            getString(R.string.dialog_clear_text_message),
            DialogInterface.OnClickListener { _, _ ->
                etInput.setText("")
            }).show()
    }

    private fun setupInputView() {
        if (textChangedListener == null) {
            textChangedListener = object : SimplifiedTextWatcher() {
                private var lastUpdatedText = ""
                override fun afterTextChanged(s: Editable?) {
                    s?.let { text ->
                        tvInputError.visibility = View.GONE
                        setupCounterView(HashtagsUtils.getNumberOfHashtagsFromText(text.toString()))
                        setupCounterCharsView()

                        val currentText = text.toString()
                        if (currentText == lastUpdatedText) {
                            btnAction.isEnabled = true
                            return
                        }

                        lastUpdatedText = currentText

                        uiScope.launch {
                            btnAction.isEnabled = false
                            delay(1500)
                            if (currentText != lastUpdatedText) {
                                btnAction.isEnabled = true
                                return@launch
                            }
                            context?.let {
                                underlineDuplicates(text.toString())
                            }
                        }
                    }
                }
            }
        }
        removeTextChangedListener()
        addTextChangedListener()
    }

    private fun underlineDuplicates(text: String) {
        // get list of all hashtags
        val listWithDuplicates: List<String> = HashtagsUtils.getHashtagsList(text)
        // extract the unique hashtags from the initial list
        val listUniqueElements: List<String> = listWithDuplicates.distinct()
        // spannable for changing the text color
        var spannable: SpannableString? = null
        // comparing the size of initial list with the list of unique hashtags
        if (listWithDuplicates.size != listUniqueElements.size) {
            // We have duplicates!
            var duplicatesList: List<String> = emptyList()

            listUniqueElements.forEach { itemUnique ->
                var counter = 0
                listWithDuplicates.forEach { item ->
                    if (itemUnique.toLowerCase() == item.toLowerCase()) {
                        counter += 1
                    }
                }
                if (counter > 1) {
                    duplicatesList = duplicatesList + itemUnique
                }
            }

            // for each duplicate, change the color of the hashtag in text to RED
            duplicatesList.forEach { duplicateItem ->
                if (spannable == null) {
                    spannable = SpannableString(text)
                }

                val word = Pattern.compile("(?![\\p{L}_0-9])$duplicateItem(?![\\p{L}_0-9])")
                val matcher = word.matcher(text)

                while (matcher.find()) {
                    spannable?.setSpan(ForegroundColorSpan(Color.RED),
                        matcher.start(),
                        matcher.end(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }
            val currentPositionInText = etInput.selectionStart
            etInput.setText(spannable, TextView.BufferType.SPANNABLE)
            etInput.setSelection(currentPositionInText)
            textHasDuplicates = true
        } else {
            if (textHasDuplicates) {
                spannable = SpannableString(text)
                spannable?.setSpan(ForegroundColorSpan(ContextCompat.getColor(MyApp.instance.applicationContext,
                    R.color.textColorPrimary)),
                    0,
                    text.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                val currentPositionInText = etInput.selectionStart
                etInput.setText(spannable, TextView.BufferType.SPANNABLE)
                etInput.setSelection(currentPositionInText)
                textHasDuplicates = false
            }
        }
        btnAction?.isEnabled = true
    }

    private fun setupCounterView(hashtagsCount: Int) {
        tvHashtagsCounter.text = HashtagsUtils.getHashtagsCounterText(hashtagsCount, context)
        tvHashtagsCounter.setTextColor(ContextCompat.getColor(MyApp.instance.applicationContext,
            ColorUtils.getTextColor(hashtagsCount)))
    }

    private fun setupCounterCharsView() {
        tvInputLength.text = HashtagsUtils.getCharsCountText(etInput.text?.length ?: 0, context)
    }

    private fun getInputText(): String = etInput.text?.toString() ?: ""

    @SuppressLint("InflateParams")
    private fun showHashtagsDialog(context: Context) {
        uiScope.launch {
            var listFromDatabase = emptyList<HashtagEntity>()
            val deferred = async(Dispatchers.Default) {
                listFromDatabase = MyDatabase.database.daoHashtag().getAllItems()
            }
            deferred.await()

            lateinit var dialog: AlertDialog
            if (listFromDatabase.isEmpty()) {
                dialog = DialogUtils.buildInfoDialog(context,
                    getString(R.string.dialog_title_no_hashtags),
                    getString(R.string.dialog_message_no_hashtags))
                    .create()
            } else {
                val fastAdapter = FastItemAdapter<HashtagCheckboxItem>()
                val customView = LayoutInflater.from(context).inflate(R.layout.hashtag_list_dialog_layout, null, false)
                var items = emptyList<HashtagCheckboxItem>()
                listFromDatabase = listFromDatabase.sortedBy(HashtagEntity::name)
                listFromDatabase.forEach {
                    items = items + HashtagCheckboxItem(HashtagCheckboxModel(it.name))
                }
                fastAdapter.setNewList(items)
                customView.rvHashtags.adapter = fastAdapter

                dialog = DialogUtils.buildNoMessageNoTitleAlertDialog(context)
                    .setPositiveButton(getString(R.string.insert), null)
                    .setView(customView)
                    .create()

                dialog.setOnShowListener { dialogObject ->
                    val yesButton = (dialogObject as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
                    val noButton = (dialogObject as AlertDialog).getButton(AlertDialog.BUTTON_NEGATIVE)
                    yesButton.setOnClickListener {
                        var selectedHashtags: List<String> = emptyList()
                        fastAdapter.adapterItems.forEach {
                            if (it.model.isChecked) {
                                selectedHashtags = selectedHashtags + it.model.name
                            }
                        }
                        if (selectedHashtags.isNotEmpty()) {
                            var inputText = etInput.text.toString()
                            if (inputText.isEmpty()) {
                                inputText += selectedHashtags[0]
                                for (i in 1..(selectedHashtags.size - 1)) {
                                    inputText += " " + selectedHashtags[i]
                                }
                            } else {
                                selectedHashtags.forEach {
                                    inputText = "$inputText $it"
                                }
                            }
                            etInput.setText(inputText)
                            etInput.requestFocus()
                            if (inputText.length > MyConstants.MAX_INPUT_LENGTH) {
                                etInput.setSelection(MyConstants.MAX_INPUT_LENGTH)
                            } else {
                                etInput.setSelection(inputText.length)
                            }
                            dialogObject.dismiss()
                        } else {
                            context.showToast(getString(R.string.toast_nothing_to_insert))
                        }
                    }
                    noButton.setOnClickListener { view ->
                        activity?.let {
                            KeyboardUtils.hideKeyboard(view, it)
                            dialogObject.dismiss()
                        }
                    }
                }
            }
            dialog.show()
        }
    }

    private fun addTextChangedListener() {
        (etInput as TextInputEditText).addTextChangedListener(textChangedListener)
    }

    private fun removeTextChangedListener() {
        (etInput as TextInputEditText).removeTextChangedListener(textChangedListener)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Timber.i("onSaveInstanceState")
        outState.putString(MyConstants.KEY_SAVE_INPUT, getInputText())
        outState.putString(MyConstants.KEY_SAVE_CHARS_NUMBER, tvInputLength.text?.toString() ?: "")
        outState.putString(MyConstants.KEY_SAVE_HASHTAGS, tvHashtagsCounter.text?.toString() ?: "")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let {
            Timber.i("restore information from saved instance state")
            etInput.setText(it.getString(MyConstants.KEY_SAVE_INPUT)?.toString() ?: "")
            tvInputLength.text = it.getString(MyConstants.KEY_SAVE_CHARS_NUMBER)?.toString() ?: ""
            tvHashtagsCounter.text = it.getString(MyConstants.KEY_SAVE_HASHTAGS)?.toString() ?: ""
        }
    }
}