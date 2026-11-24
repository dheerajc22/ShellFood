package com.shellfood.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartEntity(

    @PrimaryKey
    val foodId: String,
    val name: String,
    val description: String,
    val price: Int,
    val emoji: String,
    val isVeg: Boolean,
    val quantity: Int
)
