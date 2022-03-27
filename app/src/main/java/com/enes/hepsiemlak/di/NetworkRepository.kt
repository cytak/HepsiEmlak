package com.enes.hepsiemlak.di

import com.enes.hepsiemlak.api.ApiService
import com.enes.hepsiemlak.model.ProductList
import com.enes.hepsiemlak.model.ProductSellItem
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class NetworkRepository @Inject constructor(val api: ApiService) {
    suspend fun getProductList(): Response<ProductList> {
        return api.getProductList()
    }

    suspend fun sellItem(params: String): Response<Unit>{
        return api.sellProduct(params = params)
    }

}