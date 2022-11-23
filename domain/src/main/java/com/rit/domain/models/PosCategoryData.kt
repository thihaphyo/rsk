package com.rit.domain.models

data class PosCategoryData(
    val id: Int,
    val name: String,
    val isSelected: Boolean
): DomainModel<PosCategoryData> {
    override fun areItemsTheSame(item: PosCategoryData): Boolean {
        return this.id == item.id
    }

    override fun areContentsTheSame(item: PosCategoryData): Boolean {
        return this == item
    }
}