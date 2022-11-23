package com.rit.domain.models

import java.math.BigInteger

data class PosProductData(
    val id: Int,
    val categoryName: String,
    val productName: String,
    val itemPrice: Int
): DomainModel<PosProductData> {
    override fun areItemsTheSame(item: PosProductData): Boolean {
        return  item.id == id
    }

    override fun areContentsTheSame(item: PosProductData): Boolean {
        return item == this
    }
}