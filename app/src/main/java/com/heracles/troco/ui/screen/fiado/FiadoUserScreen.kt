package com.heracles.troco.ui.screen.fiado

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.heracles.troco.R
import com.heracles.troco.components.FiadoTransactionItem
import com.heracles.troco.domain.model.FiadoTransaction
import com.heracles.troco.domain.model.TransactionType
import com.heracles.troco.ui.screen.fiado.components.FiadoUserCardBalance
import com.heracles.troco.ui.theme.RubikFontFamily
import com.heracles.troco.ui.theme.TrocoTheme
import java.util.Date

@Composable
fun FiadoUserScreen(
    onNewSaleClick: () -> Unit,
    onGetPaidClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        FiadoUserCardBalance(
            balance = "R$ 67,89",
            isClear = false,
            dueDate = "Última compra realizada hoje às 09:12"
        )

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 160.dp),
            contentPadding = PaddingValues(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                FiadoUserButton(
                    text = "Nova Venda",
                    painter = painterResource(R.drawable.ic_add),
                    color = MaterialTheme.colorScheme.secondary,
                    onClick = onNewSaleClick
                )
            }
            item {
                FiadoUserButton(
                    text = "Receber Pago",
                    painter = painterResource(R.drawable.ic_money),
                    color = MaterialTheme.colorScheme.primary,
                    onClick = onGetPaidClick
                )
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                FiadoTransactionItem(
                    FiadoTransaction(
                        ownerId = "",
                        userId = "",
                        amount = 1299L,
                        type = TransactionType.PURCHASE,
                        date = Date().date.toLong(),
                    )
                )
            }
        }
    }
}

@Composable
fun FiadoUserButton(
    text: String,
    painter: Painter,
    color: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(color),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painter,
                contentDescription = text
            )
            Text(
                text,
                modifier = Modifier.padding(start = 8.dp),
                style = TextStyle(
                    fontFamily = RubikFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FiadoUserScreenPreview() {
    TrocoTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            FiadoUserScreen(
                onNewSaleClick = {},
                onGetPaidClick = {}
            )
        }
    }
}