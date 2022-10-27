package com.rit.shared.manager

import androidx.fragment.app.DialogFragment

/**
 * Created by Chan Myae Aung on 3/30/21.
 */
interface DialogManager {

    fun buildDialog(
        title: String,
        message: String,
        iconRes: Int? = null,
        isCancelable: Boolean = false,
        positiveAction: Pair<String, () -> Unit>,
        negativeAction: (Pair<String, () -> Unit>)? = null
    ): DialogFragment

    fun hideDialog()
}
