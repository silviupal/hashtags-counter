package silviupal.hashtagscounter.activities

import android.os.Bundle
import android.view.MenuItem
import kotlinx.android.synthetic.main.toolbar_layout.*
import silviupal.hashtagscounter.MyConstants
import silviupal.hashtagscounter.MyEnums
import silviupal.hashtagscounter.R
import silviupal.hashtagscounter.base.BaseActivity
import silviupal.hashtagscounter.fragments.CreateNewItemFragment
import silviupal.hashtagscounter.fragments.EditItemFragment
import silviupal.hashtagscounter.interfaces.ICreateOrEditActivityFragmentListener

/**
 * Created by Silviu Pal on 4/7/2019.
 */
class CreateOrEditItemActivity : BaseActivity(), ICreateOrEditActivityFragmentListener {
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
            val state: MyEnums.CreateOrEditState? = intentObject.getSerializable(MyConstants.EXTRA_STATE) as MyEnums.CreateOrEditState
            state?.let {
                when (it) {
                    MyEnums.CreateOrEditState.CREATE_ITEM -> {
                        switchFragment(CreateNewItemFragment())
                    }
                    MyEnums.CreateOrEditState.EDIT_ITEM -> {
                        switchFragment(EditItemFragment.newInstance(intentObject.getInt(MyConstants.EXTRA_ITEM_ID)))
                    }
                }
            }
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