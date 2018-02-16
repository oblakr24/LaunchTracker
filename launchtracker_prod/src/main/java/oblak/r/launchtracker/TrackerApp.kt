package oblak.r.launchtracker

import oblak.r.baseapp.BaseApp


/**
 * Created by rokoblak on 2/17/17.
 */
class TrackerApp : BaseApp() {

    private object AppConsts {
        const val baseApiUrl = "https://launchlibrary.net/1.3/"
    }

    override fun onCreate() {
        super.onCreate()


        trackerComponent = DaggerTrackerComponent.builder()
                .dataModule(DataModule(AppConsts.baseApiUrl, this))
                .build()
    }
}