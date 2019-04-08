package silviupal.hashtagscounter.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import kotlinx.android.synthetic.main.toolbar_layout.*
import silviupal.hashtagscounter.MyConstants
import silviupal.hashtagscounter.MyEnums
import silviupal.hashtagscounter.R
import silviupal.hashtagscounter.base.BaseActivity
import silviupal.hashtagscounter.fragments.HashtagsCounterFragment
import silviupal.hashtagscounter.fragments.YourPostsFragment
import silviupal.hashtagscounter.interfaces.IMainActivityFragmentListener

class MainActivity : BaseActivity(), IMainActivityFragmentListener {
    override fun getLayoutId(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        setYourPostsFragment()
        setDrawer()
    }

    private fun setYourPostsFragment() {
        val fragment = YourPostsFragment()
        switchFragment(fragment)
    }

    private fun setHashtagsCounterFragment() {
        val fragment = HashtagsCounterFragment()
        switchFragment(fragment)
    }

    private fun setDrawer() {
        val yourPostsItem = PrimaryDrawerItem().withIdentifier(1)
            .withName(R.string.toolbar_title_your_posts)
        val yourHashtagsItem = PrimaryDrawerItem().withIdentifier(2)
            .withSelectable(false)
            .withName(R.string.toolbar_title_your_hashtags)
        val hashtagsCounterItem = PrimaryDrawerItem().withIdentifier(3)
            .withName(R.string.toolbar_title_hashtags_counter_fragment)

        val aboutItem = PrimaryDrawerItem().withIdentifier(4)
            .withSelectable(false)
            .withName(R.string.toolbar_title_about)
        val rateAppItem = PrimaryDrawerItem().withIdentifier(5)
            .withSelectable(false)
            .withName(R.string.toolbar_title_rate_the_app)

        val result = DrawerBuilder().withActivity(this).withToolbar(toolbar)
            .addDrawerItems(yourPostsItem, yourHashtagsItem, hashtagsCounterItem)
            .build()

        result.addStickyFooterItem(aboutItem)
        result.addStickyFooterItem(DividerDrawerItem())
        result.addStickyFooterItem(rateAppItem)

        result.onDrawerItemClickListener = Drawer.OnDrawerItemClickListener { _, _, drawerItem ->
            result.closeDrawer()
            when (drawerItem.identifier) {
                1L -> {
                    setYourPostsFragment()
                }
                2L -> {
                    Toast.makeText(this, "Open your hashtags", Toast.LENGTH_SHORT).show()
                }
                3L -> {
                    setHashtagsCounterFragment()
                }
                4L -> {
                    Toast.makeText(this, "About", Toast.LENGTH_SHORT).show()
                }
                5L -> {
                    Toast.makeText(this, "Rate", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }
    }

    override fun setToolbarTitle(toolbarTitle: String) {
        supportActionBar?.apply {
            title = toolbarTitle
        }
    }

    override fun openCreateEditItemActivity(state: MyEnums.CreateOrEditState) {
        val intent = Intent(this, CreateOrEditItemActivity::class.java)
        intent.putExtra(MyConstants.EXTRA_STATE, state)
        startActivity(intent)
    }
}
