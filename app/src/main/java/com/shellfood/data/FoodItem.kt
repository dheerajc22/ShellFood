package com.shellfood.data

data class FoodItem(
    val id: String,
    val name: String,
    val description: String,
    val price: String,
    val emoji: String,
    val isVeg: Boolean
)