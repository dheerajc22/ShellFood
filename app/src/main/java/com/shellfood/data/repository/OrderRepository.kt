package com.shellfood.data.repository

import androidx.room.withTransaction
import com.shellfood.data.Order
import com.shellfood.data.local.ShellFoodDatabase
import com.shellfood.data.mapper.toOrder
import com.shellfood.data.mapper.toOrderEntity
import com.shellfood.data.mapper.toOrderItemEntities
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OrderRepository(
    private val database: ShellFoodDatabase
) {
    private val orderDao = database.orderDao()
    suspend fun placeOrder(order: Order) {
        require(order.items.isNotEmpty()) {
            "An order must contain at least one item"
        }
        database.withTransaction {
            orderDao.insertOrder(order.toOrderEntity())
            orderDao.insertOrderItems(order.toOrderItemEntities())
        }
    }
    fun getAllOrders(): Flow<List<Order>> {
        return orderDao.getAllOrders().map { orders ->
            orders.map { it.toOrder() }
        }
    }
    suspend fun getOrderById(orderId: String): Order? {
        return orderDao.getOrderById(orderId)?.toOrder()
    }
    suspend fun updateOrderStatus(orderId: String, newStatus: String) {
        orderDao.updateOrderStatus(orderId, newStatus)
    }
    suspend fun deleteOrder(orderId: String) {
        orderDao.deleteOrder(orderId)
    }
}
