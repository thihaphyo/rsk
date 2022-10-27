package com.rit.shared.validator

/**
 * Created by Chan Myae Aung on 7/8/21.
 */
object PostalValidator {

    fun validate(postal: String, country: String): Boolean {
        return when (country) {
            "SG", "CN" -> postal.length == 6
            "US", "MY", "ID" -> postal.length == 5
            else -> true
        }
    }
}
