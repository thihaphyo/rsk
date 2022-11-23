package com.rit.rsk.base

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment
import androidx.navigation.Navigation.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rit.rsk.BuildConfig
import com.rit.rsk.MainActivity
import com.rit.rsk.R
import com.rit.shared.base.BaseViewModel
import com.rit.shared.base.MVVMFragment
import com.rit.shared.helper.DialogManagerImpl
import com.rit.shared.manager.DialogManager
import com.rit.shared.utils.IntegrityStatus
import timber.log.Timber

/**
 * Created by khunzohn on 09/06/2021 at codigo
 */
abstract class BaseFragment<VM : BaseViewModel<E>, E> : MVVMFragment<VM, E> {

    constructor() : super()

    constructor(contentLayoutId: Int) : super(contentLayoutId)

    // TODO
    private val dialogManager: DialogManager by lazy { DialogManagerImpl() }

    open val disabledScreenshot: Boolean = false

//    private val dialog by lazy {
//        ForcedUpdateDialogFragment()
//    }

    private lateinit var firebaseDatabase: FirebaseDatabase

    override fun onStart() {
        super.onStart()
        if (disabledScreenshot) {
            requireActivity().window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
    }

    override fun onStop() {
        if (disabledScreenshot) {
            requireActivity().window.clearFlags(
                WindowManager.LayoutParams.FLAG_SECURE
            )
        }
        super.onStop()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.filterTouchesWhenObscured = true
        updateBottomNavigation()
        checkForceUpdate()
    }

    private fun updateBottomNavigation() {
//        val bottomNavView = (activity as MainActivity).binding.bottomNav
//        val currentDestinationId = findNavController().currentDestination?.id
//
//        bottomNavView.isVisible = currentDestinationId in arrayOf(
//            R.id.homeFragment,
//            R.id.parkHomeFragment,
//            R.id.walletFragment,
//            R.id.moreFragment,
//            R.id.ussHomeFragment
//        )
    }

    fun checkIntegrity(skip: Boolean, onChecked: (IntegrityStatus) -> Unit) {
        onChecked(IntegrityStatus.GOOD)
        /*if (skip) {
            onChecked(IntegrityStatus.GOOD)
        } else {

            var status = IntegrityStatus.GOOD
            when {
                IntegrityChecks.isRooted(requireContext()) -> {
                    status = IntegrityStatus.ROOTED
                }
                IntegrityChecks.isHooked() -> {
                    status = IntegrityStatus.HOOKED
                }
                IntegrityChecks.isDebuggable(requireContext()) -> {
                    status = IntegrityStatus.DEBUGGABLE
                }
                IntegrityChecks.isAttachedToDebugger() -> {
                    status = IntegrityStatus.DEBUGGER_ATTACHED
                }
            }
            onChecked(status)
        }*/
    }

    fun checkForceUpdate() {
//        firebaseDatabase = FirebaseDatabase.getInstance()
//        val dbReference = firebaseDatabase.getReference("updateInfo")
//        dbReference.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val updateInfo = snapshot.getValue(UpdateInfo::class.java)
//                updateInfo?.let {
//                    if (BuildConfig.VERSION_CODE < it.versionCode && it.forceUpdate == 1) {
//                        dialog.setDesc(it.versionName, it.downloadUrl)
//                        dialog.show(childFragmentManager, "Forced Update")
//                    }
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//            }
//        })
    }

    fun buildDialog(
        title: String = "",
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
            Timber.e(e, "Error hidingDialog")
        }
    }

    protected open fun showKeyboard() {
        val imm =
            requireContext().getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    protected open fun hideKeyboard() {
        val view = requireActivity().currentFocus
        if (view != null) {
            val imm = requireActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun onDestroy() {
        hideDialog()
        super.onDestroy()
    }
}
