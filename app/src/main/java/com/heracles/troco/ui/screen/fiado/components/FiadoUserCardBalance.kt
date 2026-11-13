package com.heracles.troco.ui.screen.fiado.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.heracles.troco.ui.theme.DMSansFontFamily
import com.heracles.troco.ui.theme.RubikFontFamily

@Composable
fun FiadoUserCardBalance(
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
                text = "SALDO DEVEDOR ATUAL",
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