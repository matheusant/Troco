package com.heracles.troco.data.local

import androidx.room.TypeConverter
import com.heracles.troco.domain.model.ProductStatus
import com.heracles.troco.domain.model.TransactionItem
import com.heracles.troco.domain.model.TransactionType
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

class Converters {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    @TypeConverter
    fun fromProductStatus(value: ProductStatus): String {
        return value.label
    }

    @TypeConverter
    fun toProductStatus(value: String): ProductStatus {
        return ProductStatus.fromLabel(value)
    }

    @TypeConverter
    fun fromTransactionType(value: TransactionType): String {
        return value.name
    }

    @TypeConverter
    fun toTransactionType(value: String): TransactionType {
        return TransactionType.valueOf(value)
    }

    @TypeConverter
    fun fromTransactionItems(value: List<TransactionItem>): String {
        return json.encodeToString(
            ListSerializer(TransactionItem.serializer()),
            value
        )
    }

    @TypeConverter
    fun toTransactionItems(value: String): List<TransactionItem> {
        return json.decodeFromString(
            ListSerializer(TransactionItem.serializer()),
            value
        )
    }
}