package oblak.r.launchtracker

import android.app.Application
import oblak.r.launchtracker.network.DaggerTrackerComponent
import oblak.r.launchtracker.network.DataModule
import oblak.r.launchtracker.network.TrackerComponent

/**
 * Created by rokoblak on 2/17/17.
 */


class TrackerApp: Application() {

    private object AppConsts {
        const val baseApiUrl = "https://launchlibrary.net/1.3/"
    }

    lateinit var trackerComponent: TrackerComponent

    override fun onCreate() {
        super.onCreate()

        trackerComponent = DaggerTrackerComponent.builder()
                .dataModule(DataModule(AppConsts.baseApiUrl, this))
                .build()
    }
}