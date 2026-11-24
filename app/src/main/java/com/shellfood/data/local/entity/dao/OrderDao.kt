package com.shellfood.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Relation
import androidx.room.Embedded
import com.shellfood.data.local.entity.OrderEntity
import com.shellfood.data.local.entity.OrderItemEntity
import kotlinx.coroutines.flow.Flow
data class OrderWithItems(
    @Embedded
    val order: OrderEntity,
    @Relation(
        parentColumn = "orderId",
        entityColumn = "orderId"
    )
    val items: List<OrderItemEntity>
)
@Dao
interface OrderDao {
    @Insert
    suspend fun insertOrder(order: OrderEntity)
    @Insert
    suspend fun insertOrderItems(items: List<OrderItemEntity>)
    @Transaction
    @Query("SELECT * FROM orders ORDER BY orderDate DESC")
    fun getAllOrders(): Flow<List<OrderWithItems>>
    @Transaction
    @Query("SELECT * FROM orders WHERE orderId = :orderId")
    suspend fun getOrderById(orderId: String): OrderWithItems?
    @Query(
        "UPDATE orders SET orderStatus = :newStatus WHERE orderId = :orderId"
    )
    suspend fun updateOrderStatus(
        orderId: String,
        newStatus: String
    )
    @Query("DELETE FROM orders WHERE orderId = :orderId")
    suspend fun deleteOrder(orderId: String)
}
