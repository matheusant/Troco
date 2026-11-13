package com.heracles.troco.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.heracles.troco.data.local.entity.FiadoTransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FiadoTransactionDao {

    @Query(
        """
            SELECT * FROM fiado_transactions
            WHERE userId = :userId
            AND ownerId = :ownerId
            ORDER BY date DESC
        """
    )
    fun observeTransactions(
        userId: String,
        ownerId: String
    ): Flow<List<FiadoTransactionEntity>>

    @Query("""
    SELECT EXISTS(
        SELECT 1
        FROM fiado_clients
        WHERE id = :userId
          AND ownerId = :ownerId
    )
""")
    suspend fun clientExists(
        userId: String,
        ownerId: String
    ): Boolean

    @Query(
        """
        SELECT * FROM fiado_transactions
        WHERE id = :id
          AND userId = :userId
          AND ownerId = :ownerId
        LIMIT 1
    """
    )
    suspend fun getTransactionById(
        id: String,
        userId: String,
        ownerId: String
    ): FiadoTransactionEntity?

    @Upsert
    suspend fun upsertTransaction(
        transaction: FiadoTransactionEntity
    )

    @Query("""
        DELETE FROM fiado_transactions
        WHERE id = :id
          AND userId = :userId
          AND ownerId = :ownerId
    """)
    suspend fun deleteTransaction(
        id: String,
        userId: String,
        ownerId: String
    )

    @Query(
        """
            SELECT COALESCE(
                SUM(
                    CASE
                        WHEN type = 'PURCHASE' THEN amount
                        WHEN type = 'PAYMENT' THEN -amount
                        ELSE 0
                    END
                ),
                0
            )
            FROM fiado_transactions
            WHERE userId = :userId
            AND ownerId = :ownerId
        """
    )
    fun observeBalance(
        userId: String,
        ownerId: String,
    ): Flow<Long>
}