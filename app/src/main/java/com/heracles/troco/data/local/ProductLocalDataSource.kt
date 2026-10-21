package com.heracles.troco.data.local

import androidx.room.withTransaction
import com.heracles.troco.data.local.dao.PendingSyncOperationDao
import com.heracles.troco.data.local.dao.ProductDao
import com.heracles.troco.data.local.entity.PendingSyncOperationEntity
import com.heracles.troco.data.local.entity.ProductEntity
import com.heracles.troco.domain.model.Product
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductLocalDataSource @Inject constructor(
    private val productDao: ProductDao,
    private val pendingSyncOperationDao: PendingSyncOperationDao,
    private val appDatabase: AppDatabase
) {

    suspend fun saveProduct(
        product: ProductEntity,
        operation: String
    ) {
        appDatabase.withTransaction {
            when (operation) {
                "CREATE" -> productDao.insertProduct(product)

                "UPDATE" -> productDao.updateProduct(
                    id = product.id,
                    name = product.name,
                    price = product.price,
                    quantity = product.quantity,
                    status = product.status
                )

                else -> throw IllegalArgumentException(
                    "Operação inválida: $operation"
                )
            }

            pendingSyncOperationDao.insert(
                operation = PendingSyncOperationEntity(
                    operationType = operation,
                    productId = product.id
                )
            )
        }
    }

    suspend fun syncProductsFromRemote(products: List<Product>) {
        appDatabase.withTransaction {
            val pendingOperations = pendingSyncOperationDao.getPendingOperations()

            val pendingProductIds = pendingOperations
                .map { it.productId }
                .toSet()

            val remoteProductsIds = products.map { it.id }.toSet()

            products.forEach { product ->
                if (product.id !in pendingProductIds) {
                    productDao.upsertProduct(
                        ProductEntity(
                            id = product.id,
                            name = product.name,
                            price = product.price,
                            quantity = product.quantity,
                            status = product.status
                        )
                    )
                }
            }

            val localProducts = productDao.getAllProductsOnce()

            localProducts.forEach { localProduct ->
                val wasRemovedRemotely =
                    localProduct.id !in remoteProductsIds

                val hasPendingOperation =
                    localProduct.id in pendingProductIds

                if (wasRemovedRemotely && !hasPendingOperation) {
                    productDao.deleteProduct(localProduct.id)
                }
            }
        }
    }

    suspend fun deleteProduct(
        productId: String
    ) {
        appDatabase.withTransaction {
            productDao.deleteProduct(productId)

            pendingSyncOperationDao.insert(
                operation = PendingSyncOperationEntity(
                    operationType = "DELETE",
                    productId = productId
                )
            )
        }
    }

    suspend fun getProductById(
        productId: String
    ): ProductEntity? {
        return productDao.getProductById(productId)
    }

    suspend fun getPendingOperations():
            List<PendingSyncOperationEntity> {
        return pendingSyncOperationDao.getPendingOperations()
    }

    suspend fun removePendingOperation(
        operationId: Long
    ) {
        pendingSyncOperationDao.deleteById(operationId)
    }

    suspend fun incrementRetryCount(
        operationId: Long
    ) {
        pendingSyncOperationDao.incrementRetryCount(operationId)
    }
}