package com.rit.rsk

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rit.domain.models.PosProductData
import com.rit.rsk.databinding.ItemProduct2Binding

class PosProductsAdapter : ListAdapter<PosProductData, PosProductsAdapter.PosProductVH>(
    PosProductDiffUtil
) {

    inner class PosProductVH(
        val binding: ItemProduct2Binding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener { }
        }

        fun bind(productData: PosProductData) {
            binding.apply {
                tvCategory.text = productData.categoryName
                tvProductName.text = productData.productName
                tvSellPrice.text =  productData.itemPrice.toString()
            }
        }
    }

    companion object {
        val PosProductDiffUtil = object : DiffUtil.ItemCallback<PosProductData>() {
            override fun areItemsTheSame(
                oldItem: PosProductData,
                newItem: PosProductData
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: PosProductData,
                newItem: PosProductData
            ): Boolean = oldItem == newItem

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PosProductVH {
        val binding =
            ItemProduct2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PosProductVH(binding)
    }

    override fun onBindViewHolder(holder: PosProductVH, position: Int) {
        holder.bind(currentList[position])
    }
}