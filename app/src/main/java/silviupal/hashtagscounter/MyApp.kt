package silviupal.hashtagscounter

import android.app.Application
import timber.log.Timber.DebugTree
import timber.log.Timber

/**
 * Created by Silviu Pal on 03/04/2019.
 */
class MyApp : Application() {
    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }

    companion object {
        lateinit var instance: MyApp
    }
}