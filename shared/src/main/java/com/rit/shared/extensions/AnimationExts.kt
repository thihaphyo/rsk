package com.rit.shared.extensions

import android.view.View
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo

/**
 * Created by Chan Myae Aung on 3/30/21.
 */

fun View.shake() {
    YoYo.with(Techniques.Shake)
        .duration(500)
        .playOn(this)
}
