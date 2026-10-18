package com.heracles.troco.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.heracles.troco.data.sync.SyncScheduler
import com.heracles.troco.domain.model.Product
import com.heracles.troco.domain.model.ProductStatus
import com.heracles.troco.domain.model.ProductStatusPolicy
import com.heracles.troco.domain.usecase.product.GetProductsUseCase
import com.heracles.troco.domain.usecase.product.SaveProductUseCase
import com.heracles.troco.domain.usecase.product.UpdateProductUseCase
import com.heracles.troco.ui.screen.ProductUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val saveProductUseCase: SaveProductUseCase,
    private val updateProductUseCase: UpdateProductUseCase,
    private val getProductsUseCase: GetProductsUseCase,
    private val syncScheduler: SyncScheduler
) : ViewModel() {
    private val _productName = MutableStateFlow("")
    private val _productPrice = MutableStateFlow("")
    private val _productQuantity = MutableStateFlow("")
    private val _isLoading = MutableStateFlow(false)
    private val _isAvailable = MutableStateFlow(false)

    private var editingProductId: String? = null


    val uiState = combine(
        _productName,
        _productPrice,
        _productQuantity,
        _isLoading,
        _isAvailable
    ) { name, price, quantity, isLoading, isAvailable ->
        ProductUiState(
            name = name,
            price = price,
            quantity = quantity,
            isLoading = isLoading,
            isAvailable = isAvailable
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ProductUiState()
        )

    fun onProdNameChange(value: String) {
        _productName.value = value
    }

    fun onProdPriceChange(value: String) {
        _productPrice.value = value
            .filter(Char::isDigit)
            .trimStart('0')
    }

    fun onProdQuantityChange(value: String) {
        _productQuantity.value = value
    }

    fun onAvailabilityChange(value: Boolean) {
        _isAvailable.value = value
    }

    fun saveProduct() {
        if (_isLoading.value) return

        val quantity = _productQuantity.value.toIntOrNull() ?: return
        val price = _productPrice.value.toLongOrNull() ?: return

        val product = Product(
            id = editingProductId ?: UUID.randomUUID().toString(),
            name = _productName.value,
            price = price,
            quantity = quantity,
            status = ProductStatusPolicy.determineStatus(
                isAvailable = _isAvailable.value,
                quantity = quantity
            )
        )

        viewModelScope.launch {
            if (_isLoading.value) return@launch

            _isLoading.value = true

            try {
                if (editingProductId != null) {
                    updateProductUseCase(product)
                } else {
                    saveProductUseCase(product)
                }
                clearFields()
                editingProductId = null
                try {
                    syncScheduler.scheduleSync()
                } catch (e: Exception) {

                }
            } catch (e: Exception) {

            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getProductById(id: String) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val product = getProductsUseCase.getProductById(id)

                editingProductId = product.id
                _productName.value = product.name
                _productPrice.value = product.price.toString()
                _productQuantity.value = product.quantity.toString()
                _isAvailable.value = product.status != ProductStatus.INACTIVE
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun clearFields() {
        _productName.value = ""
        _productPrice.value = ""
        _productQuantity.value = ""
        _isAvailable.value = false
    }

    fun resetForm() {
        clearFields()
        editingProductId = null
        _isLoading.value = false
    }

}