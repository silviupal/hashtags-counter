package silviupal.hashtagscounter.fragments

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.CompoundButton
import com.mindorks.editdrawabletext.DrawablePosition
import com.mindorks.editdrawabletext.onDrawableClickListener
import kotlinx.android.synthetic.main.fragment_edit_item.*
import kotlinx.android.synthetic.main.merge_hashtags_counter_layout.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import silviupal.hashtagscounter.MyConstants
import silviupal.hashtagscounter.R
import silviupal.hashtagscounter.activities.EditItemActivity
import silviupal.hashtagscounter.base.BaseHashtagsCounterFragment
import silviupal.hashtagscounter.database.MyDatabase
import silviupal.hashtagscounter.database.entities.PostEntity
import silviupal.hashtagscounter.events.UpdatePostsListEvent
import silviupal.hashtagscounter.extensions.showToast
import silviupal.hashtagscounter.helpers.SimplifiedTextWatcher
import silviupal.hashtagscounter.utils.DateUtils
import silviupal.hashtagscounter.utils.DialogUtils
import silviupal.hashtagscounter.utils.KeyboardUtils
import silviupal.hashtagscounter.utils.MyViewsUtils
import silviupal.hashtagscounter.utils.SharedPrefsUtils
import silviupal.hashtagscounter.utils.HashtagsUtils

/**
 * Created by Silviu Pal on 05/04/2019.
 */
class EditItemFragment : BaseHashtagsCounterFragment() {
    private var currentEntity: PostEntity? = null
    private var itemId: Int = -1

    override fun getLayoutId(): Int = R.layout.fragment_edit_item

    override fun setupToolbar() {
        listener?.setToolbarTitle(getString(R.string.toolbar_title_hashtags_update_item))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        itemId = arguments?.getInt(MyConstants.ARGS_ITEM_ID) ?: -1
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setActionButton()
        titleInputView.addTextChangedListener(object : SimplifiedTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                super.afterTextChanged(s)
                s?.let {
                    if (it.toString().isEmpty()) {
                        titleInputView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                    } else {
                        titleInputView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_delete, 0)
                    }
                }
                errorTitleInputView.visibility = View.GONE
            }
        })

        titleInputView.setDrawableClickListener(object : onDrawableClickListener {
            override fun onClick(target: DrawablePosition) {
                if (target == DrawablePosition.RIGHT) {
                    titleInputView.setText("")
                }
            }
        })
        runPopulateLayoutFromDB()
    }

    private fun showErrors(titleText: String, postText: String) {
        if (titleText.isEmpty()) {
            errorTitleInputView.visibility = View.VISIBLE
        }
        if (postText.isEmpty()) {
            tvInputError.visibility = View.VISIBLE
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.listener = context as EditItemActivity
    }

    override fun onDetach() {
        super.onDetach()
        job.cancel()
    }

    override fun setActionButton() {
        btnAction.text = getString(R.string.btn_update_post)
        btnAction.setOnClickListener {
            val titleText: String = titleInputView.text?.toString() ?: ""
            val postText: String = etInput.text?.toString() ?: ""
            when {
                titleText.isEmpty() || postText.isEmpty() -> showErrors(titleText, postText)
                nothingChanged() -> context?.showToast(getString(R.string.toast_nothing_changed))
                textHasDuplicates -> context?.showToast(getString(R.string.toast_the_text_has_duplicates))
                shouldShowUpdatePostDialog() -> showUpdatePostDialog()
                else -> runUpdatePost(titleText, postText)
            }
        }
    }

    private fun nothingChanged(): Boolean {
        currentEntity?.let { entity ->
            val titleText: String = titleInputView.text?.toString() ?: ""
            val postText: String = etInput.text?.toString() ?: ""

            if (titleText == entity.title && postText == entity.text) {
                return true
            }
        }
        return false
    }

    private fun showUpdatePostDialog() {
        context?.let {
            DialogUtils.buildAlertDialogWithCheckbox(it,
                getString(R.string.dialog_warning_title),
                getString(R.string.dialog_edit_post_message),
                getCheckbox(),
                DialogInterface.OnClickListener { _, _ ->
                    val titleText: String = titleInputView.text?.toString() ?: ""
                    val postText: String = etInput.text?.toString() ?: ""
                    runUpdatePost(titleText, postText)
                }).show()
        }
    }

    private fun shouldShowUpdatePostDialog(): Boolean =
        SharedPrefsUtils.getBool(MyConstants.SHOW_UPDATE_POST_DIALOG, true)

    private fun runPopulateLayoutFromDB() {
        uiScope.launch {
            //Use dispatcher to switch between context
            val deferred = async(Dispatchers.Default) {
                MyDatabase.database.daoPost().getListItemById(itemId)
            }
            val entity: PostEntity? = deferred.await()
            entity?.let {
                currentEntity = it
                titleInputView.setText(it.title)
                titleInputView.requestFocus()
                titleInputView.setSelection(it.title.length)
                etInput.setText(it.text)
                activity?.let { context ->
                    KeyboardUtils.showKeyboard(etInput, context)
                }
            }
        }
    }

    private fun runUpdatePost(titleText: String, postText: String) {
        uiScope.launch {
            //Use dispatcher to switch between context
            val deferred = async(Dispatchers.Default) {
                val itemEntity = PostEntity(itemId,
                    titleText,
                    postText,
                    HashtagsUtils.getNumberOfHashtagsFromText(postText),
                    postText.length,
                    DateUtils.getCurrentDateAndTime())
                MyDatabase.database.daoPost().updateOneItem(itemEntity)
            }
            deferred.await()
            context?.showToast(getString(R.string.toast_update_post_successfully))
            EventBus.getDefault().post(UpdatePostsListEvent())
            activity?.finish()
        }
    }

    private fun getCheckbox(): View? {
        val checkedListener = CompoundButton.OnCheckedChangeListener { _, isChecked ->
            SharedPrefsUtils.putBool(MyConstants.SHOW_UPDATE_POST_DIALOG, !isChecked)
        }
        context?.let {
            return MyViewsUtils.getCheckbox(it, checkedListener, getString(R.string.checkbox_dont_show_this_again))
        } ?: return null
    }

    companion object {
        fun newInstance(itemId: Int): EditItemFragment {
            val args = Bundle()
            args.putInt(MyConstants.ARGS_ITEM_ID, itemId)
            val fragment = EditItemFragment()
            fragment.arguments = args
            return fragment
        }
    }
}