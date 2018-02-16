package oblak.r.baseapp

import android.app.Application
import oblak.r.baseapp.network.BaseTrackerComponent

abstract class BaseApp: Application() {
    lateinit var trackerComponent: BaseTrackerComponent
}