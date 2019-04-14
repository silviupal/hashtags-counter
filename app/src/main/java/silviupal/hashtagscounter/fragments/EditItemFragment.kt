package silviupal.hashtagscounter.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import kotlinx.android.synthetic.main.fragment_base_post_actions_layout.*
import kotlinx.android.synthetic.main.merge_hashtags_counter_layout.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import silviupal.hashtagscounter.MyConstants
import silviupal.hashtagscounter.R
import silviupal.hashtagscounter.base.BasePostActionsFragment
import silviupal.hashtagscounter.database.MyDatabase
import silviupal.hashtagscounter.database.entities.PostEntity
import silviupal.hashtagscounter.events.UpdatePostsListEvent
import silviupal.hashtagscounter.extensions.showToast
import silviupal.hashtagscounter.utils.DateUtils
import silviupal.hashtagscounter.utils.DialogUtils
import silviupal.hashtagscounter.utils.KeyboardUtils
import silviupal.hashtagscounter.utils.MyViewsUtils
import silviupal.hashtagscounter.utils.SharedPrefsUtils
import silviupal.hashtagscounter.utils.HashtagsUtils

/**
 * Created by Silviu Pal on 05/04/2019.
 */
class EditItemFragment : BasePostActionsFragment() {
    private var currentEntity: PostEntity? = null
    private var itemId: Int = -1

    override fun setupToolbar() {
        listener?.setToolbarTitle(getString(R.string.toolbar_title_hashtags_update_item))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        itemId = arguments?.getInt(MyConstants.ARGS_ITEM_ID) ?: -1
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        runPopulateLayoutFromDB()
    }

    override fun setActionButton() {
        btnAction.text = getString(R.string.btn_update_post)
        btnAction.setOnClickListener {
            val titleText: String = titleInputView.text?.toString() ?: ""
            val postText: String = etInput.text?.toString() ?: ""

            if (titleText.isEmpty() || postText.isEmpty()) {
                showErrors(titleText, postText)
            } else {
                when {
                    nothingChanged() -> context?.showToast(getString(R.string.toast_nothing_changed))
                    shouldShowUpdatePostDialog() -> showUpdatePostDialog()
                    else -> runUpdatePost(titleText, postText)
                }
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
                activity?.let {
                    KeyboardUtils.showKeyboard(titleInputView, it)
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