package com.shellfood.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

@Composable
fun OrderStatusTracker(
    currentStatus: String
) {
    val statuses = listOf(
        "Confirmed",
        "Preparing",
        "Out for Delivery",
        "Delivered"
    )
    val descriptions = listOf(
        "Your order has been confirmed.",
        "The restaurant is preparing your food.",
        "Your order is on its way.",
        "Your order has been delivered."
    )
    val currentIndex = statuses.indexOf(currentStatus)
        .coerceAtLeast(0)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        statuses.forEachIndexed { index, status ->
            val isCompleted = index <= currentIndex
            val isCurrent = index == currentIndex
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(
                                if (isCompleted) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.surfaceVariant
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isCompleted) "✓" else "•",
                            color = if (isCompleted) {
                                MaterialTheme.colorScheme.onPrimary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            },
                            fontWeight = FontWeight.Bold
                        )
                    }
                    if (index != statuses.lastIndex) {
                        Box(
                            modifier = Modifier
                                .width(2.dp)
                                .height(35.dp)
                                .background(
                                    if (index < currentIndex) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.surfaceVariant
                                    }
                                )
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(
                    modifier = Modifier.padding(bottom = 20.dp)
                ) {
                    Text(
                        text = status,
                        fontSize = 16.sp,
                        fontWeight = if (isCurrent) {
                            FontWeight.Bold
                        } else {
                            FontWeight.Medium
                        }
                    )
                    Text(
                        text = descriptions[index],
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}