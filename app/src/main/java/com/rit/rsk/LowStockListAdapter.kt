package com.rit.rsk

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rit.domain.models.LowStockData
import com.rit.rsk.databinding.ItemLowStockBinding

class LowStockListAdapter :
    ListAdapter<LowStockData, LowStockListAdapter.LowStockVH>(LowStockDiffUtil){
    inner class LowStockVH(
        val binding: ItemLowStockBinding
    ): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                //notiClick.invoke(currentList[bindingAdapterPosition])
            }
        }

        fun bind(lowStock: LowStockData) {
            binding.apply {
                tvItemName.text = lowStock.title
            }
        }

    }

    companion object {
        val LowStockDiffUtil = object : DiffUtil.ItemCallback<LowStockData>() {
            override fun areItemsTheSame(
                oldItem: LowStockData,
                newItem: LowStockData
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: LowStockData,
                newItem: LowStockData
            ): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LowStockVH {
        val binding =
            ItemLowStockBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LowStockVH(binding)
    }

    override fun onBindViewHolder(holder: LowStockVH, position: Int) {
        holder.bind(currentList[position])
    }
}