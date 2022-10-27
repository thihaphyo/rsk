package com.rit.shared.extensions

import android.app.Activity
import android.content.Context.INPUT_METHOD_SERVICE
import android.graphics.Rect
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.HIDE_IMPLICIT_ONLY
import android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT
import androidx.fragment.app.Fragment
import kotlin.math.roundToInt

fun Fragment.toggleKeyboard() {
    (requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).also { im ->
        im.toggleSoftInput(SHOW_IMPLICIT, HIDE_IMPLICIT_ONLY)
    }
}

fun Fragment.toggleKeyboard(
    view: View
) {
    val imm =
        requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInputFromWindow(view.windowToken, 0, 0)
    view.clearFocus()
}

fun Activity.getRootView(): View {
    return findViewById<View>(android.R.id.content)
}

fun Activity.isKeyboardOpen(): Boolean {
    val visibleBounds = Rect()
    this.getRootView().getWindowVisibleDisplayFrame(visibleBounds)
    val heightDiff = getRootView().height - visibleBounds.height()
    val marginOfError = this.dpToPx(50F).toDouble().roundToInt()
    return heightDiff > marginOfError
}
