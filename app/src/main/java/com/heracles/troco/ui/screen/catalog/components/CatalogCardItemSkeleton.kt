package com.heracles.troco.ui.screen.catalog.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.heracles.troco.ui.components.modifier.shimmer
import com.heracles.troco.ui.theme.TrocoTheme

@Composable
fun CatalogCardItemSkeleton() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.outlineVariant),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(color = MaterialTheme.colorScheme.surfaceVariant)
                    .shimmer()
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.surface)
                    .padding(top = 12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(start = 12.dp, end = 12.dp, bottom = 8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .width(140.dp)
                            .height(12.dp)
                            .background(color = MaterialTheme.colorScheme.surfaceVariant)
                            .shimmer()
                    )

                    Spacer(Modifier.height(6.dp))

                    Box(
                        modifier = Modifier
                            .width(120.dp)
                            .height(12.dp)
                            .background(color = MaterialTheme.colorScheme.surfaceVariant)
                            .shimmer()
                    )

                    Spacer(Modifier.height(6.dp))

                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(12.dp)
                            .background(color = MaterialTheme.colorScheme.surfaceVariant)
                            .shimmer()
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CatalogCardItemSkeletonPreview() {
    TrocoTheme {
        CatalogCardItemSkeleton()
    }
}