package silviupal.hashtagscounter.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import kotlinx.android.synthetic.main.fragment_your_posts.*
import silviupal.hashtagscounter.MyEnums
import silviupal.hashtagscounter.R
import silviupal.hashtagscounter.activities.MainActivity
import silviupal.hashtagscounter.base.BaseFragment
import silviupal.hashtagscounter.database.entities.ListItemEntity
import silviupal.hashtagscounter.database.entities.ListItemModel
import silviupal.hashtagscounter.interfaces.IMainActivityFragmentListener
import java.util.Date

/**
 * Created by Silviu Pal on 05/04/2019.
 */
class YourPostsFragment : BaseFragment() {
    override fun getLayoutId(): Int = R.layout.fragment_your_posts

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        context?.let { contextObject ->
            this.listener = contextObject as MainActivity
        }
    }

    override fun setupToolbar() {
        listener?.setToolbarTitle(getString(R.string.toolbar_title_your_posts))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fab.setOnClickListener {
            listener?.let {
                (it as IMainActivityFragmentListener).openCreateEditItemActivity(MyEnums.CreateOrEditState.CREATE_ITEM)
            }
        }

        val fastAdapter = FastItemAdapter<ListItemModel>()
        recyclerView.adapter = fastAdapter

        val listItem = ListItemModel(ListItemEntity(0, "Titlu", "Bla bla bla", 10, 100, Date().toString()))
        fastAdapter.add(listItem)
        fastAdapter.add(listItem)
        fastAdapter.add(listItem)
        fastAdapter.add(listItem)
    }
}