package com.rit.data.helper

import com.rit.shared.extensions.offerCatching
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.distinctUntilChanged

object GlobalEvents {
    private val eventChannel = BroadcastChannel<GlobalEvent>(1)

    val eventFlow = eventChannel.asFlow().distinctUntilChanged()

    fun emit(event: GlobalEvent) {
        eventChannel.offerCatching(event)
    }

    sealed class GlobalEvent {
        object Error401 : GlobalEvent()
    }
}
