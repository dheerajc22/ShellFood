package com.shellfood.data.mapper

import com.shellfood.data.Order
import com.shellfood.data.local.dao.OrderWithItems
import com.shellfood.data.local.entity.OrderEntity
import com.shellfood.data.local.entity.OrderItemEntity

fun Order.toOrderEntity(): OrderEntity {
    return OrderEntity(
        orderId = orderId,
        subtotal = subtotal,
        deliveryFee = deliveryFee,
        tax = tax,
        totalAmount = totalAmount,
        paymentMethod = paymentMethod,
        orderStatus = orderStatus,
        orderDate = orderDate
    )
}
fun Order.toOrderItemEntities(): List<OrderItemEntity> {
    return items.map { cartItem ->
        OrderItemEntity(
            orderId = orderId,
            foodId = cartItem.food.id,
            name = cartItem.food.name,
            description = cartItem.food.description,
            price = cartItem.food.price,
            emoji = cartItem.food.emoji,
            isVeg = cartItem.food.isVeg,
            quantity = cartItem.quantity
        )
    }
}
fun OrderWithItems.toOrder(): Order {
    val cartItems = items.map { item ->
        com.shellfood.data.CartItem(
            food = com.shellfood.data.FoodItem(
                id = item.foodId,
                name = item.name,
                description = item.description,
                price = item.price,
                emoji = item.emoji,
                isVeg = item.isVeg
            ),
            quantity = item.quantity
        )
    }
    return Order(
        orderId = order.orderId,
        items = cartItems,
        subtotal = order.subtotal,
        deliveryFee = order.deliveryFee,
        tax = order.tax,
        totalAmount = order.totalAmount,
        paymentMethod = order.paymentMethod,
        orderStatus = order.orderStatus,
        orderDate = order.orderDate
    )
}
