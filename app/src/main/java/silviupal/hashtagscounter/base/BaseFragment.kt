package silviupal.hashtagscounter.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import silviupal.hashtagscounter.interfaces.IBaseActivityFragmentListener

/**
 * Created by Silviu Pal on 05/04/2019.
 */
abstract class BaseFragment : Fragment() {
    var listener: IBaseActivityFragmentListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(getLayoutId(), container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun setupToolbar()
}