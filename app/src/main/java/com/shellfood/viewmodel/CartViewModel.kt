package com.shellfood.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shellfood.data.CartItem
import com.shellfood.data.FoodItem
import com.shellfood.data.repository.CartRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class   CartViewModel(
    private val cartRepository: CartRepository
) : ViewModel() {
    // Observe cart items from Room
    val cartItems = cartRepository.getCartItems()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    // Add food to cart
    fun addToCart(food: FoodItem) {
        viewModelScope.launch {
            val currentItems = cartRepository.getCartItems().first()
            val existingItem = currentItems.find {
                it.food.id == food.id
            }
            if (existingItem != null) {
                cartRepository.updateQuantity(
                    food.id,
                    existingItem.quantity + 1
                )
            } else {
                cartRepository.addCartItem(
                    CartItem(
                        food = food,
                        quantity = 1
                    )
                )
            }
        }
    }
    // Increase or decrease quantity
    fun updateQuantity(foodId: String, quantity: Int) {
        viewModelScope.launch {
            cartRepository.updateQuantity(foodId, quantity)
        }
    }
    // Remove an item
    fun removeFromCart(foodId: String) {
        viewModelScope.launch {
            cartRepository.removeCartItem(foodId)
        }
    }
    // Clear entire cart
    fun clearCart() {
        viewModelScope.launch {
            cartRepository.clearCart()
        }
    }
}
