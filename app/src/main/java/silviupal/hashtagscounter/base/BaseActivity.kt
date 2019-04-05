package silviupal.hashtagscounter.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import silviupal.hashtagscounter.R
import silviupal.hashtagscounter.interfaces.IActivityFragmentListener
import timber.log.Timber

/**
 * Created by Silviu Pal on 05/04/2019.
 */
abstract class BaseActivity : AppCompatActivity(), IActivityFragmentListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("onCreate")
        setContentView(getLayoutId())
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    fun switchFragment(fragment: BaseFragment) {
        supportFragmentManager?.beginTransaction()
            ?.replace(R.id.container, fragment)
            ?.commit()
    }

    fun switchFragmentWithHistory(fragment: BaseFragment) {
        supportFragmentManager?.beginTransaction()
            ?.replace(R.id.container, fragment, fragment.javaClass.name)
            ?.addToBackStack(fragment.javaClass.name)
            ?.commit()
    }

    override fun onSupportNavigateUp(): Boolean {
        supportFragmentManager?.let { supportFragmentManager ->
            val currentFragment = supportFragmentManager.findFragmentById(R.id.container)
            if (currentFragment is BaseFragment) {
                supportFragmentManager.popBackStack()
                return true
            }
        }
        return true
    }
}
