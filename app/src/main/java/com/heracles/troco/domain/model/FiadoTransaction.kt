package com.heracles.troco.domain.model

import kotlinx.serialization.Serializable
import java.util.UUID

enum class TransactionType {
    PURCHASE,
    PAYMENT
}

@Serializable
data class TransactionItem(
    val name: String,
    val subtotal: Long
)

data class FiadoTransaction (
    val id: String = UUID.randomUUID().toString(),
    val ownerId: String,
    val userId: String,
    val amount: Long,                          // Ex: "- R$ 42,90" ou "+ R$ 20,00"
    val type: TransactionType,                   // Defines as cores (rosa/verde) e ícone (seta)
    val date: Long,
    val details: List<TransactionItem> = emptyList(), // Lista expandida de produtos (se houver)
)