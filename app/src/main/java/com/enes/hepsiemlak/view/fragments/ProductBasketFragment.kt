package com.enes.hepsiemlak.view.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.enes.hepsiemlak.R
import com.enes.hepsiemlak.databinding.FragmentProductBasketBinding
import com.enes.hepsiemlak.model.Product
import com.enes.hepsiemlak.utils.DataHandler
import com.enes.hepsiemlak.utils.logData
import com.enes.hepsiemlak.utils.observeOnce
import com.enes.hepsiemlak.view.adapter.ProductBasketAdapter
import com.enes.hepsiemlak.viewModel.ProductBasketViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ProductBasketFragment : Fragment(R.layout.fragment_product_basket) {

    lateinit var binding: FragmentProductBasketBinding
    val viewModel: ProductBasketViewModel by viewModels()

    @Inject
    lateinit var productBasketAdapter: ProductBasketAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProductBasketBinding.bind(view)
        setUpRecyclerView()

        viewModel.productList.observe(viewLifecycleOwner) { dataHandler ->
            when (dataHandler) {
                is DataHandler.SUCCESS -> {
                    productBasketAdapter.differ.submitList(dataHandler.data)
                }
                is DataHandler.ERROR -> {
                    productBasketAdapter.differ.submitList(mutableListOf())
                    logData("basketFragmentViewCreated: ERROR ${dataHandler.message}")
                }
                is DataHandler.LOADING -> {
                    logData("basketFragmentViewCreated: LOADING")
                }
            }
        }


        viewModel.sellProduct.observe(viewLifecycleOwner) { dataHandler ->
            when (dataHandler) {
                is DataHandler.SUCCESS -> {
                    dataHandler.data?.let {
                        Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
                    }
                    getProductPlusSearch()
                }
                is DataHandler.ERROR -> {
                    dataHandler.message?.let {
                        Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
                    }
                    logData("ProductPlus ERROR " + dataHandler.message)
                }
                is DataHandler.LOADING -> {
                    logData("ProductPlus LOADING...")
                }
            }
        }


        binding.basketListBackImage.setOnClickListener {
            onBackPressed()
        }
        binding.basketContinueBtn.setOnClickListener {
            onBackPressed()
        }
        productBasketAdapter.productAddClicked {
            viewModel.sellProduct(product = it)
        }
        productBasketAdapter.productDeleteClicked {
            getProductMinusSearch(it)
        }
        productBasketAdapter.productRemoveClicked {
            viewModel.deleteProduct(product = it)
        }
    }

    private fun getProductPlusSearch() {
        viewModel.getProductPlusSearch().observeOnce(viewLifecycleOwner){
            when(it){
                is DataHandler.SUCCESS ->{
                    it.data?.let { it1 -> viewModel.insertProduct(it1) }
                }
            }
        }
    }
    private fun getProductMinusSearch(product: Product) {
        viewModel.getProductMinusSearch(product.productId).observeOnce(viewLifecycleOwner){
            when(it){
                is DataHandler.SUCCESS ->{
                    if (it.data?.amount == 0)
                        viewModel.deleteProduct(product = it.data)
                    else
                        it.data?.let { it1 -> viewModel.insertProduct(product = it1) }
                }
            }
        }
    }

    private fun setUpRecyclerView() {
        binding.basketRecyclerView.apply {
            adapter = productBasketAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun onBackPressed() {
        findNavController().navigate(R.id.action_productBasketFragment_to_productListFragment)
    }

}