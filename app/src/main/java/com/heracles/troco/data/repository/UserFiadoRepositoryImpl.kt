package com.heracles.troco.data.repository

import com.heracles.troco.data.local.dao.UserFiadoDao
import com.heracles.troco.data.local.entity.UserFiadoEntity
import com.heracles.troco.domain.model.UserFiado
import com.heracles.troco.domain.repository.AuthRepository
import com.heracles.troco.domain.repository.UserFiadoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserFiadoRepositoryImpl @Inject constructor(
    private val userFiadoDao: UserFiadoDao,
    private val authRepository: AuthRepository
) : UserFiadoRepository {
    override fun observeClients(): Flow<List<UserFiado>> {
        val ownerId = authRepository.usuarioAtual
            ?: return flowOf(emptyList())

        return userFiadoDao.observeClients(ownerId)
            .map { clients ->
                clients.map { client ->
                    UserFiado(
                        id = client.id,
                        ownerId = client.ownerId,
                        name = client.name,
                        lastName = client.lastName,
                        phone = client.phone
                    )
                }
            }
    }

    override suspend fun getClientById(id: String): UserFiado? {
        val ownerId = requireOwnerId()

        return userFiadoDao.getClientById(id, ownerId)?.let { client ->
            UserFiado(
                id = client.id,
                ownerId = client.ownerId,
                name = client.name,
                lastName = client.lastName,
                phone = client.phone
            )
        }
    }

    override suspend fun addClient(client: UserFiado) {
        val ownerId = requireOwnerId()

        return userFiadoDao.upsertClient(
            UserFiadoEntity(
                id = client.id,
                ownerId = ownerId,
                name = client.name.trim(),
                lastName = client.lastName.trim(),
                phone = client.phone.trim()
            )
        )
    }

    override suspend fun updateClient(client: UserFiado) {
        val ownerId = requireOwnerId()

        val existingClient = userFiadoDao.getClientById(
            id = client.id,
            ownerId = ownerId
        ) ?: throw IllegalStateException("Cliente não encontrado.")

        userFiadoDao.upsertClient(
            existingClient.copy(
                name = client.name.trim(),
                lastName = client.lastName.trim(),
                phone = client.phone.trim()
            )
        )
    }

    override suspend fun deleteClient(id: String) {
        val ownerId = requireOwnerId()
        userFiadoDao.deleteClient(id, ownerId)
    }

    private fun requireOwnerId(): String {
        return authRepository.usuarioAtual
            ?: throw IllegalStateException("Nenhum usuário autenticado.")
    }
}