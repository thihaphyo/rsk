package com.rit.rsk

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.rit.rsk.base.BaseFragment
import com.rit.rsk.databinding.FragmentOtpBinding
import com.rit.rsk.extensions.setCustomLineColor
import com.rit.rsk.model.CountDown
import com.rit.shared.extensions.hide
import com.rit.shared.extensions.show
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class OtpFragment: BaseFragment<OtpViewModel, OtpEvent>() {

    private var _binding: FragmentOtpBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by activityViewModels()
    private val viewModel: OtpViewModel by viewModels()

    @Inject
    lateinit var progressHelper: ProgressHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //(activity as MainActivity).binding.bottomNav.gone()
        _binding = FragmentOtpBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        setupActions()

    }

    private fun initUI() {

        progressHelper.setup(this, viewModel.loading)
        binding.otpView.requestFocus()
        binding.otpView.postDelayed(this::showKeyboard, 200)
        binding.otpView.setCustomLineColor()
        mainViewModel.createAndStartTimer()

        mainViewModel.countDown.observe(viewLifecycleOwner){
            when(it) {
                is CountDown.Active -> {
                    binding.tvCountDownLabel.isClickable = false
                    binding.tvCountDown.show()
                    binding.tvCountDown.text = getString(R.string.lbl_timer_format, it.timeLeft)
                    binding.tvCountDownLabel.text = getString(R.string.lbl_resend_in)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        binding.tvCountDownLabel.setTextAppearance(R.style.TextAppearance_RSK_OtpLabel)
                    } else {
                        binding.tvCountDownLabel.setTextAppearance(context, R.style.TextAppearance_RSK_OtpLabel)
                    }
                    binding.tvCountDownLabel.setOnClickListener(null)

                }

                is CountDown.Idle -> {
                    binding.tvCountDown.hide()
                    binding.tvCountDownLabel.isClickable = true
                    binding.tvCountDownLabel.text = getString(R.string.lbl_resend)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        binding.tvCountDownLabel.setTextAppearance(R.style.TextAppearance_RSK_OtpLabel_Resend)
                    } else {
                        binding.tvCountDownLabel.setTextAppearance(context, R.style.TextAppearance_RSK_OtpLabel_Resend)
                    }
                    binding.tvCountDownLabel.setOnClickListener {
                        binding.otpView.setText("")
                        binding.otpView.requestFocus()
                        binding.tvCountDownLabel.isClickable = false
                        viewModel.resendOtp()
                    }

                }
            }
        }


        binding.otpView.setOtpCompletionListener {
            Timber.d("OTP"+it)
        }
    }

    private fun setupActions() {

//        binding.otpView.addTextChangedListener {
//            if (!it.isNullOrBlank() && it.length <= 5) {
//                binding.otpView.setLineColor(
//                    ContextCompat.getColor(
//                        requireContext(),
//                        R.color.black
//                    )
//                )
//                binding.otpView.setTextColor(
//                    ContextCompat.getColor(
//                        requireContext(),
//                        R.color.black
//                    )
//                )
//            }
//        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.otpView.setOtpCompletionListener {
            hideKeyboard()
            viewModel.verifyOtp(it)
//            if (args.otpPayload.isUpdatePin) {
//                viewModel.verifyPINOtp(it)
//            } else {
//                viewModel.verifyOtp(it)
//            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun getOrCreateViewModel(): OtpViewModel = viewModel

    override fun renderEvent(event: OtpEvent) {
        when (event) {
            is OtpEvent.SuccessOtpResend -> {
               mainViewModel.createAndStartTimer()
            }
            is OtpEvent.SuccessVerifyOtp -> {
                mainViewModel.navigateHome()
            }
        }
    }
}