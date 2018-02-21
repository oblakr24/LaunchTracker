package oblak.r.baseapp.base

import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_launches.*
import oblak.r.baseapp.BaseApp
import javax.inject.Inject

/**
 * Created by rokoblak on 2/18/17.
 * A base activity class
 */
abstract class BaseActivity<VM: AndroidViewModel> : AppCompatActivity(), BoundView<VM> {

    abstract val layoutResourceId: Int

    override lateinit var viewModel: VM

    override val compositeDisposable by lazy { CompositeDisposable() }

    @Inject
    override lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResourceId)
        setSupportActionBar(toolbar)
        (application as BaseApp).trackerComponent.inject(this as BaseActivity<AndroidViewModel>)
        initUI()
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(viewModelClass)
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