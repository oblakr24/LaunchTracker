package oblak.r.launchtrackerdev.network

import dagger.Module
import dagger.Provides
import oblak.r.baseapp.network.TrackerService
import javax.inject.Singleton

@Module
class TestDataModule {

    @Provides
    @Singleton
    internal fun provideTrackerService(): TrackerService {
        return MockTrackerService
    }
}