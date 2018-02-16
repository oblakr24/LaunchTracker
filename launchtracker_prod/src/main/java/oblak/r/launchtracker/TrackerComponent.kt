package oblak.r.launchtracker

import dagger.Component
import oblak.r.baseapp.network.BaseTrackerComponent
//import oblak.r.baseapp.network.BaseTrackerComponent
import javax.inject.Singleton
import android.arch.lifecycle.AndroidViewModel

@Singleton
@Component(modules = arrayOf(DataModule::class))
interface TrackerComponent : BaseTrackerComponent {


}