package silviupal.hashtagscounter.fragments

import android.os.Bundle
import android.view.View
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import kotlinx.android.synthetic.main.fragment_hashtags_list.*
import silviupal.hashtagscounter.R
import silviupal.hashtagscounter.base.BaseFragment
import silviupal.hashtagscounter.models.ListItemModel

/**
 * Created by Silviu Pal on 05/04/2019.
 */
class HashtagsListFragment : BaseFragment() {
    override fun getLayoutId(): Int = R.layout.fragment_hashtags_list

    override fun setupToolbar() {
        listener?.setupToolbarFromFragment(getString(R.string.toolbar_title_hashtags_list),
            showBackButton = false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fastAdapter = FastItemAdapter<ListItemModel>()
        recyclerView.adapter = fastAdapter

        val listItem = ListItemModel(0, "Titlu", "Bla bla bla", 10, 100)
        fastAdapter.add(listItem)
        fastAdapter.add(listItem)
        fastAdapter.add(listItem)
        fastAdapter.add(listItem)
    }
}