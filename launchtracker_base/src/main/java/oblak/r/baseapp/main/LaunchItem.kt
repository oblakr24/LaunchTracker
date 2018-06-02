package oblak.r.baseapp.main

import android.view.View
import android.widget.ImageView
import com.mikepenz.fastadapter.items.AbstractItem
import android.widget.TextView
import com.mikepenz.fastadapter.FastAdapter
import oblak.r.baseapp.extensions.loadCountryFlag
import oblak.r.baseapp.extensions.loadImage
import oblak.r.baseapp.models.Launch
import oblak.r.baseapp.utils.ModelConsts
import oblak.r.baseapp.utils.Utils
import oblak.r.launchtracker.base.R
import org.jetbrains.anko.dip
import org.jetbrains.anko.find
import java.util.*


class LaunchItem(private val launch: Launch, var expanded: Boolean = false) : AbstractItem<LaunchItem, LaunchItem.ViewHolder>() {

    override fun getType() = R.id.item_launch

    override fun getLayoutRes() = R.layout.item_launch

    override fun getViewHolder(view: View) = ViewHolder(view)

    class ViewHolder(view: View) : FastAdapter.ViewHolder<LaunchItem>(view) {

        private val launchImage by lazy { view.find<ImageView>(R.id.image_launch) }
        private val flagImage by lazy { view.find<ImageView>(R.id.flag_icon) }
        private val titleText by lazy { view.find<TextView>(R.id.text_launch_title) }
        private val descText by lazy { view.find<TextView>(R.id.text_launch_description) }
        private val dateText by lazy { view.find<TextView>(R.id.text_launch_date) }

        override fun bindView(item: LaunchItem, payloads: List<Any>) {

            val context = itemView.context

            if (item.expanded) {
                itemView.layoutParams.height = context.dip(170)
                descText.maxLines = 8
            } else {
                itemView.layoutParams.height = context.dip(90)
                descText.maxLines = 2
            }

            val data = item.launch

            data.rocket?.imageURL?.let { launchImage.loadImage(it) }

            flagImage.visibility = View.INVISIBLE
            data.lsp?.countryCode?.let {
                Utils.iso3ToIso3(it)?.let {
                    flagImage.loadCountryFlag(it.toLowerCase())
                    flagImage.visibility = View.VISIBLE
                }
            }

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