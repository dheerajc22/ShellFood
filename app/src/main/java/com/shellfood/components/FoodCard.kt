package com.shellfood.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shellfood.data.FoodItem
@Composable
fun FoodCard(
    food: FoodItem,
    isFavorite: Boolean = false,
    onFavoriteClick: () -> Unit = {},
    onAddToCart: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
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

            // Food Image
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFFFF0E2)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = food.emoji,
                    fontSize = 38.sp
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            // Food Details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = food.name,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF292929)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = food.description,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = food.price,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE86A16)
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Add to Cart Button
                Button(
                    onClick = onAddToCart,
                    modifier = Modifier.height(36.dp),
                    contentPadding = PaddingValues(
                        horizontal = 12.dp,
                        vertical = 4.dp
                    ),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE86A16)
                    )
                ) {
                    Text(
                        text = "+ Add",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            // Veg / Non-Veg Indicator and Favorite Button
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (food.isVeg) "🟢" else "🔴",
                    fontSize = 14.sp
                )
                IconButton(
                    onClick = onFavoriteClick
                ) {
                    Text(
                        text = if (isFavorite) "❤️" else "♡",
                        fontSize = 25.sp,
                        color = if (isFavorite) {
                            Color(0xFFE53935)
                        } else {
                            Color.Gray
                        }
                    )
                }
            }
        }
    }
}
