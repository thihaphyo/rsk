package com.rit.shared.validator

import java.util.regex.Matcher
import java.util.regex.Pattern

object EmailValidator {
    private val EMAIL_REGEX = "^[\\w-+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$"
    private var pattern: Pattern
    private lateinit var matcher: Matcher
    init {
        pattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE)
    }

    fun validate(email: String): Boolean {
        matcher = pattern.matcher(email)
        return matcher.matches()
    }
}
