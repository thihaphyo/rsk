package com.rit.rsk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.airbnb.epoxy.Carousel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.rit.data.helper.GlobalEvents
import com.rit.rsk.databinding.ActivityMainBinding
import com.rit.shared.base.MVVMActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainActivity : MVVMActivity<MainViewModel, MainEvent>() {

    lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    private fun findNavController(): NavController {
        val navHostFrag = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        return navHostFrag.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpEpoxyConfigs()

        viewModel.screenStateLiveData.observe(this){ screenState ->
            if (screenState == null) return@observe
            val navGraphId = when (screenState) {
                ScreenState.Splash -> {
                    R.navigation.nav_splash_screen
                }
                ScreenState.Login -> {
                    R.navigation.nav_login
                }
                ScreenState.Home -> {
                    R.navigation.nav_home
                }
            }
            val graph = findNavController().navInflater.inflate(navGraphId)
            findNavController().graph = graph

            binding.navView.setupWithNavController(findNavController())
        }

        GlobalEvents.eventFlow
            .filter { it is GlobalEvents.GlobalEvent.Error401 }
            .map { it as GlobalEvents.GlobalEvent.Error401 }
            .onEach {
                MaterialAlertDialogBuilder(this)
                    .setTitle("Error")
                    .setMessage(getString(R.string.error_session_expired))
                    .setPositiveButton(getString(R.string.action_ok)) { _, _ ->
                      //  viewModel.logout()
                    }
                    .setCancelable(false)
                    .show()
            }.launchIn(lifecycleScope)

        findNavController().addOnDestinationChangedListener { _, dest, _ ->
//            if (dest.id == R.id.parkHomeFragment || dest.id == R.id.ussHomeFragment) {
//                binding.bottomNav.menu.getItem(0).isChecked = true
//            }
        }
    }

    private fun setUpEpoxyConfigs() {
        Carousel.setDefaultGlobalSnapHelperFactory(null)
    }

    fun showAlert(message: String, iconRes: Int) {
//        with(binding) {
//            lyAlertContainer.slideUpDown(0f) {}
//            Handler(Looper.getMainLooper()).postDelayed(
//                {
//                    lyAlertContainer.slideUpDown(-400f) {}
//                },
//                4000
//            )
//            if (iconRes != 0) {
//                ivAlert.show()
//                ivAlert.setImageResource(iconRes)
//            } else {
//                ivAlert.gone()
//            }
//            tvAlertMessage.text = message
//        }
    }

    override fun getOrCreateViewModel(): MainViewModel = viewModel

    override fun renderEvent(event: MainEvent) {
        when (event) {
            is MainEvent.Error -> {
                showAlert(event.message, 0)
            }
        }
    }
}