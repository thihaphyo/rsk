package com.rit.rsk

import androidx.lifecycle.MutableLiveData
import com.rit.shared.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PosCounterViewModel @Inject constructor() : BaseViewModel<PosCounterEvent>() {
    val loading = MutableLiveData<Boolean>()
}