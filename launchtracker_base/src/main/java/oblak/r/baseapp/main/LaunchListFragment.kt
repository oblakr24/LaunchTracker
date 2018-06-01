package oblak.r.baseapp.main

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.IAdapter
import com.mikepenz.fastadapter.IItem
import com.mikepenz.fastadapter.adapters.ItemAdapter
import oblak.r.baseapp.base.BaseFragment
import oblak.r.baseapp.models.Launch
import oblak.r.launchtracker.base.R
import org.jetbrains.anko.find
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

    private val itemAdapter by lazy { ItemAdapter<LaunchItem>() }

    private val adapter by lazy { FastAdapter.with<IItem<*, *>, IAdapter<out IItem<*, *>>>(itemAdapter) }

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
                .subscribe { resetAdapter(it) }

        // observe the readiness state
        compositeDisposable += viewModel.getLaunchesState()
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

        itemAdapter.set(items.map { LaunchItem(it) })

        if (items.isEmpty()) {
            showEmpty("No launches available")
        } else {
            showContent()
        }
    }
}