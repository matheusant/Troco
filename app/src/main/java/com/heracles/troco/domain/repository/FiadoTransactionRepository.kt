package com.heracles.troco.domain.repository

import com.heracles.troco.domain.model.FiadoTransaction
import kotlinx.coroutines.flow.Flow

interface FiadoTransactionRepository {

    fun observeTransactions(userId: String): Flow<List<FiadoTransaction>>

    fun observeBalance(userId: String): Flow<Long>

    suspend fun addTransaction(transaction: FiadoTransaction)

    suspend fun deleteTransaction(id: String, userId: String)
}