package com.rit.rsk

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.rit.domain.models.PosCategoryData
import com.rit.domain.models.PosProductData
import com.rit.rsk.base.BaseFragment
import com.rit.rsk.databinding.FragmentPosCounterBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PosCounterFragment : BaseFragment<PosCounterViewModel, PosCounterEvent>() {

    private var _binding: FragmentPosCounterBinding? = null
    private val binding get() = _binding!!

    private val categoryAdapter by lazy {
        PosCategoryAdapter()
    }

    private val productAdapter by lazy {
        PosProductsAdapter()
    }

    private val mainViewModel: MainViewModel by activityViewModels()
    private val viewModel: PosCounterViewModel by viewModels()

    @Inject
    lateinit var progressHelper: ProgressHelper


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as MainActivity).binding.rootView.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        _binding = FragmentPosCounterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        setupActions()
    }

    private fun setupActions() {

        binding.rlCart.setOnClickListener {
            findNavController().navigate(
                PosCounterFragmentDirections.actionPosCounterToPaymentSummary()
            )
        }
    }

    private fun initUI() {

        val window = activity?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window?.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.purple_gradient))

        progressHelper.setup(this, viewModel.loading)
        binding.rvCategory.adapter = categoryAdapter
        categoryAdapter.submitList(
            listOf(
                PosCategoryData(1, "All", true),
                PosCategoryData(1, "Food", false),
                PosCategoryData(1, "Beverages", false),
                PosCategoryData(1, "Fast Foods", false)
            )
        )

        binding.rlProducts.adapter = productAdapter
        productAdapter.submitList(
            listOf(
                PosProductData(1,"Food","Product 1",5000),
                PosProductData(2,"Beverages","Product 2",51000),
                PosProductData(3,"Fast Food","Product 3",9000),
                PosProductData(4,"Food","Product 4",10000),
                PosProductData(5,"Beverages","Product 5",15000),
                PosProductData(6,"Fast Food","Product 6",25000),
                PosProductData(7,"Beverages","Product 7",35000),
                PosProductData(8,"Fast Food","Product 8",65000),
                PosProductData(9,"Food","Product 9",95000),
                PosProductData(10,"Beverages","Product 10",2000)
            )
        )
    }

    override fun getOrCreateViewModel(): PosCounterViewModel = viewModel

    override fun renderEvent(event: PosCounterEvent) {

    }

    override fun onStop() {
        val window = activity?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window?.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.border_radius_6_white))
        super.onStop()
    }

}