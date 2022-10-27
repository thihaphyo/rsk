package com.rit.shared.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.Fragment
import com.rit.shared.type.observeEvent

abstract class MVVMFragment<VM : BaseViewModel<E>, E> : Fragment {

    constructor() : super()

    constructor(contentLayoutId: Int) : super(contentLayoutId)

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    // Don't ever modify it in production code
    var skipInjection = false

    override fun onAttach(context: Context) {
        if (!skipInjection) {
        }
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.filterTouchesWhenObscured = true

        getOrCreateViewModel().streamEvents().observeEvent(viewLifecycleOwner, ::renderEvent)
    }

    abstract fun getOrCreateViewModel(): VM

    abstract fun renderEvent(event: E)
}
