package oblak.r.launchtracker.main

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import oblak.r.launchtracker.R
import oblak.r.launchtracker.base.BaseFragment
import oblak.r.launchtracker.base.BasicAdapter
import oblak.r.launchtracker.extensions.inflateChild
import oblak.r.launchtracker.models.Launch
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.toast
import java.util.*

/**
 * Shows a list of launches in a recycler view
 */
class LaunchListFragment : BaseFragment<LaunchesViewModel>() {

    override val layoutResourceId: Int = R.layout.fragment_launches

    override val viewModelClass: Class<LaunchesViewModel> = LaunchesViewModel::class.java

    private val recycler by lazy { layout.find<RecyclerView>(R.id.recycler_launches).apply {
        layoutManager = LinearLayoutManager(this@LaunchListFragment.context, LinearLayoutManager.VERTICAL, false)
    } }

    private var timerTask: TimerTask? = null

    private val adapter by lazy {
        BasicAdapter( { parent, _ ->
            LaunchViewHolder(parent.inflateChild(R.layout.item_launch))
        }).apply {
            onItemClicked = { data, _, _ ->
                toast("Details coming soon")
            }
        }
    }

    override fun initUI() {
        super.initUI()
        recycler.adapter = adapter

        timerTask?.cancel()
        timerTask = object: TimerTask() {
            override fun run() {
                activity?.runOnUiThread {
                    adapter.notifyDataSetChanged()
                }
            }
        }

        Timer().scheduleAtFixedRate(timerTask, 1000, 1000)
    }

    override fun requestData() {
        // observer the launches
        compositeDisposable += viewModel.getLaunches()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { resetAdapter(it) }

        // observe the readiness state
        compositeDisposable += viewModel.getLaunchesState()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { ready ->
                    if (!ready) {
                        showLoading()
                    }
                }
    }

    override fun onPause() {
        super.onPause()
        timerTask?.cancel()
    }

    private fun resetAdapter(items: List<Launch>) {
        adapter.setNewItems(items)
        if (items.isEmpty()) {
            showEmpty("No launches available")
        } else {
            showContent()
        }
    }
}