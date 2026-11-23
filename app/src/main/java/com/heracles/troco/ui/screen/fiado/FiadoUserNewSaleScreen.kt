package com.heracles.troco.ui.screen.fiado

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.heracles.troco.components.ButtonComponent
import com.heracles.troco.components.TrocoSearchBar
import com.heracles.troco.components.formatMoney
import com.heracles.troco.domain.model.Product
import com.heracles.troco.domain.model.ProductStatus
import com.heracles.troco.ui.theme.DMSansFontFamily
import com.heracles.troco.ui.theme.RubikFontFamily
import com.heracles.troco.ui.theme.TrocoTheme

@Composable
fun FiadoUserNewSaleScreen(
    state: FiadoNewSaleUiState,
    onQueryChange: (String) -> Unit,
    scaffoldPadding: PaddingValues
) {
    var selectedQuantities by rememberSaveable {
        mutableStateOf<Map<String, Int>>(emptyMap())
    }

    val total = state.products.sumOf { product ->
        val quantity = selectedQuantities[product.id] ?: 0
        product.price * quantity
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                bottom = scaffoldPadding.calculateBottomPadding()
            ),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            TrocoSearchBar(
                query = state.query,
                onQueryChange = onQueryChange,
                placeholder = "Filtrar produtos..."
            )

            Spacer(modifier = Modifier.height(28.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                contentPadding = PaddingValues(
                    bottom = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    items = state.products,
                    key = { it.id }
                ) { product ->

                    val quantity =
                        selectedQuantities[product.id] ?: 0

                    FiadoNewSaleCardItem(
                        product = product,
                        quantitySelected = quantity,
                        onQuantityChange = { selectedProduct, newQuantity ->
                            selectedQuantities =
                                selectedQuantities.toMutableMap().apply {
                                    if (newQuantity == 0) {
                                        remove(selectedProduct.id)
                                    } else {
                                        put(
                                            selectedProduct.id,
                                            newQuantity
                                        )
                                    }
                                }
                        }
                    )
                }
            }
        }

        val outlineColor = MaterialTheme.colorScheme.outlineVariant

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .drawWithContent {
                    drawContent()
                    drawLine(
                        color = outlineColor,
                        start = Offset(-20.dp.toPx(), 0f),
                        end = Offset(size.width + 20.dp.toPx(), 0f),
                        strokeWidth = 1.dp.toPx()
                    )
                }
        ) {
//            HorizontalDivider(
//                color = outlineColor
//            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total do débito:",
                    style = TextStyle(
                        fontFamily = RubikFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                )

                Text(
                    text = total.formatMoney(),
                    style = TextStyle(
                        fontFamily = RubikFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                ButtonComponent(
                    content = {
                        Text(
                            text = "Confirmar venda",
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontStyle = FontStyle.Normal,
                                fontFamily = RubikFontFamily
                            ),
                        )
                    },
                    onClick = {},
                    enabled = selectedQuantities.isNotEmpty()
                )
            }
        }
    }
}

@Composable
fun FiadoNewSaleCardItem(
    product: Product,
    quantitySelected: Int,
    onQuantityChange: (Product, Int) -> Unit
) {
    val isEnabled = (product.quantity > 0)

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        ),
        modifier = Modifier.alpha(if (isEnabled) 1f else 0.5f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .padding(end = 12.dp)
                    .size(60.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {}

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = product.name,
                    style = TextStyle(
                        fontFamily = RubikFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Text(
                    text = if (isEnabled) {
                        product.price.formatMoney()
                    } else {
                        "Sem estoque"
                    },
                    style = TextStyle(
                        fontFamily = DMSansFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 13.sp,
                        color = if (isEnabled) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onTertiaryContainer
                        }
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            Row(
                modifier = Modifier
                    .height(IntrinsicSize.Max)
                    .padding(start = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .size(32.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable(
                            enabled = quantitySelected > 0
                        ) {
                            onQuantityChange(
                                product,
                                quantitySelected - 1
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "-",
                        style = TextStyle(
                            fontFamily = DMSansFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }

                Text(
                    text = quantitySelected.toString(),
                    style = TextStyle(
                        fontFamily = RubikFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )

                Box(
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .size(32.dp)
                        .background(
                            color = if (isEnabled) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.surfaceVariant
                            },
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable(
                            enabled = isEnabled &&
                                    quantitySelected < product.quantity
                        ) {
                            onQuantityChange(
                                product,
                                quantitySelected + 1
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "+",
                        style = TextStyle(
                            fontFamily = DMSansFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = if (isEnabled) {
                                MaterialTheme.colorScheme.surface
                            } else {
                                Color.Unspecified
                            }
                        )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FiadoUserNewSaleScreenPreview() {
    TrocoTheme {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            FiadoUserNewSaleScreen(
                state = FiadoNewSaleUiState(
                    products = listOf(
                        Product(
                            id = "1",
                            name = "Produto 1",
                            price = 1000,
                            quantity = 6,
                            status = ProductStatus.ACTIVE
                        ),
                        Product(
                            id = "2",
                            name = "Produto 2",
                            price = 1500,
                            quantity = 3,
                            status = ProductStatus.ACTIVE
                        ),
                        Product(
                            id = "3",
                            name = "Produto 3",
                            price = 100,
                            quantity = 0,
                            status = ProductStatus.ACTIVE
                        ),
                        Product(
                            id = "4",
                            name = "Produto 3",
                            price = 100,
                            quantity = 0,
                            status = ProductStatus.ACTIVE
                        ),
                        Product(
                            id = "5",
                            name = "Produto 3",
                            price = 100,
                            quantity = 0,
                            status = ProductStatus.ACTIVE
                        ),
                        Product(
                            id = "6",
                            name = "Produto 3",
                            price = 100,
                            quantity = 0,
                            status = ProductStatus.ACTIVE
                        ),
                        Product(
                            id = "78",
                            name = "Produto 3",
                            price = 100,
                            quantity = 0,
                            status = ProductStatus.ACTIVE
                        ),
                        Product(
                            id = "8",
                            name = "Produto 3",
                            price = 100,
                            quantity = 0,
                            status = ProductStatus.ACTIVE
                        ),
                    )
                ),
                onQueryChange = {},
                scaffoldPadding = PaddingValues()
            )
        }
    }
}
