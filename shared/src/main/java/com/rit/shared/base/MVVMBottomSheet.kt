package com.rit.shared.base

import android.os.Bundle
import android.view.View
import com.rit.shared.type.observeEvent

/**
 * Created by Chan Myae Aung on 3/16/21.
 */
abstract class MVVMBottomSheet<VM : BaseViewModel<E>, E> : BaseBottomSheet() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getOrCreateViewModel().streamEvents().observeEvent(viewLifecycleOwner, ::renderEvent)
    }

    abstract fun getOrCreateViewModel(): VM

    abstract fun renderEvent(event: E)
}
