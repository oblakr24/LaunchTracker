package oblak.r.baseapp.base

import android.arch.lifecycle.AndroidViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Represents a view that has a reference to a ViewModel
 */
interface BoundView<VM: AndroidViewModel> {

    var viewModel: VM

    val viewModelClass: Class<VM>

    val compositeDisposable: CompositeDisposable

    /**
     * Any observables should be added to the composite disposable here
     */
    fun requestData()

    /**
     * Should be called when the view's usual lifecycle has begun or has resumed
     */
    fun bind() {
        requestData()
    }

    /**
     * Should be called when the view's usual lifecycle has stopped or was paused
     */
    fun unbind() {
        compositeDisposable.clear()
    }

    /**
     * Helper extension allowing '+' to be used on CompositeDisposable additions
     */
    operator fun CompositeDisposable.plusAssign(disposable: Disposable) { add(disposable) }
}