package com.heracles.troco.ui.screen.fiado

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.heracles.troco.components.formatMoney
import com.heracles.troco.domain.model.UserFiado
import com.heracles.troco.ui.theme.DMSansFontFamily
import com.heracles.troco.ui.theme.RubikFontFamily
import com.heracles.troco.ui.theme.TrocoTheme

@Composable
fun FiadoNotebookScreen(
    scaffoldPadding: PaddingValues,
    onSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val users = listOf(
            UserFiado(
                name = "Matheus",
                ownerId = "",
                lastName = "Santos",
                phone = "(11) 94982-7165",
            ),
            UserFiado(
                name = "Bruna",
                lastName = "Riças",
                ownerId = "",
                phone = "(11) 91111-9999",
            ),
            UserFiado(
                name = "Maria",
                lastName = "José da Silva Santos",
                ownerId = "",
                phone = "(11) 92222-2222",
            ),
        )
        Text(
            text = "Clientes com Fiado Ativo",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = RubikFontFamily,
                color = MaterialTheme.colorScheme.onSurface
            )
        )
        Text(
            text = "Listagem ordenada pelo maior saldo devedor.",
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = DMSansFontFamily,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(bottom = scaffoldPadding.calculateBottomPadding() + 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = users,
                key = { user -> user.phone }
            ) { userFiado ->
                FiadoNotebookItem(
                    userFiado = userFiado,
                    onSelected = onSelected
                )
            }
        }
    }
}

@Composable
fun FiadoNotebookItem(
    userFiado: UserFiado,
    onSelected: (String) -> Unit
) {
    val lastName = userFiado.lastName.split(" ").last()
    Card(
        onClick = {
            val fullName = "${userFiado.name} $lastName"
            onSelected(fullName)
        },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.outlineVariant),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .padding(all = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .padding(end = 12.dp)
                    .size(40.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(20.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${userFiado.name[0].uppercase()}${lastName.first().uppercase()}",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = RubikFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = "${userFiado.name} $lastName",
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = RubikFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Text(
                    text = userFiado.phone,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = DMSansFontFamily,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
            Text(
                text = 560L.formatMoney(),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = RubikFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FiadoNotebookScreenPreview() {
    TrocoTheme {
        Column(modifier = Modifier.padding(20.dp)) {
            FiadoNotebookScreen(
                scaffoldPadding = PaddingValues(),
                onSelected = {}
            )
        }
    }
}