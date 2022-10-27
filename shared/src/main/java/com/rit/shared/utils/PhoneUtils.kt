package com.rit.shared.utils

import io.michaelrocks.libphonenumber.android.PhoneNumberUtil

/**
 * Created by Chan Myae Aung on 4/8/21.
 */
class PhoneUtils(private val phoneNumberUtil: PhoneNumberUtil) {

    fun extractCountryCode(phone: String): Pair<String, String> {
        val copy = if (phone.startsWith("+")) phone else "+$phone"
        return try {
            val number = phoneNumberUtil.parse(copy, "")
            Pair("+${number.countryCode}", number.nationalNumber.toString())
        } catch (e: Exception) {
            Pair("", copy)
        }
    }
}
