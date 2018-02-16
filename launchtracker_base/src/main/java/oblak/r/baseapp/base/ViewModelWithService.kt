package oblak.r.baseapp.base

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import oblak.r.baseapp.BaseApp
import oblak.r.baseapp.network.TrackerService
import javax.inject.Inject


/**
 * A view model with a service injected to it
 */
abstract class ViewModelWithService(app: Application) : AndroidViewModel(app) {

    @Inject
    lateinit var service: TrackerService

    init {
        getApplication<BaseApp>().trackerComponent.inject(this)
    }
}