package com.enes.hepsiemlak.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.enes.hepsiemlak.db.dao.ProductDao
import com.enes.hepsiemlak.db.entity.ProductEntity

@Database(
    version = 2,
    entities = [ProductEntity::class],
    exportSchema = true
)
abstract class AppDatabase :RoomDatabase(){

    abstract fun productDao(): ProductDao
}