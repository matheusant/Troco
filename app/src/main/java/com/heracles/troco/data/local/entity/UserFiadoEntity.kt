package com.heracles.troco.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "fiado_clients",
    indices = [
        Index(
            value = ["ownerId", "phone"],
            unique = true
        )
    ]
)
data class UserFiadoEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val ownerId: String,
    val name: String,
    val lastName: String,
    val phone: String,
)
