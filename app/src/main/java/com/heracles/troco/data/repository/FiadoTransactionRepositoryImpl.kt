package com.heracles.troco.data.repository

import com.heracles.troco.data.local.dao.FiadoTransactionDao
import com.heracles.troco.data.local.entity.FiadoTransactionEntity
import com.heracles.troco.domain.model.FiadoTransaction
import com.heracles.troco.domain.model.TransactionItem
import com.heracles.troco.domain.repository.AuthRepository
import com.heracles.troco.domain.repository.FiadoTransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import javax.inject.Inject

class FiadoTransactionRepositoryImpl @Inject constructor(
    private val transactionDao: FiadoTransactionDao,
    private val authRepository: AuthRepository
) : FiadoTransactionRepository {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    override fun observeTransactions(userId: String): Flow<List<FiadoTransaction>> {
        val ownerId = authRepository.usuarioAtual
            ?: return flowOf(emptyList())

        return transactionDao.observeTransactions(
            userId = userId,
            ownerId = ownerId
        ).map { transactions ->
            transactions.map { transaction ->
                FiadoTransaction(
                    id = transaction.id,
                    ownerId = transaction.ownerId,
                    userId = transaction.userId,
                    amount = transaction.amount,
                    type = transaction.type,
                    date = transaction.date,
                    details = json.decodeFromString(
                        ListSerializer(TransactionItem.serializer()),
                        transaction.details
                    )
                )
            }
        }
    }

    override fun observeBalance(userId: String): Flow<Long> {
        val ownerId = authRepository.usuarioAtual
            ?: return flowOf(0L)

        return transactionDao.observeBalance(userId, ownerId)
    }

    override suspend fun addTransaction(transaction: FiadoTransaction) {
        val ownerId = requireOwnerId()

        val clientExists = transactionDao.clientExists(transaction.userId, ownerId)

        require(clientExists) {
            "Cliente não encontrado."
        }

        require(transaction.amount > 0L) {
            "O valor da transação deve ser maior que zero."
        }

        val detailsJson = json.encodeToString(
            ListSerializer(TransactionItem.serializer()),
            transaction.details
        )

        transactionDao.upsertTransaction(
            FiadoTransactionEntity(
                id = transaction.id,
                ownerId = ownerId,
                userId = transaction.userId,
                amount = transaction.amount,
                type = transaction.type,
                date = transaction.date,
                details = detailsJson
            )
        )
    }

    override suspend fun deleteTransaction(
        id: String,
        userId: String
    ) {
        val ownerId = requireOwnerId()

        transactionDao.deleteTransaction(
            id = id,
            userId = userId,
            ownerId = ownerId
        )
    }

    private fun requireOwnerId(): String {
        return authRepository.usuarioAtual
            ?: throw IllegalStateException("Nenhum usuário autenticado.")
    }
}