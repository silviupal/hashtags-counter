package silviupal.hashtagscounter.fragments

import android.content.Context
import silviupal.hashtagscounter.R
import silviupal.hashtagscounter.activities.MainActivity
import silviupal.hashtagscounter.base.BaseHashtagsCounterFragment

/**
 * Created by Silviu Pal on 05/04/2019.
 */
open class HashtagsCounterFragment : BaseHashtagsCounterFragment() {
    override fun getLayoutId(): Int = R.layout.fragment_hashtags_counter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.listener = context as MainActivity
    }

    override fun setupToolbar() {
        listener?.setToolbarTitle(getString(R.string.toolbar_title_hashtags_counter_fragment))
    }
}