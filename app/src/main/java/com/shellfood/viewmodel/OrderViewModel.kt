package com.shellfood.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shellfood.data.Order
import com.shellfood.data.repository.OrderRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class OrderViewModel(
    private val orderRepository: OrderRepository
) : ViewModel() {
    // Observe all orders from Room
    val orders = orderRepository.getAllOrders()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    // Save order and report success or failure
    fun placeOrder(
        order: Order,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                orderRepository.placeOrder(order)
                onSuccess()
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                onError(
                    exception.message ?: "Unable to place order"
                )
            }
        }
    }
    // Update order status
    fun updateOrderStatus(
        orderId: String,
        newStatus: String
    ) {
        viewModelScope.launch {
            orderRepository.updateOrderStatus(
                orderId,
                newStatus
            )
        }
    }
    // Delete an order
    fun deleteOrder(orderId: String) {
        viewModelScope.launch {
            orderRepository.deleteOrder(orderId)
        }
    }
}
