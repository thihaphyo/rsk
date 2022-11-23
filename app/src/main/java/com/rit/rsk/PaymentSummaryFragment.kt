package com.rit.rsk

import KeyboardTriggerBehavior
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.rit.domain.models.CartItemData
import com.rit.rsk.base.BaseFragment
import com.rit.rsk.databinding.FragmentPaymentSummaryBinding
import com.rit.shared.extensions.gone
import com.rit.shared.extensions.hide
import com.rit.shared.extensions.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentSummaryFragment: BaseFragment<PaymentSummaryViewModel, PaymentSummaryEvent>() {

    private var _binding: FragmentPaymentSummaryBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by activityViewModels()
    private val viewModel: PaymentSummaryViewModel by viewModels()

    private var currentItem: CartItemData? = null

    private var keyboardTriggerBehavior: KeyboardTriggerBehavior? = null

    private val cartItemsAdapter by lazy {
        CartItemsAdapter{ position, data, isFocused ->
            currentItem = data
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = this.activity ?: return
        keyboardTriggerBehavior = KeyboardTriggerBehavior(activity).apply {
            observe(viewLifecycleOwner, Observer {
                when (it) {
                    KeyboardTriggerBehavior.Status.OPEN -> {
                        binding.paymentInfo.gone()
                    }
                    KeyboardTriggerBehavior.Status.CLOSED -> {
                        binding.paymentInfo.show()
                    }
                }
            })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as MainActivity).binding.rootView.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        _binding = FragmentPaymentSummaryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        setupActions()
    }

    private fun initUI() {

        binding.rlItems.adapter = cartItemsAdapter
        cartItemsAdapter.submitList(
            listOf(
                CartItemData(1,2,1,"Icecream", "cones",2000),
                CartItemData(2,12,2,"Burger", "cones",2000),
                CartItemData(3,6,3,"Beef Bacon", "cones",2000),
                CartItemData(4,2,4,"Beer", "cones",2000),
                CartItemData(5,4,5,"Tea", "cones",2000),
                CartItemData(6,11,6,"Coffee", "cones",2000),
                CartItemData(7,9,7,"Cake", "cones",2000),
                CartItemData(8,6,8,"Fruit Juice", "cones",2000),
                CartItemData(9,3,9,"Apple Juice", "cones",2000),
                CartItemData(10,1,10,"Milk Shake", "cones",2000)

            )
        )

    }

    private fun setupActions() {
        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

    }

    override fun getOrCreateViewModel(): PaymentSummaryViewModel = viewModel

    override fun renderEvent(event: PaymentSummaryEvent) {

    }

}