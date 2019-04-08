package silviupal.hashtagscounter.activities

import android.os.Bundle
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
        intent?.extras?.let {
            val state: MyEnums.CreateOrEditState? = it.getSerializable(MyConstants.EXTRA_STATE) as MyEnums.CreateOrEditState
            state?.let {
                when (it) {
                    MyEnums.CreateOrEditState.CREATE_ITEM -> {
                        setCreateItemFragment()
                    }
                    MyEnums.CreateOrEditState.EDIT_ITEM -> {
                        setEditItemFragment()
                    }
                }
            }
        }
        // take extra enum from intent
        // show new item or edit item fragment
    }

    private fun setCreateItemFragment() {
        val fragment = CreateNewItemFragment()
        switchFragment(fragment)
    }

    private fun setEditItemFragment() {
        val fragment = EditItemFragment()
        switchFragment(fragment)
    }
}