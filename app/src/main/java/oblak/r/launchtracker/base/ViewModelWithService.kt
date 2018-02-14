package oblak.r.launchtracker.base

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import oblak.r.launchtracker.TrackerApp
import oblak.r.launchtracker.network.TrackerService
import javax.inject.Inject


/**
 * A view model with a service injected to it
 */
abstract class ViewModelWithService(app: Application) : AndroidViewModel(app) {

    @Inject
    lateinit var service: TrackerService

    init {
        getApplication<TrackerApp>().trackerComponent.inject(this)
    }
}