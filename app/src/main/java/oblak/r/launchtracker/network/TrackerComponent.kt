package oblak.r.launchtracker.network


import dagger.Component
import oblak.r.launchtracker.base.ViewModelWithService
import javax.inject.Singleton

/**
 * Created by rokoblak on 2/17/17.
 */


@Singleton
@Component(modules = arrayOf(DataModule::class))
interface TrackerComponent {

    fun inject(viewModel: ViewModelWithService)
}