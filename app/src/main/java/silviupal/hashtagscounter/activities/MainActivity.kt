package silviupal.hashtagscounter.activities

import android.os.Bundle
import android.widget.Toast
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import kotlinx.android.synthetic.main.activity_main.*
import silviupal.hashtagscounter.Enums
import silviupal.hashtagscounter.R
import silviupal.hashtagscounter.base.BaseActivity
import silviupal.hashtagscounter.fragments.HashtagsCounterFragment
import silviupal.hashtagscounter.fragments.HashtagsListFragment

class MainActivity : BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        setHashtagsListFragment()
        setDrawer()
    }

    private fun setHashtagsListFragment() {
        val fragment = HashtagsListFragment()
        switchFragment(fragment)
    }

    private fun setHashtagsCounterFragment() {
        val fragment = HashtagsCounterFragment()
        fragment.setState(Enums.HashtagsCounterFragmentStates.NO_BACK_BUTTON)
        switchFragment(fragment)
    }

    private fun setDrawer() {
        val hashtagsListItem = PrimaryDrawerItem().withIdentifier(1)
            .withName(R.string.toolbar_title_hashtags_list)
        val hashtagsCounterItem = PrimaryDrawerItem().withIdentifier(2)
            .withName(R.string.toolbar_title_hashtags_counter_fragment)
        val aboutItem = PrimaryDrawerItem().withIdentifier(3)
            .withSelectable(false)
            .withName(R.string.toolbar_title_about)
        val rateAppItem = PrimaryDrawerItem().withIdentifier(4)
            .withSelectable(false)
            .withName(R.string.toolbar_title_rate_the_app)

        val result = DrawerBuilder().withActivity(this).withToolbar(toolbar)
            .addDrawerItems(hashtagsListItem, hashtagsCounterItem, DividerDrawerItem(), aboutItem, rateAppItem)
            .build()

        result.onDrawerItemClickListener = Drawer.OnDrawerItemClickListener { _, _, drawerItem ->
            when (drawerItem.identifier) {
                1L -> {
                    setHashtagsListFragment()
                }
                2L -> {
                    setHashtagsCounterFragment()
                }
                3L -> {
                    Toast.makeText(this, "About", Toast.LENGTH_SHORT).show()
                }
                4L -> {
                    Toast.makeText(this, "Rate", Toast.LENGTH_SHORT).show()
                }
            }
            result.closeDrawer()
            true
        }
    }

    override fun setupToolbarFromFragment(toolbarTitle: String, showBackButton: Boolean) {
        supportActionBar?.apply {
            title = toolbarTitle
        }
    }
}
