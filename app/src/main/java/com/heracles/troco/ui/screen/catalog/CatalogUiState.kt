package com.heracles.troco.ui.screen.catalog

import com.heracles.troco.domain.model.CatalogProduct

data class CatalogUiState(
    val products: List<CatalogProduct> = emptyList(),
    val query: String = "",
    val isLoading: Boolean = false
)