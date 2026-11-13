package com.heracles.troco.ui.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.heracles.troco.components.FiadoTransactionItem
import com.heracles.troco.domain.model.FiadoTransaction
import com.heracles.troco.domain.model.TransactionType
import com.heracles.troco.ui.theme.DMSansFontFamily
import com.heracles.troco.ui.theme.RubikFontFamily
import com.heracles.troco.ui.theme.TrocoTheme
import java.util.Date

@Composable
fun MeuFiadoScreen(
    state: MeuFiadoUiState,
    scaffoldPadding: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        MeuFiadoCard(
            balance = state.balance,
            isClear = state.isClear,
            dueDate = state.dueDate
        )

        AnimatedContent(
            targetState = state.isClear,
            label = "FiadoContentTransition"
        ) { isClear ->
            if (isClear) {
                MeuFiadoPaid()
            } else {
                MeuFiadoTransactionList(state.fiadoTransactions, scaffoldPadding = scaffoldPadding)
            }
        }
    }
}

@Composable
fun MeuFiadoPaid() {
    Column(
        modifier = Modifier
            .padding(top = 20.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Check",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Você está em dia!",
            style = TextStyle(
                fontFamily = RubikFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.primary
            )
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Agradecemos pela preferência e confiança. Sua caderneta não tem nenhuma pendência com Dona Maria.",
            style = TextStyle(
                fontFamily = DMSansFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}

@Composable
fun MeuFiadoCard(
    balance: String,
    isClear: Boolean,
    dueDate: String?,
) {
    val (backgroundColor, textColor) = when (isClear) {
        true -> MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.primary
        false -> MaterialTheme.colorScheme.secondaryContainer to MaterialTheme.colorScheme.secondary
    }
    Card(
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, textColor),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "SALDO DA CADERNETA",
                style = TextStyle(
                    fontFamily = DMSansFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    letterSpacing = 0.5.sp,
                    color = textColor
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = balance,
                style = TextStyle(
                    fontFamily = RubikFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    color = textColor
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (isClear) "Tudo pago por aqui!" else dueDate!!,
                style = TextStyle(
                    fontFamily = DMSansFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

@Composable
fun MeuFiadoTransactionList(
    fiadoTransactions: List<FiadoTransaction>,
    scaffoldPadding: PaddingValues
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Extrato de Compras",
            style = TextStyle(
                fontFamily = RubikFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(bottom = scaffoldPadding.calculateBottomPadding()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(fiadoTransactions) { transaction ->
                FiadoTransactionItem(transaction)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MeuFiadoScreenPreview() {
    TrocoTheme {
        Column(modifier = Modifier.padding(20.dp)) {
            MeuFiadoScreen(
                scaffoldPadding = PaddingValues(),
                state = MeuFiadoUiState(
                    balance = "R$ 54,90",
                    dueDate = "11 de maio de 2026",
                    fiadoTransactions = listOf(
                        FiadoTransaction(
                            ownerId = "",
                            userId = "",
                            amount = 1299L,
                            type = TransactionType.PURCHASE,
                            date = Date().date.toLong(),
                        )
                    )
                )
            )
        }
    }
}