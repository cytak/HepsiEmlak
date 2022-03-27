package com.enes.hepsiemlak.view.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.enes.hepsiemlak.R
import com.enes.hepsiemlak.databinding.FragmentProductListBinding
import com.enes.hepsiemlak.utils.DataHandler
import com.enes.hepsiemlak.utils.logData
import com.enes.hepsiemlak.utils.observeOnce
import com.enes.hepsiemlak.view.adapter.ProductListAdapter
import com.enes.hepsiemlak.viewModel.ProductListViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProductListFragment : Fragment(R.layout.fragment_product_list) {

    private lateinit var binding: FragmentProductListBinding

    @Inject
    lateinit var productListAdapter: ProductListAdapter

    val viewModel: ProductListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProductListBinding.bind(view)
        setUpRecyclerView()
        viewModel.productListLiveData.observe(viewLifecycleOwner) { dataHandler ->
            when (dataHandler) {
                is DataHandler.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    productListAdapter.differ.submitList(dataHandler.data)
                }
                is DataHandler.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    dataHandler.message?.let {
                        Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
                    }
                    logData("ProductListCreate ERROR " + dataHandler.message)
                }
                is DataHandler.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    logData("ProductListCreate LOADING...")

                }
            }

        }
        viewModel.getProductList()

        viewModel.sellProduct.observe(viewLifecycleOwner) { dataHandler ->
            when (dataHandler) {
                is DataHandler.SUCCESS -> {
                    dataHandler.data?.let {
                        Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
                    }
                    getProductSearch()
                }
                is DataHandler.ERROR -> {
                    dataHandler.message?.let {
                        Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
                    }
                    logData("ProductSell ERROR " + dataHandler.message)
                }
                is DataHandler.LOADING -> {
                    logData("ProductSell LOADING...")
                }
            }
        }

    }

    private fun getProductSearch() {
        viewModel.getProductSearch().observeOnce(viewLifecycleOwner){
            when (it) {
                is DataHandler.SUCCESS -> {
                    it.data?.let { it1 -> viewModel.insertProduct(it1) }
                }
            }
        }
    }


    private fun setUpRecyclerView() {
        productListAdapter.productSellClicked {
            viewModel.sellProduct(it)
        }

        binding.toolbarFrameLyt.setOnClickListener {
            findNavController().navigate(R.id.action_productListFragment_to_productBasketFragment)
        }

        binding.productListRecyclerView.apply {
            adapter = productListAdapter
            layoutManager = GridLayoutManager(activity, 2)
            setHasFixedSize(true)
        }

    }
}