package silviupal.hashtagscounter

import android.app.Application
import silviupal.hashtagscounter.dagger.AppComponent
import silviupal.hashtagscounter.dagger.DaggerAppComponent
import timber.log.Timber
import timber.log.Timber.DebugTree

/**
 * Created by Silviu Pal on 03/04/2019.
 */
class MyApp : Application() {
    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().build()

        //val fabric = Fabric.Builder(this)
            //.kits(Crashlytics())

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
            //fabric.debuggable(true)
        }

        //Fabric.with(fabric.build())
    }

    companion object {
        lateinit var instance: MyApp
        lateinit var appComponent: AppComponent
    }
}