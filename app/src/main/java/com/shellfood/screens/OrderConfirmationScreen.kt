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

@Composable
fun OrderConfirmationScreen(
    order: Order,
    onContinueShopping: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        // SUCCESS MESSAGE
        Text(
            text = "✅",
            fontSize = 64.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Order Placed Successfully!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Thank you for ordering with ShellFood!",
            modifier = Modifier.padding(top = 8.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        // ORDER ID
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Order ID",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "#${order.orderId}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Status: ${order.orderStatus}")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        // ORDER ITEMS
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Your Order",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
                order.items.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
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
                    modifier = Modifier.padding(vertical = 10.dp)
                )
                Text("Subtotal: ₹${order.subtotal}")
                Text("Delivery Fee: ₹${order.deliveryFee}")
                Text("Tax: ₹${order.tax}")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Total Paid: ₹${order.totalAmount}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Payment: ${order.paymentMethod}")
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        // CONTINUE SHOPPING
        Button(
            onClick = onContinueShopping,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Continue Shopping",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
