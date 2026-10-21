package com.heracles.troco.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.heracles.troco.data.remote.ProductRemoteDataSource
import com.heracles.troco.domain.model.Product
import com.heracles.troco.domain.model.ProductStatus
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreProductDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ProductRemoteDataSource {
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

        return parseProducts(snapshot)
    }

    override fun observeProducts(): Flow<List<Product>> = callbackFlow {
        val registration = productsCollection
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot == null) {
                    close(
                        IllegalStateException(
                            "Não foi possível observar produtos do Firestore"
                        )
                    )
                    return@addSnapshotListener
                }

                if (snapshot.metadata.isFromCache) {
                    return@addSnapshotListener
                }

                try {
                    trySend(parseProducts(snapshot))
                } catch (e: Exception) {
                    close(e)
                }
            }

        awaitClose {
            registration.remove()
        }
    }

    private fun parseProducts(snapshot: QuerySnapshot): List<Product> {
        return snapshot.documents.map { document ->
            val id = document.id

            val name = document.getString("name")
                ?: throw IllegalStateException(
                    "Produto $id sem o campo name no Firestore"
                )

            val price = document.getLong("price")
                ?: throw IllegalStateException(
                    "Produto $id sem o campo price no Firestore"
                )

            val quantity = document.getLong("quantity")
                ?: throw IllegalStateException(
                    "Produto $id sem o campo quantity no Firestore"
                )

            val statusName = document.getString("status")
                ?: throw IllegalStateException(
                    "Produto $id sem o campo status no Firestore"
                )

            val status = try {
                ProductStatus.fromLabel(statusName)
            } catch (e: Exception) {
                throw IllegalStateException(
                    "Produto $id possui status inválido: $statusName",
                    e
                )
            }

            Product(
                id = id,
                name = name,
                price = price,
                quantity = quantity.toInt(),
                status = status
            )
        }
    }
}