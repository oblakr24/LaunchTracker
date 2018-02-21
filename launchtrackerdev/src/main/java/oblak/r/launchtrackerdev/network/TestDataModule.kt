package oblak.r.launchtrackerdev.network

import dagger.Module
import dagger.Provides
import oblak.r.baseapp.BaseApp
import oblak.r.baseapp.network.BaseDataModule
import oblak.r.baseapp.network.TrackerService
import javax.inject.Singleton


@Module
class TestDataModule(application: BaseApp) : BaseDataModule(application) {

    @Provides
    @Singleton
    internal fun provideTrackerService(): TrackerService {
        return MockTrackerService
    }
}