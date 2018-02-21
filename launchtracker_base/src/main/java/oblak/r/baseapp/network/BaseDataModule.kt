package oblak.r.baseapp.network

import android.arch.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import oblak.r.baseapp.BaseApp
import oblak.r.baseapp.base.BaseViewModelFactory
import javax.inject.Singleton

@Module
abstract class BaseDataModule(private val application: BaseApp) {

    @Provides
    @Singleton
    internal fun providesApplication(): BaseApp {
        return application
    }

    @Provides
    @Singleton
    internal fun provideViewModelFactory(application: BaseApp): ViewModelProvider.Factory {
        return BaseViewModelFactory(application)
    }
}