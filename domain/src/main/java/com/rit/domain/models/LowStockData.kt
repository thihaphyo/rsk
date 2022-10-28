package com.rit.domain.models

data class LowStockData(
    val id: String,
    val title: String
) : DomainModel<LowStockData> {

    override fun areItemsTheSame(item: LowStockData): Boolean {
        return this.id == item.id
    }

    override fun areContentsTheSame(item: LowStockData): Boolean {
        return this == item
    }
}