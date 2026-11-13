package com.heracles.troco.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.heracles.troco.data.local.entity.UserFiadoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserFiadoDao {

    @Query(
        """
            SELECT * FROM fiado_clients
            WHERE ownerId = :ownerId
            ORDER BY name COLLATE NOCASE ASC, lastName COLLATE NOCASE ASC
        """
    )
    fun observeClients(ownerId: String): Flow<List<UserFiadoEntity>>

    @Query(
        """
            SELECT * FROM fiado_clients
            WHERE id = :id AND ownerId = :ownerId
            LIMIT 1
        """
    )
    suspend fun getClientById(id: String, ownerId: String): UserFiadoEntity?

    @Upsert
    suspend fun upsertClient(client: UserFiadoEntity)

    @Query("""
        DELETE FROM fiado_clients
        WHERE id = :id AND ownerId = :ownerId
    """)
    suspend fun deleteClient(
        id: String,
        ownerId: String
    )
}