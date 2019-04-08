package silviupal.hashtagscounter.fragments

import silviupal.hashtagscounter.R
import silviupal.hashtagscounter.base.BaseFragment

/**
 * Created by Silviu Pal on 4/7/2019.
 */
class CreateNewItemFragment : BaseFragment() {
    override fun getLayoutId(): Int = R.layout.fragment_hashtags_create_new_item

    override fun setupToolbar() {
        listener?.setToolbarTitle(getString(R.string.toolbar_title_hashtags_create_new_item))
    }

}