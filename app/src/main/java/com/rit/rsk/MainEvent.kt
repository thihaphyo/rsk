package com.rit.rsk

sealed class MainEvent {
    data class Error(val title: String, val message: String) : MainEvent()
}
