package com.heracles.troco.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.heracles.troco.domain.model.TransactionType
import java.util.UUID

@Entity(
    tableName = "fiado_transactions",
    foreignKeys = [
        ForeignKey(
            entity = UserFiadoEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("userId"),
        Index("ownerId")
    ]
)
data class FiadoTransactionEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val ownerId: String,
    val userId: String,
    val amount: Long,
    val type: TransactionType,
    val date: Long,
    val details: String = "[]"
)