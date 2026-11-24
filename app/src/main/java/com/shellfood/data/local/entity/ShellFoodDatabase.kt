package com.shellfood.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.shellfood.data.local.dao.CartDao
import com.shellfood.data.local.dao.OrderDao
import com.shellfood.data.local.entity.CartEntity
import com.shellfood.data.local.entity.OrderEntity
import com.shellfood.data.local.entity.OrderItemEntity

@Database(
    entities = [
        CartEntity::class,
        OrderEntity::class,
        OrderItemEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class ShellFoodDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao
    companion object {
        @Volatile
        private var INSTANCE: ShellFoodDatabase? = null
        fun getDatabase(context: Context): ShellFoodDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ShellFoodDatabase::class.java,
                    "shellfood_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
