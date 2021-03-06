package oblak.r.baseapp.main

import android.support.v7.widget.RecyclerView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.IAdapter
import com.mikepenz.fastadapter.IItem
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter_extensions.items.ProgressItem
import com.mikepenz.fastadapter_extensions.scroll.EndlessRecyclerOnScrollListener
import oblak.r.baseapp.base.BaseFragment
import oblak.r.baseapp.models.Launch
import oblak.r.baseapp.utils.VerticalSpaceItemDecoration
import oblak.r.launchtracker.base.R
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.dip
import java.util.*

/**
 * Shows a list of launches in a recycler view
 */
class LaunchListFragment : BaseFragment<LaunchesViewModel>() {

    override val layoutResourceId: Int = R.layout.fragment_launches

    override val viewModelClass: Class<LaunchesViewModel> = LaunchesViewModel::class.java

    private val recycler by lazy { layout.find<RecyclerView>(R.id.recycler_launches) }

    private var timerTask: TimerTask? = null

    private val itemAdapter by lazy { ItemAdapter<LaunchItem>() }

    private val footerAdapter by lazy { ItemAdapter<IItem<*, *>>() }

    private val adapter by lazy {
        FastAdapter.with<IItem<*, *>, IAdapter<out IItem<*, *>>>(listOf(itemAdapter, footerAdapter))
                .withOnClickListener { _, _, item, position ->
                    (item as? LaunchItem)?.let { onItemClicked(it, position) }
                    false
                }
    }

    private var scrollListener: EndlessRecyclerOnScrollListener? = null

    private fun onItemClicked(item: LaunchItem, position: Int) {
        item.expanded = !item.expanded
        adapter.notifyAdapterItemChanged(position)
    }

    override fun initUI() {
        super.initUI()
        recycler.adapter = adapter

        recycler.addItemDecoration(VerticalSpaceItemDecoration(dip(3)))

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
        // observe the launches
        compositeDisposable += viewModel.getLaunches()
                .subscribe { setNewItems(it) }

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

    private fun addNextItems(items: List<Launch>) {
        itemAdapter.add(items.toAdapterItems())
    }

    private fun setNewItems(items: List<Launch>) {

        itemAdapter.set(items.toAdapterItems())

        scrollListener?.let { recycler.removeOnScrollListener(it) }
        scrollListener = object : EndlessRecyclerOnScrollListener(footerAdapter) {
            override fun onLoadMore(currentPage: Int) {
                footerAdapter.clear()
                footerAdapter.add(ProgressItem().withEnabled(false))

                compositeDisposable += viewModel.getNextLaunches()
                        .subscribe {
                            footerAdapter.clear()
                            addNextItems(it)
                        }
            }
        }

        recycler.scrollToPosition(0)

        recycler.addOnScrollListener(scrollListener)

        if (items.isEmpty()) {
            showEmpty("No launches available")
        } else {
            showContent()
        }
    }

    private fun List<Launch>.toAdapterItems() = map { LaunchItem(it) }
}