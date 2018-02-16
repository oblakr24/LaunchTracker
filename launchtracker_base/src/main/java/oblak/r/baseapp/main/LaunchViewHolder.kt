package oblak.r.baseapp.main

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import oblak.r.baseapp.base.BaseViewHolder
import oblak.r.baseapp.extensions.loadImage
import oblak.r.baseapp.models.Launch
import oblak.r.baseapp.utils.ModelConsts
import oblak.r.launchtracker.base.R
import org.jetbrains.anko.find

/**
 * Displays a single launch item
 */
class LaunchViewHolder(view: View) : BaseViewHolder<Launch>(view) {

    private val launchImage by lazy { view.find<ImageView>(R.id.image_launch) }
    private val titleText by lazy { view.find<TextView>(R.id.text_launch_title) }
    private val descText by lazy { view.find<TextView>(R.id.text_launch_description) }
    private val dateText by lazy { view.find<TextView>(R.id.text_launch_date) }

    override fun populateView(data: Launch, position: Int) {

        launchImage.loadImage(data.rocket.imageURL)

        titleText.text = data.name
        descText.text = data.getDescription()

        dateText.text = ModelConsts.format(data.net)
    }
}