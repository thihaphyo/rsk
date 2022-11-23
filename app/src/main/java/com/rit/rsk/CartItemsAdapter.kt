package com.rit.rsk

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rit.domain.models.CartItemData
import com.rit.rsk.databinding.ItemPurchaseItemsBinding

class CartItemsAdapter(
    private val onFocused: (Int, CartItemData, Boolean) -> Unit
): ListAdapter<CartItemData, CartItemsAdapter.CartItemVH>(CartItemDiffUtil) {

    inner class CartItemVH(
        val binding: ItemPurchaseItemsBinding
    ): RecyclerView.ViewHolder(binding.root) {
        init {

        }

        fun bind(itemData: CartItemData) {
            binding.apply {
                tvQty.setText(itemData.qty.toString())
                tvItemName.text = itemData.productName
                tvPrice.text = "${itemData.unitPrice} MMK per ${itemData.unitType}"
                tvTotal.text = "${(itemData.qty * itemData.unitPrice)} MMK"
            }
        }
    }

    companion object {
        val CartItemDiffUtil = object : DiffUtil.ItemCallback<CartItemData>() {
            override fun areItemsTheSame(oldItem: CartItemData, newItem: CartItemData): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: CartItemData, newItem: CartItemData): Boolean {
               return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemVH {
        val binding = ItemPurchaseItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartItemVH(binding)
    }

    override fun onBindViewHolder(holder: CartItemVH, position: Int) {
       holder.bind(currentList[position])
       holder.binding.tvQty.setOnFocusChangeListener { view, b ->
            onFocused.invoke(position, currentList[position], b)
       }
    }
}