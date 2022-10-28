package com.rit.rsk

sealed class OtpEvent {
    object SuccessOtpResend: OtpEvent()
    object SuccessVerifyOtp: OtpEvent()
}