package com.shellfood.data
data class CartItem(
    val food: FoodItem,
    val quantity: Int
) {
    val totalPrice: Int
        get() {
            val numericPrice = food.price
                .replace("₹", "")
                .toIntOrNull() ?: 0

            return numericPrice * quantity
        }
}
