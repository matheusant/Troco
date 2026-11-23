package com.heracles.troco.ui.screen.fiado

import com.heracles.troco.domain.model.Product

data class FiadoNewSaleUiState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val query: String = ""
)