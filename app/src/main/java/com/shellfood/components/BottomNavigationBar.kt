package com.shellfood.components

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
@Composable
fun BottomNavigationBar(
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {
    val navigationItems = listOf(
        "Home" to "🏠",
        "Cart" to "🛒",
        "Favorites" to "❤️",
        "Orders" to "📦",
        "Profile" to "👤"
    )

    NavigationBar(
        containerColor = Color.White
    ) {
        navigationItems.forEach { (label, emoji) ->

            NavigationBarItem(
                selected = selectedItem == label,
                onClick = {
                    onItemSelected(label)
                },
                icon = {
                    Text(text = emoji)
                },
                label = {
                    Text(text = label)
                }
            )
        }
    }
}
