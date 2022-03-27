package com.enes.hepsiemlak.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.enes.hepsiemlak.db.entity.ProductEntity

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(productEntity: ProductEntity):Long

    @Query("SELECT * FROM PRODUCT")
    fun getProductList():LiveData<List<ProductEntity>>

    @Query("SELECT * FROM PRODUCT WHERE productId LIKE :productId")
    fun getSearchProduct(productId :Int):LiveData<List<ProductEntity>>

    @Delete
    suspend fun delete(productEntity: ProductEntity)
}