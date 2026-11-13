package com.heracles.troco.ui.screen

import com.heracles.troco.domain.model.FiadoTransaction

data class MeuFiadoUiState(
    val balance: String = "R$ 0,00",
    val dueDate: String? = null,
    val fiadoTransactions: List<FiadoTransaction> = emptyList(),
    val isLoading: Boolean = false
) {
    val isClear: Boolean get() = fiadoTransactions.isEmpty()
}
