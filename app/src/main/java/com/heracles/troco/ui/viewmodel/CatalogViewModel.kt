package com.heracles.troco.ui.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heracles.troco.components.Constants.CRITICAL_QUANTITY
import com.heracles.troco.components.formatMoney
import com.heracles.troco.domain.ProductAvailability
import com.heracles.troco.domain.model.CatalogProduct
import com.heracles.troco.domain.model.ProductStatus
import com.heracles.troco.domain.usecase.product.GetProductsUseCase
import com.heracles.troco.ui.screen.catalog.CatalogUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class CatalogViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase
) : ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    private val _searchQuery = MutableStateFlow("")

    private val catalogProducts = getProductsUseCase.getAllProducts()
        .onStart {
            _isLoading.value = true
        }
        .map { products ->
            products
                .filter { it.status != ProductStatus.INACTIVE }
                .map { product ->
                val productAvailability = ProductAvailabilityPolicy.determine(product.quantity)
                CatalogProduct(
                    id = product.id,
                    bg = Color.LightGray,
                    name = product.name,
                    price = product.price.formatMoney(),
                    status = productAvailability
                )
            }
        }
        .onEach {
            _isLoading.value = false
        }

    private val searchResults: StateFlow<List<CatalogProduct>> = _searchQuery
        .debounce(500.milliseconds)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            catalogProducts
                .map { products ->
                    if (query.isBlank()) {
                        products
                    } else {
                        products.filter { it.name.contains(query, ignoreCase = true) }
                    }
                }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    val uiState = combine(
        searchResults,
        _searchQuery,
        _isLoading
    ) { products, query, loading ->

        CatalogUiState(
            products = products,
            query = query,
            isLoading = loading
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        CatalogUiState()
    )

    fun onSearchQueryChange(value: String) {
        _searchQuery.value = value
    }

    object ProductAvailabilityPolicy {
        fun determine(
            quantity: Int
        ): ProductAvailability {
            return when {
                quantity <= 0 -> ProductAvailability.OUT_OF_STOCK

                quantity <= CRITICAL_QUANTITY ->
                    ProductAvailability.LAST_UNITS

                else -> ProductAvailability.AVAILABLE
            }
        }
    }
}