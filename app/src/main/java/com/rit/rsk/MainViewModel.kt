package com.rit.rsk

import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.distinctUntilChanged
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.rit.rsk.extensions.toSCountDown
import com.rit.rsk.model.CountDown
import com.rit.shared.base.BaseViewModel
import kotlinx.coroutines.launch
import org.threeten.bp.Duration
import kotlin.concurrent.schedule

@HiltViewModel
class MainViewModel @Inject constructor(): BaseViewModel<MainEvent>() {

    private val _screenStateLiveData = MutableLiveData<ScreenState>()
    val screenStateLiveData: LiveData<ScreenState>
        get() = _screenStateLiveData.distinctUntilChanged()

    private val _keyboardState = MutableLiveData<Boolean>()
    val keyboardStateLiveData: LiveData<Boolean>
        get() = _keyboardState

    private val _countDown = MutableLiveData<CountDown>(CountDown.Idle)
    val countDown: LiveData<CountDown>
        get() = _countDown

    private var countDownTimer: CountDownTimer? = null

    private val defaultCountDownDuration = Duration.ofSeconds(30)

    fun createAndStartTimer() {
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(
            defaultCountDownDuration.toMillis(),
            1000
        ) {
            override fun onTick(millisUntilFinished: Long) {
                _countDown.value =
                    CountDown.Active(timeLeft = millisUntilFinished.toSCountDown())
            }

            override fun onFinish() {
                _countDown.value = CountDown.Idle
            }
        }.start()
    }

    init {
        viewModelScope.launch {
            val uniqueID: String = UUID.randomUUID().toString()
           // preferenceStorage.saveDeviceId(uniqueID)
        }
        navigateSplashScreen()
        //getFcmToken()
    }

    fun navigateSplashScreen() {
        _screenStateLiveData.value = ScreenState.Splash
        val mainLooper = Looper.getMainLooper()
        Timer("SettingUp", false).schedule(1000) {
            Handler(mainLooper).post{
                _screenStateLiveData.value = ScreenState.Login
            }
        }
        //getCoverImages()
    }

    fun navigateHome() {
//        getProfile()
//        updateFcmToken()
        _screenStateLiveData.value = ScreenState.Home
    }

    fun keyboardChange(isOpen: Boolean = false) {
        _keyboardState.value = isOpen
    }
}