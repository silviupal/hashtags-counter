package silviupal.hashtagscounter

import android.app.Application
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
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
        val fabric = Fabric.Builder(this)
            .kits(Crashlytics())

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
            fabric.debuggable(true)
        }

        Fabric.with(fabric.build())
    }

    companion object {
        lateinit var instance: MyApp
    }
}