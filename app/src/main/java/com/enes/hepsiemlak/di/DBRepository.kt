package com.enes.hepsiemlak.di

import androidx.lifecycle.LiveData
import com.enes.hepsiemlak.db.AppDatabase
import com.enes.hepsiemlak.db.entity.ProductEntity
import com.enes.hepsiemlak.di.Transformer.convertProductModelToProductEntity
import com.enes.hepsiemlak.model.Product

import javax.inject.Inject

class DBRepository @Inject constructor(val appDatabase: AppDatabase) {

    suspend fun insertProduct(product: Product): Long {
        return appDatabase.productDao().insert(convertProductModelToProductEntity(product))
    }

    suspend fun delete(product: Product) {
        appDatabase.productDao().delete(convertProductModelToProductEntity(product))
    }

    fun getAllProductList(): LiveData<List<ProductEntity>> {
        return appDatabase.productDao().getProductList()
    }

    fun getSearchProductList(productId: Int): LiveData<List<ProductEntity>> {
        return appDatabase.productDao().getSearchProduct(productId)
    }

}