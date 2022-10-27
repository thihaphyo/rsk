package com.rit.shared.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Debug
import com.scottyab.rootbeer.BuildConfig
import com.scottyab.rootbeer.RootBeer
import timber.log.Timber

/**
 * Created by khunzohn on 7/10/20 at codigo
 */

enum class IntegrityStatus {
    ROOTED,
    DEBUGGABLE,
    DEBUGGER_ATTACHED,
    HOOKED,
    GOOD
}

object IntegrityChecks {

    fun isRooted(context: Context): Boolean {
        return if (BuildConfig.DEBUG) {
            false
        } else {
            RootBeer(context).isRooted
        }
    }

    fun isDebuggable(context: Context): Boolean {
        return 0 != context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
    }

    fun isAttachedToDebugger(): Boolean {
        return Debug.isDebuggerConnected() || Debug.waitingForDebugger()
    }

    fun isHooked(): Boolean {
        var hooked = false
        try {
            throw Exception("blah")
        } catch (e: Exception) {
            var zygoteInitCallCount = 0
            for (stackTraceElement in e.stackTrace) {
                if (stackTraceElement.className == "com.android.internal.os.ZygoteInit") {
                    zygoteInitCallCount++
                    if (zygoteInitCallCount == 2) {
                        hooked = true
                        Timber.e("Substrate is active on the device.")
                    }
                }
                if (stackTraceElement.className == "com.saurik.substrate.MS$2" &&
                    stackTraceElement.methodName == "invoked"
                ) {
                    hooked = true
                    Timber.e(
                        "A method on the stack trace has been hooked using Substrate."
                    )
                }
                if (stackTraceElement.className == "de.robv.android.xposed.XposedBridge" &&
                    stackTraceElement.methodName == "main"
                ) {
                    hooked = true
                    Timber.e("Xposed is active on the device.")
                }
                if (stackTraceElement.className == "de.robv.android.xposed.XposedBridge" &&
                    stackTraceElement.methodName == "handleHookedMethod"
                ) {
                    hooked = true
                    Timber.e(
                        "A method on the stack trace has been hooked using Xposed."
                    )
                }
            }
        }

        return hooked
    }
}
