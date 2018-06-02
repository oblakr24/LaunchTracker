package oblak.r.baseapp.extensions

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

object ImageConsts {
    val ASSET_PATH = "file:///android_asset/"
    val FLAGS_DIR = "flags/"
}

fun ViewGroup.inflateChild(resourceId: Int, attach: Boolean = false): View {
    return LayoutInflater.from(context).inflate(resourceId, this, attach)
}

fun ImageView.loadImage(url: String) {
    Glide.with(this)
            .load(url)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(this)
}

fun ImageView.loadCountryFlag(countryCode: String) {
    if (countryCode.isBlank()) {
        return
    }
    val flagPath = "${ImageConsts.ASSET_PATH}${ImageConsts.FLAGS_DIR}$countryCode.png"
    loadImage(flagPath)
}

/**
 * Fades in a view from a previous one
 */
fun View.crossFadeFrom(previous: View, fadeDuration: Long = 800L) {
    visibility = View.VISIBLE
    previous.visibility = View.VISIBLE

    val newView = this
    if (this == previous) return

    if (fadeDuration == 0L) {
        previous.visibility = View.GONE
    } else {

        val fadeOutDurationFactor = 0.7f

        animation = AlphaAnimation(0f, 1f).apply {
            interpolator = DecelerateInterpolator()
            duration = fadeDuration
        }

        previous.animation = AlphaAnimation(1f, 0f).apply {
            interpolator = AccelerateInterpolator()
            duration = (fadeDuration * fadeOutDurationFactor).toLong()
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(p0: Animation?) {}
                override fun onAnimationStart(p0: Animation?) {}
                override fun onAnimationEnd(p0: Animation?) {
                    newView.visibility = View.VISIBLE
                    previous.visibility = View.GONE
                }
            })
        }
    }
}

fun View.fadeIn(duration: Long = 1000L) {
    animateVisibility(View.VISIBLE, duration)
}

fun View.fadeOut(duration: Long = 1000L) {
    animateVisibility(View.GONE, duration)
}

fun View.animateVisibility(endVisibility: Int, animDuration: Long) {
    val isHiddenInitially = visibility == View.GONE
    visibility = View.VISIBLE
    animation = AlphaAnimation(if (isHiddenInitially) 0f else 1f, if (endVisibility == View.GONE) 0f else 1f).apply {
        duration = animDuration
        interpolator = if (isHiddenInitially) DecelerateInterpolator() else AccelerateInterpolator()
        setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {}
            override fun onAnimationStart(p0: Animation?) {}
            override fun onAnimationEnd(p0: Animation?) {
                visibility = endVisibility
            }
        })
    }
}

/**
 * Updates the visible items on a recycler view
 */
fun RecyclerView.updateVisibleItems() {
    if (adapter == null || layoutManager == null) {
        return
    }

    val layoutManager = layoutManager as? GridLayoutManager ?: layoutManager as? LinearLayoutManager
    if (layoutManager != null) {
        val startIdx = layoutManager.findFirstVisibleItemPosition()
        val endIdx = layoutManager.findLastVisibleItemPosition()

        if (startIdx != RecyclerView.NO_POSITION && endIdx != RecyclerView.NO_POSITION) {
            adapter.notifyItemRangeChanged(startIdx, endIdx + 1 - startIdx)
        }
    }
}