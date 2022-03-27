package com.enes.hepsiemlak.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val currency: String,
    val productId: Int,
    val imageUrl: String,
    val name: String,
    val price: String,
    var amount: Int):Parcelable