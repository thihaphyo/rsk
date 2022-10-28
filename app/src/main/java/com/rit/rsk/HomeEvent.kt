package com.rit.rsk

sealed class HomeEvent {
    data class error(val title: String,val msg:String): HomeEvent()
}