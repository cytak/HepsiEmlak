package com.enes.hepsiemlak.di

import com.enes.hepsiemlak.db.entity.ProductEntity
import com.enes.hepsiemlak.model.Product
import com.enes.hepsiemlak.model.ProductListItem


object Transformer {

    fun convertProductModelToProductEntity(product: Product): ProductEntity {
        return ProductEntity(
            currency = product.currency,
            productId = product.productId,
            imageUrl = product.imageUrl,
            name = product.name,
            price = product.price,
            amount = product.amount
        )
    }

    fun convertProductEntityToProductModel(productEntity: ProductEntity): Product {
        return Product(
            currency = productEntity.currency,
            productId = productEntity.productId,
            imageUrl = productEntity.imageUrl,
            name = productEntity.name,
            price = productEntity.price,
            amount = productEntity.amount
        )
    }

    fun convertProductListItemModelToProductModel(productListItem: ProductListItem,amount: Int): Product {
        return Product(
            currency = productListItem.currency,
            productId = productListItem.id,
            imageUrl = productListItem.image,
            name = productListItem.name,
            price = productListItem.price,
            amount = amount
        )
    }


}