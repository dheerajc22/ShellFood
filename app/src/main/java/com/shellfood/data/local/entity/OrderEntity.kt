package com.shellfood.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(

    @PrimaryKey
    val orderId: String,

    val subtotal: Int,
    val deliveryFee: Int,
    val tax: Int,
    val totalAmount: Int,

    val paymentMethod: String,
    val orderStatus: String,

    val orderDate: Long
)
