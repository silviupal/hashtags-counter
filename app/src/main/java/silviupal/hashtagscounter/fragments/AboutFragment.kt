package silviupal.hashtagscounter.fragments

import android.content.Context
import silviupal.hashtagscounter.R
import silviupal.hashtagscounter.activities.MainActivity
import silviupal.hashtagscounter.base.BaseFragment

/**
 * Created by Silviu Pal on 4/18/2019.
 */
class AboutFragment : BaseFragment() {
    override fun getLayoutId(): Int = R.layout.fragment_about

    override fun setupToolbar() {
        listener?.setToolbarTitle(getString(R.string.toolbar_title_about))
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.listener = context as MainActivity
    }
}