package silviupal.hashtagscounter.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import silviupal.hashtagscounter.interfaces.IActivityFragmentListener

/**
 * Created by Silviu Pal on 05/04/2019.
 */
abstract class BaseFragment : Fragment() {
    var listener: IActivityFragmentListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(getLayoutId(), container, false)

    @LayoutRes
    abstract fun getLayoutId(): Int
}