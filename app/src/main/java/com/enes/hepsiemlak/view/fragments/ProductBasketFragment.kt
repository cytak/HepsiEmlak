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
import com.enes.hepsiemlak.view.adapter.ProductBasketAdapter
import com.enes.hepsiemlak.viewModel.ProductBasketViewModel
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

        viewModel.product.observe(viewLifecycleOwner) { dataHandler ->
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

        binding.basketListBackImage.setOnClickListener {
            onBackPressed()
        }
        binding.basketContinueBtn.setOnClickListener {
            onBackPressed()
        }
        productBasketAdapter.productAddClicked {
            //TODO bu kısımda istek atılacak daha sonra ise veritabanına eklenecek
        }
        productBasketAdapter.productDeleteClicked {

        }
        productBasketAdapter.productRemoveClicked {
            viewModel.deleteProduct(product = it)
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