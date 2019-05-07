package silviupal.hashtagscounter.fragments

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.IAdapter
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.mikepenz.fastadapter.listeners.ClickEventHook
import com.mikepenz.fastadapter.listeners.OnClickListener
import kotlinx.android.synthetic.main.fragment_your_posts.*
import kotlinx.android.synthetic.main.merge_empty_screen_layout.*
import kotlinx.android.synthetic.main.post_item_layout.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import silviupal.hashtagscounter.R
import silviupal.hashtagscounter.activities.MainActivity
import silviupal.hashtagscounter.base.BaseFragment
import silviupal.hashtagscounter.database.MyDatabase
import silviupal.hashtagscounter.database.entities.PostEntity
import silviupal.hashtagscounter.events.UpdatePostsListEvent
import silviupal.hashtagscounter.interfaces.IMainActivityFragmentListener
import silviupal.hashtagscounter.items.PostItem
import silviupal.hashtagscounter.utils.DialogUtils

/**
 * Created by Silviu Pal on 05/04/2019.
 */
class YourPostsFragment : BaseFragment() {
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)
    private var fastAdapter = FastItemAdapter<PostItem>()

    override fun getLayoutId(): Int = R.layout.fragment_your_posts

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.listener = context as MainActivity
        EventBus.getDefault().register(this)
    }

    override fun onDetach() {
        super.onDetach()
        EventBus.getDefault().unregister(this)
        job.cancel()
    }

    override fun setupToolbar() {
        listener?.setToolbarTitle(getString(R.string.toolbar_title_your_posts))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setEmptyScreen()
        configureAdapter()
        rvPosts.adapter = fastAdapter
        updateAdapter()
    }

    private fun setEmptyScreen() {
        emptyScreenTitle.setText(R.string.empty_screen_no_posts_title)
        emptyScreenMessage.setText(R.string.empty_screen_no_posts_message)
    }

    private fun configureAdapter() {
        fastAdapter.withSelectable(true)
        fastAdapter.withOnClickListener(object : OnClickListener<PostItem> {
            override fun onClick(v: View?,
                adapter: IAdapter<PostItem>,
                item: PostItem,
                position: Int): Boolean {
                listener?.let {
                    (it as IMainActivityFragmentListener).openCreateEditItemActivity(item.itemEntity.id)
                }
                return true
            }
        })

        fastAdapter.withEventHook(object : ClickEventHook<PostItem>() {
            @Nullable
            override fun onBind(viewHolder: RecyclerView.ViewHolder): View? {
                //return the views on which you want to bind this event
                return if (viewHolder is PostItem.ViewHolder) {
                    viewHolder.itemView.deleteBtn
                } else null
            }

            override fun onClick(v: View, position: Int, fastAdapter: FastAdapter<PostItem>, item: PostItem) {
                showDeleteDialog(v.context, item.itemEntity, position)
            }
        })
    }

    private fun showDeleteDialog(context: Context,
        itemEntity: PostEntity,
        position: Int) {
        DialogUtils.buildAlertDialogWithCheckbox(context,
            getString(R.string.dialog_warning_title),
            getString(R.string.dialog_delete_post_message),
            //getCheckbox(),
            null,
            DialogInterface.OnClickListener { _, _ ->
                runDeletePost(itemEntity, position)
            }).show()
    }

    private fun updateAdapter() {
        uiScope.launch {
            //Use dispatcher to switch between context
            var listFromDatabase = emptyList<PostEntity>()
            val deferred = async(Dispatchers.Default) {
                listFromDatabase = MyDatabase.database.daoPost().getAllItems()
            }
            deferred.await()
            if (listFromDatabase.isEmpty()) {
                showEmptyScreen()
            } else {
                hideEmptyScreen()
                var items = emptyList<PostItem>()
                listFromDatabase.forEach {
                    items = items + PostItem(it)
                }
                fastAdapter.setNewList(items)
            }
        }
    }

    private fun runDeletePost(itemEntity: PostEntity, position: Int) {
        uiScope.launch {
            val deferred = async(Dispatchers.Default) {
                MyDatabase.database.daoPost().deleteOneItem(itemEntity)
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onUpdateListFromDatabase(event: UpdatePostsListEvent) {
        updateAdapter()
    }
}