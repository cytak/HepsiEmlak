package com.enes.hepsiemlak.viewModel

import androidx.lifecycle.*
import com.enes.hepsiemlak.di.DBRepository
import com.enes.hepsiemlak.di.Transformer
import com.enes.hepsiemlak.model.Product
import com.enes.hepsiemlak.utils.DataHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductBasketViewModel @Inject constructor(private val dbRepository: DBRepository) : ViewModel() {


    var product: LiveData<DataHandler<List<Product>>> = Transformations.map(dbRepository.getAllProductList()) { list ->

        val temp = list.map {
            Transformer.convertProductEntityToProductModel(it)
        }
        if (temp.isNullOrEmpty()) {
            DataHandler.ERROR(null, "List Empty or Null Error")
        } else {
            DataHandler.SUCCESS(temp)
        }
    }
    lateinit var  searchProduct : LiveData<DataHandler<List<Product>>>

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

    fun updateProduct(product: Product){

    }
    fun getProductList() = dbRepository.getAllProductList()

    fun getSearchProduct(pId: Int) = dbRepository.getSearchProductList(pId)



}