package com.rit.shared.base

import androidx.fragment.app.Fragment

/**
 * Fragment's onResume is tied to its parent activity's thus it's onResume won't be triggered
 * by just simply popping the fragment on top of it. This component fills this gap.
 * For the stack A -> B. When B is popped, A.didResume() will be called.
 */
interface BackStackAwareComponent<T : Fragment> {

    val clazz: Class<T>

    /**
     * this function mimics onResume() of Activity
     */
    fun didResume()
}
