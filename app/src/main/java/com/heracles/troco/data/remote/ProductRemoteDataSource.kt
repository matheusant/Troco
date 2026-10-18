package com.heracles.troco.data.remote

import com.heracles.troco.domain.model.Product

interface ProductRemoteDataSource {
    suspend fun upsertProduct(product: Product)
    suspend fun deleteProduct(productId: String)
    suspend fun fetchProducts(): List<Product>
}