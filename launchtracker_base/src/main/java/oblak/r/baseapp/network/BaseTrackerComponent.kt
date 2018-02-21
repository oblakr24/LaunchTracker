package oblak.r.baseapp.network

import android.arch.lifecycle.AndroidViewModel
import oblak.r.baseapp.base.BaseActivity
import oblak.r.baseapp.base.BaseFragment
import oblak.r.baseapp.base.ViewModelWithService


/**
 * Created by rokoblak on 2/17/17.
 */
interface BaseTrackerComponent {
    fun inject(viewModel: ViewModelWithService)
    fun inject(activity: BaseActivity<AndroidViewModel>)
    fun inject(activity: BaseFragment<AndroidViewModel>)
}