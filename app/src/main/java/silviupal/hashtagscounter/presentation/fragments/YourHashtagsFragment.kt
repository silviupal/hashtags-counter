package silviupal.hashtagscounter.presentation.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.IAdapter
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.mikepenz.fastadapter.listeners.ClickEventHook
import com.mikepenz.fastadapter.listeners.OnClickListener
import kotlinx.android.synthetic.main.fragment_your_hashtags.*
import kotlinx.android.synthetic.main.hashtag_dialog_layout.view.*
import kotlinx.android.synthetic.main.hashtag_item_layout.view.*
import kotlinx.android.synthetic.main.merge_empty_screen_layout.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import silviupal.hashtagscounter.R
import silviupal.hashtagscounter.presentation.activities.MainActivity
import silviupal.hashtagscounter.presentation.base.BaseFragment
import silviupal.hashtagscounter.data.database.MyDatabase
import silviupal.hashtagscounter.data.database.entities.HashtagEntity
import silviupal.hashtagscounter.common.extensions.showToast
import silviupal.hashtagscounter.common.helpers.SimplifiedTextWatcher
import silviupal.hashtagscounter.presentation.adapterItems.HashtagItem
import silviupal.hashtagscounter.common.utils.DialogUtils
import silviupal.hashtagscounter.common.utils.HashtagsUtils
import silviupal.hashtagscounter.common.utils.KeyboardUtils

/**
 * Created by Silviu Pal on 4/12/2019.
 */
class YourHashtagsFragment : BaseFragment() {
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)
    private var fastAdapter = FastItemAdapter<HashtagItem>()

    override fun getLayoutId(): Int = R.layout.fragment_your_hashtags

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.listener = context as MainActivity
    }

    override fun onDetach() {
        super.onDetach()
        job.cancel()
    }

    override fun setupToolbar() {
        listener?.setToolbarTitle(getString(R.string.toolbar_title_your_hashtags))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setEmptyScreen()
        setFabClickListener()
        configureAdapter()

        rvPosts.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    fab.hide()
                } else {
                    fab.show()
                }
            }
        })

        rvPosts.adapter = fastAdapter
        updateAdapter()
    }

    private fun setEmptyScreen() {
        emptyScreenTitle.setText(R.string.empty_screen_no_hashtags_title)
        emptyScreenMessage.setText(R.string.empty_screen_no_hashtags_message)
    }

    private fun configureAdapter() {
        fastAdapter.withSelectable(true)
        fastAdapter.withOnClickListener(object : OnClickListener<HashtagItem> {
            override fun onClick(v: View?, adapter: IAdapter<HashtagItem>, item: HashtagItem, position: Int): Boolean {
                context?.let {
                    showEditDialog(it, item.itemEntity)
                }
                return true
            }
        })
        fastAdapter.withEventHook(object : ClickEventHook<HashtagItem>() {
            override fun onBind(viewHolder: RecyclerView.ViewHolder): View? {
                return if (viewHolder is HashtagItem.ViewHolder) {
                    viewHolder.itemView.deleteBtn
                } else null
            }

            override fun onClick(v: View, position: Int, fastAdapter: FastAdapter<HashtagItem>, item: HashtagItem) {
                showDeleteDialog(v.context, item.itemEntity, position)
            }
        })
    }

    private fun setFabClickListener() {
        fab.setOnClickListener {
            context?.let(this::showCreateHashtagDialog)
        }
    }

    @SuppressLint("InflateParams")
    private fun showCreateHashtagDialog(context: Context) {
        val customView = LayoutInflater.from(context).inflate(R.layout.hashtag_dialog_layout, null, false)
        customView.tvDialogTitleView.text = getString(R.string.dialog_create_hashtag_title)
        customView.textInputLayout.hint = getString(R.string.hint_hashtag_input)
        customView.inputEditText.addTextChangedListener(object : SimplifiedTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                super.afterTextChanged(s)
                customView.tvInputError.visibility = View.GONE
            }
        })

        val dialog = DialogUtils.buildNoMessageNoTitleAlertDialog(context)
            .setPositiveButton(getString(R.string.create), null)
            .setView(customView)
            .create()

        dialog.setOnShowListener { dialogObject ->
            val yesButton = (dialogObject as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
            val noButton = dialogObject.getButton(AlertDialog.BUTTON_NEGATIVE)
            yesButton.setOnClickListener { onClick ->
                val hashtagName = customView.inputEditText.text.toString()
                when {
                    hashtagName.isEmpty() -> {
                        customView.tvInputError.text = getString(R.string.error_empty_error)
                        customView.tvInputError.visibility = View.VISIBLE
                    }
                    HashtagsUtils.isNotAHashtag(hashtagName) -> {
                        customView.tvInputError.text = getString(R.string.error_hashtag_invalid)
                        customView.tvInputError.visibility = View.VISIBLE
                    }
                    isHashtagAlreadyAdded(hashtagName) -> {
                        customView.tvInputError.text = getString(R.string.error_hashtag_already_created)
                        customView.tvInputError.visibility = View.VISIBLE
                    }
                    else -> {
                        runCreateHashtag(hashtagName)
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

    private fun isHashtagAlreadyAdded(hashtagName: String): Boolean {
        fastAdapter.adapterItems.forEach {
            if (hashtagName == it.itemEntity.name) {
                return true
            }
        }
        return false
    }

    @SuppressLint("InflateParams")
    private fun showEditDialog(context: Context, hashtagEntity: HashtagEntity) {
        val inputView = LayoutInflater.from(context).inflate(R.layout.hashtag_dialog_layout, null, false)
        inputView.tvDialogTitleView.text = getString(R.string.dialog_edit_hashtag_title)
        inputView.textInputLayout.hint = getString(R.string.hint_hashtag_input)
        inputView.inputEditText.setText(hashtagEntity.name)
        inputView.inputEditText.setSelection(hashtagEntity.name.length)
        inputView.inputEditText.addTextChangedListener(object : SimplifiedTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                super.afterTextChanged(s)
                inputView.tvInputError.visibility = View.GONE
            }
        })

        val dialog = DialogUtils.buildNoMessageNoTitleAlertDialog(context)
            .setPositiveButton(getString(R.string.update), null)
            .setView(inputView)
            .create()

        dialog.setOnShowListener { dialogObject ->
            val yesButton = (dialogObject as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
            val noButton = dialogObject.getButton(AlertDialog.BUTTON_NEGATIVE)
            yesButton.setOnClickListener { onClick ->
                val hashtagName = inputView.inputEditText.text.toString()
                when {
                    hashtagName.isEmpty() -> {
                        inputView.tvInputError.text = getString(R.string.error_empty_error)
                        inputView.tvInputError.visibility = View.VISIBLE
                    }
                    hashtagEntity.name == hashtagName -> context.showToast(getString(R.string.toast_nothing_changed))
                    HashtagsUtils.isNotAHashtag(hashtagName) -> {
                        inputView.tvInputError.text = getString(R.string.error_hashtag_invalid)
                        inputView.tvInputError.visibility = View.VISIBLE
                    }
                    else -> {
                        runUpdateHashtag(HashtagEntity(hashtagEntity.id,
                            hashtagName))
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

    private fun showDeleteDialog(context: Context,
        itemEntity: HashtagEntity,
        position: Int) {
        DialogUtils.buildAlertDialogWithCheckbox(context,
            getString(R.string.dialog_warning_title),
            getString(R.string.dialog_delete_hashtag_message),
            //getCheckbox(),
            null,
            DialogInterface.OnClickListener { _, _ ->
                runDeleteHashtag(itemEntity, position)
            }).show()
    }

    private fun updateAdapter() {
        uiScope.launch {
            //Use dispatcher to switch between context
            var listFromDatabase = emptyList<HashtagEntity>()
            val deferred = async(Dispatchers.Default) {
                listFromDatabase = MyDatabase.database.daoHashtag().getAllItems()
            }
            deferred.await()
            if (listFromDatabase.isEmpty()) {
                showEmptyScreen()
            } else {
                hideEmptyScreen()
                listFromDatabase = listFromDatabase.sortedBy { it.name.toLowerCase() }
                var items = emptyList<HashtagItem>()
                listFromDatabase.forEach {
                    items = items + HashtagItem(it)
                }
                fastAdapter.setNewList(items)
            }
        }
    }

    private fun runCreateHashtag(hashtagName: String) {
        uiScope.launch {
            //Use dispatcher to switch between context
            val deferred = async(Dispatchers.Default) {
                val itemEntity = HashtagEntity(0, hashtagName)
                MyDatabase.database.daoHashtag().insertOneItem(itemEntity)
            }
            deferred.await()
            context?.showToast(getString(R.string.toast_create_hashtag_successfully))
            updateAdapter()
        }
    }

    private fun runUpdateHashtag(itemEntity: HashtagEntity) {
        uiScope.launch {
            //Use dispatcher to switch between context
            val deferred = async(Dispatchers.Default) {
                MyDatabase.database.daoHashtag().updateOneItem(itemEntity)
            }
            deferred.await()
            context?.showToast(getString(R.string.toast_update_hashtag_successfully))
            updateAdapter()
        }
    }

    private fun runDeleteHashtag(itemEntity: HashtagEntity, position: Int) {
        uiScope.launch {
            val deferred = async(Dispatchers.Default) {
                MyDatabase.database.daoHashtag().deleteOneItem(itemEntity)
            }
            deferred.await()
            if (fastAdapter.itemCount == 1) {
                fastAdapter.remove(position)
                showEmptyScreen()
            } else {
                fastAdapter.remove(position)
            }
        }
    }

    private fun hideEmptyScreen() {
        rvPosts.visibility = View.VISIBLE
        emptyScreenLayout.visibility = View.GONE
    }

    private fun showEmptyScreen() {
        rvPosts.visibility = View.GONE
        emptyScreenLayout.visibility = View.VISIBLE
    }
}