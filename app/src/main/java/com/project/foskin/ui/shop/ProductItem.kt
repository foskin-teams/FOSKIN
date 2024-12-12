package com.project.foskin.ui.shop

data class ProductItem(
    val imageUrl: String,
    val title: String,
    val price: Int,
    val rating: Float,
    val soldCount: Int,
    val shopLink: String
)