package com.shellfood.data

import androidx.compose.runtime.mutableStateListOf

class CartManager {
    // Shared cart items
    private val _cartItems = mutableStateListOf<CartItem>()
    val cartItems: List<CartItem>
        get() = _cartItems.toList()

    // Add food to cart
    fun addToCart(food: FoodItem) {
        val existingItem = _cartItems.find {
            it.food.id == food.id
        }
        if (existingItem != null) {

            updateQuantity(
                food.id,
                existingItem.quantity + 1
            )
        } else {
            _cartItems.add(
                CartItem(
                    food = food,
                    quantity = 1
                )
            )
        }
    }
    // Remove food from cart
    fun removeFromCart(foodId: String) {
        _cartItems.removeAll {
            it.food.id == foodId
        }
    }
    // Update food quantity
    fun updateQuantity(
        foodId: String,
        newQuantity: Int
    ) {

        val index = _cartItems.indexOfFirst {
            it.food.id == foodId
        }
        if (index != -1) {

            if (newQuantity <= 0) {
                _cartItems.removeAt(index)
            } else {

                val existingItem = _cartItems[index]

                _cartItems[index] = existingItem.copy(
                    quantity = newQuantity
                )
            }
        }
    }
    // Calculate total cart price
    fun getCartTotal(): Int {
        return _cartItems.sumOf {
            it.totalPrice
        }
    }
    // Calculate total number of items
    fun getCartItemCount(): Int {
        return _cartItems.sumOf {
            it.quantity
        }
    }
    // Clear entire cart
    fun clearCart() {
        _cartItems.clear()
    }
}
