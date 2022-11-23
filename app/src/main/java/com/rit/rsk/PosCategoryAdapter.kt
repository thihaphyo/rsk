package com.rit.rsk

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rit.domain.models.LowStockData
import com.rit.domain.models.PosCategoryData
import com.rit.rsk.databinding.ItemCategoryBinding

class PosCategoryAdapter : ListAdapter<PosCategoryData, PosCategoryAdapter.PosCategoryVH>(
    PosCategoryDiffUtil) {

    inner class PosCategoryVH(
        val binding: ItemCategoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener { }
        }

        fun bind(category: PosCategoryData) {
            binding.apply {
                tvCategory.text = category.name
                if (category.isSelected) {
                    binding.rlContainer.setBackgroundResource(R.drawable.border_radius_6_white)
                    tvCategory.setTextColor(
                        ContextCompat.getColor(binding.tvCategory.context, R.color.category_selected)
                    )
                } else {
                    binding.rlContainer.setBackgroundResource(R.drawable.border_radius_6_category_blue)
                    tvCategory.setTextColor(
                        ContextCompat.getColor(binding.tvCategory.context, R.color.category_normal)
                    )
                }
            }
        }


    }

    companion object {
        val PosCategoryDiffUtil = object : DiffUtil.ItemCallback<PosCategoryData>() {
            override fun areItemsTheSame(
                oldItem: PosCategoryData,
                newItem: PosCategoryData
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: PosCategoryData,
                newItem: PosCategoryData
            ): Boolean = oldItem == newItem

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PosCategoryVH {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PosCategoryVH(binding)
    }

    override fun onBindViewHolder(holder: PosCategoryVH, position: Int) {
       holder.bind(currentList[position])
    }
}