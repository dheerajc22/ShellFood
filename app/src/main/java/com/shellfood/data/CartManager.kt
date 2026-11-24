package com.shellfood.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
class CartManager {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    // Public read-only cart state for observing UI changes
    val cartItemsFlow: StateFlow<List<CartItem>> =
        _cartItems.asStateFlow()
    // Current cart snapshot for business logic
    val cartItems: List<CartItem>
        get() = _cartItems.value
    // Add food to cart
    fun addToCart(food: FoodItem) {
        _cartItems.update { currentItems ->
            val existingItem = currentItems.find {
                it.food.id == food.id
            }
            if (existingItem != null) {
                currentItems.map { item ->
                    if (item.food.id == food.id) {
                        item.copy(quantity = item.quantity + 1)
                    } else {
                        item
                    }
                }
            } else {
                currentItems + CartItem(
                    food = food,
                    quantity = 1
                )
            }
        }
    }
    // Remove food from cart
    fun removeFromCart(foodId: String) {
        _cartItems.update { currentItems ->
            currentItems.filterNot {
                it.food.id == foodId
            }
        }
    }
    // Update food quantity
    fun updateQuantity(foodId: String, newQuantity: Int) {
        _cartItems.update { currentItems ->
            if (newQuantity <= 0) {
                currentItems.filterNot {
                    it.food.id == foodId
                }
            } else {
                currentItems.map { item ->
                    if (item.food.id == foodId) {
                        item.copy(quantity = newQuantity)
                    } else {
                        item
                    }
                }
            }
        }
    }
    // Calculate total cart price
    fun getCartTotal(): Int {
        return _cartItems.value.sumOf {
            it.totalPrice
        }
    }
    // Calculate total number of items
    fun getCartItemCount(): Int {
        return _cartItems.value.sumOf {
            it.quantity
        }
    }
    // Clear entire cart
    fun clearCart() {
        _cartItems.value = emptyList()
    }
}