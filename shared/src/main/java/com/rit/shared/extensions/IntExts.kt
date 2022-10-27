package com.rit.shared.extensions

import android.content.res.Resources
import android.util.TypedValue

fun Int.toPix(resources: Resources): Int {
    val px =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), resources.displayMetrics
        )
    return px.toInt()
}
