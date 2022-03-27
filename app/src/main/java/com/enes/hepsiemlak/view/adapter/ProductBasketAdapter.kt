package com.enes.hepsiemlak.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.enes.hepsiemlak.databinding.ProductBasketItemBinding
import com.enes.hepsiemlak.model.Product
import com.enes.hepsiemlak.utils.loadImageFromGlide
import javax.inject.Inject

class ProductBasketAdapter @Inject constructor() : RecyclerView.Adapter<ProductBasketAdapter.ProductBasketViewHolder>() {


    inner class ProductBasketViewHolder(val binding: ProductBasketItemBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallBack = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.productId == newItem.productId
        }
        override fun areContentsTheSame(
            oldItem: Product,
            newItem: Product
        ): Boolean {
            return newItem == oldItem
        }

    }

    val differ = AsyncListDiffer(this, diffCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductBasketViewHolder {
        return ProductBasketViewHolder(ProductBasketItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ProductBasketViewHolder, position: Int) {
        val product = productList[position]
        holder.binding.apply {
            basketProductImage.loadImageFromGlide(product.imageUrl)
            basketProductTitleTxt.text = product.name
            basketProductPriceTxt.text = "${product.price} ${product.currency}"
            basketProductSumPriceTxt.text = "Sum : ${product.price.toFloat()*product.amount} ${product.currency}"
            basketProductCount.setText(product.amount.toString())
        }
        holder.binding.basketAddBtn.setOnClickListener {
            setAddClickListener?.let {
                it(product)
            }
        }
        holder.binding.basketDeleteBtn.setOnClickListener {
            setDeleteClickListener?.let {
                it(product)
            }
        }
        holder.binding.basketRemoveProduct.setOnClickListener {
            setRemoveClickListener?.let {
                it(product)
            }
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    private var setAddClickListener : ((product: Product)->Unit)? =null
    private var setDeleteClickListener : ((product: Product)->Unit)? =null
    private var setRemoveClickListener : ((product: Product)->Unit)? =null

    fun productAddClicked(listener:(Product)->Unit){
        setAddClickListener = listener
    }
    fun productDeleteClicked(listener:(Product)->Unit){
        setDeleteClickListener = listener
    }
    fun productRemoveClicked(listener:(Product)->Unit){
        setRemoveClickListener = listener
    }

    private var productList: List<Product>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }
}