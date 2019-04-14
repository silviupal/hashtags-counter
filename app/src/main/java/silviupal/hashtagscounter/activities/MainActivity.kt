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
import silviupal.hashtagscounter.fragments.YourHashtagsFragment
import silviupal.hashtagscounter.fragments.YourPostsFragment
import silviupal.hashtagscounter.interfaces.IMainActivityFragmentListener
import silviupal.hashtagscounter.utils.KeyboardUtils

class MainActivity : BaseActivity(), IMainActivityFragmentListener {
    private var selectedDrawerItem: Long = 0L
    override fun getLayoutId(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)

        selectedDrawerItem = 1L
        switchFragment(YourPostsFragment())
        setDrawer()
    }

    private fun setDrawer() {
        val yourPostsItem = PrimaryDrawerItem().withIdentifier(1)
            .withName(R.string.toolbar_title_your_posts)
        val yourHashtagsItem = PrimaryDrawerItem().withIdentifier(2)
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

        result.onDrawerItemClickListener = Drawer.OnDrawerItemClickListener { view, _, drawerItem ->

            result.closeDrawer()
            KeyboardUtils.hideKeyboard(view, this)
            when (drawerItem.identifier) {
                1L -> {
                    if (selectedDrawerItem != 1L) {
                        selectedDrawerItem = 1L
                        switchFragment(YourPostsFragment())
                    }
                }
                2L -> {
                    if (selectedDrawerItem != 2L) {
                        selectedDrawerItem = 2L
                        switchFragment(YourHashtagsFragment())
                    }
                }
                3L -> {
                    if (selectedDrawerItem != 3L) {
                        selectedDrawerItem = 3L
                        switchFragment(HashtagsCounterFragment())
                    }
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

    override fun openCreateEditItemActivity(state: MyEnums.CreateOrEditState, itemId: Int?) {
        val intent = Intent(this, CreateOrEditItemActivity::class.java)
        intent.putExtra(MyConstants.EXTRA_STATE, state)
        itemId?.let {
            intent.putExtra(MyConstants.EXTRA_ITEM_ID, it)
        }
        startActivity(intent)
    }
}
