package com.rit.shared.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import com.rit.shared.type.observeEvent

/**
 * Created by Chan Myae Aung on 3/16/21.
 */
abstract class MVVMDialogFragment<VM : BaseViewModel<E>, E> : BaseDialogFragment {

    constructor() : super()

    constructor(contentLayoutId: Int) : super(contentLayoutId)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set Transparent background to Dialog
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = false

        getOrCreateViewModel().streamEvents().observeEvent(viewLifecycleOwner, ::renderEvent)
    }

    abstract fun getOrCreateViewModel(): VM

    abstract fun renderEvent(event: E)
}
