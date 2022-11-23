package com.rit.rsk

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.rit.domain.models.LowStockData
import com.rit.rsk.base.BaseFragment
import com.rit.rsk.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment: BaseFragment<HomeViewModel, HomeEvent>() {

    private val viewModel: HomeViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val lowStockListAdapter by lazy {
        LowStockListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as MainActivity).binding.rootView.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        _binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI(){

        binding.rvLowStock.adapter = lowStockListAdapter
        lowStockListAdapter.submitList(
            listOf(
                LowStockData("1","Spicy Fish Curry"),
                LowStockData("2","Spicy Fish Curry"),
                LowStockData("3","Spicy Fish Curry"),
                LowStockData("4","Spicy Fish Curry")
            )
        )
        binding.ivMenu.setOnClickListener {
            (activity as MainActivity).binding.rootView.openDrawer(Gravity.LEFT)
        }

        binding.rlPosCounter.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToPosCounterFragment()
            )
        }
    }

    override fun getOrCreateViewModel() = viewModel

    override fun renderEvent(event: HomeEvent) {
       when(event) {
           is HomeEvent.error -> {
               Timber.d(event.msg)
           }
       }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()

    }
}