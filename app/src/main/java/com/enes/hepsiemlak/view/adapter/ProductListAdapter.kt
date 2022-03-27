package com.enes.hepsiemlak.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.enes.hepsiemlak.databinding.ProductListItemBinding
import com.enes.hepsiemlak.di.Transformer
import com.enes.hepsiemlak.model.Product
import com.enes.hepsiemlak.model.ProductSellItem
import com.enes.hepsiemlak.model.ProductListItem
import com.enes.hepsiemlak.utils.loadImageFromGlide
import javax.inject.Inject

class ProductListAdapter @Inject constructor() : RecyclerView.Adapter<ProductListAdapter.ProductListViewHolder>() {


    inner class ProductListViewHolder(val binding: ProductListItemBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallBack = object : DiffUtil.ItemCallback<ProductListItem>() {
        override fun areItemsTheSame(oldItem: ProductListItem, newItem: ProductListItem): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(
            oldItem: ProductListItem,
            newItem: ProductListItem
        ): Boolean {
            return newItem == oldItem
        }

    }

    val differ = AsyncListDiffer(this, diffCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductListViewHolder {
        return ProductListViewHolder(ProductListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ProductListViewHolder, position: Int) {
       val productItem = productList[position]
        holder.binding.apply {
            productImageView.loadImageFromGlide(productItem.image)
            productTitle.text = productItem.name
            productPrice.text = "${productItem.price} ${productItem.currency}"
        }
        holder.binding.productSell.setOnClickListener {
            setClickListener?.let {
                it(Transformer.convertProductListItemModelToProductModel(productItem,  1))
            }
        }
    }

    override fun getItemCount(): Int {
      return productList.size
    }

    private var setClickListener : ((product: Product)->Unit)? =null

    fun productSellClicked(listener:(Product)->Unit){
        setClickListener =listener
    }
    
    var productList: List<ProductListItem>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }
}