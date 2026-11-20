package com.shellfood.screens
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shellfood.data.Order
import com.shellfood.data.OrderManager
import com.shellfood.components.OrderStatusTracker
@Composable
fun MyOrdersScreen(
    orderManager: OrderManager
) {
    val orders = orderManager.orders
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
        if (orders.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment =    Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🛍️",
                        fontSize = 50.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No Orders Yet",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Your placed orders will appear here."
                    )
                }
            }
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                orders.forEach { order ->
                    OrderCard(
                        order = order,
                        orderManager = orderManager
                    )
                }
            }
        }
    }
}
@Composable
private fun OrderCard(
    order: Order,
    orderManager: OrderManager
){
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
                        orderManager.updateOrderStatus(
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
