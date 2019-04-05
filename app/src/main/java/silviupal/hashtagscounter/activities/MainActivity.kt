package silviupal.hashtagscounter.activities

import android.os.Bundle
import com.mikepenz.materialdrawer.DrawerBuilder
import kotlinx.android.synthetic.main.activity_main.*
import silviupal.hashtagscounter.Enums
import silviupal.hashtagscounter.R
import silviupal.hashtagscounter.base.BaseActivity
import silviupal.hashtagscounter.fragments.HashtagsCounterFragment

class MainActivity : BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        setHashtagsCounterFragment()
        setDrawer()
    }

    private fun setHashtagsCounterFragment() {
        val fragment = HashtagsCounterFragment()
        fragment.listener = this
        fragment.setState(Enums.HashtagsCounterFragmentStates.NO_BACK_BUTTON)
        switchFragment(fragment)
    }

    private fun setDrawer() {
        DrawerBuilder().withActivity(this).withToolbar(toolbar).build()
    }

    override fun setupToolbarFromFragment(toolbarTitle: String, showBackButton: Boolean) {
        supportActionBar?.apply {
            title = toolbarTitle
        }
    }
}
