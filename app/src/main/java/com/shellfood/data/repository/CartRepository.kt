package com.shellfood.data.repository

import com.shellfood.data.CartItem
import com.shellfood.data.local.dao.CartDao
import com.shellfood.data.mapper.toCartEntity
import com.shellfood.data.mapper.toCartItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CartRepository(
    private val cartDao: CartDao
) {
    fun getCartItems(): Flow<List<CartItem>> {
        return cartDao.getAllCartItems().map { entities ->
            entities.map { it.toCartItem() }
        }
    }
    suspend fun addCartItem(item: CartItem) {
        cartDao.insertCartItem(item.toCartEntity())
    }
    suspend fun updateQuantity(foodId: String, quantity: Int) {
        if (quantity <= 0) {
            cartDao.deleteCartItem(foodId)
        } else {
            cartDao.updateQuantity(foodId, quantity)
        }
    }
    suspend fun removeCartItem(foodId: String) {
        cartDao.deleteCartItem(foodId)
    }
    suspend fun clearCart() {
        cartDao.clearCart()
    }
}
