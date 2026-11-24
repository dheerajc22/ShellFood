package com.shellfood.data.mapper

import com.shellfood.data.CartItem
import com.shellfood.data.FoodItem
import com.shellfood.data.local.entity.CartEntity

fun CartEntity.toCartItem(): CartItem {
    return CartItem(
        food = FoodItem(
            id = foodId,
            name = name,
            description = description,
            price = price,
            emoji = emoji,
            isVeg = isVeg
        ),
        quantity = quantity
    )
}
fun CartItem.toCartEntity(): CartEntity {
    return CartEntity(
        foodId = food.id,
        name = food.name,
        description = food.description,
        price = food.price,
        emoji = food.emoji,
        isVeg = food.isVeg,
        quantity = quantity
    )
}
