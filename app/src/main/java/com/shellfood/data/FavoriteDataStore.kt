package com.shellfood.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
private val Context.favoriteDataStore by preferencesDataStore(
    name = "favorite_foods"
)
class FavoriteDataStore(private val context: Context) {
    companion object {
        private val FAVORITE_IDS =
            stringSetPreferencesKey("favorite_food_ids")
    }
    val favoriteIds: Flow<Set<String>> =
        context.favoriteDataStore.data.map { preferences ->
            preferences[FAVORITE_IDS] ?: emptySet()
        }
    suspend fun addFavorite(foodId: String) {
        context.favoriteDataStore.edit { preferences ->
            val currentFavorites =
                preferences[FAVORITE_IDS] ?: emptySet()

            preferences[FAVORITE_IDS] =
                currentFavorites + foodId
        }
    }
    suspend fun removeFavorite(foodId: String) {
        context.favoriteDataStore.edit { preferences ->
            val currentFavorites =
                preferences[FAVORITE_IDS] ?: emptySet()

            preferences[FAVORITE_IDS] =
                currentFavorites - foodId
        }
    }
}
