package silviupal.hashtagscounter.base

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import kotlinx.android.synthetic.main.hashtag_list_dialog_layout.view.*
import kotlinx.android.synthetic.main.merge_hashtags_counter_layout.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
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

/**
 * Created by Silviu Pal on 4/9/2019.
 */
abstract class BaseHashtagsCounterFragment : BaseFragment() {
    val job = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + job)

    private var textChangedListener: SimplifiedTextWatcher? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActionsOnTools()
        setupInputView()
    }

    private fun setupActionsOnTools() {
        tvCopyView.setOnClickListener {
            if (!getInputText().isEmpty()) {
                context?.apply {
                    copyToClipboard(getInputText())
                    showToast(getString(R.string.toast_copy_text))
                }
            }
        }

        tvPasteView.setOnClickListener {
            context?.let { contextObject ->
                if (getInputText().length == MyConstants.MAX_INPUT_LENGTH) {
                    contextObject.showToast(getString(R.string.toast_input_view_limit_achieved))
                } else {
                    val textToPaste = contextObject.pasteFromClipboard()
                    textToPaste?.let { pasteThisText ->
                        val textAfterPaste = getInputText() + pasteThisText
                        etInput.setText(textAfterPaste)
                        etInput.requestFocus()
                        etInput.setSelection(etInput.length())
                    } ?: contextObject.showToast(getString(R.string.toast_nothing_to_paste))
                }
            }
        }

        tvClearView.setOnClickListener {
            if (!getInputText().isEmpty()) {
                etInput.setText("")
            }
        }

        tvInsertHashtag.setOnClickListener {
            view?.let {
                showHashtagsDialog(it.context)
            }
        }
    }

    private fun setupInputView() {
        if (textChangedListener == null) {
            textChangedListener = object : SimplifiedTextWatcher() {
                override fun afterTextChanged(s: Editable?) {
                    s?.let { text ->
                        tvError.visibility = View.GONE
                        setupCounterView(HashtagsUtils.getNumberOfHashtagsFromText(text.toString()))
                        setupCounterCharsView()
                    }
                }
            }
        }
        removeTextChangedListener()
        addTextChangedListener()
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

    private fun showHashtagsDialog(context: Context) {
        val customView = LayoutInflater.from(context).inflate(R.layout.hashtag_list_dialog_layout, null, false)

        val fastAdapter = FastItemAdapter<HashtagCheckboxItem>()
        customView.rvHashtags.adapter = fastAdapter




        uiScope.launch {
            //Use dispatcher to switch between context
            var listFromDatabase = emptyList<HashtagEntity>()
            val deferred = async(Dispatchers.Default) {
                listFromDatabase = MyDatabase.database.daoHashtag().getAllItems()
            }
            deferred.await()
            if (listFromDatabase.isEmpty()) {
                //showEmptyScreen()
            } else {
                //hideEmptyScreen()
                var items = emptyList<HashtagCheckboxItem>()
                listFromDatabase.forEach {
                    items = items + HashtagCheckboxItem(HashtagCheckboxModel(it.name))
                }
                fastAdapter.setNewList(items)
            }
        }

        val dialog = DialogUtils.buildNoMessageNoTitleAlertDialog(context)
            .setPositiveButton(getString(R.string.create), null)
            .setView(customView)
            .create()

        dialog.setOnShowListener { dialogObject ->
            val yesButton = (dialogObject as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
            yesButton.setOnClickListener {
                dialogObject.dismiss()


                /*val hashtagName = customView.inputEditText.text.toString()
                if (hashtagName.isEmpty()) {
                    customView.tvError.text = getString(R.string.error_empty_error)
                    customView.tvError.visibility = View.VISIBLE
                } else {
                    if (HashtagsUtils.isNotAHashtag(hashtagName)) {
                        customView.tvError.text = getString(R.string.error_hashtag_invalid)
                        customView.tvError.visibility = View.VISIBLE
                    } else {
                        runCreateHashtag(hashtagName)
                        dialogObject.dismiss()
                    }
                }*/
            }
        }
        dialog.show()

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