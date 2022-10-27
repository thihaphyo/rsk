package com.rit.rsk.extensions

import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView

class ClickSpan(
    view: TextView,
    clickableText: String,
    private val color: Int,
    private val showUnderline: Boolean,
    private val action: () -> Unit
) : ClickableSpan() {

    override fun onClick(widget: View) {
        action()
    }

    init {
        val text = view.text
        val string = text.toString()
        val start = string.indexOf(clickableText)
        val end = start + clickableText.length
        if (start != -1) {
            if (text is Spannable) {
                text.setSpan(this, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            } else {
                val s = SpannableString.valueOf(text)
                s.setSpan(this, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                view.text = s
            }
            val m = view.movementMethod
            if (m !is LinkMovementMethod) {
                view.movementMethod = LinkMovementMethod.getInstance()
            }
        }
    }

    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.color = color
        ds.isUnderlineText = showUnderline
    }
}

fun TextView.clickify(
    clickableText: String,
    color: Int,
    showUnderline: Boolean = true,
    action: () -> Unit
) {
    ClickSpan(this, clickableText, color, showUnderline, action)
}
