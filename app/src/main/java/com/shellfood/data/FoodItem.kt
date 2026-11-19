package com.shellfood.data

data class FoodItem(
    val id: String,
    val name: String,
    val description: String,
    val price: Int,
    val emoji: String,
    val isVeg: Boolean
)
