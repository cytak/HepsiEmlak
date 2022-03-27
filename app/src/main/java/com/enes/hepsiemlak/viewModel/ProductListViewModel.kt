package com.enes.hepsiemlak.viewModel

import androidx.lifecycle.*
import com.enes.hepsiemlak.di.DBRepository
import com.enes.hepsiemlak.di.NetworkRepository
import com.enes.hepsiemlak.di.Transformer
import com.enes.hepsiemlak.model.Product
import com.enes.hepsiemlak.model.ProductList
import com.enes.hepsiemlak.model.ProductSellItem
import com.enes.hepsiemlak.utils.DataHandler
import com.enes.hepsiemlak.utils.toJson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val dbRepository: DBRepository
) : ViewModel() {

    private val _productListMutableData = MutableLiveData<DataHandler<ProductList>>()
    val productListLiveData: LiveData<DataHandler<ProductList>> = _productListMutableData
    private val _sellProduct = MutableLiveData<DataHandler<String>>()
    val sellProduct: LiveData<DataHandler<String>> = _sellProduct
    private val sellItemList: MutableList<ProductSellItem> = mutableListOf()
    lateinit var product: Product

    fun getProductList() {
        _productListMutableData.postValue(DataHandler.LOADING())
        viewModelScope.launch {
            val response = networkRepository.getProductList()
            _productListMutableData.postValue(handleResponse(response))
        }
    }

    fun sellProduct(product: Product) {
        _sellProduct.postValue(DataHandler.LOADING())
        this.product = product
        sellItemList.clear()
        sellItemList.add(ProductSellItem(1, product.productId))
        val body = sellItemList.toJson()
        viewModelScope.launch {
            val response = networkRepository.sellItem(body)
            _sellProduct.postValue(handleSellResponse(response))
        }
    }

    private fun handleResponse(response: Response<ProductList>): DataHandler<ProductList> {
        if (response.isSuccessful) {
            response.body()?.let { productList ->
                return DataHandler.SUCCESS(productList)
            }
        }
        return DataHandler.ERROR(message = response.errorBody().toString())
    }

    private fun handleSellResponse(response: Response<Unit>): DataHandler<String> {
        if (response.isSuccessful) {
            response.body()?.let {
                return DataHandler.SUCCESS("Success")
            }
        }
//        if(response.code() == 404)
        return DataHandler.ERROR(message = response.errorBody().toString())
    }


    fun insertProduct(product: Product) {
        viewModelScope.launch {
            dbRepository.insertProduct(product)
        }
    }

    fun getProductSearch(): LiveData<DataHandler.SUCCESS<Product>> =
        Transformations.map(dbRepository.getSearchProductList(product.productId)) { list ->
            val searchProduct: Product
            if (list.isNullOrEmpty()) {
                searchProduct = product
            } else {
                searchProduct = Transformer.convertProductEntityToProductModel(list[0])
                searchProduct.amount += 1
            }
            DataHandler.SUCCESS(searchProduct)
        }
}