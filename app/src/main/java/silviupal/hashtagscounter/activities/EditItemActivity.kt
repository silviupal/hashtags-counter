package silviupal.hashtagscounter.activities

import android.os.Bundle
import android.view.MenuItem
import kotlinx.android.synthetic.main.toolbar_layout.*
import silviupal.hashtagscounter.MyConstants
import silviupal.hashtagscounter.R
import silviupal.hashtagscounter.base.BaseActivity
import silviupal.hashtagscounter.fragments.EditItemFragment
import silviupal.hashtagscounter.interfaces.IEditActivityFragmentListener

/**
 * Created by Silviu Pal on 4/7/2019.
 */
class EditItemActivity : BaseActivity(), IEditActivityFragmentListener {
    override fun setToolbarTitle(toolbarTitle: String) {
        supportActionBar?.apply {
            title = toolbarTitle
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_create_or_edit_item

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        intent?.extras?.let { intentObject ->
            switchFragment(EditItemFragment.newInstance(intentObject.getInt(MyConstants.EXTRA_ITEM_ID)))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                this.finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}