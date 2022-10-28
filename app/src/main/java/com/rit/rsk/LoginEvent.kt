package com.rit.rsk

sealed class LoginEvent {
    data class Error(val title: String, val message: String) : LoginEvent()
}