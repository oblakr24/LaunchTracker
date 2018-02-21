package oblak.r.baseapp.base

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import oblak.r.baseapp.main.LaunchesViewModel


class BaseViewModelFactory(private val app: Application) : ViewModelProvider.Factory {

    private val launchesViewModel by lazy {
        LaunchesViewModel(app)
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass) {
            LaunchesViewModel::class.java -> {
                launchesViewModel as T
            }
            else -> TODO("No factory method provided for ${modelClass.canonicalName}")
        }
    }
}