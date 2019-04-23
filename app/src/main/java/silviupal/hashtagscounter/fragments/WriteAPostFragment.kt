package silviupal.hashtagscounter.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.hashtag_dialog_layout.view.*
import kotlinx.android.synthetic.main.merge_hashtags_counter_layout.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import silviupal.hashtagscounter.R
import silviupal.hashtagscounter.activities.MainActivity
import silviupal.hashtagscounter.base.BaseHashtagsCounterFragment
import silviupal.hashtagscounter.database.MyDatabase
import silviupal.hashtagscounter.database.entities.PostEntity
import silviupal.hashtagscounter.extensions.showToast
import silviupal.hashtagscounter.helpers.SimplifiedTextWatcher
import silviupal.hashtagscounter.utils.DateUtils
import silviupal.hashtagscounter.utils.DialogUtils
import silviupal.hashtagscounter.utils.HashtagsUtils
import silviupal.hashtagscounter.utils.KeyboardUtils

/**
 * Created by Silviu Pal on 05/04/2019.
 */
open class WriteAPostFragment : BaseHashtagsCounterFragment() {
    override fun getLayoutId(): Int = R.layout.fragment_write_a_post

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.listener = context as MainActivity
    }

    override fun setupToolbar() {
        listener?.setToolbarTitle(getString(R.string.toolbar_title_write_a_post_fragment))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setActionButton()
    }

    override fun setActionButton() {
        btnAction.text = getString(R.string.btn_save_post)
        btnAction.setOnClickListener { view ->
            val postText: String = etInput.text?.toString() ?: ""
            when {
                postText.isEmpty() -> showErrors(postText)
                textHasDuplicates -> context?.showToast(getString(R.string.toast_the_text_has_duplicates))
                else -> showInsertTitleDialog(view.context, postText)
            }
        }
    }

    @SuppressLint("InflateParams")
    private fun showInsertTitleDialog(context: Context, postText: String) {
        val customView = LayoutInflater.from(context).inflate(R.layout.save_post_dialog_layout, null, false)
        customView.inputEditText.addTextChangedListener(object : SimplifiedTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                super.afterTextChanged(s)
                customView.tvInputError.visibility = View.GONE
            }
        })

        val dialog = DialogUtils.buildNoMessageNoTitleAlertDialog(context)
            .setPositiveButton(getString(R.string.save), null)
            .setView(customView)
            .create()

        dialog.setOnShowListener { dialogObject ->
            val yesButton = (dialogObject as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
            val noButton = dialogObject.getButton(AlertDialog.BUTTON_NEGATIVE)
            yesButton.setOnClickListener { onClick ->
                val postTitle = customView.inputEditText.text.toString()
                when {
                    postTitle.isEmpty() -> {
                        customView.tvInputError.text = getString(R.string.error_empty_error)
                        customView.tvInputError.visibility = View.VISIBLE
                    }
                    else -> {
                        runCreatePost(postTitle, postText)
                        context.showToast(getString(R.string.toast_create_post_successfully))
                        activity?.let {
                            KeyboardUtils.hideKeyboard(onClick, it)
                        }
                        dialogObject.dismiss()
                    }
                }
            }
            noButton.setOnClickListener { view ->
                activity?.let {
                    KeyboardUtils.hideKeyboard(view, it)
                    dialogObject.dismiss()
                }
            }
        }
        dialog.show()
    }

    private fun showErrors(postText: String) {
        if (postText.isEmpty()) {
            tvInputError.visibility = View.VISIBLE
        }
    }

    private fun runCreatePost(titleText: String, postText: String) {
        uiScope.launch {
            //Use dispatcher to switch between context
            val deferred = async(Dispatchers.Default) {
                val itemEntity = PostEntity(0,
                    titleText,
                    postText,
                    HashtagsUtils.getNumberOfHashtagsFromText(postText),
                    postText.length,
                    DateUtils.getCurrentDateAndTime())
                MyDatabase.database.daoPost().insertOneItem(itemEntity)
            }
            deferred.await()
        }
    }
}