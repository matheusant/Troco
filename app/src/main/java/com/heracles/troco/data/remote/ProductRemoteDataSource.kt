package com.heracles.troco.data.remote

import com.heracles.troco.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRemoteDataSource {
    suspend fun upsertProduct(product: Product)
    suspend fun deleteProduct(productId: String)
    suspend fun fetchProducts(): List<Product>
    fun observeProducts(): Flow<List<Product>>
}