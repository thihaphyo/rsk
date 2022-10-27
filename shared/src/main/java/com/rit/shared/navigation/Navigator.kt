package com.rit.shared.navigation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

/**
 * Created by khunzohn on 2020-03-11 at codigo
 */
interface Navigator {

    /**
     * The preferred way to push fragment as it's easier to test with Fragment's Class instead of
     * Fragment's instance
     */
    fun <T : Fragment> pushFragment(
        clazz: Class<T>,
        sharedElementList: List<Pair<View, String>>? = null,
        extra: Bundle? = null,
        horizontalTransition: Boolean = true
    ) {
        pushFragment(
            clazz.newInstance().apply { arguments = extra },
            sharedElementList,
            horizontalTransition
        )
    }

    fun pushFragment(
        fragment: Fragment,
        sharedElementList: List<Pair<View, String>>? = null,
        horizontalTransition: Boolean = true
    )

    fun popFragment(
        sharedElementList: List<Pair<View, String>>? = null,
        horizontalTransition: Boolean = true
    )

    /**
     * The preferred way to replace fragment as it's easier to test with Fragment's Class instead of
     * Fragment's instance
     */
    fun <T : Fragment> replaceFragment(
        clazz: Class<T>,
        sharedElementList: List<Pair<View, String>>? = null,
        extra: Bundle? = null,
        horizontalTransition: Boolean = true
    ) {
        replaceFragment(
            clazz.newInstance().apply { arguments = extra },
            sharedElementList,
            horizontalTransition
        )
    }

    fun replaceFragment(
        fragment: Fragment,
        sharedElementList: List<Pair<View, String>>? = null,
        horizontalTransition: Boolean = true
    )

    /**
     * go back to root fragment of current tab
     */
    fun gotoRoot()

    fun goToHome()

    companion object {
        val EMPTY = object : Navigator {
            override fun pushFragment(
                fragment: Fragment,
                sharedElementList: List<Pair<View, String>>?,
                horizontalTransition: Boolean
            ) {
                TODO("Not yet implemented")
            }

            override fun popFragment(
                sharedElementList: List<Pair<View, String>>?,
                horizontalTransition: Boolean
            ) {
                TODO("Not yet implemented")
            }

            override fun replaceFragment(
                fragment: Fragment,
                sharedElementList: List<Pair<View, String>>?,
                horizontalTransition: Boolean
            ) {
                TODO("Not yet implemented")
            }

            override fun gotoRoot() {
                TODO("Not yet implemented")
            }

            override fun goToHome() {
                TODO("Not yet implemented")
            }
        }
    }
}
