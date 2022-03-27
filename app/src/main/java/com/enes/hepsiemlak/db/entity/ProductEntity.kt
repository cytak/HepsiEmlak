package com.enes.hepsiemlak.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "PRODUCT")
data class ProductEntity(
    @PrimaryKey val productId: Int = 0,
    val currency: String,
    val imageUrl: String,
    val name: String,
    val price: String,
    var amount: Int)