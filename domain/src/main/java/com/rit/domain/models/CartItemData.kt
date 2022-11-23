package com.rit.domain.models

data class CartItemData(
    val id: Int,
    val qty: Int,
    val productId: Int,
    val productName: String,
    val unitType: String,
    val unitPrice: Int
): DomainModel<CartItemData> {
    override fun areItemsTheSame(item: CartItemData): Boolean {
        return item.id == this.id
    }

    override fun areContentsTheSame(item: CartItemData): Boolean {
        return item == this
    }

}