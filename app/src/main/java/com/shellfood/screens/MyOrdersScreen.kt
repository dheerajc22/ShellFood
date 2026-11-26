package com.shellfood.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shellfood.data.Order
import com.shellfood.components.OrderStatusTracker
import com.shellfood.viewmodel.OrderViewModel

@Composable
fun MyOrdersScreen(
    orderViewModel: OrderViewModel
) {
    // Observe orders stored in Room
    val orders by orderViewModel.orders.collectAsState()

    // Store the selected order ID
    var selectedOrderId by remember {
        mutableStateOf<String?>(null)
    }
    // Always get the latest version of the selected order
    val selectedOrder = orders.firstOrNull {
        it.orderId == selectedOrderId
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "My Orders 📦",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (selectedOrder != null) {
            OrderDetailsScreen(
                order = selectedOrder,
                onBackClick = {
                    selectedOrderId = null
                }
            )
        } else if (orders.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "You haven't placed any orders yet.",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                orders.forEach { order ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedOrderId = order.orderId
                            },
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        OrderCard(
                            order = order,
                            orderViewModel = orderViewModel
                        )
                    }
                }
            }
        }
    }
}
@Composable
private fun OrderCard(
    order: Order,
    orderViewModel: OrderViewModel
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Order #${order.orderId}",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Status: ${order.orderStatus}",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(12.dp))
            OrderStatusTracker(
                currentStatus = order.orderStatus
            )
            val statuses = listOf(
                "Confirmed",
                "Preparing",
                "Out for Delivery",
                "Delivered"
        )
            val currentIndex = statuses.indexOf(order.orderStatus)
            if (currentIndex in 0 until statuses.lastIndex) {
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {
                        orderViewModel.updateOrderStatus(
                            orderId = order.orderId,
                            newStatus = statuses[currentIndex + 1]
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Move to Next Status")
                }
            } else if (order.orderStatus == "Delivered") {
                Text(
                    text = "Order Completed ✓",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp)
            )
            order.items.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${item.food.name} × ${item.quantity}",
                        modifier = Modifier.weight(1f)
                    )
                    Text("₹${item.totalPrice}")
                }
            }
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp)
            )
            Text("Payment: ${order.paymentMethod}")
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total Amount",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "₹${order.totalAmount}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
    }
}
