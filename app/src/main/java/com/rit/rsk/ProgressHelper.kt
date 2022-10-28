package com.rit.rsk

import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import com.rit.rsk.databinding.DialogProgressBinding
import javax.inject.Inject

class ProgressHelper @Inject constructor(): DialogFragment() {

    private var _binding: DialogProgressBinding? = null
    private val binding get() = _binding!!
    private var progressHelper: ProgressHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.Theme_RSK_Loading)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogProgressBinding.inflate(inflater)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            dialog?.window?.setBackgroundDrawable(
                ColorDrawable(requireContext().getColor(R.color.transparent))
            )
        }
        return binding.root
    }

    fun show(fm: FragmentManager, cancellable: Boolean = false) {
        if (progressHelper == null) {
            progressHelper = ProgressHelper()
        }
        progressHelper!!.isCancelable = cancellable
        progressHelper!!.show(fm, "loading-dialog")
    }

    fun hide() {
        progressHelper?.dismiss()
    }

    private fun setVisibility(fm: FragmentManager, isVisible: Boolean) {
        if (isVisible) {
            show(fm)
        } else {
            hide()
        }
    }

    fun setup(fragment: Fragment, loading: LiveData<Boolean>) {
        loading.observe(fragment.viewLifecycleOwner) {
            setVisibility(fragment.childFragmentManager, it)
        }
    }

    fun setup(activity: AppCompatActivity, loading: LiveData<Boolean>) {
        loading.observe(activity) {
            setVisibility(activity.supportFragmentManager, it)
        }
    }
}