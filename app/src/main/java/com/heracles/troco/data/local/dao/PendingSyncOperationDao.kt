package com.heracles.troco.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.heracles.troco.data.local.entity.PendingSyncOperationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PendingSyncOperationDao {

    @Insert
    suspend fun insert(
        operation: PendingSyncOperationEntity
    ): Long

    @Query(
        """
            SELECT * FROM pending_sync_operations
            ORDER BY createdAt ASC, id ASC
        """
    )
    suspend fun getPendingOperations(): List<PendingSyncOperationEntity>

    @Query(
        """
            SELECT COUNT(*) FROM pending_sync_operations
        """
    )
    fun observePendingCount(): Flow<Int>

    @Query("""
        DELETE FROM pending_sync_operations
        WHERE id = :operationId
    """)
    suspend fun deleteById(operationId: Long)

    @Query(
        """
            UPDATE pending_sync_operations
            SET retryCount = retryCount + 1
            WHERE id = :operationId
        """
    )
    suspend fun incrementRetryCount(operationId: Long)
}