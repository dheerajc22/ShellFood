    package com.shellfood.screens

    import androidx.compose.foundation.background
    import androidx.compose.foundation.layout.*
    import androidx.compose.foundation.shape.RoundedCornerShape
    import androidx.compose.material3.*
    import androidx.compose.runtime.Composable
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import com.shellfood.data.CartManager
    import com.shellfood.data.CartItem
    private val CartOrange = Color(0xFFE86A16)

    @Composable
    fun CartScreen(
        cartManager: CartManager,
        onCheckoutClick: () -> Unit
    ) {
        // Observe cart items
        val cartItems = cartManager.cartItems
        val subtotal = cartManager.getCartTotal()
        val itemCount = cartManager.getCartItemCount()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "My Cart 🛒",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF292929)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "$itemCount items in your cart",
                color = Color.Gray,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(20.dp))
            if (cartItems.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 70.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🛒",
                        fontSize = 65.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Your Cart is Empty!",
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Add some delicious food to get started.",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            } else {
                cartItems.forEach { item ->
                    CartItemCard(
                        item = item,
                        onIncrease = {
                            cartManager.updateQuantity(
                                item.food.id,
                                item.quantity + 1
                            )
                        },
                        onDecrease = {
                            cartManager.updateQuantity(
                                item.food.id,
                                item.quantity - 1
                            )
                        },
                        onRemove = {
                            cartManager.removeFromCart(item.food.id)
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
                Spacer(modifier = Modifier.height(12.dp))
                // Bill Summary
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp)
                    ) {
                        Text(
                            text = "Bill Summary",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Items ($itemCount)")
                            Text("₹$subtotal")
                        }
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 14.dp)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Total Amount",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "₹$subtotal",
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold,
                                color = CartOrange
                            )
                        }
                        Spacer(modifier = Modifier.height(18.dp))
                        Button(
                            onClick = onCheckoutClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CartOrange
                            )
                        ) {
                            Text(
                                text = "Proceed to Checkout",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(
                    onClick = {
                        cartManager.clearCart()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Clear Cart",
                        color = Color.Red
                    )
                }
            }
        }
    }
    @Composable
    private fun CartItemCard(
        item: CartItem,
        onIncrease: () -> Unit,
        onDecrease: () -> Unit,
        onRemove: () -> Unit
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(65.dp)
                        .background(
                            Color(0xFFFFF0E2),
                            RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.food.emoji,
                        fontSize = 34.sp
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = item.food.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = item.food.price,
                        color = CartOrange,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = onDecrease,
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier.size(32.dp)
                        ) {
                            Text("−")
                        }
                        Text(
                            text = "${item.quantity}",
                            modifier = Modifier.padding(horizontal = 12.dp),
                            fontWeight = FontWeight.Bold
                        )
                        OutlinedButton(
                            onClick = onIncrease,
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier.size(32.dp)
                        ) {
                            Text("+")
                        }
                    }
                }
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "₹${item.totalPrice}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    TextButton(
                        onClick = onRemove
                    ) {
                           Text(
                            text = "Remove",
                            color = Color.Red,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
