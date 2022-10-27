package com.rit.shared.extensions

import java.text.DecimalFormat

/**
 * Created by Chan Myae Aung on 5/1/21.
 */
fun Double.asAmount(): String = DecimalFormat("#,###.##").format(this)

fun Double.asDollar(): String = this.toLong().toString()
