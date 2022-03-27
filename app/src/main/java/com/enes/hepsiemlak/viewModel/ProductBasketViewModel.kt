package com.enes.hepsiemlak.viewModel

import androidx.lifecycle.*
import com.enes.hepsiemlak.di.DBRepository
import com.enes.hepsiemlak.di.NetworkRepository
import com.enes.hepsiemlak.di.Transformer
import com.enes.hepsiemlak.model.Product
import com.enes.hepsiemlak.model.ProductSellItem
import com.enes.hepsiemlak.utils.DataHandler
import com.enes.hepsiemlak.utils.toJson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ProductBasketViewModel @Inject constructor(private val dbRepository: DBRepository,private val networkRepository: NetworkRepository) : ViewModel() {



    private val _sellProduct = MutableLiveData<DataHandler<String>>()
    val sellProduct: LiveData<DataHandler<String>> = _sellProduct
    private val sellItemList: MutableList<ProductSellItem> = mutableListOf()
    lateinit var product: Product

    var productList: LiveData<DataHandler<List<Product>>> = Transformations.map(dbRepository.getAllProductList()) { list ->

        val temp = list.map {
            Transformer.convertProductEntityToProductModel(it)
        }
        if (temp.isNullOrEmpty()) {
            DataHandler.ERROR(null, "List Empty or Null Error")
        } else {
            DataHandler.SUCCESS(temp)
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

    private fun handleSellResponse(response: Response<Unit>): DataHandler<String> {
        if (response.isSuccessful) {
            response.body()?.let {
                return DataHandler.SUCCESS("Success")
            }
        }

       val message =  if (response.code() == 404)
            "There is no product in stock."
        else response.errorBody().toString()
        return DataHandler.ERROR(message = message)
    }

    fun insertProduct(product: Product) {
        viewModelScope.launch {
            dbRepository.insertProduct(product)
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch() {
            dbRepository.delete(product)
        }
    }

    fun getProductPlusSearch(): LiveData<DataHandler.SUCCESS<Product>> =
        Transformations.map(dbRepository.getSearchProductList(product.productId)) { list ->
            val searchProduct: Product = Transformer.convertProductEntityToProductModel(list[0])
            searchProduct.amount += 1
            DataHandler.SUCCESS(searchProduct)
        }

    fun getProductMinusSearch(productId: Int): LiveData<DataHandler.SUCCESS<Product>> =
        Transformations.map(dbRepository.getSearchProductList(productId)) { list ->
            val searchProduct: Product = Transformer.convertProductEntityToProductModel(list[0])
            searchProduct.amount -= 1
            DataHandler.SUCCESS(searchProduct)
        }

}