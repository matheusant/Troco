package com.heracles.troco.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.heracles.troco.data.remote.ProductRemoteDataSource
import com.heracles.troco.domain.model.Product
import com.heracles.troco.domain.model.ProductStatus
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreProductDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
): ProductRemoteDataSource {
    private val productsCollection = firestore.collection("products")

    override suspend fun upsertProduct(product: Product) {
        val data = mapOf(
            "id" to product.id,
            "name" to product.name,
            "price" to product.price,
            "quantity" to product.quantity,
            "status" to product.status.label
        )

        productsCollection
            .document(product.id)
            .set(data)
            .await()
    }

    override suspend fun deleteProduct(productId: String) {
        productsCollection
            .document(productId)
            .delete()
            .await()
    }

    override suspend fun fetchProducts(): List<Product> {
        val snapshot = productsCollection
            .get()
            .await()

        return snapshot.documents.mapNotNull { document ->
            val id = document.id
            val name = document.getString("name") ?: return@mapNotNull null
            val price = document.getLong("price") ?: return@mapNotNull null
            val quantity = document.getLong("quantity")?.toInt()
                ?: return@mapNotNull null
            val statusName = document.getString("status") ?: return@mapNotNull null

            val status = runCatching {
                ProductStatus.fromLabel(statusName)
            }.getOrNull() ?: return@mapNotNull null

            Product(
                id = id,
                name = name,
                price = price,
                quantity = quantity,
                status = status
            )
        }
    }
}