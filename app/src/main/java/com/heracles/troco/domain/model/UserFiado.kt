package com.heracles.troco.domain.model

import java.util.UUID

data class UserFiado(
    val id: String = UUID.randomUUID().toString(),
    val ownerId: String,
    val name: String,
    val lastName: String,
    val phone: String
)
