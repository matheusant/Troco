package com.heracles.troco.domain.usecase.sync

import com.heracles.troco.data.local.ProductLocalDataSource
import com.heracles.troco.data.remote.ProductRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveAndSyncProductsUseCase @Inject constructor(
    private val localDataSource: ProductLocalDataSource,
    private val remoteDataSource: ProductRemoteDataSource
) {
    operator fun invoke(): Flow<Unit> {
        return remoteDataSource.observeProducts()
            .map { products ->
                localDataSource.syncProductsFromRemote(products)
            }
    }
}