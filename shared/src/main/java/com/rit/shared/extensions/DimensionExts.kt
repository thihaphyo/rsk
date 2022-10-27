package com.rit.shared.extensions

import android.content.Context
import android.util.DisplayMetrics

fun Context.dpToPx(dp: Float): Int {
    val metrics = resources.displayMetrics
    return (dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
}
