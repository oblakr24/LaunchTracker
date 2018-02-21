package oblak.r.baseapp.base

import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.reactivex.disposables.CompositeDisposable
import oblak.r.baseapp.BaseApp
import oblak.r.launchtracker.base.R
import org.jetbrains.anko.find
import javax.inject.Inject

/**
 * Created by rokoblak on 2/18/17.
 * A base fragment class
 */
abstract class BaseFragment<VM: AndroidViewModel> : Fragment(), BoundView<VM>, StateControlView {

    abstract val layoutResourceId: Int

    protected lateinit var layout: View

    override lateinit var viewModel: VM

    @Inject
    override lateinit var viewModelFactory: ViewModelProvider.Factory

    override val compositeDisposable by lazy { CompositeDisposable() }

    override val contentView by lazy { layout.find<ViewGroup>(R.id.layout_content) }
    override val loadingView by lazy { layout.find<ViewGroup>(R.id.layout_loading) }
    override val emptyView by lazy { layout.find<ViewGroup>(R.id.layout_empty) }
    override val emptyMessage by lazy { emptyView.find<TextView>(R.id.text_empty) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(layoutResourceId, container, false)
        layout = view
        (activity?.application as BaseApp).trackerComponent.inject(this as BaseFragment<AndroidViewModel>)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(viewModelClass)
        initUI()
        showContent()
        return view
    }

    open fun initUI() {}

    override fun onResume() {
        super.onResume()
        bind()
    }

    override fun onPause() {
        super.onPause()
        unbind()
    }
}