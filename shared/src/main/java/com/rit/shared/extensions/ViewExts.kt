package com.rit.shared.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import kotlinx.coroutines.channels.SendChannel

fun View.show() {
    isVisible = true
}

fun View.hide() {
    isInvisible = true
}

fun View.gone() {
    isGone = true
}

fun RecyclerView.clearItemDecoration() {
    for (i in 0 until itemDecorationCount) {
        removeItemDecorationAt(i)
    }
}

fun View.setMargins(
    leftMarginDp: Int? = null,
    topMarginDp: Int? = null,
    rightMarginDp: Int? = null,
    bottomMarginDp: Int? = null
) {
    if (layoutParams is ViewGroup.MarginLayoutParams) {
        val params = layoutParams as ViewGroup.MarginLayoutParams
        leftMarginDp?.run { params.leftMargin = context.dpToPx(this.toFloat()) }
        topMarginDp?.run { params.topMargin = context.dpToPx(this.toFloat()) }
        rightMarginDp?.run { params.rightMargin = context.dpToPx(this.toFloat()) }
        bottomMarginDp?.run { params.bottomMargin = context.dpToPx(this.toFloat()) }
        requestLayout()
    }
}

fun View.fadeIn(duration: Long = 300) {
    alpha = 0f
    visibility = View.VISIBLE
    animate()
        .alpha(1f)
        .setDuration(duration)
        .setInterpolator(AccelerateDecelerateInterpolator())
        .setListener(null)
}

fun View.fadeOut(duration: Long = 300) {
    animate()
        .alpha(0f)
        .setDuration(duration)
        .setInterpolator(AccelerateDecelerateInterpolator())
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                visibility = View.GONE
            }
        })
}
/**
 * Setup Page Snapping Effect for View Pager
 *
 * @param marginDp Margin in [dp] for distance between each item
 */
fun ViewPager2.setupPreviewPageTransformer(marginDp: Float = 10f, offsetDp: Float = 30f) {
    clipToPadding = false
    clipChildren = false
    offscreenPageLimit = 2

    // Setup Offset
    val offsetPx = context.dpToPx(offsetDp)
    setPadding(offsetPx, 0, offsetPx, 0)

    // increase this offset to increase distance between 2 items
    val pageMarginPx = context.dpToPx(marginDp)
    val marginTransformer = MarginPageTransformer(pageMarginPx)
    setPageTransformer(marginTransformer)
}

/**
 * Setup a NestedScrollView with an Actionbar layout
 *
 * Setup the Action Bar Layout to have elevation whenever the view scrolls from its original position
 *
 * @param view Actionbar [View]
 * @param elevation Elevation height
 */
fun NestedScrollView.setupWithActionBarView(view: View, elevation: Float = 15f) {
    setOnScrollChangeListener { _, _, scrollY, _, _ ->
        if (scrollY > 0) {
            view.elevation = context.dpToPx(elevation).toFloat()
        } else {
            view.elevation = 0f
        }
        true
    }
}

fun <E> SendChannel<E>.offerCatching(element: E): Boolean {
    return runCatching { trySend(element).isSuccess }.getOrDefault(false)
}
