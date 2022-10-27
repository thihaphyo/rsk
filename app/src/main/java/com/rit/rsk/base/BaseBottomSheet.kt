package com.rit.rsk.base

import com.rit.shared.base.BaseViewModel
import com.rit.shared.base.MVVMBottomSheet


/**
 * Created by khunzohn on 09/06/2021 at codigo
 */
abstract class BaseBottomSheet<VM : BaseViewModel<E>, E> : MVVMBottomSheet<VM, E>() {

    override fun onDestroyView() {
        super.onDestroyView()
    }
}
