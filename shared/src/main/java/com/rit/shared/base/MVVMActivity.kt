package com.rit.shared.base

import android.os.Bundle
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import com.rit.shared.type.observeEvent

/**
 * Created by khunzohn on 2020-02-27 at Codigo
 */
abstract class MVVMActivity<VM : BaseViewModel<E>, E> : AppCompatActivity {

    constructor() : super()

    constructor(contentLayoutId: Int) : super(contentLayoutId)

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    // Don't ever modify it in production code
    var skipInjection = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getOrCreateViewModel().streamEvents().observeEvent(this, ::renderEvent)
    }

    abstract fun getOrCreateViewModel(): VM

    abstract fun renderEvent(event: E)
}
