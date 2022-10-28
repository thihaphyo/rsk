package com.rit.rsk

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.rit.rsk.base.BaseFragment
import com.rit.rsk.databinding.FragmentLoginBinding
import com.rit.shared.extensions.gone
import timber.log.Timber

class LoginFragment: BaseFragment<LoginViewModel, LoginEvent>() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //(activity as MainActivity).binding.bottomNav.gone()
        (activity as MainActivity).binding.rootView.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        _binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etPhone.requestFocus()
        binding.etPhone.postDelayed(this::showKeyboard, 200)

        binding.btnLogin.setOnClickListener {
            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToOtpFragment()
            )
        }

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun getOrCreateViewModel(): LoginViewModel = viewModel

    override fun renderEvent(event: LoginEvent) {
        when(event) {
            is LoginEvent.Error -> {
                Timber.d(event.message)
            }
        }
    }
}