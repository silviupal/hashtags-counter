package silviupal.hashtagscounter.base

import android.app.Activity
import android.content.Context
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

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        context?.let {
            if (context is BaseActivity) {
                this.listener = it as BaseActivity
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    @LayoutRes
    abstract fun getLayoutId(): Int
}