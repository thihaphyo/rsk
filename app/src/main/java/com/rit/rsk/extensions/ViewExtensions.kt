package com.rit.rsk.extensions

import android.animation.Animator
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.mukesh.OtpView
import com.rit.rsk.R
import java.lang.NumberFormatException
import java.math.RoundingMode
import java.text.DecimalFormat
import java.time.Instant
import java.util.Date
import java.util.concurrent.TimeUnit
import timber.log.Timber

fun OtpView.setCustomLineColor() {
    val states =
        arrayOf(
            intArrayOf(android.R.attr.state_selected),
            intArrayOf(-android.R.attr.state_selected)
        )
    val colors = intArrayOf(
        Color.parseColor("#886EF7"), // unfilled color
        Color.parseColor("#E8E8E8") // filled color
    )
    val colorsList = ColorStateList(states, colors)
    val textColorList = ColorStateList(states, intArrayOf(
        Color.parseColor("#000000"),
        Color.parseColor("#000000")
    ))
    this.setLineColor(colorsList)
    this.setTextColor(textColorList)
}

//fun ImageView.loadFromUrl(url: String?) =
//    url?.let {
//        Glide.with(this.context)
//            .load(it)
//            .placeholder(R.drawable.white_placeholder)
//            .into(this)
//    }

//fun ImageView.loadFromUrl(bitmap: Bitmap?) =
//    bitmap?.let {
//        Glide.with(this.context)
//            .load(it)
//            .placeholder(R.color.pale_grey)
//            .into(this)
//    }

fun Long.toSCountDown() = String.format(
    "%02d",
    TimeUnit.MILLISECONDS.toSeconds(this) - TimeUnit.MINUTES.toSeconds(
        TimeUnit.MILLISECONDS.toMinutes(this)
    )
)

fun View.slideUpDown(yValue: Float, done: () -> Unit?) {
    this.animate()
        .translationY(yValue)
        .setDuration(200)
        .setInterpolator(AccelerateDecelerateInterpolator())
        .setListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                done()
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }
        }).start()
}

fun View.rotate(duration: Int = 200, expand: Boolean = true) {
    if (expand) {
        animate().setDuration(duration.toLong()).rotation(180f)
    } else {
        animate().setDuration(duration.toLong()).rotation(0f)
    }
}

fun View.expand() {
    measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    val actualHeight = measuredHeight
    layoutParams.height = 0
    visibility = View.VISIBLE
    val animation: Animation = object : Animation() {
        override fun applyTransformation(
            interpolatedTime: Float,
            t: Transformation?
        ) {
            layoutParams.height =
                if (interpolatedTime == 1f)
                    ViewGroup.LayoutParams.WRAP_CONTENT
                else
                    (actualHeight * interpolatedTime).toInt()
            requestLayout()
        }
    }
    animation.duration = (actualHeight / context.resources.displayMetrics.density).toLong()
    startAnimation(animation)
}

fun View.collapse() {
    val actualHeight = measuredHeight
    val animation: Animation = object : Animation() {
        override fun applyTransformation(
            interpolatedTime: Float,
            t: Transformation?
        ) {
            if (interpolatedTime == 1f) {
                visibility = View.GONE
            } else {
                layoutParams.height =
                    actualHeight - (actualHeight * interpolatedTime).toInt()
                requestLayout()
            }
        }
    }
    animation.duration = (actualHeight / context.resources.displayMetrics.density).toLong()
    startAnimation(animation)
}

//fun View.openMapView(
//    userLatitude: String,
//    userLongitude: String,
//    destinationLatitude: String,
//    destinationLongitude: String
//) {
//    val googleMapUrl = context.getString(
//        R.string.google_map_url,
//        userLatitude,
//        userLongitude,
//        destinationLatitude,
//        destinationLongitude
//    )
//    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(googleMapUrl))
//    intent.setPackage("com.google.android.apps.maps")
//    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//
//    context.startActivity(intent)
//}

fun String.getQrCodeBitmap(qrCodeSize: Int = 200): Bitmap? {
    if (this.isBlank()) return null
    val hints = hashMapOf<EncodeHintType, Int>().also {
        it[EncodeHintType.MARGIN] = 1
    } // Make the QR code buffer border narrower
    val bits = QRCodeWriter().encode(
        this,
        BarcodeFormat.QR_CODE,
        qrCodeSize,
        qrCodeSize,
        hints
    )
    return Bitmap.createBitmap(qrCodeSize, qrCodeSize, Bitmap.Config.RGB_565).also {
        for (x in 0 until qrCodeSize) {
            for (y in 0 until qrCodeSize) {
                it.setPixel(x, y, if (bits[x, y]) Color.BLACK else Color.WHITE)
            }
        }
    }
}

fun String.getBarCodeBitmap(): Bitmap? {
    if (this.isBlank()) return null
    return try {
        val barcodeEncoder = BarcodeEncoder()
        val bitmap = barcodeEncoder.encodeBitmap(
            this,
            BarcodeFormat.CODABAR,
            273, 51
        )
        bitmap
    } catch (e: Exception) {
        null
    }
}

//fun String.durationFormat(): String {
//    return if (this.isNotBlank()) {
//        this.split("-").let {
//            val startTime = it.first().formatHour().replace(" ", "")
//            val endTime = it.last().formatHour().replace(" ", "")
//            "$startTime - $endTime"
//        }
//    } else {
//        this
//    }
//}

fun Int.getNewXCoordinate(screenWidth: Int): Int {
    val originalWidth = 1125 // From server image Width
    return (this) * screenWidth / originalWidth
}

fun Int.getNewYCoordinate(screenHeight: Int): Int {
    val originalHeight = 2436 // From server image Height
    return this * screenHeight / originalHeight
}

fun Int.getNewMarkerWidth(screenWidth: Int): Int {
    val originalWidth = 1125 // From server image Width
    return (this) * screenWidth / originalWidth
}

fun Int.getNewMarkerHeight(screenHeight: Int): Int {
    val originalHeight = 2436 // From server image Height
    return this * screenHeight / originalHeight
}

fun Int.getNewUssXCoordinate(screenWidth: Int): Int {
    val originalWidth = 1125 // From server image Width
    return (this) * screenWidth / originalWidth
}

fun Int.getNewUssYCoordinate(screenHeight: Int): Int {
    val originalHeight = 2436 // From server image Height
    return this * screenHeight / originalHeight
}

fun Int.getNewUssMarkerWidth(screenWidth: Int): Int {
    val originalWidth = 1125 // From server image Width
    return (this) * screenWidth / originalWidth
}

fun Int.getNewUssMarkerHeight(screenHeight: Int): Int {
    val originalHeight = 2436 // From server image Height
    return this * screenHeight / originalHeight
}

fun Fragment.safetyNavigate(directions: NavDirections) {
    try {
        findNavController().navigate(directions)
    } catch (e: IllegalArgumentException) {
        Timber.i("Multiple navigation attempts handled.")
    }
}

//fun String.distanceFormat(context: Context): String {
//
//    return try {
//        val distance = this.toInt()
//        if (distance >= 1000) {
//            val kmDistance = distance / 1000.00f
//            val df = DecimalFormat("#.##")
//            df.roundingMode = RoundingMode.CEILING
//            df.format(kmDistance)
//            "${df.format(kmDistance)}km away"
//        } else {
//            context.getString(R.string.lbl_away_distant, distance)
//        }
//    } catch (e: NumberFormatException) {
//        ""
//    }
//}

//@RequiresApi(Build.VERSION_CODES.O)
//fun String.toNotiDateFormat(): String {
//
//    val ins = Instant.parse(this)
//    val date = Date.from(ins)
//    var dateStr = date.toString("MMMM d, yyyy")
//    val todayStr = Date().toString("MMMM d, yyyy")
//    if (dateStr == todayStr) dateStr = "Today"
//    val time = date.toString("hh:mm a")
//    return "$dateStr at $time"
//}

//fun ImageView.loadFromMapUrl(url: String?) {
//    url?.let {
//        Glide.with(this.context)
//            .load(it)
//            .placeholder(R.drawable.ic_amenities_marker_blank)
//            .into(this)
//    }
//    this.scaleType = ImageView.ScaleType.CENTER
//    this.adjustViewBounds = true
//}
