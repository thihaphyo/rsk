package com.rit.rsk

import com.rit.shared.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PaymentSummaryViewModel @Inject constructor() : BaseViewModel<PaymentSummaryEvent>() {
}