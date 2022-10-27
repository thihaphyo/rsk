package com.rit.shared.base

import android.content.Context
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheet : BottomSheetDialogFragment() {

    abstract val className: String

    abstract val screenName: String

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }
}
