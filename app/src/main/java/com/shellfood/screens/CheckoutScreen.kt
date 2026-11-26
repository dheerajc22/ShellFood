
package com.shellfood.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shellfood.viewmodel.CartViewModel
import kotlin.math.roundToInt

@Composable
fun CheckoutScreen(
    cartViewModel: CartViewModel,
    onBackClick: () -> Unit,
    onPlaceOrder: (String) -> Unit
) {
    // Observe cart items from Room through CartViewModel
    val cartItems by cartViewModel.cartItems.collectAsState()

    val subtotal = cartItems.sumOf { it.totalPrice }
    val deliveryFee = 30
    val tax = (subtotal * 0.05).roundToInt()
    val total = subtotal + deliveryFee + tax

    var selectedPayment by remember {
        mutableStateOf("UPI")
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // HEADER
        Text(
            text = "Checkout",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        TextButton(onClick = onBackClick) {
            Text("← Back to Cart")
        }

        // DELIVERY ADDRESS
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Delivery Address 📍",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Home Address",
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "Add your delivery address here"
                )
            }
        }

        // ORDER SUMMARY
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Order Summary 🛒",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (cartItems.isEmpty()) {
                    Text("Your cart is empty.")
                } else {
                    cartItems.forEach { item ->
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
                }
            }
        }

        // PAYMENT METHODS
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Payment Method 💳",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                val paymentMethods = listOf(
                    "UPI",
                    "Credit / Debit Card",
                    "Cash on Delivery"
                )

                paymentMethods.forEach { method ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        RadioButton(
                            selected = selectedPayment == method,
                            onClick = {
                                selectedPayment = method
                            }
                        )

                        Text(
                            text = method,
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }
                }
            }
        }

        // BILL DETAILS
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Bill Details",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                BillRow("Item Subtotal", subtotal)
                BillRow("Delivery Fee", deliveryFee)
                BillRow("Tax (5%)", tax)

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Total Payable",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Text(
                        text = "₹$total",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }
        }

        // PLACE ORDER
        Button(
            onClick = {
                onPlaceOrder(selectedPayment)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            enabled = cartItems.isNotEmpty(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Place Order • ₹$total",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun BillRow(
    label: String,
    amount: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label)
        Text("₹$amount")
    }
}
