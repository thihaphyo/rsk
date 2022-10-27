package com.rit.shared.base

import androidx.fragment.app.DialogFragment
import com.rit.shared.AnalyticsHelper
import com.rit.shared.manager.DialogManager
import javax.inject.Inject
import timber.log.Timber

abstract class BaseDialogFragment : DialogFragment {

    constructor() : super()

    constructor(contentLayoutId: Int) : super(contentLayoutId)

    @Inject
    lateinit var analytics: AnalyticsHelper

    @Inject
    lateinit var dialogManager: DialogManager

    abstract val className: String

    abstract val screenName: String

    override fun onResume() {
        super.onResume()

        if (className.isNotBlank() && screenName.isNotBlank()) {
            analytics.logEvent(screenName = screenName, className = className)
        }
    }

    fun buildDialog(
        title: String,
        message: String,
        iconRes: Int? = null,
        isCancelable: Boolean = true,
        positiveAction: Pair<String, () -> Unit>,
        negativeAction: (Pair<String, () -> Unit>)? = null
    ): DialogFragment {
        return dialogManager.buildDialog(
            title, message, iconRes, isCancelable, positiveAction, negativeAction
        )
    }

    fun hideDialog() {
        try {
            dialogManager.hideDialog()
        } catch (e: Exception) {
            Timber.tag(className).e("Error hidingDialog ${e.stackTraceToString()}")
        }
    }

    override fun onDestroy() {
        hideDialog()
        super.onDestroy()
    }
}
