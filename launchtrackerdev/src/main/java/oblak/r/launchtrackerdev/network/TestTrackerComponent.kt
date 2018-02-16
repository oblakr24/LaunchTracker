package oblak.r.launchtrackerdev.network

import dagger.Component
import oblak.r.baseapp.network.BaseTrackerComponent
import javax.inject.Singleton


@Singleton
@Component(modules = arrayOf(TestDataModule::class))
interface TestTrackerComponent : BaseTrackerComponent {


}