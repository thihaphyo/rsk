package com.rit.shared.type

class Option<T> private constructor(private val value: T?) {

    fun get(): T? = value

    companion object {
        fun <T> of(value: T?): Option<T> {
            return Option(value)
        }
    }
}
