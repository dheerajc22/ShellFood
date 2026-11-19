package com.shellfood.data

import androidx.compose.runtime.mutableStateListOf
import java.util.UUID
class OrderManager {
    private val _orders = mutableStateListOf<Order>()
    val orders: List<Order>
        get() = _orders.toList()
    fun placeOrder(
        cartManager: CartManager,
        paymentMethod: String
    ): Order? {
        val cartItems = cartManager.cartItems
        if (cartItems.isEmpty()) {
            return null
        }
        val subtotal = cartManager.getCartTotal()
        val deliveryFee = 30
        val tax = (subtotal * 0.05).toInt()
        val totalAmount = subtotal + deliveryFee + tax
        val order = Order(
            orderId = UUID.randomUUID()
                .toString()
                .take(8)
                .uppercase(),
            items = cartItems,
            subtotal = subtotal,
            deliveryFee = deliveryFee,
            tax = tax,
            totalAmount = totalAmount,
            paymentMethod = paymentMethod,
            orderStatus = "Confirmed"
        )
        _orders.add(0, order)
        cartManager.clearCart()
        return order
    }
    fun getOrderById(orderId: String): Order? {
        return _orders.find { it.orderId == orderId }
    }
    fun updateOrderStatus(
        orderId: String,
        newStatus: String
    ) {
        val index = _orders.indexOfFirst {
            it.orderId == orderId
        }
        if (index != -1) {
            _orders[index] = _orders[index].copy(
                orderStatus = newStatus
            )
        }
    }
}
