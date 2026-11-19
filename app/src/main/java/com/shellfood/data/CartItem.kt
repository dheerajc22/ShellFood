package com.shellfood.data

data class CartItem(
    val food: FoodItem,
    val quantity: Int
) {
    val totalPrice: Int
        get() = food.price * quantity
}
