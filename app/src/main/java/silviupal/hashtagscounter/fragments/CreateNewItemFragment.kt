package silviupal.hashtagscounter.fragments

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_base_post_actions_layout.*
import kotlinx.android.synthetic.main.merge_hashtags_counter_layout.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import silviupal.hashtagscounter.R
import silviupal.hashtagscounter.base.BasePostActionsFragment
import silviupal.hashtagscounter.database.MyDatabase
import silviupal.hashtagscounter.database.entities.PostEntity
import silviupal.hashtagscounter.events.UpdatePostsListEvent
import silviupal.hashtagscounter.extensions.showToast
import silviupal.hashtagscounter.utils.DateUtils
import silviupal.hashtagscounter.utils.KeyboardUtils
import silviupal.hashtagscounter.utils.HashtagsUtils

/**
 * Created by Silviu Pal on 4/7/2019.
 */
class CreateNewItemFragment : BasePostActionsFragment() {
    override fun setupToolbar() {
        listener?.setToolbarTitle(getString(R.string.toolbar_title_hashtags_create_new_item))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        titleInputView.requestFocus()
        activity?.let {
            KeyboardUtils.showKeyboard(titleInputView, it)
        }
    }

    override fun setActionButton() {
        btnAction.text = getString(R.string.btn_create_post)
        btnAction.setOnClickListener {
            val titleText: String = titleInputView.text?.toString() ?: ""
            val postText: String = etInput.text?.toString() ?: ""

            if (titleText.isEmpty() || postText.isEmpty()) {
                showErrors(titleText, postText)
            } else {
                runCreatePost(titleText, postText)
            }
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
            context?.showToast(getString(R.string.toast_create_post_successfully))
            EventBus.getDefault().post(UpdatePostsListEvent())
            activity?.finish()
        }
    }
}