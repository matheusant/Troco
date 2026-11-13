package com.heracles.troco.domain.repository

import com.heracles.troco.domain.model.UserFiado
import kotlinx.coroutines.flow.Flow

interface UserFiadoRepository {

    fun observeClients(): Flow<List<UserFiado>>

    suspend fun getClientById(id: String): UserFiado?

    suspend fun addClient(client: UserFiado)

    suspend fun updateClient(client: UserFiado)

    suspend fun deleteClient(id: String)
}