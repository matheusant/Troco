package com.heracles.troco.domain.usecase.sync

import com.heracles.troco.data.local.ProductLocalDataSource
import com.heracles.troco.data.mapper.toDomain
import com.heracles.troco.data.remote.ProductRemoteDataSource
import javax.inject.Inject

class SyncProductsUseCase @Inject constructor(
    private val localDataSource: ProductLocalDataSource,
    private val remoteDataSource: ProductRemoteDataSource
) {
    suspend operator fun invoke() {
        val operations =
            localDataSource.getPendingOperations()

        for (operation in operations) {
            try {
                when (operation.operationType) {
                    "CREATE", "UPDATE" -> {
                        val productEntity =
                            localDataSource.getProductById(operation.productId) ?: continue
                        val product = productEntity.toDomain()

                        remoteDataSource.upsertProduct(product)
                    }

                    "DELETE" -> {
                        remoteDataSource.deleteProduct(operation.productId)
                    }

                    else -> {
                        continue
                    }
                }

                localDataSource.removePendingOperation(operation.id)
            } catch (e: Exception) {
                localDataSource.incrementRetryCount(operation.id)
                throw e
            }
        }
    }
}