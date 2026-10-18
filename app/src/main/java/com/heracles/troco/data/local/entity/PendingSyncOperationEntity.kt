package com.heracles.troco.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pending_sync_operations")
data class PendingSyncOperationEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val productId: String,
    val operationType: String,
    val createdAt: Long = System.currentTimeMillis(),
    val retryCount: Int = 0
)