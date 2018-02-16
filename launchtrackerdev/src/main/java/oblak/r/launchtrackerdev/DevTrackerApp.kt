package oblak.r.launchtrackerdev

import oblak.r.baseapp.BaseApp
import oblak.r.launchtrackerdev.network.DaggerTestTrackerComponent
import oblak.r.launchtrackerdev.network.TestDataModule


class DevTrackerApp : BaseApp() {

    override fun onCreate() {
        super.onCreate()

        trackerComponent = DaggerTestTrackerComponent.builder()
                .testDataModule(TestDataModule())
                .build()
    }
}