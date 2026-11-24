package com.shellfood.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shellfood.data.local.entity.CartEntity
import kotlinx.coroutines.flow.Flow
@Dao
interface CartDao {
    // Insert a food item into the cart
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(item: CartEntity)
    // Retrieve all cart items
    @Query("SELECT * FROM cart_items")
    fun getAllCartItems(): Flow<List<CartEntity>>
    // Update the quantity of a food item
    @Query("UPDATE cart_items SET quantity = :quantity WHERE foodId = :foodId")
    suspend fun updateQuantity(foodId: String, quantity: Int)
    // Delete a particular food item
    @Query("DELETE FROM cart_items WHERE foodId = :foodId")
    suspend fun deleteCartItem(foodId: String)

    // Clear the entire cart
    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}
