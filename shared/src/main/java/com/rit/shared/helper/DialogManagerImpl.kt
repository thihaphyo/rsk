package com.rit.shared.helper

import androidx.fragment.app.DialogFragment
import com.rit.shared.manager.DialogManager

class DialogManagerImpl : DialogManager {
    override fun buildDialog(
        title: String,
        message: String,
        iconRes: Int?,
        isCancelable: Boolean,
        positiveAction: Pair<String, () -> Unit>,
        negativeAction: Pair<String, () -> Unit>?
    ): DialogFragment {
        TODO("Not yet implemented")
    }

    override fun hideDialog() {}
}
