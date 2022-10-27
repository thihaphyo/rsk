package com.rit.rsk.model

sealed class CountDown {
    data class Active(val timeLeft: String) : CountDown()

    object Idle : CountDown()
}