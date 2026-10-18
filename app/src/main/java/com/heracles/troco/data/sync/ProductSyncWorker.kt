package com.heracles.troco.data.sync

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.heracles.troco.domain.usecase.sync.SyncProductsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CancellationException

@HiltWorker
class ProductSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val syncUseCase: SyncProductsUseCase
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        return try {
            syncUseCase()
            Result.success()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(
                "ProductSyncWorker",
                "Erro ao sincronizar produtos",
                e
            )
            Result.retry()
        }
    }
}