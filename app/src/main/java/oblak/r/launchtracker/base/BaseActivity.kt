package oblak.r.launchtracker.base

import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_launches.*
import oblak.r.launchtracker.R
import oblak.r.launchtracker.TrackerApp
import oblak.r.launchtracker.main.LaunchesViewModel
import oblak.r.launchtracker.network.TrackerService
import javax.inject.Inject

/**
 * Created by rokoblak on 2/18/17.
 * A base activity class
 */
abstract class BaseActivity<VM: AndroidViewModel> : AppCompatActivity(), BoundView<VM> {

    abstract val layoutResourceId: Int

    override lateinit var viewModel: VM

    override val compositeDisposable by lazy { CompositeDisposable() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResourceId)
        setSupportActionBar(toolbar)
        initUI()
        viewModel = ViewModelProviders.of(this).get(viewModelClass)
    }

    open fun initUI() { }

    override fun onResume() {
        super.onResume()
        bind()
    }

    override fun onPause() {
        super.onPause()
        unbind()
    }
}