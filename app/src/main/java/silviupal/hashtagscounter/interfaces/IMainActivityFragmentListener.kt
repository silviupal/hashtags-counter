package silviupal.hashtagscounter.interfaces

import silviupal.hashtagscounter.MyEnums

/**
 * Created by Silviu Pal on 05/04/2019.
 */
interface IMainActivityFragmentListener : IBaseActivityFragmentListener {
    fun openCreateEditItemActivity(state: MyEnums.CreateOrEditState)
}