package com.shellfood.data

data class Order(
    val orderId: String,
    val items: List<CartItem>,
    val subtotal: Int,
    val deliveryFee: Int,
    val tax: Int,
    val totalAmount: Int,
    val paymentMethod: String,
    val orderStatus: String = "Confirmed",
    val orderDate: Long = System.currentTimeMillis()
)
