package com.rit.shared.validator

import androidx.core.text.isDigitsOnly
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil

/**
 * Created by Chan Myae Aung on 3/30/21.
 */
class PhoneNumberValidator(val phoneNumberUtil: PhoneNumberUtil) {

    fun isValid(countryCode: String, phoneNumber: String): Boolean {
        val number = phoneNumber.replace(" ", "")
        val code = if (countryCode.startsWith("+")) countryCode else "+$countryCode"
        return try {

            if (code == "+65") { // SG
                val trimmed = phoneNumber.replace(" ", "").trim()
                trimmed.isDigitsOnly() && trimmed.length == 8
            } else {
                val phone = phoneNumberUtil.parse("$code$number", "")
                phoneNumberUtil.isValidNumber(phone)
            }
        } catch (e: Exception) {
            false
        }
    }
}
