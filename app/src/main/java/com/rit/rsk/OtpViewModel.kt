package com.rit.rsk

import androidx.lifecycle.MutableLiveData
import com.rit.shared.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OtpViewModel @Inject constructor(): BaseViewModel<OtpEvent>() {

    val loading = MutableLiveData<Boolean>()

    fun verifyOtp(otp: String){
//        loading.value = true
        emitEvent(OtpEvent.SuccessVerifyOtp)
    }

    fun resendOtp() {
        emitEvent(OtpEvent.SuccessOtpResend)
    }
}