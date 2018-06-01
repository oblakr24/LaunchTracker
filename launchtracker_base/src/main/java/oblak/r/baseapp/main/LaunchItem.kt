package oblak.r.baseapp.main

import android.view.View
import android.widget.ImageView
import com.mikepenz.fastadapter.items.AbstractItem
import android.widget.TextView
import com.mikepenz.fastadapter.FastAdapter
import oblak.r.baseapp.extensions.loadImage
import oblak.r.baseapp.models.Launch
import oblak.r.baseapp.utils.ModelConsts
import oblak.r.launchtracker.base.R
import org.jetbrains.anko.find
import java.util.*


class LaunchItem(private val launch: Launch) : AbstractItem<LaunchItem, LaunchItem.ViewHolder>() {

    override fun getType() = R.id.item_launch

    override fun getLayoutRes() = R.layout.item_launch

    override fun getViewHolder(view: View) = ViewHolder(view)

    class ViewHolder(view: View) : FastAdapter.ViewHolder<LaunchItem>(view) {

        private val launchImage by lazy { view.find<ImageView>(R.id.image_launch) }
        private val titleText by lazy { view.find<TextView>(R.id.text_launch_title) }
        private val descText by lazy { view.find<TextView>(R.id.text_launch_description) }
        private val dateText by lazy { view.find<TextView>(R.id.text_launch_date) }

        override fun bindView(item: LaunchItem, payloads: List<Any>) {

            val data = item.launch

            data.rocket?.imageURL?.let { launchImage.loadImage(it) }

            titleText.text = data.name
            descText.text = data.getDescription()

            // check if less than 30 days
            val netDate = ModelConsts.parseLaunchDate(data.net)
            dateText.text = if (Date().time + 24 * 3600 * 1000L < netDate.time) {
                ModelConsts.formatLaunchDate(netDate)
            } else {
                ModelConsts.formatToRemaining(netDate)
            }
        }

        override fun unbindView(item: LaunchItem?) { }
    }
}