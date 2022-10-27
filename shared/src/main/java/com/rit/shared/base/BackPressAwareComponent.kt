package com.rit.shared.base

/**
 * Created by khunzohn on 5/15/20 at codigo
 */
interface BackPressAwareComponent {

    /**
     * Triggers on user pressing hard ware back button.
     *
     * @return true if the back press is handled by this component and its parent
     * doesn't have to do a thing in this case.
     * false if its parent is to handle the back press.
     */
    fun onBackPressed(): Boolean
}
