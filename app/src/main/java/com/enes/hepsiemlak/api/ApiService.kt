package com.enes.hepsiemlak.api


import com.enes.hepsiemlak.model.ProductList
import com.enes.hepsiemlak.utils.Constants
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET(Constants.LIST)
    suspend fun getProductList(): Response<ProductList>

    @Headers("Content-Type: text/html; charset=UTF-8")
    @POST(Constants.ORDER)
    suspend fun sellProduct(@Body params: String): Response<Unit>
}