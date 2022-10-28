package com.rit.rsk

import androidx.lifecycle.MutableLiveData
import com.rit.shared.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel<HomeEvent>() {
    val loading = MutableLiveData<Boolean>()

}